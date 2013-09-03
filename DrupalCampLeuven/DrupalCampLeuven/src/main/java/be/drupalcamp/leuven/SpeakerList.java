package be.drupalcamp.leuven;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class SpeakerList extends BaseActivity {

    public List<Speaker> speakers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.speaker_list);
        super.onCreate(savedInstanceState);

        DatabaseHandler db = new DatabaseHandler(this);
        speakers = db.getSpeakers();

        ListView speaker_list = (ListView) findViewById(R.id.speaker_list);
        SpeakerListAdapter adapter = new SpeakerListAdapter(this, speakers);
        speaker_list.setAdapter(adapter);

        // Set header title.
        setTextViewString(R.id.header_title, R.string.menu_speakers);

        // Set fonts and colors.
        setFontToOpenSansLight(R.id.header_title);
        setHeaderBackgroundColor(getResources().getColor(R.color.speakers));
    }
}