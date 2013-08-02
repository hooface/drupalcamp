package be.drupalcamp.leuven;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProgramList extends BaseActivity {

    // The eventUrl.
    public String eventUrl = "http://dcleuven-api.timleytens.be/api/timeslots/list.json";

    // The filename to save to.
    public static String fileName = "list.json";

    // Number of sessions - we should actually do a count on the array of sessions.
    public int numberOfSessions = 0;

    // Other variables.
    ProgressDialog dialog;
    public static int siteStatus = 200;
    public static InputStream dataFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Do not hide refresh button.
        hideRefreshButton = false;

        setContentView(R.layout.program_list);
        super.onCreate(savedInstanceState);

        // Set header title.
        setTextViewString(R.id.header_title, R.string.menu_program);

        // Set fonts.
        setFontToOpenSansLight(R.id.header_title);
        setFontToOpenSansLight(R.id.day_1_text);
        setFontToOpenSansLight(R.id.day_2_text);

        // Get flipper, refresh and no events.
        ImageButton refresh = (ImageButton) findViewById(R.id.refresh);
        TextView noSessions = (TextView) findViewById(R.id.no_sessions);
        final ViewFlipper switcher = (ViewFlipper) findViewById(R.id.dayFlipper);

        // Always set refresh listener on the button.
        refresh.setOnClickListener(refreshProgram);

        // Check number of sessions. In case there are none, hide the flipper.
        DatabaseHandler db = new DatabaseHandler(this);
        int total = db.getSessionCount();

        if (total > 0) {
            // Hide empty no sessions message.
            noSessions.setVisibility(TextView.GONE);

            // Set listeners on day arrows.
            ImageButton day1 = (ImageButton) findViewById(R.id.day_1);
            day1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new AnimationUtils();
                    switcher.setAnimation(AnimationUtils.makeInAnimation(ProgramList.this, true));
                    switcher.showNext();
                }
            });
            ImageButton day2 = (ImageButton) findViewById(R.id.day_2);
            day2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new AnimationUtils();
                    switcher.setAnimation(AnimationUtils.makeInAnimation(ProgramList.this, true));
                    switcher.showPrevious();
                }
            });
        }
        else {
            // Hide flipper.
            switcher.setVisibility(ViewFlipper.GONE);
            // Set listener on text view button to refresh the program.
            noSessions.setOnClickListener(refreshProgram);
        }
    }

    /**
     * Refresh program.
     */
    private final View.OnClickListener refreshProgram = new View.OnClickListener() {
        public void onClick(View v) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if ((cm.getActiveNetworkInfo() != null) && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
                dialog = new customProgressDialog(ProgramList.this);
                dialog.setTitle(R.string.updating);
                dialog.setMessage(getString(R.string.updating_message));
                dialog.setIndeterminate(false);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setCancelable(false);
                dialog.show();
                new updateTask().execute();
            }
            else {
                Toast.makeText(ProgramList.this, getString(R.string.update_offline), Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * Update task.
     */
    class updateTask extends AsyncTask<Context, Integer, String> {

        protected String doInBackground(Context... params) {

            try {

                try {
                    siteStatus = downloadProgram();
                }
                catch (IOException ignored) {}

                if (siteStatus == 200) {

                    JSONArray sessions = null;
                    dataFile = openFileInput(fileName);
                    InputStreamReader inputreader = new InputStreamReader(dataFile, "UTF-8");
                    BufferedReader reader = new BufferedReader(inputreader);
                    String json = reader.readLine();

                    try {
                        sessions = new JSONArray(json);
                    }
                    catch (Exception e) {
                        Log.d("PARSING FAIL", "" + e.getMessage());
                    }

                    if (sessions == null) {
                        return "parsingfailed";
                    }

                    numberOfSessions = sessions.length();

                    try {

                        int count = 0;
                        DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
                        handler.truncateTable();

                        for (int i = 0; i < sessions.length(); i++){

                            JSONObject jsonSession = sessions.getJSONObject(i);

                            Session session = new Session();
                            session.setId(jsonSession.getInt("id"));
                            session.setTitle(jsonSession.getString("title"));
                            session.setDescription(jsonSession.getString("description"));
                            session.setSpecial(jsonSession.getInt("special"));
                            session.setStartDate(jsonSession.getInt("from"));
                            session.setEndDate(jsonSession.getInt("to"));
                            session.setLevel(jsonSession.getInt("level"));
                            handler.insertSession(session);

                            // Notify dialog.
                            count++;
                            int update = (count*100/numberOfSessions);
                            publishProgress(update);

                        }

                    }
                    catch (Exception ignored) {}
                }
                else {
                    return "servicedown";
                }
            }
            catch (IOException ignored) {}

            return "alldone";
        }

        @Override
        public void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String sResponse) {
            if (sResponse.equals("servicedown")) {
                serviceDown(dialog);
            }
            else if (sResponse.equals("parsingfailed")) {
                parsingFailed(dialog);
            }
            else {
                closeDialog(dialog);
            }
        }
    }

    /**
     * Download the program from the internet and save it locally.
     */
    public int downloadProgram() throws IOException {
        siteStatus = -1;

        try {

            URL downloadFileUrl = new URL(eventUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) downloadFileUrl.openConnection();
            siteStatus = httpConnection.getResponseCode();
            if (siteStatus == 200) {
                InputStream inputStream = httpConnection.getInputStream();

                byte[] buffer = new byte[1024];
                int bufferLength;

                // Write data to local file.
                FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                while ((bufferLength = inputStream.read(buffer)) > 0 ) {
                    fos.write(buffer, 0, bufferLength);
                }
                fos.flush();
                fos.close();
            }

            httpConnection.disconnect();
        }
        catch (UnsupportedEncodingException ignored) {}
        catch (ClientProtocolException ignored) {}
        catch (IOException ignored) {}

        return siteStatus;
    }

    /**
     * Close the dialog and inform that the service is down.
     */
    public void serviceDown(Dialog dialog) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        Toast.makeText(ProgramList.this, getString(R.string.service_offline), Toast.LENGTH_LONG).show();
    }

    /**
     * Close the dialog and inform that the parsing failed.
     */
    public void parsingFailed(Dialog dialog) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        Toast.makeText(ProgramList.this, getString(R.string.parsing_failed), Toast.LENGTH_LONG).show();
    }

    /**
     * Close the dialog and remove the file.
     */
    public void closeDialog(Dialog dialog) {

        this.deleteFile(fileName);

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        Toast.makeText(ProgramList.this, getString(R.string.updating_done), Toast.LENGTH_LONG).show();

        Intent refresh = new Intent(getBaseContext(), ProgramList.class);
        startActivity(refresh);
    }
}