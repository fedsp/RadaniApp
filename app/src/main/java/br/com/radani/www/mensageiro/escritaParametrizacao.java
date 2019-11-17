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


public class escritaParametrizacao extends Fragment {
    private static final String TAG = "leituraParametrizacao";
    public Bundle dadosTotais;
    public ListView listView;
    public listaEscritaAdapter mAdapter;
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
        View view = inflater.inflate(R.layout.leitura_parametrizacao,container,false);
        listView = view.findViewById(R.id.lista_parametros);
        return view;
    }

    public void populaLista(Bundle listaFinal) {
        ArrayList<Frases> listaParametro;
        listaParametro = new ArrayList<>();
        if (listaFinal != null) {
            for (String key : listaFinal.keySet()) {
                Log.d("myApplication", key + " is a key in the bundle");
            }
            for (String key : listaFinal.keySet()) {
                String tipoDado;
                String labelOuValor;
                String valor_atual = "123";
                String key_valor = "P" + key.substring(1, 3) + "V";
                String label_atual = "ABC";
                String unidade_atual = "XYZ";
                String key_unidade = "P" + key.substring(1, 3) + "U";
                tipoDado = key.substring(0, 1);
                labelOuValor = key.substring(3, 4);
                if (tipoDado.equals("P")) {
                    if (labelOuValor.equals("L")) {
                        label_atual = listaFinal.getString(key);
                        valor_atual = listaFinal.getString(key_valor);
                        unidade_atual = listaFinal.getString(key_unidade);
                        if (unidade_atual != null) {
                            listaParametro.add(new Frases("[" + key.substring(0, 3) + "] " + label_atual + ": " + valor_atual + " " + unidade_atual + "\n"));
                        } else {
                            listaParametro.add(new Frases("[" + key.substring(0, 3) + "] " + label_atual + ": " + valor_atual + " " + "\n"));
                        }
                    } else {
                    }
                } else {
                }

            }
            mAdapter = new listaEscritaAdapter(mContext,listaParametro);
            listView.setAdapter(mAdapter);


        }
        else {
            Toast.makeText(getContext(), "LISTA DE DADOS VAZIA", Toast.LENGTH_SHORT).show();
        }


    }



}