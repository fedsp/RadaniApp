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

public class leituraFalhas extends Fragment {
    private static final String TAG = "leituraFalhas";
    public TextView messagesFalhas;
    public Bundle dadosTotais;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leitura_falhas,container,false);
        messagesFalhas = view.findViewById(R.id.messagesFalhas);
        if (dadosTotais != null) {
            for (String key : dadosTotais.keySet()) {
                String tipoDado;
                String labelOuValor;
                tipoDado = key.substring(0,1);
                labelOuValor = key.substring(3,4);
                if (tipoDado.equals("F")) {
                    if (labelOuValor.equals("L")) {
                        messagesFalhas.append(dadosTotais.getString(key) + ":");
                    }
                    else if (labelOuValor.equals("V")) {
                        messagesFalhas.append(dadosTotais.getString(key)+"\n");
                    }
                    else {messagesFalhas.append("[Dado inválido]");}
                }
                else {}
            }
        }
        return view;
    }


    public void escreveFalhas(Bundle dados) {
        if (dados != null) {
            for (String key : dados.keySet()) {
                Log.d("Debug no Fragment", key + " = \"" + dados.get(key) + "\"");
                dadosTotais = dados;
            }
        }
    }


}