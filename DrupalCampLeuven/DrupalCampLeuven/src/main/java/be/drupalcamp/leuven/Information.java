package be.drupalcamp.leuven;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Information extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.information);
        super.onCreate(savedInstanceState);

        // Set header title.
        setTextViewString(R.id.header_title, R.string.menu_info);

        // Set information text. We're doing this programmatically so we can
        // use HTML in the strings and make them behave what they are made for.
        String text = (String)getResources().getText(R.string.information_text);
        TextView t = (TextView) findViewById(R.id.information_text);
        t.setMovementMethod(LinkMovementMethod.getInstance());
        t.setText(Html.fromHtml(text));

        // Set fonts.
        setFontToOpenSansLight(R.id.header_title);
    }
}