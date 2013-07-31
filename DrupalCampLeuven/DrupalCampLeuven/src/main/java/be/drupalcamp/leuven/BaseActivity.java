package be.drupalcamp.leuven;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseActivity extends Activity {

    // The eventUrl.
    public final String EventUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add listener on menu buttons.
        ImageView go_to_program = (ImageView) findViewById(R.id.menu_program);
        go_to_program.setId(1);
        go_to_program.setOnClickListener(menuBar);
        ImageView go_to_speakers = (ImageView) findViewById(R.id.menu_speakers);
        go_to_speakers.setId(2);
        go_to_speakers.setOnClickListener(menuBar);
        ImageView go_to_location = (ImageView) findViewById(R.id.menu_location);
        go_to_location.setId(3);
        go_to_location.setOnClickListener(menuBar);
        ImageView go_to_information = (ImageView) findViewById(R.id.menu_information);
        go_to_information.setId(4);
        go_to_information.setOnClickListener(menuBar);
    }

    /**
     * MenuBar button listener.
     */
    private final View.OnClickListener menuBar = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 1:
                    Intent program = new Intent(getBaseContext(), ProgramList.class);
                    startActivity(program);
                    break;
                case 2:
                    Intent speakers = new Intent(getBaseContext(), SpeakerList.class);
                    startActivity(speakers);
                    break;
                case 3:
                    Intent location = new Intent(getBaseContext(), Location.class);
                    startActivity(location);
                    break;
                case 4:
                    Intent information = new Intent(getBaseContext(), Information.class);
                    startActivity(information);
                    break;

            }
        }
    };

    /**
     * Set text title.
     *
     * @param textViewId
     *   The textView id.
     * @param stringId
     *   The string resource id.
     */
    public void setTextViewString(Integer textViewId, Integer stringId) {
        TextView tv = (TextView) findViewById(textViewId);
        tv.setText(stringId);
    }

    /**
     * Set Font to OpenSans-Light.
     *
     * @param textViewId
     *   The textView id.
     */
    public void setFontToOpenSansLight(Integer textViewId) {
        TextView tv = (TextView) findViewById(textViewId);
        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        tv.setTypeface(tf);
    }
}
