package orangepi.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import static orangepi.client.MainActivity.testMode;

public class BalconyAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_balcony_area);

        if (testMode)
        {
            setTitle(getResources().getString(R.string.app_name)  + " [Test mode]");
            findViewById(R.id.background_BAA).setBackgroundColor(
                    getResources().getColor(R.color.unclickable)
            );
        }
    }
}
