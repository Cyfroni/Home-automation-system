package orangepi.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;

import static orangepi.client.MainActivity.synchronizationEveryClick;
import static orangepi.client.MainActivity.testMode;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_options);

        ((Switch)findViewById(R.id.switch_1)).setChecked(synchronizationEveryClick);
        ((Switch)findViewById(R.id.switch_2)).setChecked(testMode);
    }

    public void option(View view) {

        switch(view.getId())
        {
            case R.id.switch_1:
                synchronizationEveryClick = !synchronizationEveryClick;
                break;
            case R.id.switch_2:
                testMode = !testMode;
                break;
            default:
                return;
        }
    }
}
