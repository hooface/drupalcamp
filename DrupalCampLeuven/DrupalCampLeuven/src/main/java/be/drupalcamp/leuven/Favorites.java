package be.drupalcamp.leuven;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

public class Favorites extends BaseActivity {

    public List<Session> sessions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Do not show on favorites button.
        showFavoritesButton = false;

        setContentView(R.layout.favorites);
        super.onCreate(savedInstanceState);

        DatabaseHandler db = new DatabaseHandler(this);
        sessions = db.getFavorites();

        SessionListSmallAdapter adapter = new SessionListSmallAdapter(this, sessions);

        int dp = (int) getResources().getDimension(R.dimen.global_padding);
        int dp_small = (int) getResources().getDimension(R.dimen.global_small_padding);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dp, dp, dp, dp_small);

        LinearLayout favorites_list = (LinearLayout) findViewById(R.id.favorites_list);

        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, null);
            item.setLayoutParams(layoutParams);
            favorites_list.addView(item);
        }

        // Set header title.
        setTextViewString(R.id.header_title, R.string.menu_favorites);

        // Set fonts and colors.
        setFontToOpenSansLight(R.id.header_title);
        setHeaderBackgroundColor(getResources().getColor(R.color.session));
    }}
