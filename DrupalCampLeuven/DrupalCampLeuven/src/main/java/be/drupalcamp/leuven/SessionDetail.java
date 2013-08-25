package be.drupalcamp.leuven;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SessionDetail extends BaseActivity {

    public Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.session_detail);
        super.onCreate(savedInstanceState);

        // Get session.
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        int sessionId = extras.getInt("sessionId");
        DatabaseHandler db = new DatabaseHandler(this);
        session = db.getSession(sessionId);

        // Set header title.
        setTextViewString(R.id.header_title, R.string.menu_program);

        // Set session title.
        TextView st = (TextView) findViewById(R.id.session_title);
        st.setText(session.getTitle());
        TextView sd = (TextView) findViewById(R.id.session_description);
        sd.setText(session.getDescription());

        // Set fonts.
        setFontToOpenSansLight(R.id.header_title);

        // Set favorite button and attach listener.
        ImageButton favoriteButton = (ImageButton) findViewById(R.id.session_favorite);
        favoriteButton.setOnClickListener(actionFavorite);
        if (session.getFavorite() == 0) {
            favoriteButton.setImageResource(R.drawable.non_favorited_session);
        }
        else {
            favoriteButton.setImageResource(R.drawable.favorited_session);
        }

        // Set fonts.
        setFontToOpenSansRegular(R.id.session_title);
    }

    /**
     * Favorite listener.
     */
    private final View.OnClickListener actionFavorite = new View.OnClickListener() {
        public void onClick(View v) {
            // Get favorite.
            int favorite = session.getFavorite();

            // Switch image.
            ImageView i = (ImageView) findViewById(R.id.session_favorite);
            int setFavorite;
            if (favorite == 0) {
                setFavorite = 1;
                i.setImageResource(R.drawable.favorited_session);
            } else {
                setFavorite = 0;
                i.setImageResource(R.drawable.non_favorited_session);
            }

            // Update in database.
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            db.saveFavorite(setFavorite, session.getId());

            // Update session in memory as well.
            session.setFavorite(setFavorite);
        }
    };
}
