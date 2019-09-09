package br.com.radani.www.mensageiro;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;

public class leitura extends AppCompatActivity implements ServiceConnection, SerialListener {
    ListView texto_leitura_conf,texto_leitura_disp,texto_leitura_param,
            texto_leitura_seqi,texto_leitura_seqc,texto_leitura_seqf,texto_leitura_seq1,texto_leitura_seq2;
    ViewPager viewPager;
    SectionsPageAdapter viewAdapter;
    gerenciadorBanco meuBanco;
    Bundle bundletodosDados = new Bundle();
    List<String> pedidos = Arrays.asList("FA54000155","FA54000256","FA54000357",
            "FA54000450","FA54000551","FA54000652","FA54000753","FA5400085C","FA5400095D",
            "FA54000A5E","FA54000B5F","FA54000C58","FA54000D59","FA54000E5A","FA54000F5B",
            "FA54001044","FA54001145","FA54001246","FA54001347","FA54001440","FA54001541",
            "FA54001642","FA54001743","FA5400184C","FA5400194D","FA54001A4E","FA54001B4F",
            "FA54001C48","FA54001D49","FA54001E4A","FA54001F4B","FA54002074","FA64000064",
            "FA64000165","FA64000266","FA64000367","FA64000460","FA64000561","FA64000662",
            "FA64000763","FA6400086C","FA6400096D","FA64000A6E","FA64000B6F","FA64000C68",
            "FA64000D69","FA64000E6A","FA64000F6B","FA64001074","FA64001175","FA64001276",
            "FA64001377","FA64001470","FA64001571","FA64001672","FA64001773","FA6400187C",
            "FA6400197D","FA64001A7E","FA64001B7F","FA64001C78","FA64001D79","FA64001E7A",
            "FA64001F7B","FA64002044","FA64002145","FA64002246","FA64002347","FA64002440",
            "FA64002541","FA64002642","FA64002743","FA6400284C","FA6400294D","FA64002A4E",
            "FA64002B4F","FA64002C48","FA64002D49","FA64002E4A","FA64002F4B","FA64003054",
            "FA64003155","FA64003256","FA64003357","FA64003450","FA64003551","FA64003652",
            "FA64003753","FA6400385C","FA6400395D","FA64003A5E","FA64003B5F","FA64003C58",
            "FA64003D59","FA64003E5A","FA64003F5B","FA64004024","FA74000074","FA74000175",
            "FA74000276","FA74000377","FA74000470","FA74000571","FA74000672","FA74000773",
            "FA7400087C","FA7400097D","FA74001064","FA74001165","FA74001266","FA74001367",
            "FA74001460","FA74001561","FA04000004","FA04000105","FA04000206","FA04000307",
            "FA04000400","FA04000501","FA04000602","FA04000703","FA0400080C","FA0400090D",
            "FA04000A0E","FA04000B0F","FA04000C08","FA04000D09","FA04000E0A","FA04000F0B",
            "FA04001014","FA04001115","FA04001216","FA04001317","FA04001410","FA04001511",
            "FA04001612","FA04001713","FA0400181C","FA0400191D","FA04001A1E","FA04001B1F",
            "FA04001C18","FA04001D19","FA04001E1A","FA04001F1B","FA04002024","FA14000014",
            "FA14000115","FA14000216","FA14000317","FA14000410","FA14000511","FA14000612",
            "FA14000713","FA1400081C","FA1400091D","FA14000A1E","FA14000B1F","FA14000C18",
            "FA14000D19","FA14000E1A","FA14000F1B","FA14001004","FA14001105","FA14001206",
            "FA14001307","FA14001400","FA14001501","FA14001602","FA14001703","FA1400180C",
            "FA1400190D","FA14001A0E","FA14001B0F","FA14001C08","FA14001D09","FA14001E0A",
            "FA14001F0B","FA24000024","FA24000125","FA24000226","FA24000327","FA24000420",
            "FA24000521","FA24000622","FA24000723","FA2400082C","FA2400092D","FA24000A2E",
            "FA24000B2F","FA24000C28","FA24000D29","FA24000E2A","FA24000F2B","FA24001034",
            "FA24001135","FA24001236","FA24001337","FA24001430","FA24001531","FA24001632",
            "FA24001733","FA2400183C","FA2400193D","FA24001A3E","FA24001B3F","FA24001C38",
            "FA24001D39","FA24001E3A","FA24001F3B","FA34000034","FA34000135","FA34000236",
            "FA34000337","FA34000430","FA34000531","FA34000632","FA34000733","FA3400083C",
            "FA3400093D","FA34000A3E","FA34000B3F","FA34000C38","FA34000D39","FA34000E3A",
            "FA34000F3B","FA34001024","FA34001125","FA34001226","FA34001327","FA34001420",
            "FA34001521","FA34001622","FA34001723","FA3400182C","FA3400192D","FA34001A2E",
            "FA34001B2F","FA34001C28","FA34001D29","FA34001E2A","FA34001F2B","FA44000044",
            "FA44000145","FA44000246","FA44000347","FA44000440","FA44000541","FA44000642",
            "FA44000743","FA4400084C","FA4400094D","FA44000A4E","FA44000B4F","FA44000C48",
            "FA44000D49","FA44000E4A","FA44000F4B","FA44001054","FA44001155","FA44001256",
            "FA44001357","FA44001450","FA44001551","FA44001652","FA44001753","FA4400185C",
            "FA4400195D","FA44001A5E","FA44001B5F","FA44001C58","FA44001D59","FA44001E5A",
            "FA44001F5B");




    //Bluetooth connection variables
    private enum Connected { False, Pending, True }
    private String deviceAddress;
    private String newline = "\r\n";
    private SerialSocket socket;
    private SerialService service;
    private boolean initialStart = true;
    private Connected connected = Connected.False;
    private Integer contadorPedido=1;
    private Integer limiteContadorPedido = pedidos.size();
    private Double progresso =0.0;
    private Long progressoInt;

    private Integer next_percent;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        viewAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        next_percent=0;
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

        Bundle b = getIntent().getExtras();
        deviceAddress = b.getString("address");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_leitura);
        texto_leitura_conf = findViewById(R.id.lista_configuracao);
        texto_leitura_disp = findViewById(R.id.lista_display);
        texto_leitura_param = findViewById(R.id.lista_parametros);
        texto_leitura_seqi = findViewById(R.id.lista_seq_inicial);
        texto_leitura_seqc = findViewById(R.id.lista_seq_ciclica);
        texto_leitura_seqf = findViewById(R.id.lista_seq_final);
        texto_leitura_seq1 = findViewById(R.id.lista_seq_rot_1);
        texto_leitura_seq2 = findViewById(R.id.lista_seq_rot_2);
        viewPager = findViewById(R.id.fragment_container);
        this.bindService(new Intent(this, SerialService.class), this, Context.BIND_AUTO_CREATE);




        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                onChangeTab(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });




    }

    private void onChangeTab(int position) {
        if (position == 0);{
        }
        if (position == 1);
        {
        }
        if (position == 2);
        {
        }
        if (position == 3);
        {
        }
        if (position == 4);
        {
        }
        if (position == 5);
        {
        }

    }

    //Bluetooth connections
    @Override
    public void onDestroy() {
        if (connected != Connected.False)
            disconnect();
        stopService(new Intent(this, SerialService.class));
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        startService(new Intent(this, SerialService.class)); // prevents service destroy on unbind from recreated activity caused by orientation change
    }

    @Override
    public void onStop() {
        if(service != null && !this.isChangingConfigurations())
            service.detach();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(initialStart && service !=null) {
            initialStart = false;
            runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        if(initialStart) {
            initialStart = false;
            runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

    private void connect() {

        try {

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            String deviceName = device.getName() != null ? device.getName() : device.getAddress();
            connected = Connected.Pending;
            socket = new SerialSocket();
            service.connect(this, "Connected to " + deviceName);
            socket.connect(this, service, device);
            Toast.makeText(getApplicationContext(),"Tentando conexão...", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Falha ao conectar", Toast.LENGTH_SHORT).show();

            onSerialConnectError(e);

        }
    }

    private void disconnect() {
        connected = Connected.False;
        service.disconnect();
        socket.disconnect();
        socket = null;
    }

    private void send(byte[] data) {
        if(connected != Connected.True) {
            Toast.makeText(getApplicationContext(),"Não está conectado", Toast.LENGTH_SHORT).show();
            return;
        }
        try {                socket.write(data);

        } catch (Exception e) {
            onSerialIoError(e);
            Toast.makeText(getApplicationContext(),"Falha ao enviar dados", Toast.LENGTH_SHORT).show();

        }
    }

    private void receive(byte[] data) {
        String hexatual;
        String codigo;
        String valor;
        String header;
        String tipoDado;
        String label;
        String tipoSequencia;


        byte[] msgbyte = data;
        hexatual = Utils.bytesToHex(msgbyte);
        Log.i("HEXATUAL RECEBIDO", hexatual);
        tipoDado = Utils.obterTipoDado(hexatual);
        codigo = Utils.obterCodigo(hexatual, tipoDado);
        header = Utils.obterHeader(hexatual);
        valor = Utils.obterValor(hexatual, tipoDado);
        tipoSequencia = Utils.obterTipoSequencia(hexatual);

        //trata o dado recebido se for do tipo 'Parametro' (5)
        if (tipoDado.equals("5")) {
            if (!header.equals("19")) {
                Double multiplicador;
                String unidade;
                unidade = meuBanco.getUnidadeParametro(codigo);
                multiplicador = meuBanco.getMultiplicadorParametro(codigo);
                label = meuBanco.getLabelParametro(codigo);
                valor = meuBanco.getValorParametro(codigo, valor, multiplicador);
                bundletodosDados.putString(codigo + "U", unidade);
                bundletodosDados.putString(codigo + "L", label);
                bundletodosDados.putString(codigo + "V", valor);
                //meuBanco.insertData_PARAM_ATUAL(codigo,valor);
            } else {
            }
        }

        //trata o dado recebido se for do tipo 'Sequência Inicial' (0)
        else if (tipoDado.equals("0")) {
            if (!header.equals("19")) {
                label = Utils.obterLabelSequencia(tipoSequencia);
                valor = Utils.obterValorSequencia(hexatual);
                bundletodosDados.putString(codigo + "L", label);
                bundletodosDados.putString(codigo + "V", valor);
                //meuBanco.insertData_CONFIG_ATUAL(codigo,valor);
            } else {
            }
        }

        //trata o dado recebido se for do tipo 'Sequência Cíclica' (1)
        else if (tipoDado.equals("1")) {
            if (!header.equals("19")) {
                label = Utils.obterLabelSequencia(tipoSequencia);
                valor = Utils.obterValorSequencia(hexatual);
                bundletodosDados.putString(codigo + "L", label);
                bundletodosDados.putString(codigo + "V", valor);
                //meuBanco.insertData_CONFIG_ATUAL(codigo,valor);
            } else {
            }
        }

        //trata o dado recebido se for do tipo 'Sequência Final' (2)
        else if (tipoDado.equals("2")) {
            if (!header.equals("19")) {
                label = Utils.obterLabelSequencia(tipoSequencia);
                valor = Utils.obterValorSequencia(hexatual);
                bundletodosDados.putString(codigo + "L", label);
                bundletodosDados.putString(codigo + "V", valor);
                //meuBanco.insertData_CONFIG_ATUAL(codigo,valor);
            } else {
            }
        }

        //trata o dado recebido se for do tipo 'Sequência Rotina 1' (3)
        else if (tipoDado.equals("3")) {
            if (!header.equals("19")) {
                label = Utils.obterLabelSequencia(tipoSequencia);
                valor = Utils.obterValorSequencia(hexatual);
                bundletodosDados.putString(codigo + "L", label);
                bundletodosDados.putString(codigo + "V", valor);
                //meuBanco.insertData_CONFIG_ATUAL(codigo,valor);
            }
        }

        //trata o dado recebido se for do tipo 'Sequência Rotina 2' (4)
        else if (tipoDado.equals("4")) {
            if (!header.equals("19")) {
                label = Utils.obterLabelSequencia(tipoSequencia);
                valor = Utils.obterValorSequencia(hexatual);
                bundletodosDados.putString(codigo + "L", label);
                bundletodosDados.putString(codigo + "V", valor);
                //meuBanco.insertData_CONFIG_ATUAL(codigo,valor);
            }
        }

        //trata o dado recebido se for do tipo 'Configuração' (6)
        else if (tipoDado.equals("6")) {
            if (!header.equals("19")) {
                double multiplicador;
                String unidade;
                unidade = meuBanco.getUnidadeConfig(codigo);
                multiplicador = meuBanco.getMultiplicadorConfig(codigo);
                label = meuBanco.getLabelConfig(codigo);
                valor = meuBanco.getValorConfig(codigo, valor, multiplicador);
                bundletodosDados.putString(codigo + "U", unidade);
                bundletodosDados.putString(codigo + "L", label);
                bundletodosDados.putString(codigo + "V", valor);
                //meuBanco.insertData_CONFIG_ATUAL(codigo,valor);
            }
        }

        //trata o dado recebido se for do tipo 'Display' (7)
        else if (tipoDado.equals("7")) {
            if (!header.equals("19")) {
                double multiplicador;
                String unidade;
                multiplicador = meuBanco.getMultiplicadorDisplay(codigo);
                label = meuBanco.getLabelDisplay(codigo);
                valor = meuBanco.getValorDisplay(codigo, valor, multiplicador);
                unidade = meuBanco.getUnidadeDisplay(codigo);
                bundletodosDados.putString(codigo + "U", unidade);
                bundletodosDados.putString(codigo + "L", label);
                bundletodosDados.putString(codigo + "V", valor);
                //meuBanco.insertData_DISPLAY_ATUAL(codigo,valor);
            }
        }

        String pedido;
        pedido = pedidos.get(contadorPedido);
        contadorPedido = contadorPedido + 1;
        if (contadorPedido < limiteContadorPedido) {
            //Toast.makeText(getApplicationContext(),"Enviando pedido: "+pedido, Toast.LENGTH_SHORT).show();
            send(Utils.comando(pedido));
            progresso = contadorPedido.doubleValue()/limiteContadorPedido.doubleValue();
            progresso = progresso*100;
            progressoInt = Math.round(progresso);


            if ((progressoInt == 10 || progressoInt==11)) {
                if (next_percent==0) {
                    Toast.makeText(getApplicationContext(), "10%", Toast.LENGTH_SHORT).show();
                    Log.d("PERCENTAGE", progressoInt.toString());
                    next_percent=10;
                }
            }
            if (progressoInt == 20 || progressoInt==21) {
                if (next_percent==10) {
                    Toast.makeText(getApplicationContext(), "20%", Toast.LENGTH_SHORT).show();
                    Log.d("PERCENTAGE", progressoInt.toString());
                    next_percent=20;
                }
            }
            if (progressoInt == 30 || progressoInt==31) {
                if (next_percent==20) {
                    Toast.makeText(getApplicationContext(), "30%", Toast.LENGTH_SHORT).show();
                    Log.d("PERCENTAGE", progressoInt.toString());
                    next_percent=30;
                }
            }
            if (progressoInt == 40 || progressoInt==41) {
                if (next_percent==30) {
                    Toast.makeText(getApplicationContext(), "40%", Toast.LENGTH_SHORT).show();
                    Log.d("PERCENTAGE", progressoInt.toString());
                    next_percent=40;
                }
            }
            if (progressoInt == 50 || progressoInt==51) {
                if (next_percent==40) {
                    Toast.makeText(getApplicationContext(), "50%", Toast.LENGTH_SHORT).show();
                    Log.d("PERCENTAGE", progressoInt.toString());
                    next_percent=50;
                }
            }
            if (progressoInt == 60 || progressoInt==61) {
                if (next_percent==50) {
                    Toast.makeText(getApplicationContext(), "60%", Toast.LENGTH_SHORT).show();
                    Log.d("PERCENTAGE", progressoInt.toString());
                    next_percent=60;
                }
            }
            if (progressoInt == 70 || progressoInt==71) {
                if (next_percent==60) {
                    Toast.makeText(getApplicationContext(), "70%", Toast.LENGTH_SHORT).show();
                    Log.d("PERCENTAGE", progressoInt.toString());
                    next_percent=70;
                }
            }
            if (progressoInt == 80 || progressoInt==81) {
                if (next_percent==70) {
                    Toast.makeText(getApplicationContext(), "80%", Toast.LENGTH_SHORT).show();
                    Log.d("PERCENTAGE", progressoInt.toString());
                    next_percent=80;
                }
            }
            if (progressoInt == 90 || progressoInt==91) {
                if (next_percent==80) {
                    Toast.makeText(getApplicationContext(), "90%", Toast.LENGTH_SHORT).show();
                    Log.d("PERCENTAGE", progressoInt.toString());
                    next_percent=90;
                }
            }

        }
        else {

            viewAdapter.recebeBundle(bundletodosDados);
            viewPager.setAdapter(viewAdapter);

            //Captura a instancia de cada fragment para poder operar eles
            leituraConfiguracao fragmentConfiguracao = (leituraConfiguracao) viewAdapter.getItem(0);
            leituraDisplay fragmentDisplay = (leituraDisplay) viewAdapter.getItem(1);
            leituraFalhas fragmentFalhas = (leituraFalhas) viewAdapter.getItem(2);
            leituraParametrizacao fragmentParametrizacao = (leituraParametrizacao) viewAdapter.getItem(3);
            leituraSequenciaInicial fragmentSequenciaInicial = (leituraSequenciaInicial) viewAdapter.getItem(4);
            leituraSequenciaCiclica fragmentSequenciaCiclica = (leituraSequenciaCiclica) viewAdapter.getItem(5);
            leituraSequenciaFinal fragmentSequenciaFinal = (leituraSequenciaFinal) viewAdapter.getItem(6);
            leituraSequenciaRotina1 fragmentSequenciaRotina1 = (leituraSequenciaRotina1) viewAdapter.getItem(7);
            leituraSequenciaRotina2 fragmentSequenciaRotina2 = (leituraSequenciaRotina2) viewAdapter.getItem(8);

        }

    }
    /*
     * SerialListener
     */
    @Override
    public void onSerialConnect() {

            connected = Connected.True;
            Toast.makeText(getApplicationContext(), "Conexão bem sucedida", Toast.LENGTH_SHORT).show();

            // Pedido de valores de parâmetros
            send(Utils.comando("FA54000054"));
    }

    @Override
    public void onSerialConnectError(Exception e) {
        disconnect();
    }

    @Override
    public void onSerialRead(byte[] data) {
        receive(data);
    }

    @Override
    public void onSerialIoError(Exception e) {
        disconnect();
    }



}
