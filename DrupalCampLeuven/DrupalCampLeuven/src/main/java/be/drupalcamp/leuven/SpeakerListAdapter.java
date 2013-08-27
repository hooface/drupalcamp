package be.drupalcamp.leuven;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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

    public View getView(final int position, View convertView, ViewGroup parent) {

        return convertView;
    }
}
