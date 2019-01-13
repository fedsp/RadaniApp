package br.com.radani.www.mensageiro;

import android.app.Activity;
import android.app.FragmentManager;
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
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentTransaction;
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
import java.util.concurrent.TimeUnit;



public class MainActivity extends AppCompatActivity implements BLeSerialPortService.Callback, View.OnClickListener {

    gerenciadorBanco meuBanco;
    public String query = "";


    // UI elements
    private TextView messages;
    private EditText input;
    private Button   connect;
    private static final String TAG2 = "MainActivity";
    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    // BLE serial port instance.  This is defined in BLeSerialPortService.java.
    private BLeSerialPortService serialPort;
    private final int REQUEST_DEVICE = 3;
    private final int REQUEST_ENABLE_BT = 2;
    private int rindex = 0;
    Bundle bundletodosDados = new Bundle();

    Globals sharedData = Globals.getInstance();


    private void setupViewPager(ViewPager viewPager, Bundle bundleDados) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new leituraConfiguracao(), "Configuração");
        adapter.addFragment(new leituraDisplay(), "Display");
        adapter.addFragment(new leituraFalhas(), "Falhas");
        adapter.addFragment(new leituraParametrizacao(), "Parametrização");
        adapter.addFragment(new leituraSequencia(), "Sequência");

        leituraConfiguracao fragmentConfiguracao = (leituraConfiguracao) adapter.getItem(0);
        fragmentConfiguracao.escreveConfiguracoes(bundleDados);

        leituraDisplay fragmentDisplay = (leituraDisplay) adapter.getItem(1);
        fragmentDisplay.escreveDisplay(bundleDados);

        leituraFalhas fragmentFalhas = (leituraFalhas) adapter.getItem(2);
        fragmentFalhas.escreveFalhas(bundleDados);

        leituraParametrizacao fragmentParametrizacao = (leituraParametrizacao) adapter.getItem(3);
        fragmentParametrizacao.escreveParametros(bundleDados);

        leituraSequencia fragmentSequencia = (leituraSequencia) adapter.getItem(4);
        fragmentSequencia.escreveSequencia(bundleDados);

        viewPager.setAdapter(adapter);

    }

    // controla para que só envie uma segunda mensagem depois que a primeira for respondida
    public String controlePacotes(String comandotexto)
    {

        Boolean flagControle;
        flagControle = sharedData.getValue();


        while (!flagControle){
            flagControle = sharedData.getValue();
            }

        return comandotexto;
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

    // Escreve textos na textview
    private void writeLine(final CharSequence text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                messages.append(text);
                messages.append("\n");
            }
        });
    }




    public void sendView(View view) {


        if (input != null) {
            String commandString = input.getText().toString();
            if (commandString.isEmpty()) return;

   // Adição de comandos em HEX
            if (commandString.length() % 2 == 1) {
                commandString = "0" + commandString;
                input.setText(commandString);
            }
            byte[] command = (Utils.toHex(commandString));
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


    // Assim que main activity é criada
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        leituraParametrizacao leitura = new leituraParametrizacao();
    // Passando um link
        String link = "google.com";
        Bundle bundleteste = new Bundle();
        bundleteste.putString("link", link);
        leitura.setArguments(bundleteste);

        meuBanco = new gerenciadorBanco(this);
        query = gerenciadorBanco.retornaquery(query);

        // cria tabela de parametros 1 já com valores
        meuBanco.insertData_PARAM_1("P00","Tmp.Ciclos",0.0,8.0,0.5, null, null, null, null, null, null, null, null,"s");
        meuBanco.insertData_PARAM_1("P01","Sel.Ciclo", null, null, null,"Contínuo", "Único", null, null, null, null, null, null,"s");
        meuBanco.insertData_PARAM_1("P02","Impressão", null, null, null,"Monocolor", "Bicolor", "Tricolor", "Estendido", null, null, null, null,null);
        meuBanco.insertData_PARAM_1("P03","Nr.Mistura",1.0,5.0,1.0, null, null, null, null, null, null, null, null,null);
        meuBanco.insertData_PARAM_1("P04","Tempo_00",0.1,10.0,0.1, null, null, null, null, null, null, null, null,"s");
        meuBanco.insertData_PARAM_1("P05","Tempo_01",0.1,10.0,0.1, null, null, null, null, null, null, null, null,"s");
        meuBanco.insertData_PARAM_1("P06","Tempo_Topo",0.1,10.0,0.1, null, null, null, null, null, null, null, null,"s");
        meuBanco.insertData_PARAM_1("P07","Tp.Clic.Av",0.0,0.5,0.1, null, null, null, null, null, null, null, null,"s");
        meuBanco.insertData_PARAM_1("P08","Tp.Clic.Rv",0.0,0.5,0.1, null, null, null, null, null, null, null, null,"s");
        meuBanco.insertData_PARAM_1("P09","Acel.Imp.",0.0,0.5,0.01, null, null, null, null, null, null, null, null,"s");
        meuBanco.insertData_PARAM_1("P10","Edita_Seq.", null, null, null,"Não", "Sim", null, null, null, null, null, null,"s");
        meuBanco.insertData_PARAM_1("P11","Reset", null, null, null,"Não", "Sim", null, null, null, null, null, null,"s");


        // cria tabela de configuracoes 1 já com valores
        meuBanco.insertData_CONFIG_1("C00","Salva",null,null,null,"Não","Sim",null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C01","Master",null,null,null,"Não","Sim",null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C02","Tmp.Sensor",0.0,10.0,0.5,null,null,null,null,null,null,null,null,"s");
        meuBanco.insertData_CONFIG_1("C03","Modo _Start",null,null,null,"Start Unico","Start Uni/Const","Start OU Tecla","Start E Tecla","Start Nivel",null,null,null,null);
        meuBanco.insertData_CONFIG_1("C04","Temp.Start",0.0,5.0,0.5,null,null,null,null,null,null,null,null,"s");
        meuBanco.insertData_CONFIG_1("C05","Fim Impr.",null,null,null,"Sensor Topo","Sensor Tint.","Sensor Peca",null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C06","Tmp AV/Rec",0.0,0.5,0.1,null,null,null,null,null,null,null,null,"s");
        meuBanco.insertData_CONFIG_1("C07","Tmp.Peca",0.0,0.5,0.01,null,null,null,null,null,null,null,null,"s");
        meuBanco.insertData_CONFIG_1("C08","Modo E1",null,null,null,"Normal Aberto","Normal Fechado",null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C09","Funcao E1",null,null,null,"Sequencia","Start","Emergencia","Porta","Lib.Ciclo","Seq.Monit.",null,null,null);
        meuBanco.insertData_CONFIG_1("C10","Modo E7",null,null,null,"Normal Aberto","Normal Fechado",null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C11","Funcao E7",null,null,null,"Sequencia","Start","Emergencia","Porta","Lib.Ciclo","Seq.Monit.",null,null,null);
        meuBanco.insertData_CONFIG_1("C12","Modo E8",null,null,null,"Normal Aberto","Normal Fechado",null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C13","Funcao E8",null,null,null,"Sequencia","Start","Emergencia","Porta","Lib.Ciclo","Seq.Monit.",null,null,null);
        meuBanco.insertData_CONFIG_1("C14","Funcao S3",null,null,null,"Sequencia","Bicolor","Tricolor","Maq.Pronta","Maq.Ciclo","Ciclo Int.","Ciclo Bloq.","Parada Prog",null);
        meuBanco.insertData_CONFIG_1("C15","Funcao S4",null,null,null,"Sequencia","Bicolor","Tricolor","Maq.Pronta","Maq.Ciclo","Ciclo Int.","Ciclo Bloq.","Parada Prog",null);
        meuBanco.insertData_CONFIG_1("C16","Funcao S5",null,null,null,"Sequencia","Bicolor","Tricolor","Maq.Pronta","Maq.Ciclo","Ciclo Int.","Ciclo Bloq.","Parada Prog",null);
        meuBanco.insertData_CONFIG_1("C17","Funcao S6",null,null,null,"Sequencia","Bicolor","Tricolor","Maq.Pronta","Maq.Ciclo","Ciclo Int.","Ciclo Bloq.","Parada Prog",null);
        meuBanco.insertData_CONFIG_1("C18","Funcao S7",null,null,null,"Sequencia","Bicolor","Tricolor","Maq.Pronta","Maq.Ciclo","Ciclo Int.","Ciclo Bloq.","Parada Prog",null);
        meuBanco.insertData_CONFIG_1("C19","Rst.Saídas",null,null,null,"Não","Sim",null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C20","Sens.Topo",null,null,null,"Magnetico","Otico",null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C21","Tmp.Tp.Col",0.0,0.5,0.1,null,null,null,null,null,null,null,null,"s");
        meuBanco.insertData_CONFIG_1("C22","Bloqueio",null,null,null,"C+T","C+T+S","C+T+S+P","C+T+S+P+A","C+T+S+P+A+R","C+T+S+P+A+R+RT",null,null,null);
        meuBanco.insertData_CONFIG_1("C23","Tmp.Tp.Col",0.0,31.0,1.0,null,null,null,null,null,null,null,null,null);


        // cria 32 linhas iniciais na tabela atual com os ID's
        for (int i = 0; i < 32; i++){
            Integer codigo_numero;
            String codigo;
            codigo_numero = i;
                if (i<=9){
                codigo = "P0" + String.valueOf(codigo_numero);
            }
                else     {
                codigo = "P" + String.valueOf(codigo_numero);
            }
            meuBanco.insertData_PARAM_ATUAL(codigo,null);
        }

        // cria 64 linhas iniciais na tabela atual com os ID's
        for (int i = 0; i < 64; i++){
            Integer codigo_numero;
            String codigo;
            codigo_numero = i;
            if (i<=9){
                codigo = "C0" + String.valueOf(codigo_numero);
            }
            else     {
                codigo = "C" + String.valueOf(codigo_numero);
            }
            meuBanco.insertData_CONFIG_ATUAL(codigo,null);
        }

        // cria 16 linhas iniciais na tabela atual com os ID's
        for (int i = 0; i < 16; i++){
            Integer codigo_numero;
            String codigo;
            codigo_numero = i;
            if (i<=9){
                codigo = "D0" + String.valueOf(codigo_numero);
            }
            else     {
                codigo = "D" + String.valueOf(codigo_numero);
            }
            meuBanco.insertData_DISPLAY_ATUAL(codigo,null);
        }


        Log.d(TAG, "Request Location Permissions:");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
            mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());






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

                // Pedido de valores de parâmetros
                sharedData.setValue(true);

                serialPort.send(Utils.comando(controlePacotes("FA54000054")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA54000155")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA54000256")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA54000357")));
                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54000450")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54000551")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54000652")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54000753")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA5400085C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA5400095D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54000A5E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54000B5F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54000C58")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54000D59")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54000E5A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54000F5B")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001044")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001145")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001246")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001347")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001440")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001541")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001642")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001743")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA5400184C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA5400194D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001A4E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001B4F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001C48")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001D49")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001E4A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54001F4B")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA54002074")));
//                sharedData.setValue(false);

                //pedido de configurações
                serialPort.send(Utils.comando(controlePacotes("FA64000064")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64000165")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64000266")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64000367")));
                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64000460")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64000561")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64000662")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64000763")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA6400086C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA6400096D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64000A6E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64000B6F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64000C68")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64000D69")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64000E6A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64000F6B")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001074")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001175")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001276")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001377")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001470")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001571")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001672")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001773")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA6400187C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA6400197D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001A7E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001B7F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001C78")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001D79")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001E7A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64001F7B")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002044")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002145")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002246")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002347")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002440")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002541")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002642")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002743")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA6400284C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA6400294D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002A4E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002B4F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002C48")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002D49")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002E4A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64002F4B")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003054")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003155")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003256")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003357")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003450")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003551")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003652")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003753")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA6400385C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA6400395D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003A5E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003B5F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003C58")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003D59")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003E5A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64003F5B")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA64004024")));
//                sharedData.setValue(false);
                for (String key : bundletodosDados.keySet())
                {
                    Log.d("Bundle Debug", key + " = \"" + bundletodosDados.get(key) + "\"");
                }



                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                setupViewPager(mViewPager,bundletodosDados);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager,true);

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

    // quando recebe os dados bluetooth
    @Override
    public void onReceive(Context context, BluetoothGattCharacteristic rx) {

        meuBanco = new gerenciadorBanco(this);
        String hexatual;
        String codigo;
        String valor;
        String header;
        String tipoDado;
        String label;
        String msg = rx.getStringValue(0);
        rindex = rindex + msg.length();
        byte[] msgbyte = rx.getValue();
        hexatual = Utils.bytesToHex(msgbyte);
        codigo = Utils.obterCodigo(hexatual);
        header = Utils.obterHeader(hexatual);
        valor = Utils.obterValor(hexatual);
        tipoDado = Utils.obterTipoDado(hexatual);

        //trata o dado recebido se for do tipo 'Parametro' (5)

            if (tipoDado.equals("5")) {
                if (!header.equals("19")) {
                    label = meuBanco.getLabelParametro(codigo);
                    valor = meuBanco.getValorParametro(codigo,valor);
                    bundletodosDados.putString(codigo+"L",label);
                    writeLine(codigo+"label"+label);
                    bundletodosDados.putString(codigo+"V",valor);
                    writeLine(codigo+"valor"+valor);
                    //            meuBanco.insertData_PARAM_ATUAL(codigo,valor);
                    sharedData.setValue(true);
                } else {
                    sharedData.setValue(true);
                }
            }

            //trata o dado recebido se for do tipo 'Configuracao' (6)
            else if (tipoDado.equals("6")) {
                if (!header.equals("19")) {
                    writeLine(codigo);
                    writeLine(valor);
                    //            meuBanco.insertData_PARAM_ATUAL(codigo,valor);
                    sharedData.setValue(true);
                } else {
                    sharedData.setValue(true);
                }
            }

            //trata se for um tipo de dado desconhecido
            else {
//                writeLine("Tipo de dado Desconhecido");
                sharedData.setValue(true);
            }
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


    public void espera() {
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}

