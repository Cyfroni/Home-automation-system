package orangepi.client;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    static Socket soc;
    static DataOutputStream out;
    static DataInputStream in;
    static int connection = -1;
    static boolean testMode = false;
    static boolean synchronizationEveryClick = true;
    static boolean status[] = {
            false, false,
            false, false,
            false, false,
            false, false
    };

    Button sb;
    Button cb;
    TextView hostView;
    TextView portView;
    TextView infoView;

    class Connect extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... arg0) {
            try {
                soc = new Socket(hostView.getText().toString(), Integer.parseInt(portView.getText().toString()));
                out = new DataOutputStream(soc.getOutputStream());
                in = new DataInputStream(soc.getInputStream());

                out.writeChars("XXX");
                out.flush();
                if(in.readByte() == '1') return true;
            } catch (Exception ignored) {

            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                infoView.setText("Connected successfully");
                cb.setText("Disconnect");
                sb.setBackgroundColor(getResources().getColor(R.color.clickable));
                connection = 1;
                testMode = false;
            }
            else {
                infoView.setText("Connection Error");
                connection = -1;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        sb = findViewById(R.id.start_b);
        cb = findViewById(R.id.connect_b);
        hostView = findViewById(R.id.ip_f);
        portView = findViewById(R.id.port_f);
        infoView = findViewById(R.id.info_f);


        if(connection == 1){
            cb.setText("Disconnect");
            sb.setBackgroundColor(getResources().getColor(R.color.clickable));
        }
    }

    public void start(View view) {
        if (connection == 1 || testMode) {
            startActivity(new Intent(this, ChooseActivity.class));
        }
    }

    public void options(View view) {
        startActivity(new Intent(this, OptionsActivity.class));
    }

    public void shutDown(View view) {
        disconnect(true);
    }

    public void connect(View view) {

        if(connection == -1) {
            infoView.setText("Connecting..");
            connection = 0;
            new Connect().execute();

        }
        else if (connection == 1)
        {
            disconnect(false);
        }
    }

    public void disconnect(final boolean shutDown) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if(shutDown)
                            {
                                if(connection == 1) {
                                    out.writeChars("SHD");
                                    out.flush();
                                    //if(in.readByte() != '1') throw new Exception("error");
                                    connection = -1;
                                }
                                finish();
                            }
                            out.close();
                            soc.close();
                            connection = -1;
                            cb.setText("Connect");
                            infoView.setText("Disconnected successfully");
                            sb.setBackgroundColor(getResources().getColor(R.color.unclickable));
                        } catch (Exception e) {
                            infoView.setText("Disconnection Error");
                        }
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
