package be.drupalcamp.leuven;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Event list adapter.
 */
public class SessionsListAdapter extends BaseAdapter implements OnClickListener {
    private final Context context;
    private final List<Session> sessions;

    public SessionsListAdapter(Context context, List<Session> sessions) {
        this.context = context;
        this.sessions = sessions;
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

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.session_list_item, null);
        }

        final Session session = sessions.get(position);
        if (session != null) {

            // Title.
            final TextView tt = (TextView) convertView.findViewById(R.id.session_title);
            String title = session.getTitle();
            tt.setText(title);

        }

        return convertView;
    }
}