package be.drupalcamp.leuven;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.List;

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

        ImageView iv = (ImageView) findViewById(R.id.speaker_avatar);
        // Avatar.
        try {
            if (speaker.getAvatar().length() > 0) {
                FileInputStream in = openFileInput(speaker.getAvatar());
                iv.setImageBitmap(BitmapFactory.decodeStream(in));
            }
            else {
                //FileInputStream in =
                //Drawable d = getResources().getDrawable(R.drawable.no_avatar);
                iv.setImageResource(R.drawable.no_avatar);
            }
        }
        catch (Exception ignored) {}

        // Speaker name.
        TextView st = (TextView) findViewById(R.id.speaker_name);
        st.setText(speaker.getFirstName() + ' ' + speaker.getLastName());

        // Speaker organisation.
        TextView so = (TextView) findViewById(R.id.speaker_organisation);
        if (speaker.getOrganisation().length() > 0) {
            so.setText(speaker.getOrganisation());
        }
        else {
            so.setVisibility(TextView.GONE);
        }

        // Speaker twitter.
        TextView stw = (TextView) findViewById(R.id.speaker_twitter);
        if (speaker.getTwitter().length() > 0) {
            stw.setText("@" + speaker.getTwitter());
        }
        else {
            stw.setVisibility(TextView.GONE);
        }

        // Sessions of this speaker.
        List<Session> sessions = db.getSpeakerSessions(speaker.getId());
        SessionListSmallAdapter adapter = new SessionListSmallAdapter(this, sessions);
        int dp = (int) getResources().getDimension(R.dimen.global_padding);
        int dp_small = (int) getResources().getDimension(R.dimen.global_small_padding);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dp, dp_small, dp, dp_small);

        LinearLayout session_list = (LinearLayout) findViewById(R.id.session_list);

        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, null);
            item.setLayoutParams(layoutParams);
            session_list.addView(item);
        }

        // Set fonts.
        setFontToOpenSansLight(R.id.header_title);
        setFontToOpenSansLight(R.id.speaker_name);
    }

}
