package be.drupalcamp.leuven;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.List;

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
    public List<Session> sessions;

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

            // Define layout params.
            int dp = (int) getResources().getDimension(R.dimen.global_padding);
            int dp_small = (int) getResources().getDimension(R.dimen.global_small_padding);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(dp, dp, dp, dp_small);

            // @todo make this more configurable so you can have multiple days without hardcoding.
            // For instance, put this in array integer list. That will require us to create dynamic
            // flippers, listeners on the fly, but that will be good in the end.
            int day1_integer = Integer.parseInt(getString(R.string.day_1_integer));
            int day2_integer = Integer.parseInt(getString(R.string.day_2_integer));

            LinearLayout day1_layout = (LinearLayout) findViewById(R.id.day_flip_1);
            getSessionsPerDay(day1_layout, layoutParams, db, day1_integer);

            LinearLayout day2_layout = (LinearLayout) findViewById(R.id.day_flip_2);
            getSessionsPerDay(day2_layout, layoutParams, db, day2_integer);

            // Set listeners on day bars.
            RelativeLayout day1 = (RelativeLayout) findViewById(R.id.day_1_bar);
            day1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new AnimationUtils();
                    switcher.setAnimation(AnimationUtils.makeInAnimation(ProgramList.this, false));
                    switcher.showNext();
                }
            });
            RelativeLayout day2 = (RelativeLayout) findViewById(R.id.day_2_bar);
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
     * Get sessions per day.
     */
    public void getSessionsPerDay(LinearLayout layout, LinearLayout.LayoutParams layoutParams, DatabaseHandler db, Integer day) {
        sessions = db.getSessions(day);

        SessionsListAdapter adapter = new SessionsListAdapter(this, sessions);

        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, null);
            item.setLayoutParams(layoutParams);
            layout.addView(item);
        }
    }

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
                    // TODO cleanup this whole routine and move to separate file.

                    JSONArray sessions = null;
                    String json = new BufferedReader(new InputStreamReader(openFileInput(fileName), "UTF-8")).readLine();

                    try {
                        sessions = new JSONArray(json);
                    }
                    catch (Exception ignored) {}

                    // Return early if necessary.
                    if (sessions == null) {
                        return "parsingfailed";
                    }

                    // Get number of sessions.
                    numberOfSessions = sessions.length();

                    try {

                        int count = 1;
                        DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
                        handler.truncateTable();

                        for (int i = 0; i < numberOfSessions; i++){

                            // Get session from json.
                            JSONObject jsonSession = sessions.getJSONObject(i);

                            // Create new session object.
                            Session session = new Session();
                            session.setId(jsonSession.getInt("id"));
                            session.setTitle(jsonSession.getString("title"));
                            if (!jsonSession.isNull("description")) {
                                session.setDescription(jsonSession.getString("description"));
                            }
                            session.setSpecial(jsonSession.getInt("special"));
                            session.setStartDate(jsonSession.getInt("from"));
                            session.setEndDate(jsonSession.getInt("to"));
                            if (!jsonSession.isNull("level")) {
                                session.setLevel(jsonSession.getInt("level"));
                            }
                            session.setDay(jsonSession.getInt("day"));

                            // Save session
                            handler.insertSession(session);

                            // Speakers.
                            if (!jsonSession.isNull("speakers")) {
                                JSONArray speakers = jsonSession.getJSONArray("speakers");
                                if (speakers.length() > 0) {
                                    for (int j = 0; j < speakers.length(); j++) {
                                        // Get speaker from from json.
                                        JSONObject jsonSpeaker = speakers.getJSONObject(j);

                                        // Create new speaker object.
                                        Speaker speaker = new Speaker();
                                        speaker.setId(jsonSpeaker.getInt("id"));
                                        speaker.setSessionId(session.getId());
                                        speaker.setUsername(jsonSpeaker.getString("username"));
                                        speaker.setFirstName(jsonSpeaker.getString("first_name"));
                                        speaker.setLastName(jsonSpeaker.getString("last_name"));
                                        if (!jsonSpeaker.isNull("organization")) {
                                            speaker.setOrganisation(jsonSpeaker.getString("organization"));
                                        }
                                        if (!jsonSpeaker.isNull("twitter")) {
                                            speaker.setTwitter(jsonSpeaker.getString("twitter"));
                                        }
                                        if (!jsonSpeaker.isNull("avatar")) {
                                            speaker.setAvatar(jsonSpeaker.getString("avatar"));
                                        }

                                        // Save speaker.
                                        handler.insertSpeaker(speaker);
                                    }
                                }
                            }

                            // Notify dialog.
                            int update = (count*100/numberOfSessions);
                            count++;
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
     * Close the dialog, remove the file and refresh the activity.
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