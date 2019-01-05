package br.com.radani.www.mensageiro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class gerenciadorBanco extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "banco.db";

    //cria tabela de parametro para maquina 1
    public static final String PARAM_1_TABLE_NAME = "parametros_1";
    public static final String PARAM_1_col_1 = "codigo";
    public static final String PARAM_1_col_2 = "label";
    public static final String PARAM_1_col_3 = "valor_min";
    public static final String PARAM_1_col_4 = "valor_max";
    public static final String PARAM_1_col_5 = "passo";
    public static final String PARAM_1_col_6 = "texto_1";
    public static final String PARAM_1_col_7 = "texto_2";
    public static final String PARAM_1_col_8 = "texto_3";
    public static final String PARAM_1_col_9 = "texto_4";
    public static final String PARAM_1_col_10 = "texto_5";
    public static final String PARAM_1_col_11 = "texto_6";
    public static final String PARAM_1_col_12 = "texto_7";
    public static final String PARAM_1_col_13 = "texto_8";
    public static final String PARAM_1_col_14 = "unidade_medida";
    public static final String cria_parametro_1 = "CREATE TABLE "
            + PARAM_1_TABLE_NAME + "(" + PARAM_1_col_1 + " TEXT PRIMARY KEY, " + PARAM_1_col_2
            + " TEXT, " + PARAM_1_col_3 + " REAL," + PARAM_1_col_4
            + " REAL, " + PARAM_1_col_5
            + " REAL, " + PARAM_1_col_6
            + " TEXT, " + PARAM_1_col_7
            + " TEXT, " + PARAM_1_col_8
            + " TEXT, " + PARAM_1_col_9
            + " TEXT, " + PARAM_1_col_10
            + " TEXT, " + PARAM_1_col_11
            + " TEXT, " + PARAM_1_col_12
            + " TEXT, " + PARAM_1_col_13
            + " TEXT, " + PARAM_1_col_14
            + " TEXT" + ")";


    //cria tabela de parametro atual
    public static final String PARAM_ATUAL_TABLE_NAME = "parametro_atual";
    public static final String PARAM_ATUAL_col_1 = "codigo";
    public static final String PARAM_ATUAL_col_2 = "valor";
    public static final String cria_parametro_atual = "CREATE TABLE "
            + PARAM_ATUAL_TABLE_NAME + "(" + PARAM_ATUAL_col_1 + " TEXT PRIMARY KEY, " + PARAM_ATUAL_col_2
            + " TEXT)";


    //cria tabela de configuração atual
    public static final String CONFIG_ATUAL_TABLE_NAME = "parametro_atual";
    public static final String CONFIG_ATUAL_col_1 = "codigo";
    public static final String CONFIG_ATUAL_col_2 = "valor";
    public static final String cria_configuracao_atual = "CREATE TABLE "
            + CONFIG_ATUAL_TABLE_NAME + "(" + CONFIG_ATUAL_col_1 + " TEXT PRIMARY KEY, " + CONFIG_ATUAL_col_2
            + " TEXT)";



    public gerenciadorBanco(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(cria_parametro_1);
        db.execSQL(cria_parametro_atual);
        db.execSQL(cria_configuracao_atual);
    }


//    para fins de debug
    public static String retornaquery(String query)
    {
        query = query + cria_parametro_1;
        return query;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ PARAM_1_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ PARAM_ATUAL_TABLE_NAME);
        onCreate(db);
    }



    public boolean insertData_PARAM_1(String codigo, String label, Double valor_min, Double valor_max, Double passo, String texto1, String texto2, String texto3, String texto4, String texto5, String texto6, String texto7, String texto8, String unidademedida) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PARAM_1_col_1, codigo);
        contentValues.put(PARAM_1_col_2, label);
        contentValues.put(PARAM_1_col_3, valor_min);
        contentValues.put(PARAM_1_col_4, valor_max);
        contentValues.put(PARAM_1_col_5, passo);
        contentValues.put(PARAM_1_col_6, texto1);
        contentValues.put(PARAM_1_col_7, texto2);
        contentValues.put(PARAM_1_col_8, texto3);
        contentValues.put(PARAM_1_col_9, texto4);
        contentValues.put(PARAM_1_col_10, texto5);
        contentValues.put(PARAM_1_col_11, texto6);
        contentValues.put(PARAM_1_col_12, texto7);
        contentValues.put(PARAM_1_col_13, texto8);
        contentValues.put(PARAM_1_col_14, unidademedida);

        long result_do_insert = db.insert(PARAM_1_TABLE_NAME, null, contentValues);
        if (result_do_insert == -1)
            return false;
        else
            return true;

    }

    public boolean insertData_PARAM_ATUAL(String codigo, String valor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PARAM_ATUAL_col_1, codigo);
        contentValues.put(PARAM_ATUAL_col_2, valor);


        long result_do_insert = db.insert(PARAM_ATUAL_TABLE_NAME, null, contentValues);
        if (result_do_insert == -1)
            return false;
        else
            return true;

    }

    public boolean insertData_CONFIG_ATUAL(String codigo, String valor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONFIG_ATUAL_col_1, codigo);
        contentValues.put(CONFIG_ATUAL_col_2, valor);


        long result_do_insert = db.insert(CONFIG_ATUAL_TABLE_NAME, null, contentValues);
        if (result_do_insert == -1)
            return false;
        else
            return true;

    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + PARAM_ATUAL_TABLE_NAME, null);
        return res;
    }

//    public boolean updateData_PARAM_ATUAL(String codigo, String valor) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(PARAM_ATUAL_col_1, codigo);
//        contentValues.put(PARAM_ATUAL_col_2, valor);
//
//
//        long result_do_update = db.update(PARAM_ATUAL_TABLE_NAME, null, contentValues);
//        if (result_do_update == -1)
//            return false;
//        else
//            return true;
//
//    }

}
