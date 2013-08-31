package be.drupalcamp.leuven;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.List;

public class SpeakerListAdapter extends BaseAdapter implements View.OnClickListener {

    private final Context context;
    private final List<Speaker> speakers;
    private LayoutInflater mInflater;

    public SpeakerListAdapter(Context context, List<Speaker> speakers) {
        this.context = context;
        this.speakers = speakers;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return speakers.size();
    }

    public Speaker getItem(int position) {
        return speakers.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void onClick(View view) {

    }

    public static class ViewHolder {
        public int speakerId;
        public TextView speaker;
        public TextView organisation;
        public ImageView avatar;
        public LinearLayout speaker_item;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Speaker speaker = speakers.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.speaker_item, null);
            holder = new ViewHolder();
            holder.speakerId = speaker.getId();
            holder.avatar = (ImageView) convertView.findViewById(R.id.speaker_avatar);
            holder.speaker = (TextView) convertView.findViewById(R.id.speaker_name);
            holder.organisation = (TextView) convertView.findViewById(R.id.speaker_organisation);
            holder.speaker_item = (LinearLayout) convertView.findViewById(R.id.speaker_item);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (speaker != null) {

            // Avatar.
            // @todo default image vs actual avatar looks wonky.
            try {
                if (speaker.getAvatar().length() > 0) {
                    FileInputStream in =  context.getApplicationContext().openFileInput(speaker.getAvatar());
                    holder.avatar.setImageBitmap(BitmapFactory.decodeStream(in));
                }
                else {
                    holder.avatar.setImageResource(R.drawable.no_avatar);
                }
            }
            catch (Exception ignored) {}

            // Name.
            holder.speaker.setText(speaker.getFirstName() + " " + speaker.getLastName());

            // Organisation.
            holder.organisation.setText(speaker.getOrganisation());

            // Set touch listener.
            convertView.setOnTouchListener(speakerTouch);
        }

        return convertView;
    }

    /**
     * onTouchListener for speaker.
     */
    View.OnTouchListener speakerTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            ViewHolder holder = (ViewHolder)v.getTag();
            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    holder.speaker.setTextColor(context.getResources().getColor(R.color.white));
                    holder.organisation.setTextColor(context.getResources().getColor(R.color.white));
                    holder.speaker_item.setBackgroundColor(context.getResources().getColor(R.color.session_blue));
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    holder.speaker.setTextColor(context.getResources().getColor(R.color.text_dark));
                    holder.organisation.setTextColor(context.getResources().getColor(R.color.dark_grey));
                    holder.speaker_item.setBackgroundColor(context.getResources().getColor(R.color.white));
                    if (action == MotionEvent.ACTION_UP) {
                        int sessionId = holder.speakerId;
                        Intent intent = new Intent(context, SpeakerDetail.class);
                        intent.putExtra("speakerId", sessionId);
                        context.startActivity(intent);
                    }
                    break;
            }
            return true;
        }
    };
}
