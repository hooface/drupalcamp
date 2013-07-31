package be.drupalcamp.leuven;

import android.os.Bundle;

public class Location extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.location);
        super.onCreate(savedInstanceState);

        // Set header title.
        setTextViewString(R.id.header_title, R.string.menu_location);

        // Set fonts.
        setFontToOpenSansLight(R.id.header_title);

    }
}