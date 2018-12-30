package br.com.radani.www.mensageiro;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class gerenciadorBanco extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "banco.db";
    public static final String TABLE_NAME = "parametro_atual";
    public static final String col_1 = "codigo";
    public static final String col_2 = "label";
    public static final String col_3 = "valor_min";
    public static final String col_4 = "valor_max";
    public static final String col_5 = "passo";
    public static final String col_6 = "texto_1";
    public static final String col_7 = "texto_2";
    public static final String col_8 = "texto_3";
    public static final String col_9 = "texto_4";
    public static final String col_10 = "texto_5";
    public static final String col_11 = "texto_6";
    public static final String col_12 = "texto_7";
    public static final String col_13 = "texto_8";

    public static final String cria_parametro_atual = "CREATE TABLE "
            + TABLE_NAME + "(" + col_1 + " TEXT PRIMARY KEY, " + col_2
            + " TEXT, " + col_3 + " REAL," + col_4
            + " REAL, " + col_5
            + " REAL, " + col_6
            + " TEXT, " + col_7
            + " TEXT, " + col_8
            + " TEXT, " + col_9
            + " TEXT, " + col_10
            + " TEXT, " + col_11
            + " TEXT, " + col_12
            + " TEXT, " + col_13
            + " TEXT" + ")";

    public static final String cria_parametro_1 = "CREATE TABLE "+TABLE_NAME+" (COLUNA1 TEXT, COLUNA3 TEXT)";

    public gerenciadorBanco(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(cria_parametro_atual);

    }


//    para fins de debug
    public static String retornaquery(String query)
    {
        query = query + cria_parametro_atual;
        return query;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String codigo, String label, Double valor_min, Double valor_max, Double passo, String texto1, String texto2, String texto3, String texto4, String texto5, String texto6, String texto7, String texto8) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1, codigo);
        contentValues.put(col_2, label);
        contentValues.put(col_3, valor_min);
        contentValues.put(col_4, valor_max);
        contentValues.put(col_5, passo);
        contentValues.put(col_6, texto1);
        contentValues.put(col_7, texto2);
        contentValues.put(col_8, texto3);
        contentValues.put(col_9, texto4);
        contentValues.put(col_10, texto5);
        contentValues.put(col_11, texto6);
        contentValues.put(col_12, texto7);
        contentValues.put(col_13, texto8);

        long result_do_insert = db.insert(TABLE_NAME, null, contentValues);
        if (result_do_insert == -1)
            return false;
        else
            return true;

    }


}
