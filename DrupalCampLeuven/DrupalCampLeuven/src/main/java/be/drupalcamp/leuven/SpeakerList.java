package be.drupalcamp.leuven;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

public class SpeakerList extends BaseActivity {

    public List<Speaker> speakers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.speaker_list);
        super.onCreate(savedInstanceState);

        DatabaseHandler db = new DatabaseHandler(this);
        speakers = db.getSpeakers();

        SpeakerListAdapter adapter = new SpeakerListAdapter(this, speakers);

        int dp = (int) getResources().getDimension(R.dimen.global_padding);
        int dp_small = (int) getResources().getDimension(R.dimen.global_small_padding);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dp, dp, dp, dp_small);

        LinearLayout speaker_list = (LinearLayout) findViewById(R.id.speaker_list);

        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, null);
            item.setLayoutParams(layoutParams);
            speaker_list.addView(item);
        }

        // Set header title.
        setTextViewString(R.id.header_title, R.string.menu_speakers);

        // Set fonts.
        setFontToOpenSansLight(R.id.header_title);
    }
}