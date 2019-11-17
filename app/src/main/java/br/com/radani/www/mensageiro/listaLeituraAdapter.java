package br.com.radani.www.mensageiro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class listaLeituraAdapter extends ArrayAdapter<frasesLeitura> {

    private Context mContext;
    private List<frasesLeitura> frasesLeituraList;

    public listaLeituraAdapter(@NonNull Context context, ArrayList<frasesLeitura> list) {
        super(context, 0 , list);
        mContext = context;
        frasesLeituraList = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.leitura_list_item,parent,false);

        frasesLeitura currentFrasesLeitura = frasesLeituraList.get(position);
        TextView name = listItem.findViewById(R.id.text_view_leitura);
        name.setText(currentFrasesLeitura.getmName());
        return listItem;
    }

}