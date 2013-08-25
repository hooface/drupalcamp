package be.drupalcamp.leuven;

import android.os.Bundle;

public class SpeakerList extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.speaker_list);
        super.onCreate(savedInstanceState);

        // Set header title.
        setTextViewString(R.id.header_title, R.string.menu_speakers);

        // Set fonts.
        setFontToOpenSansLight(R.id.header_title);



    }
}