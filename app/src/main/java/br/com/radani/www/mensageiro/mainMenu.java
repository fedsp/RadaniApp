package br.com.radani.www.mensageiro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainMenu extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button botaoLeitura = findViewById(R.id.botao_leitura);
        botaoLeitura.setOnClickListener(this::vaiparaleitura);
        Button botaoEscrita = findViewById(R.id.botao_edicao);
        botaoEscrita.setOnClickListener(this::vaiparaescrita);
    }

    public void vaiparaleitura(View view){
        Intent intentLeitura = new Intent(mainMenu.this, leituraDeviceListActivity.class);
        startActivity(intentLeitura);
    }

    public void vaiparaescrita(View view){
        Intent intentEscrita = new Intent(mainMenu.this, escritaDeviceListActivity.class);
        startActivity(intentEscrita);
    }


}
