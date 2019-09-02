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

import java.util.ArrayList;


public class leituraConfiguracao extends Fragment {
    private static final String TAG = "leituraConfiguracao";
    private ListView listView;
    private ListaLeituraAdapter mAdapter;
    ArrayList<Frases> listaConfiguracao;
    private Context mActivity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            mActivity = context;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leitura_configuracao,container,false);
//        if (dadosTotais != null) {
//            for (String key: dadosTotais.keySet())
//            {
//                Log.d ("myApplication", key + " is a key in the bundle");
//            }
//        }
        listView = view.findViewById(R.id.lista_configuracao);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listaConfiguracao = new ArrayList<>();
        listaConfiguracao.add(new Frases("Teste"));
        mAdapter = new ListaLeituraAdapter(mActivity,listaConfiguracao);
        listView.setAdapter(mAdapter);
    }

    public void escreveConfiguracoes(Bundle dados) {


        if (dados != null) {
            for (String key : dados.keySet()) {
                String tipoDado;
                String labelOuValor;
                String valor_atual = "123";
                String key_valor = "C"+key.substring(1,3)+"V";
                String label_atual = "ABC";
                String unidade_atual = "XYZ";
                String key_unidade = "C"+key.substring(1,3)+"U";
                tipoDado = key.substring(0,1);
                labelOuValor = key.substring(3,4);
                if (tipoDado.equals("C")) {
                    if (labelOuValor.equals("L")) {
                        label_atual = dados.getString(key);
                        valor_atual = dados.getString(key_valor);
                        unidade_atual = dados.getString(key_unidade);
                        if (unidade_atual!=null) {
                            //listaConfiguracao.add(new Frases("[" + key.substring(0, 3) + "] " + label_atual + ": " + valor_atual + " " + unidade_atual + "\n"));
                            Log.d ("$$$", "[" + key.substring(0, 3) + "] " + label_atual + ": " + valor_atual + " " + unidade_atual + "\n");
                        }
                        else {
                            //listaConfiguracao.add(new Frases("[" + key.substring(0, 3) + "] " + label_atual + ": " + valor_atual + " " +"\n"));
                            Log.d ("$$$","[" + key.substring(0, 3) + "] " + label_atual + ": " + valor_atual + " " +"\n");
                        }
                    }
                }
            }
//            mAdapter.notifyDataSetChanged();
        }
    }


}