package be.drupalcamp.leuven;


import android.os.Bundle;
import android.widget.TextView;

public class SpeakerDetail extends BaseActivity {

    public Speaker speaker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.speaker_detail);
        super.onCreate(savedInstanceState);

        // Get speaker.
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        int speakerId = extras.getInt("speakerId");
        DatabaseHandler db = new DatabaseHandler(this);
        speaker = db.getSpeaker(speakerId);

        // Set header title.
        setTextViewString(R.id.header_title, R.string.menu_speakers);

        // Set speaker name.
        TextView st = (TextView) findViewById(R.id.speaker_name);
        st.setText(speaker.getFirstName() + ' ' + speaker.getLastName());
        //TextView sd = (TextView) findViewById(R.id.session_description);
        //sd.setText(speaker.get);

        // Set fonts.
        setFontToOpenSansLight(R.id.header_title);

        // Set fonts.
        setFontToOpenSansLight(R.id.speaker_name);
    }

}
