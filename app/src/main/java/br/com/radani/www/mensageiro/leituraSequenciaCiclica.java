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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class leituraSequenciaCiclica extends Fragment {
    private static final String TAG = "leituraSequenciaCiclica";
    public Bundle dadosTotais;
    private ListView listView;
    private ListaLeituraAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leitura_sequencia_ciclica, container, false);
        ArrayList<Frases> listaSequenciaCiclica;
        listaSequenciaCiclica = new ArrayList<>();
        if (dadosTotais != null) {
            for (String key : dadosTotais.keySet()) {
                String tipoDado;
                String labelOuValor;
                tipoDado = key.substring(0, 2);
                String valor_atual = "123";
                String key_valor = "SC" + key.substring(2, 4) + "V";
                if (tipoDado.equals("SC")) {
                    labelOuValor = key.substring(4, 5);
                    valor_atual = dadosTotais.getString(key_valor);
                    if (labelOuValor.equals("L")) {
                        String passo;
                        passo = key.substring(0, 1) + key.substring(2, 4);
                        if (valor_atual != null) {
                            listaSequenciaCiclica.add(new Frases(("[" + passo + "] " + dadosTotais.getString(key) + dadosTotais.getString(key_valor))));

                        } else {
                            listaSequenciaCiclica.add(new Frases(("[" + passo + "] " + dadosTotais.getString(key))));
                        }
                    }
                    else {
                    }
                }
                else {
                }
            }
        }
        else {
        }
        listView = view.findViewById(R.id.lista_seq_ciclica);
        mAdapter = new ListaLeituraAdapter(getActivity(), listaSequenciaCiclica);
        listView.setAdapter(mAdapter);
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