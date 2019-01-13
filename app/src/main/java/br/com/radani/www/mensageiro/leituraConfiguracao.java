package br.com.radani.www.mensageiro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.TextView;

import java.util.Set;

public class leituraConfiguracao extends Fragment {
    private static final String TAG = "leituraConfiguracao";
    public TextView messagesConfiguracoes;
    public Bundle dadosTotais;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leitura_configuracao,container,false);
        messagesConfiguracoes = view.findViewById(R.id.messagesConfiguracoes);
        if (dadosTotais != null) {
            for (String key : dadosTotais.keySet()) {
                String tipoDado;
                String labelOuValor;
                tipoDado = key.substring(0,1);
                labelOuValor = key.substring(2,3);
                if (tipoDado.equals("P")) {
                    messagesConfiguracoes.append(dadosTotais.getString(labelOuValor));
                    messagesConfiguracoes.append(dadosTotais.getString(key));
                }
                else {}
            }
        }
        return view;
    }


    public void escreveConfiguracoes(Bundle dados) {
        if (dados != null) {
            for (String key : dados.keySet()) {
                Log.d("Debug no Fragment", key + " = \"" + dados.get(key) + "\"");
                dadosTotais = dados;
            }
        }
    }


}