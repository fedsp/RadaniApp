package br.com.radani.www.mensageiro;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class ListaLeituraAdapter extends ArrayAdapter<Frases> {

    private Context mContext;
    private List<Frases> frasesList;

    public ListaLeituraAdapter(@NonNull Context context, ArrayList<Frases> list) {
        super(context, 0 , list);
        mContext = context;
        frasesList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.simple_list_item_1,parent,false);

        Frases currentFrases = frasesList.get(position);



        TextView name = listItem.findViewById(R.id.textView_name);
        name.setText(currentFrases.getmName());



        return listItem;
    }
}