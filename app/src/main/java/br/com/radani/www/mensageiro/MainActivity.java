package br.com.radani.www.mensageiro;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements BLeSerialPortService.Callback, View.OnClickListener {

    gerenciadorBanco meuBanco;

    public String query = "";

    // UI elements
    private TextView messages;
    private EditText input;
    private Button   connect;
    private static final String TAG2 = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    // BLE serial port instance.  This is defined in BLeSerialPortService.java.
    private BLeSerialPortService serialPort;
    private final int REQUEST_DEVICE = 3;
    private final int REQUEST_ENABLE_BT = 2;
    private  int rindex = 0;


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new leituraConfiguracao(), "Configuração");
        adapter.addFragment(new leituraDisplay(), "Display");
        adapter.addFragment(new leituraFalhas(), "Falhas");
        adapter.addFragment(new leituraParametrizacao(), "Parametrização");
        adapter.addFragment(new leituraSequencia(), "Sequência");

        viewPager.setAdapter(adapter);
    }



    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            serialPort = ((BLeSerialPortService.LocalBinder) rawBinder).getService()

                    //register the application context to service for callback
                    .setContext(getApplicationContext())
                    .registerCallback(MainActivity.this);
        }

        public void onServiceDisconnected(ComponentName classname) {
            serialPort.unregisterCallback(MainActivity.this)
                    //Close the bluetooth gatt connection.
                    .close();
        }
    };

    // Write some text to the messages text view.
    private void writeLine(final CharSequence text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {





                messages.append(text);
                messages.append("\n");
            }
        });
    }

private final boolean bolha = true;
    public void sendView(View view) {


        if (input != null) {
            String commandString = input.getText().toString();
            if (commandString.isEmpty()) return;

   // Adição de comandos em HEX
            if (commandString.length() % 2 == 1) {
                commandString = "0" + commandString;
                input.setText(commandString);
            }
            byte[] command = (bolha ? Utils.toHex(commandString) : commandString.getBytes());
            serialPort.send(command);
        }
    }


    private static int PERMISSION_REQUEST_CODE = 1;
    private final static String TAG = MainActivity.class.getSimpleName();


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if(requestCode == PERMISSION_REQUEST_CODE)
        {
            //Do something based on grantResults
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d(TAG, "coarse location permission granted");
            }
            else
            {
                Log.d(TAG, "coarse location permission denied");
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        meuBanco = new gerenciadorBanco(this);
        query = gerenciadorBanco.retornaquery(query);

        meuBanco.insertData("P00","Tmp.Ciclos",0.0,8.0,0.5,"texto1", "texto2", "texto3", "texto4", "texto5", "texto6", "texto7", "texto8");

        Log.d(TAG, "Request Location Permissions:");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
            Log.d(TAG, "onCreate: Starting.");
            mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            setupViewPager(mViewPager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);


            // Grab references to UI elements.
            messages = (TextView) findViewById(R.id.messages);
            input = (EditText) findViewById(R.id.input);
            input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            input.setFilters(new InputFilter[]{new Utils.InputFilterHex()});

            // Enable auto-scroll in the TextView
            messages.setMovementMethod(new ScrollingMovementMethod());

            connect = (Button) findViewById(R.id.connect);
            connect.setOnClickListener(this);

            // bind and start the bluetooth service
            Intent bindIntent = new Intent(this, BLeSerialPortService.class);
            bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    // OnResume, called right before UI is displayed.  Connect to the bluetooth device.
    @Override
    protected void onResume() {
        super.onResume();

        // set the screen to portrait
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        // if the bluetooth adatper is not support and enabled
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            finish();
        }

        // request to open the bluetooth adapter
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    // OnStop, close the service connection
    @Override
    protected void onStop() {
        super.onStop();
        serialPort.stopSelf();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    @Override
    public  void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        Button bt = (Button) v;
        if (v.getId() == R.id.connect) {
            // the device can send data to
            if (bt.getText().equals(getResources().getString(R.string.send))) {
                sendView(v);
            }
            // if the device is not connectted
            if (bt.getText().equals(getResources().getString(R.string.connect))) {
                Intent intent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(intent, REQUEST_DEVICE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // serial port Callback handlers.
    @Override
    public void onConnected(Context context) {
        // when serial port device is connected
        writeLine("Connected!");
        writeLine(query);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CharSequence message = extras.getString("message");
            writeLine(message);
        }

        // Enable the send button
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connect = (Button) findViewById(R.id.connect);
                connect.setText(R.string.send);

                // Onde dispara as mensagens iniciais

                serialPort.send(Utils.comando("FA54000054"));
                serialPort.send(Utils.comando("FA54000155"));
                serialPort.send(Utils.comando("FA54000256"));
                serialPort.send(Utils.comando("FA54000357"));
                serialPort.send(Utils.comando("FA54000450"));
                serialPort.send(Utils.comando("FA54000551"));
                serialPort.send(Utils.comando("FA54000652"));
                serialPort.send(Utils.comando("FA54000753"));
                serialPort.send(Utils.comando("FA5400085C"));
                serialPort.send(Utils.comando("FA5400095D"));
                serialPort.send(Utils.comando("FA54000A5E"));
                serialPort.send(Utils.comando("FA54000B5F"));
                serialPort.send(Utils.comando("FA54000C58"));
                serialPort.send(Utils.comando("FA54000D59"));
                serialPort.send(Utils.comando("FA54000E5A"));
                serialPort.send(Utils.comando("FA54000F5B"));
                serialPort.send(Utils.comando("FA54001044"));
                serialPort.send(Utils.comando("FA54001145"));
                serialPort.send(Utils.comando("FA54001246"));
                serialPort.send(Utils.comando("FA54001347"));
                serialPort.send(Utils.comando("FA54001440"));
                serialPort.send(Utils.comando("FA54001541"));
                serialPort.send(Utils.comando("FA54001743"));
                serialPort.send(Utils.comando("FA5400184C"));
                serialPort.send(Utils.comando("FA5400194D"));
                serialPort.send(Utils.comando("FA54001A4E"));
                serialPort.send(Utils.comando("FA54001B4F"));
                serialPort.send(Utils.comando("FA54001C48"));
                serialPort.send(Utils.comando("FA54001D49"));
                serialPort.send(Utils.comando("FA54001E4A"));
                serialPort.send(Utils.comando("FA54001F4B"));
                serialPort.send(Utils.comando("FA54002074"));
            }
        });
    }

    @Override
    public void onConnectFailed(Context context) {
        // when some error occured which prevented serial port connection from completing.
        writeLine("Error connecting to device!");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connect = (Button) findViewById(R.id.connect);
                connect.setText(R.string.connect);
            }
        });
    }

    @Override
    public void onDisconnected(Context context) {
        //when device disconnected.
        writeLine("Disconnected!");
        // update the send button text to connect
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connect = (Button)findViewById(R.id.connect);
                connect.setText(R.string.connect);
            }
        });
    }

    @Override
    public void onCommunicationError(int status, String msg) {
        // get the send value bytes
        if (status > 0) {
        }// when the send process found error, for example the send thread  time out
        else {
            writeLine("send error status = " + status);

        }
    }

    @Override
    public void onReceive(Context context, BluetoothGattCharacteristic rx) {


        String msg = rx.getStringValue(0);
        rindex = rindex + msg.length();
        byte[] msgbyte = rx.getValue();
        writeLine(Utils.bytesToHex(msgbyte));

    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {
        // Called when a UART device is discovered (after calling startScan).
        writeLine("Found device : " + device.getAddress());
        writeLine("Waiting for a connection ...");

    }

    @Override
    public void onDeviceInfoAvailable() {
        writeLine(serialPort.getDeviceInfo());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DEVICE:
                //When the DeviceListActivity return, with the selected device address
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
                    serialPort.connect(device);
                    showMessage(device.getName());
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth ligado ", Toast.LENGTH_SHORT).show();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(this, "Problema ao ligar o Bluetooth ", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void showMessage(String msg) {
        Log.e(MainActivity.class.getSimpleName(), msg);
    }

}