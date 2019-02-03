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

public class leituraSequenciaRotina2 extends Fragment {
    private static final String TAG = "leituraSequenciaRotina2";
    public TextView messageSequenciaRotina2;
    public Bundle dadosTotais;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leitura_sequencia_rotina_2,container,false);
        messageSequenciaRotina2 = view.findViewById(R.id.messagesSequenciaRotina2);
        messageSequenciaRotina2.setMovementMethod((new ScrollingMovementMethod()));
        if (dadosTotais != null) {
            for (String key : dadosTotais.keySet()) {
                String tipoDado;
                String labelOuValor;
                tipoDado = key.substring(0,2);

                if (tipoDado.equals("S2")) {
                    labelOuValor = key.substring(4,5);
                    if (labelOuValor.equals("L")) {
                        String passo;
                        passo = key.substring(0,1)+key.substring(2,4);
                        messageSequenciaRotina2.append("["+passo+"] ");
                        messageSequenciaRotina2.append(dadosTotais.getString(key));
                    }
                    else if (labelOuValor.equals("V")) {
                        messageSequenciaRotina2.append(dadosTotais.getString(key)+"\n");
                    }
                    else {messageSequenciaRotina2.append("[Dado inválido]");}
                }
                else {}
            }
        }
        return view;
    }

    public void escreveSequenciaRotina2(Bundle dados) {
        if (dados != null) {
            for (String key : dados.keySet()) {
                Log.d("Debug no Fragment", key + " = \"" + dados.get(key) + "\"");
                dadosTotais = dados;
            }
        }
    }


}