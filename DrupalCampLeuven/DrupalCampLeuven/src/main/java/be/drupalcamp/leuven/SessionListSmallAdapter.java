package be.drupalcamp.leuven;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Session list adapter.
 */
public class SessionListSmallAdapter extends BaseAdapter implements OnClickListener {
    private final Context context;
    private final List<Session> sessions;
    private LayoutInflater mInflater;

    private static final int NORMAL = 0;
    private static final int SPECIAL = 1;

    public SessionListSmallAdapter(Context context, List<Session> sessions) {
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
        public int sessionId;
        public TextView title;
        public TextView time;
        public LinearLayout session_item;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Session session = sessions.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            holder.sessionId = session.getId();

            convertView = mInflater.inflate(R.layout.session_small_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.session_title);
            holder.time = (TextView) convertView.findViewById(R.id.session_time);
            holder.session_item = (LinearLayout) convertView.findViewById(R.id.session_item);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (session != null) {

            // Title.
            holder.title.setText(session.getTitle());

            // Time.
            String Date = "";
            int from = session.getStartDate();
            int to = session.getEndDate();
            DateFormat sdf = new SimpleDateFormat("kk:mm");
            Date startHour = new Date((long)from * 1000);
            Date endHour = new Date((long)to * 1000);
            if (session.getDay() == 14) {
                Date = "September 14th";
            }
            else {
                Date = "September 15th";
            }
            holder.time.setText(Date + " | " + sdf.format(startHour) + " - " + sdf.format(endHour));

            // Set touch listener.
            convertView.setOnTouchListener(sessionTouch);
        }

        return convertView;
    }

    /**
     * onTouchListener for session.
     */
    View.OnTouchListener sessionTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            ViewHolder holder = (ViewHolder)v.getTag();
            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    holder.title.setTextColor(context.getResources().getColor(R.color.white));
                    holder.time.setTextColor(context.getResources().getColor(R.color.white));
                    holder.session_item.setBackgroundColor(context.getResources().getColor(R.color.session));
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    holder.title.setTextColor(context.getResources().getColor(R.color.text_dark));
                    holder.time.setTextColor(context.getResources().getColor(R.color.dark_grey));
                    holder.session_item.setBackgroundColor(context.getResources().getColor(R.color.white));
                    if (action == MotionEvent.ACTION_UP) {
                        int sessionId = holder.sessionId;
                        Intent intent = new Intent(context, SessionDetail.class);
                        intent.putExtra("sessionId", sessionId);
                        context.startActivity(intent);
                    }
                    break;
            }
            return true;
        }
    };

}