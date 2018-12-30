package br.com.radani.www.mensageiro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.TextView;

public class leituraConfiguracao extends Fragment {
    private static final String TAG = "leituraConfiguracao";

    String mensagem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leitura_configuracao, container, false);
        TextView textView = view.findViewById(R.id.text_config);
        textView.setText("Salva:                        Sim \n Leitura:                      Não \n Temperatura do Sensor:        18ºc \n");
        return view;

    }

}