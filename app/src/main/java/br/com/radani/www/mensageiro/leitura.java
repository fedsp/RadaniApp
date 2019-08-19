package br.com.radani.www.mensageiro;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

public class leitura extends AppCompatActivity {
    ListView texto_leitura_conf,texto_leitura_disp,texto_leitura_param,
            texto_leitura_seqi,texto_leitura_seqc,texto_leitura_seqf,texto_leitura_seq1,texto_leitura_seq2;
    ViewPager viewPager;
    SectionsPageAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        viewAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewAdapter);

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
            Toast.makeText(getApplicationContext(), "Leitura Configuração", Toast.LENGTH_SHORT).show();
        }
        if (position == 1);
        {
            Toast.makeText(getApplicationContext(), "Leitura 2", Toast.LENGTH_SHORT).show();
        }
        if (position == 2);
        {
            Toast.makeText(getApplicationContext(), "Leitura 3", Toast.LENGTH_SHORT).show();
        }
        if (position == 3);
        {
            Toast.makeText(getApplicationContext(), "Leitura 4", Toast.LENGTH_SHORT).show();
        }
        if (position == 4);
        {
            Toast.makeText(getApplicationContext(), "Leitura 5", Toast.LENGTH_SHORT).show();
        }
        if (position == 5);
        {
            Toast.makeText(getApplicationContext(), "Leitura 6", Toast.LENGTH_SHORT).show();
        }

    }


}
