package orangepi.client;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import static orangepi.client.MainActivity.connection;
import static orangepi.client.MainActivity.in;
import static orangepi.client.MainActivity.out;
import static orangepi.client.MainActivity.status;
import static orangepi.client.MainActivity.synchronizationEveryClick;


public class BalconyButtonsActivity extends AppCompatActivity {

    public class SendTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                for (String string : strings) {
                    out.writeChars(string);
                    out.flush();
                    in.skipBytes(1);
                }
            }catch(Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (!result){
                connection = -1;
                Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_balcony_buttons);

        final Button lb[] = {
                findViewById(R.id.light_b1), findViewById(R.id.light_b2),
                findViewById(R.id.light_b3), findViewById(R.id.light_b4),
                findViewById(R.id.light_b5), findViewById(R.id.light_b6),
                findViewById(R.id.light_b7), findViewById(R.id.light_b8)
        };


        for(int i=0;i<8;i++)
        {
            if (status[i]) {
                lb[i].setBackgroundColor(getResources().getColor(R.color.light));
                lb[i].setTextColor(getResources().getColor(R.color.black));
            }
            else {
                lb[i].setBackgroundColor(getResources().getColor(R.color.dark));
                lb[i].setTextColor(getResources().getColor(R.color.white));
            }
        }

    }

    public void action(View view) {
        Button b = findViewById(view.getId());
        int nr = Integer.parseInt(b.getText().toString()) - 1;

        CharSequence message = "B" + nr;
        if (status[nr]) {
            status[nr] = false;
            b.setBackgroundColor(getResources().getColor(R.color.dark));
            b.setTextColor(getResources().getColor(R.color.white));
            message = message + "D";
        } else {
            status[nr] = true;
            b.setBackgroundColor(getResources().getColor(R.color.light));
            b.setTextColor(getResources().getColor(R.color.black));
            message = message + "L";
        }
        if (synchronizationEveryClick) new SendTask().execute(message.toString());

    }

    public void synchronize(View view) {
        CharSequence message;
        String[] messages = new String[8];
        for (int i = 0; i < 8; i++) {
            message = "B" + i;
            if (status[i])
                message = message + "L";
            else
                message = message + "D";

            messages[i] = message.toString();
        }
        new SendTask().execute(messages);
    }
}
