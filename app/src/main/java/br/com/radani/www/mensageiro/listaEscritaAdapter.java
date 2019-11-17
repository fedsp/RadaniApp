package br.com.radani.www.mensageiro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class listaEscritaAdapter extends ArrayAdapter<frasesEscrita> {

    private Context mContext;
    private List<frasesEscrita> frasesEscritaList;

    public listaEscritaAdapter(@NonNull Context context, ArrayList<frasesEscrita> list) {
        super(context, 0 , list);
        mContext = context;
        frasesEscritaList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.escrita_list_item,parent,false);

        frasesEscrita currentFrasesEscrita = frasesEscritaList.get(position);
        TextView name = listItem.findViewById(R.id.text_view_escrita);

        name.setText(currentFrasesEscrita.getmName());

        ImageButton editaItem = listItem.findViewById(R.id.button_listview_escrita);
        editaItem.setOnClickListener(v -> Toast.makeText(getContext(),"[][][]", Toast.LENGTH_SHORT).show());

        return listItem;
    }

    


}