package br.com.radani.www.mensageiro;

import android.Manifest;
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
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;



public class leituraPrincipal extends AppCompatActivity implements BLeSerialPortService.Callback, View.OnClickListener {

    gerenciadorBanco meuBanco;
    public String query = "";


    // UI elements
    private TextView messages;
    private EditText input;
    private Button   connect;
    private static final String TAG2 = "MainActivity";
    final android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();

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

        adapter.addFragment(new leituraConfiguracao(), "C");
        adapter.addFragment(new leituraDisplay(), "D");
        adapter.addFragment(new leituraFalhas(), "F");
        adapter.addFragment(new leituraParametrizacao(), "P");
        adapter.addFragment(new leituraSequenciaInicial(), "SI");
        adapter.addFragment(new leituraSequenciaCiclica(), "SC");
        adapter.addFragment(new leituraSequenciaFinal(), "SF");
        adapter.addFragment(new leituraSequenciaRotina1(), "SR1");
        adapter.addFragment(new leituraSequenciaRotina2(), "SR2");

        leituraConfiguracao fragmentConfiguracao = (leituraConfiguracao) adapter.getItem(0);
        fragmentConfiguracao.escreveConfiguracoes(bundleDados);

        leituraDisplay fragmentDisplay = (leituraDisplay) adapter.getItem(1);
        fragmentDisplay.escreveDisplay(bundleDados);

        leituraFalhas fragmentFalhas = (leituraFalhas) adapter.getItem(2);
        fragmentFalhas.escreveFalhas(bundleDados);

        leituraParametrizacao fragmentParametrizacao = (leituraParametrizacao) adapter.getItem(3);
        fragmentParametrizacao.escreveParametros(bundleDados);

        leituraSequenciaInicial fragmentSequenciaInicial = (leituraSequenciaInicial) adapter.getItem(4);
        fragmentSequenciaInicial.escreveSequenciaInicial(bundleDados);

        leituraSequenciaCiclica fragmentSequenciaCiclica = (leituraSequenciaCiclica) adapter.getItem(5);
        fragmentSequenciaCiclica.escreveSequenciaCiclica(bundleDados);

        leituraSequenciaFinal fragmentSequenciaFinal = (leituraSequenciaFinal) adapter.getItem(6);
        fragmentSequenciaFinal.escreveSequenciaFinal(bundleDados);

        leituraSequenciaRotina1 fragmentSequenciaRotina1 = (leituraSequenciaRotina1) adapter.getItem(7);
        fragmentSequenciaRotina1.escreveSequenciaRotina1(bundleDados);

        leituraSequenciaRotina2 fragmentSequenciaRotina2 = (leituraSequenciaRotina2) adapter.getItem(8);
        fragmentSequenciaRotina2.escreveSequenciaRotina2(bundleDados);

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
                    .registerCallback(leituraPrincipal.this);
        }

        public void onServiceDisconnected(ComponentName classname) {
            serialPort.unregisterCallback(leituraPrincipal.this)
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
    private final static String TAG = leituraPrincipal.class.getSimpleName();


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if(requestCode == PERMISSION_REQUEST_CODE)
        {
            //Do something based on grantResults
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d(TAG, "Fine location permissão concedida");
            }
            else
            {
                Log.d(TAG, "Fine location permissão negada");
            }
        }
    }


    // Assim que main activity é criada
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // Checar permissões
        if (ContextCompat.checkSelfPermission(leituraPrincipal.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(leituraPrincipal.this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                //ActivityCompat.requestPermissions(leituraPrincipal.this,
                //        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                //        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }





        meuBanco = new gerenciadorBanco(this);

        // cria tabela de parametros 1 já com valores
        meuBanco.insertData_PARAM_1("P00","Tmp.Ciclos",0.0,8.0,0.5, null, null, null, null, null, null, null, null,"s",0.1);
        meuBanco.insertData_PARAM_1("P01","Sel.Ciclo", null, null, null,"Contínuo", "Único", null, null, null, null, null, null,null,null);
        meuBanco.insertData_PARAM_1("P02","Impressão", null, null, null,"Monocolor", "Bicolor", "Tricolor", "Estendido", null, null, null, null,null,null);
        meuBanco.insertData_PARAM_1("P03","Nr.Mistura",1.0,5.0,1.0, null, null, null, null, null, null, null, null,null,1.0);
        meuBanco.insertData_PARAM_1("P04","Tempo_00",0.1,10.0,0.1, null, null, null, null, null, null, null, null,"s",0.1);
        meuBanco.insertData_PARAM_1("P05","Tempo_01",0.1,10.0,0.1, null, null, null, null, null, null, null, null,"s",0.1);
        meuBanco.insertData_PARAM_1("P06","Tempo_Topo",0.1,10.0,0.1, null, null, null, null, null, null, null, null,"s",0.1);
        meuBanco.insertData_PARAM_1("P07","Tp.Clic.Av",0.0,0.5,0.1, null, null, null, null, null, null, null, null,"s",0.1);
        meuBanco.insertData_PARAM_1("P08","Tp.Clic.Rv",0.0,0.5,0.1, null, null, null, null, null, null, null, null,"s",0.1);
        meuBanco.insertData_PARAM_1("P09","Acel.Imp.",0.0,0.5,0.01, null, null, null, null, null, null, null, null,"s",0.01);
        meuBanco.insertData_PARAM_1("P10","Edita_Seq.", null, null, null,"Não", "Sim", null, null, null, null, null, null,null,null);
        meuBanco.insertData_PARAM_1("P11","Reset", null, null, null,"Não", "Sim", null, null, null, null, null, null,null,null);


        // cria tabela de configuracoes 1 já com valores
        meuBanco.insertData_CONFIG_1("C00","Salva",null,null,null,"Não","Sim",null,null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C01","Master",null,null,null,"Não","Sim",null,null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C02","Tmp.Sensor",0.0,10.0,0.5,null,null,null,null,null,null,null,null,"s",0.1);
        meuBanco.insertData_CONFIG_1("C03","Modo_Start",null,null,null,"Start Unico","Start Uni/Const","Start OU Tecla","Start E Tecla","Start Nivel",null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C04","Temp.Start",0.0,5.0,0.5,null,null,null,null,null,null,null,null,"s",0.1);
        meuBanco.insertData_CONFIG_1("C05","Fim Impr.",null,null,null,"Sensor Topo","Sensor Tint.","Sensor Peca",null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C06","Tmp AV/Rec",0.0,0.5,0.1,null,null,null,null,null,null,null,null,"s",0.1);
        meuBanco.insertData_CONFIG_1("C07","Tmp.Peca",0.0,0.5,0.01,null,null,null,null,null,null,null,null,"s",0.01);
        meuBanco.insertData_CONFIG_1("C08","Modo E1",null,null,null,"Normal Aberto","Normal Fechado",null,null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C09","Funcao E1",null,null,null,"Sequencia","Start","Emergencia","Porta","Lib.Ciclo","Seq.Monit.",null,null,null,null);
        meuBanco.insertData_CONFIG_1("C10","Modo E7",null,null,null,"Normal Aberto","Normal Fechado",null,null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C11","Funcao E7",null,null,null,"Sequencia","Start","Emergencia","Porta","Lib.Ciclo","Seq.Monit.",null,null,null,null);
        meuBanco.insertData_CONFIG_1("C12","Modo E8",null,null,null,"Normal Aberto","Normal Fechado",null,null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C13","Funcao E8",null,null,null,"Sequencia","Start","Emergencia","Porta","Lib.Ciclo","Seq.Monit.",null,null,null,null);
        meuBanco.insertData_CONFIG_1("C14","Funcao S3",null,null,null,"Sequencia","Bicolor","Tricolor","Maq.Pronta","Maq.Ciclo","Ciclo Int.","Ciclo Bloq.","Parada Prog",null,null);
        meuBanco.insertData_CONFIG_1("C15","Funcao S4",null,null,null,"Sequencia","Bicolor","Tricolor","Maq.Pronta","Maq.Ciclo","Ciclo Int.","Ciclo Bloq.","Parada Prog",null,null);
        meuBanco.insertData_CONFIG_1("C16","Funcao S5",null,null,null,"Sequencia","Bicolor","Tricolor","Maq.Pronta","Maq.Ciclo","Ciclo Int.","Ciclo Bloq.","Parada Prog",null,null);
        meuBanco.insertData_CONFIG_1("C17","Funcao S6",null,null,null,"Sequencia","Bicolor","Tricolor","Maq.Pronta","Maq.Ciclo","Ciclo Int.","Ciclo Bloq.","Parada Prog",null,null);
        meuBanco.insertData_CONFIG_1("C18","Funcao S7",null,null,null,"Sequencia","Bicolor","Tricolor","Maq.Pronta","Maq.Ciclo","Ciclo Int.","Ciclo Bloq.","Parada Prog",null,null);
        meuBanco.insertData_CONFIG_1("C19","Rst.Saídas",null,null,null,"Não","Sim",null,null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C20","Sens.Topo",null,null,null,"Magnetico","Otico",null,null,null,null,null,null,null,null);
        meuBanco.insertData_CONFIG_1("C21","Tmp.Tp.Col",0.0,0.5,0.1,null,null,null,null,null,null,null,null,"s",0.1);
        meuBanco.insertData_CONFIG_1("C22","Bloqueio",null,null,null,"C+T","C+T+S","C+T+S+P","C+T+S+P+A","C+T+S+P+A+R","C+T+S+P+A+R+RT",null,null,null,null);
        meuBanco.insertData_CONFIG_1("C23","Inicio Seq.",0.0,31.0,1.0,null,null,null,null,null,null,null,null,null,1.0);

        // cria tabela de DISPLAY 1 já com valores
        meuBanco.insertData_DISPLAY_1("D00","Contador",null,null,null, "Producao", "Lote de Pecas", null, null, null, null, null, null,null,null,null);
        meuBanco.insertData_DISPLAY_1("D01","Valor Lote", 1.0, 9999.0, 1.0,null, null, null, null, null, null, null, null,null,"uni.",1.0);
        meuBanco.insertData_DISPLAY_1("D02","Val. Parada", 0.0, 8000.0, 10.0,null, null, null, null, null, null, null, null,null,"uni.",1.0);
        meuBanco.insertData_DISPLAY_1("D03","Tol. Parada", 0.0, 1000.0, 10.0,null, null, null, null, null, null, null, null,null,"uni.",10.0);
        meuBanco.insertData_DISPLAY_1("D04","Produtiv.",null,null,null, "Pecas/Hora", "Pecas/Min", null, null, null, null, null, null,null,null,null);
        meuBanco.insertData_DISPLAY_1("D05","Info Linha",null,null,null, "Nenhuma", "Qtd.Pecas Lote", "Qtd.Pecas Prog", "Passo Seq.", "Tempo T0", "Tempo T1", "Tmp. Ent. Ciclos", "Tmp. Do Ciclo","Nr. da Receita",null,null);
        meuBanco.insertData_DISPLAY_1("D06","Display Dummy1", 0.0, 1000.0, 10.0,null, null, null, null, null, null, null, null,null,"uni.",1.0);
        meuBanco.insertData_DISPLAY_1("D07","Display Dummy2", 0.0, 1000.0, 10.0,null, null, null, null, null, null, null, null,null,"uni.",1.0);

        // cria 32 linhas iniciais na tabela de parametro atual com os ID's
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

        // cria 64 linhas iniciais na tabela de config atual com os ID's
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

        // cria 16 linhas iniciais na tabela de display atual com os ID's
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
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.minilogo);
            setContentView(R.layout.activity_main);
            mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());



            // Referencia aos elementos visuais na tela
            messages = (TextView) findViewById(R.id.messages);
            input = (EditText) findViewById(R.id.input);
            input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            input.setFilters(new InputFilter[]{new Utils.InputFilterHex()});

            // Habilita auto scroll na text view
            messages.setMovementMethod(new ScrollingMovementMethod());

            connect = (Button) findViewById(R.id.connect);
            connect.setOnClickListener(this);

            // bind and start the bluetooth service
            Intent bindIntent = new Intent(this, BLeSerialPortService.class);
            bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        Intent intent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(intent, REQUEST_DEVICE);
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
//        writeLine(query);
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
                //connect.setText(R.string.send);
                connect.setEnabled(false);

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
                serialPort.send(Utils.comando(controlePacotes("FA54000450")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA54000551")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA54000652")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA54000753")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA5400085C")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA5400095D")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA54000A5E")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA54000B5F")));
                sharedData.setValue(false);
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
                serialPort.send(Utils.comando(controlePacotes("FA64000460")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64000561")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64000662")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64000763")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA6400086C")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA6400096D")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64000A6E")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64000B6F")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64000C68")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64000D69")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64000E6A")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64000F6B")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64001074")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64001175")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64001276")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64001377")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64001470")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64001571")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64001672")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA64001773")));
                sharedData.setValue(false);
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

                //pedido de Display
                serialPort.send(Utils.comando(controlePacotes("FA74000074")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA74000175")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA74000276")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA74000377")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA74000470")));
                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA74000571")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA74000672")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA74000773")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA7400087C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA7400097D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA74001064")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA74001165")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA74001266")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA74001367")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA74001460")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA74001561")));
//                sharedData.setValue(false);


                // Pedido de valores de Sequência Inicial
                serialPort.send(Utils.comando(controlePacotes("FA04000004")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA04000105")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA04000206")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA04000307")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA04000400")));
                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04000501")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04000602")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04000703")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA0400080C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA0400090D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04000A0E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04000B0F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04000C08")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04000D09")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04000E0A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04000F0B")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001014")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001115")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001216")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001317")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001410")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001511")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001612")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001713")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA0400181C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA0400191D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001A1E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001B1F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001C18")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001D19")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001E1A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04001F1B")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA04002024")));
//                sharedData.setValue(false);



                // Pedido de valores de Sequência Cíclica
                serialPort.send(Utils.comando(controlePacotes("FA14000014")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA14000115")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA14000216")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA14000317")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA14000410")));
                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14000511")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14000612")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14000713")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA1400081C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA1400091D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14000A1E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14000B1F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14000C18")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14000D19")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14000E1A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14000F1B")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001004")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001105")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001206")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001307")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001400")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001501")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001602")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001703")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA1400180C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA1400190D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001A0E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001B0F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001C08")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001D09")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001E0A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA14001F0B")));
//                sharedData.setValue(false);


                // Pedido de valores de Sequência Final
                serialPort.send(Utils.comando(controlePacotes("FA24000024")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA24000125")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA24000226")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA24000327")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA24000420")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA24000521")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24000622")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24000723")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA2400082C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA2400092D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24000A2E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24000B2F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24000C28")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24000D29")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24000E2A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24000F2B")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001034")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001135")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001236")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001337")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001430")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001531")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001632")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001733")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA2400183C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA2400193D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001A3E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001B3F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001C38")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001D39")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001E3A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA24001F3B")));
//                sharedData.setValue(false);


                // Pedido de valores de Sequência Rotina 1
                serialPort.send(Utils.comando(controlePacotes("FA34000034")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA34000135")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA34000236")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA34000337")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA34000430")));
                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34000531")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34000632")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34000733")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA3400083C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA3400093D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34000A3E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34000B3F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34000C38")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34000D39")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34000E3A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34000F3B")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001024")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001125")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001226")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001327")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001420")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001521")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001622")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001723")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA3400182C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA3400192D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001A2E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001B2F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001C28")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001D29")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001E2A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA34001F2B")));
//                sharedData.setValue(false);


                // Pedido de valores de Sequência Rotina 2
                serialPort.send(Utils.comando(controlePacotes("FA44000044")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA44000145")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA44000246")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA44000347")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA44000440")));
                sharedData.setValue(false);
                serialPort.send(Utils.comando(controlePacotes("FA44000541")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44000642")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44000743")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA4400084C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA4400094D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44000A4E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44000B4F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44000C48")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44000D49")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44000E4A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44000F4B")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001054")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001155")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001256")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001357")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001450")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001551")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001652")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001753")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA4400185C")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA4400195D")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001A5E")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001B5F")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001C58")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001D59")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001E5A")));
//                sharedData.setValue(false);
//                serialPort.send(Utils.comando(controlePacotes("FA44001F5B")));
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

                Toast.makeText(getApplicationContext(),"Erro ao tentar conectar. Por favor tente novamente.", Toast.LENGTH_SHORT).show();
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
        String tipoSequencia;

        String msg = rx.getStringValue(0);
        rindex = rindex + msg.length();
        byte[] msgbyte = rx.getValue();
        hexatual = Utils.bytesToHex(msgbyte);
        tipoDado = Utils.obterTipoDado(hexatual);
        codigo = Utils.obterCodigo(hexatual,tipoDado);
        header = Utils.obterHeader(hexatual);
        valor = Utils.obterValor(hexatual,tipoDado);
        tipoSequencia = Utils.obterTipoSequencia(hexatual);

        //trata o dado recebido se for do tipo 'Parametro' (5)
            if (tipoDado.equals("5")) {
                if (!header.equals("19")) {
                    writeLine("Param:"+hexatual);
                    Double multiplicador;
                    String unidade;
                    unidade = meuBanco.getUnidadeParametro(codigo);
                    multiplicador = meuBanco.getMultiplicadorParametro(codigo);
                    label = meuBanco.getLabelParametro(codigo);
                    valor = meuBanco.getValorParametro(codigo,valor,multiplicador);
                    bundletodosDados.putString(codigo+"U",unidade);
                    bundletodosDados.putString(codigo+"L",label);
                    bundletodosDados.putString(codigo+"V",valor);
                    //            meuBanco.insertData_PARAM_ATUAL(codigo,valor);
                    sharedData.setValue(true);
                } else {
                    sharedData.setValue(true);
                }
            }

            //trata o dado recebido se for do tipo 'Sequência Inicial' (0)
            else if (tipoDado.equals("0")) {
                if (!header.equals("19")) {
                    writeLine("Sequência Inicial:"+hexatual);

                    writeLine("Tipo Sequencia:"+tipoSequencia);
                    label = Utils.obterLabelSequencia(tipoSequencia);
                    valor = Utils.obterValorSequencia(hexatual);
                    bundletodosDados.putString(codigo+"L",label);
                    bundletodosDados.putString(codigo+"V",valor);
                    //            meuBanco.insertData_CONFIG_ATUAL(codigo,valor);
                    sharedData.setValue(true);
                } else {
                    sharedData.setValue(true);
                }
            }

            //trata o dado recebido se for do tipo 'Sequência Cíclica' (1)
            else if (tipoDado.equals("1")) {
                if (!header.equals("19")) {
                    writeLine("Sequência Cíclica:"+hexatual);

                    label = Utils.obterLabelSequencia(tipoSequencia);
                    valor = Utils.obterValorSequencia(hexatual);
                    bundletodosDados.putString(codigo+"L",label);
                    bundletodosDados.putString(codigo+"V",valor);
                    //            meuBanco.insertData_CONFIG_ATUAL(codigo,valor);
                    sharedData.setValue(true);
                } else {
                    sharedData.setValue(true);
                }
            }

            //trata o dado recebido se for do tipo 'Sequência Final' (2)
            else if (tipoDado.equals("2")) {
                if (!header.equals("19")) {
                    writeLine("Sequência Final:"+hexatual);

                    label = Utils.obterLabelSequencia(tipoSequencia);
                    valor = Utils.obterValorSequencia(hexatual);
                    bundletodosDados.putString(codigo+"L",label);
                    bundletodosDados.putString(codigo+"V",valor);
                    //            meuBanco.insertData_CONFIG_ATUAL(codigo,valor);
                    sharedData.setValue(true);
                } else {
                    sharedData.setValue(true);
                }
            }

            //trata o dado recebido se for do tipo 'Sequência Rotina 1' (3)
            else if (tipoDado.equals("3")) {
                if (!header.equals("19")) {
                    writeLine("Sequência Rotina 1:"+hexatual);

                    label = Utils.obterLabelSequencia(tipoSequencia);
                    valor = Utils.obterValorSequencia(hexatual);
                    bundletodosDados.putString(codigo+"L",label);
                    bundletodosDados.putString(codigo+"V",valor);
                    //            meuBanco.insertData_CONFIG_ATUAL(codigo,valor);
                    sharedData.setValue(true);
                } else {
                    sharedData.setValue(true);
                }
            }

            //trata o dado recebido se for do tipo 'Sequência Rotina 2' (4)
            else if (tipoDado.equals("4")) {
                if (!header.equals("19")) {
                    writeLine("Sequência Rotina 2:"+hexatual);
                    label = Utils.obterLabelSequencia(tipoSequencia);
                    valor = Utils.obterValorSequencia(hexatual);
                    bundletodosDados.putString(codigo+"L",label);
                    bundletodosDados.putString(codigo+"V",valor);
                    //            meuBanco.insertData_CONFIG_ATUAL(codigo,valor);
                    sharedData.setValue(true);
                } else {
                    sharedData.setValue(true);
                }
            }

            //trata o dado recebido se for do tipo 'Configuração' (6)
            else if (tipoDado.equals("6")) {
                if (!header.equals("19")) {
                    writeLine("Config:"+hexatual);
                    double multiplicador;
                    String unidade;
                    unidade = meuBanco.getUnidadeConfig(codigo);
                    multiplicador = meuBanco.getMultiplicadorConfig(codigo);
                    label = meuBanco.getLabelConfig(codigo);
                    valor = meuBanco.getValorConfig(codigo,valor,multiplicador);

                    bundletodosDados.putString(codigo+"U",unidade);
                    bundletodosDados.putString(codigo+"L",label);
                    bundletodosDados.putString(codigo+"V",valor);
                    //            meuBanco.insertData_CONFIG_ATUAL(codigo,valor);
                    sharedData.setValue(true);
                } else {
                    sharedData.setValue(true);
                }
            }

            //trata o dado recebido se for do tipo 'Display' (7)
            else if (tipoDado.equals("7")) {
                if (!header.equals("19")) {
                    writeLine("Display:"+hexatual);
                    double multiplicador;
                    String unidade;
                    multiplicador = meuBanco.getMultiplicadorDisplay(codigo);
                    label = meuBanco.getLabelDisplay(codigo);
                    valor = meuBanco.getValorDisplay(codigo,valor,multiplicador);
                    unidade = meuBanco.getUnidadeDisplay(codigo);

                    bundletodosDados.putString(codigo+"U",unidade);
                    bundletodosDados.putString(codigo+"L",label);
                    bundletodosDados.putString(codigo+"V",valor);
                    //            meuBanco.insertData_DISPLAY_ATUAL(codigo,valor);
                    sharedData.setValue(true);
                } else {
                    sharedData.setValue(true);
                }
            }

            //trata se for um tipo de dado desconhecido
            else {
                writeLine("Tipo de dado Desconhecido:"+hexatual);
                sharedData.setValue(true);
                writeLine(tipoDado);
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
        Log.e(leituraPrincipal.class.getSimpleName(), msg);
    }


    public void espera() {
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}

