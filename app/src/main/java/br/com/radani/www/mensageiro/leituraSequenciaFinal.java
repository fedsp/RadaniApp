package br.com.radani.www.mensageiro;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class leituraSequenciaFinal extends Fragment {
    private static final String TAG = "leituraSequenciaFinal";
    public Bundle dadosTotais;
    public ListView listView;
    public listaLeituraAdapter mAdapter;
    public Activity a;
    private Context mContext;

    // Initialise it from onAttach()
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments()!=null) {
            dadosTotais = getArguments();
            populaLista(dadosTotais);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leitura_sequencia_final,container,false);
        listView = view.findViewById(R.id.lista_seq_final);
        return view;
    }

    public void populaLista(Bundle listaFinal) {
        ArrayList<frasesLeitura> listaSequenciaFinal;
        listaSequenciaFinal = new ArrayList<>();
        if (listaFinal != null) {
            for (String key : listaFinal.keySet()) {
                Log.d("myApplication", key + " is a key in the bundle");
            }
            for (String key : dadosTotais.keySet()) {
                String tipoDado;
                String labelOuValor;
                tipoDado = key.substring(0, 2);
                String valor_atual = "123";
                String key_valor = "SF" + key.substring(2, 4) + "V";
                if (tipoDado.equals("SF")) {
                    labelOuValor = key.substring(4, 5);
                    valor_atual = dadosTotais.getString(key_valor);
                    if (labelOuValor.equals("L")) {
                        String passo;
                        passo = key.substring(0, 1) + key.substring(2, 4);
                        if (valor_atual != null) {
                            listaSequenciaFinal.add(new frasesLeitura(("[" + passo + "] " + dadosTotais.getString(key) + dadosTotais.getString(key_valor))));
                        } else {
                            listaSequenciaFinal.add(new frasesLeitura(("[" + passo + "] " + dadosTotais.getString(key))));
                        }
                    }
                    else {
                    }
                }
                else {
                }
            }
            mAdapter = new listaLeituraAdapter(mContext,listaSequenciaFinal);
            listView.setAdapter(mAdapter);


        }
        else {
            Toast.makeText(getContext(), "LISTA DE DADOS VAZIA", Toast.LENGTH_SHORT).show();
        }


    }



}