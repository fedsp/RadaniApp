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


public class leituraDisplay extends Fragment {
    private static final String TAG = "leituraDisplay";
    public Bundle dadosTotais;
    public ListView listView;
    public ListaLeituraAdapter mAdapter;
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
        View view = inflater.inflate(R.layout.leitura_display,container,false);
        listView = view.findViewById(R.id.lista_display);
        return view;
    }

    public void populaLista(Bundle listaFinal) {
        ArrayList<Frases> listaDisplay;
        listaDisplay = new ArrayList<>();
        if (listaFinal != null) {
            for (String key : listaFinal.keySet()) {
                Log.d("myApplication", key + " is a key in the bundle");
            }
            for (String key : dadosTotais.keySet()) {
                String tipoDado;
                String labelOuValor;
                String valor_atual = "123";
                String key_valor = "D"+key.substring(1,3)+"V";
                String label_atual = "ABC";
                String unidade_atual = "XYZ";
                String key_unidade = "D"+key.substring(1,3)+"U";

                tipoDado = key.substring(0,1);
                labelOuValor = key.substring(3,4);
                if (tipoDado.equals("D")) {
                    if (labelOuValor.equals("L")) {
                        label_atual = dadosTotais.getString(key);
                        valor_atual = dadosTotais.getString(key_valor);
                        valor_atual = Utils.trataValor(valor_atual);
                        unidade_atual = dadosTotais.getString(key_unidade);
                        if (unidade_atual!=null) {
                            listaDisplay.add(new Frases("[" + key.substring(0, 3) + "] " + label_atual + ": " + valor_atual + " " + unidade_atual + "\n"));
                        }
                        else {
                            listaDisplay.add(new Frases("[" + key.substring(0, 3) + "] " + label_atual + ": " + valor_atual + " " +"\n"));
                        }
                    }
                    else {}
                }
                else {}

            }
            mAdapter = new ListaLeituraAdapter(mContext,listaDisplay);
            listView.setAdapter(mAdapter);


        }
        else {
            Toast.makeText(getContext(), "LISTA DE DADOS VAZIA", Toast.LENGTH_SHORT).show();
        }


    }



}