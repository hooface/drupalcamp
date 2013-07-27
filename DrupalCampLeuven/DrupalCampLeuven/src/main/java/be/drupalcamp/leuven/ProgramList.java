package be.drupalcamp.leuven;

import android.os.Bundle;

public class ProgramList extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.program_list);
        super.onCreate(savedInstanceState);

        // Set font of header.
        setFontToOpenSansLight(R.id.header_title);
    }
}