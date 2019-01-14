package br.com.radani.www.mensageiro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class leituraSequenciaInicial extends Fragment {
    private static final String TAG = "leituraSequenciaInicial";
    public TextView messageSequenciaInicial;
    public Bundle dadosTotais;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leitura_sequencia_inicial,container,false);
        messageSequenciaInicial = view.findViewById(R.id.messagesSequenciaInicial);
        if (dadosTotais != null) {
            for (String key : dadosTotais.keySet()) {
                String tipoDado;
                String labelOuValor;
                tipoDado = key.substring(0,1);
                labelOuValor = key.substring(3,4);
                if (tipoDado.equals("S")) {
                    if (labelOuValor.equals("L")) {
                        messageSequenciaInicial.append(dadosTotais.getString(key) + ":");
                    }
                    else if (labelOuValor.equals("V")) {
                        messageSequenciaInicial.append(dadosTotais.getString(key)+"\n");
                    }
                    else {messageSequenciaInicial.append("[Dado inválido]");}
                }
                else {}
            }
        }
        return view;
    }


    public void escreveSequenciaInicial(Bundle dados) {
        if (dados != null) {
            for (String key : dados.keySet()) {
                Log.d("Debug no Fragment", key + " = \"" + dados.get(key) + "\"");
                dadosTotais = dados;
            }
        }
    }


}