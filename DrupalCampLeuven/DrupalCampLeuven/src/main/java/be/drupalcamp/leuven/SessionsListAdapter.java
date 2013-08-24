package be.drupalcamp.leuven;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Session list adapter.
 */
public class SessionsListAdapter extends BaseAdapter implements OnClickListener {
    private final Context context;
    private final List<Session> sessions;
    private LayoutInflater mInflater;

    private static final int NORMAL = 0;
    private static final int SPECIAL = 1;

    public SessionsListAdapter(Context context, List<Session> sessions) {
        this.context = context;
        this.sessions = sessions;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return sessions.size();
    }

    public Session getItem(int position) {
        return sessions.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void onClick(View view) {

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return sessions.get(position).getSpecial() == 0 ? NORMAL : SPECIAL;
    }

    public static class ViewHolder {
        public TextView title;
        public TextView speaker;
        public TextView time;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        Session session = sessions.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case NORMAL:
                    convertView = mInflater.inflate(R.layout.session_normal_item, null);
                    holder.title = (TextView) convertView.findViewById(R.id.session_title);
                    holder.speaker = (TextView) convertView.findViewById(R.id.session_speakers);
                    holder.time = (TextView) convertView.findViewById(R.id.session_time);
                    break;
                case SPECIAL:
                    convertView = mInflater.inflate(R.layout.session_special_item, null);
                    holder.title = (TextView) convertView.findViewById(R.id.session_title);
                    break;
            }
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (session != null) {

            // Title.
            holder.title.setText(session.getTitle());

            // Normal sessions get speakers, hour and favorite button.
            if (session.getSpecial() == 0) {

                String speakers = "";
                List<Speaker> speakerList = session.getSpeakers();
                for (int i = 0; i < speakerList.size(); i++) {
                    Speaker speaker = speakerList.get(i);
                    // @todo count and implode with ','.
                    speakers += speaker.getFirstName() + " " + speaker.getLastName() + " ";
                }
                holder.speaker.setText(speakers);


                // Session time.
                int from = session.getStartDate();
                int to = session.getEndDate();
                DateFormat sdf = new SimpleDateFormat("kk:mm");
                Date startHour = new Date((long)from * 1000);
                Date endHour = new Date((long)to * 1000);
                holder.time.setText(sdf.format(startHour) + " - " + sdf.format(endHour));
            }
        }

        return convertView;
    }
}