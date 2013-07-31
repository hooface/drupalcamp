package be.drupalcamp.leuven;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

public class ProgramList extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.program_list);
        super.onCreate(savedInstanceState);

        // Set header title.
        setTextViewString(R.id.header_title, R.string.menu_program);

        // Set fonts.
        setFontToOpenSansLight(R.id.header_title);
        setFontToOpenSansLight(R.id.day_1_text);
        setFontToOpenSansLight(R.id.day_2_text);

        // Set listeners on day arrows.
        final ViewFlipper switcher = (ViewFlipper) findViewById(R.id.dayFlipper);
        ImageButton day1 = (ImageButton) findViewById(R.id.day_1);
        day1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AnimationUtils();
                switcher.setAnimation(AnimationUtils.makeInAnimation(getApplicationContext(), true));
                switcher.showNext();
            }
        });
        ImageButton day2 = (ImageButton) findViewById(R.id.day_2);
        day2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AnimationUtils();
                switcher.setAnimation(AnimationUtils.makeInAnimation(getApplicationContext(), true));
                switcher.showPrevious();
            }
        });

    }
}