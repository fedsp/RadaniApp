package br.com.radani.www.mensageiro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class leituraDisplay extends Fragment {
    private static final String TAG = "leituraDisplay";
    public TextView messagesDisplay;
    public Bundle dadosTotais;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leitura_display,container,false);
        messagesDisplay = view.findViewById(R.id.messagesDisplay);
        messagesDisplay.setMovementMethod((new ScrollingMovementMethod()));
        if (dadosTotais != null) {
            for (String key : dadosTotais.keySet()) {
                String tipoDado;
                String labelOuValor;
                tipoDado = key.substring(0,1);
                labelOuValor = key.substring(3,4);
                if (tipoDado.equals("D")) {
                    if (labelOuValor.equals("L")) {
                        messagesDisplay.append(dadosTotais.getString(key) + ":");
                    }
                    else if (labelOuValor.equals("V")) {
                        messagesDisplay.append(dadosTotais.getString(key)+"\n");
                    }
                    else {messagesDisplay.append("[Dado inválido]");}
                }
                else {}
            }
        }
        return view;
    }


    public void escreveDisplay(Bundle dados) {
        if (dados != null) {
            for (String key : dados.keySet()) {
                Log.d("Debug no Fragment", key + " = \"" + dados.get(key) + "\"");
                dadosTotais = dados;
            }
        }
    }


}