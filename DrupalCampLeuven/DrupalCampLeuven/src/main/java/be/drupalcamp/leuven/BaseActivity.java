package be.drupalcamp.leuven;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Set Font to OpenSans-Light.
     *
     * @param Id
     *   The resource id.
     */
    public void setFontToOpenSansLight(Integer Id) {
        TextView tv = (TextView) findViewById(Id);
        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        tv.setTypeface(tf);
    }
}
