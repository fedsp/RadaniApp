package br.com.radani.www.mensageiro;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class leitura extends Activity {


    TextView caixaprincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leitura);
        caixaprincipal = findViewById(R.id.texto_principal);
        caixaprincipal.setText("deucerto");
    }


}
