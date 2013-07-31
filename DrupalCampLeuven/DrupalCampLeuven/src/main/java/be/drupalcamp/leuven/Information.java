package be.drupalcamp.leuven;

import android.os.Bundle;

public class Information extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.information);
        super.onCreate(savedInstanceState);

        // Set header title.
        setTextViewString(R.id.header_title, R.string.menu_info);

        // Set fonts.
        setFontToOpenSansLight(R.id.header_title);

    }
}