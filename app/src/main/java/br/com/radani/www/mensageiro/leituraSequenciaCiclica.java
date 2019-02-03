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

public class leituraSequenciaCiclica extends Fragment {
    private static final String TAG = "leituraSequenciaCiclica";
    public TextView messageSequenciaCiclica;
    public Bundle dadosTotais;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leitura_sequencia_ciclica,container,false);
        messageSequenciaCiclica = view.findViewById(R.id.messagesSequenciaCiclica);
        messageSequenciaCiclica.setMovementMethod((new ScrollingMovementMethod()));
        if (dadosTotais != null) {
            for (String key : dadosTotais.keySet()) {
                String tipoDado;
                String labelOuValor;
                tipoDado = key.substring(0,2);

                if (tipoDado.equals("SC")) {
                    labelOuValor = key.substring(4,5);
                    if (labelOuValor.equals("L")) {
                        String passo;
                        passo = key.substring(0,1)+key.substring(2,4);
                        messageSequenciaCiclica.append("["+passo+"] ");
                        messageSequenciaCiclica.append(dadosTotais.getString(key));
                    }
                    else if (labelOuValor.equals("V")) {
                        messageSequenciaCiclica.append(dadosTotais.getString(key)+"\n");
                    }
                    else {messageSequenciaCiclica.append("[Dado inválido]");}
                }
                else {}
            }
        }
        return view;
    }

    public void escreveSequenciaCiclica(Bundle dados) {
        if (dados != null) {
            for (String key : dados.keySet()) {
                Log.d("Debug no Fragment", key + " = \"" + dados.get(key) + "\"");
                dadosTotais = dados;
            }
        }
    }


}