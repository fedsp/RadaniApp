package br.com.radani.www.mensageiro;

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
    public Bundle dadosTotais;
    private ListView listView;
    private ListaLeituraAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leitura_configuracao,container,false);
        ArrayList<Frases> listaConfiguracao;
        listaConfiguracao = new ArrayList<>();

        if (dadosTotais != null) {
            for (String key: dadosTotais.keySet())
            {
                Log.d ("myApplication", key + " is a key in the bundle");
            }

            for (String key : dadosTotais.keySet()) {
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
                        label_atual = dadosTotais.getString(key);
                        valor_atual = dadosTotais.getString(key_valor);
                        unidade_atual = dadosTotais.getString(key_unidade);
                        if (unidade_atual!=null) {
                            listaConfiguracao.add(new Frases("[" + key.substring(0, 3) + "] " + label_atual + ": " + valor_atual + " " + unidade_atual + "\n"));
                        }
                        else {
                            listaConfiguracao.add(new Frases("[" + key.substring(0, 3) + "] " + label_atual + ": " + valor_atual + " " +"\n"));
                        }
                    }
                    else {}
                }
                else {}

            }
        }
        listView = view.findViewById(R.id.lista_configuracao);
        mAdapter = new ListaLeituraAdapter(getActivity(),listaConfiguracao);
        listView.setAdapter(mAdapter);
        return view;
    }

    public void escreveConfiguracoes(Bundle dados) {
        if (dados != null) {
            for (String key : dados.keySet()) {
                Log.d("Debug no Fragment", key + " = \"" + dados.get(key) + "\"");
                dadosTotais = dados;
            }
        }
    }


}