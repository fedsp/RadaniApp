package br.com.radani.www.mensageiro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


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
    public static final String PARAM_1_col_15 = "multiplicador";
    public static final String cria_parametro_1 = "CREATE TABLE IF NOT EXISTS "
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
            + " TEXT, " + PARAM_1_col_15
            + " TEXT" + ")";


    //cria tabela de configuracoes para maquina 1
    public static final String CONFIG_1_TABLE_NAME = "configuracoes_1";
    public static final String CONFIG_1_col_1 = "codigo";
    public static final String CONFIG_1_col_2 = "label";
    public static final String CONFIG_1_col_3 = "valor_min";
    public static final String CONFIG_1_col_4 = "valor_max";
    public static final String CONFIG_1_col_5 = "passo";
    public static final String CONFIG_1_col_6 = "texto_1";
    public static final String CONFIG_1_col_7 = "texto_2";
    public static final String CONFIG_1_col_8 = "texto_3";
    public static final String CONFIG_1_col_9 = "texto_4";
    public static final String CONFIG_1_col_10 = "texto_5";
    public static final String CONFIG_1_col_11 = "texto_6";
    public static final String CONFIG_1_col_12 = "texto_7";
    public static final String CONFIG_1_col_13 = "texto_8";
    public static final String CONFIG_1_col_14 = "unidade_medida";
    public static final String CONFIG_1_col_15 = "multiplicador";
    public static final String cria_configuracao_1 = "CREATE TABLE IF NOT EXISTS "
            + CONFIG_1_TABLE_NAME + "(" + CONFIG_1_col_1 + " TEXT PRIMARY KEY, " + CONFIG_1_col_2
            + " TEXT, " + CONFIG_1_col_3 + " REAL," + CONFIG_1_col_4
            + " REAL, " + CONFIG_1_col_5
            + " REAL, " + CONFIG_1_col_6
            + " TEXT, " + CONFIG_1_col_7
            + " TEXT, " + CONFIG_1_col_8
            + " TEXT, " + CONFIG_1_col_9
            + " TEXT, " + CONFIG_1_col_10
            + " TEXT, " + CONFIG_1_col_11
            + " TEXT, " + CONFIG_1_col_12
            + " TEXT, " + CONFIG_1_col_13
            + " TEXT, " + CONFIG_1_col_14
            + " TEXT, " + CONFIG_1_col_15
            + " TEXT" + ")";


    //cria tabela de display para maquina 1
    public static final String DISPLAY_1_TABLE_NAME = "display_1";
    public static final String DISPLAY_1_col_1 = "codigo";
    public static final String DISPLAY_1_col_2 = "label";
    public static final String DISPLAY_1_col_3 = "valor_min";
    public static final String DISPLAY_1_col_4 = "valor_max";
    public static final String DISPLAY_1_col_5 = "passo";
    public static final String DISPLAY_1_col_6 = "texto_1";
    public static final String DISPLAY_1_col_7 = "texto_2";
    public static final String DISPLAY_1_col_8 = "texto_3";
    public static final String DISPLAY_1_col_9 = "texto_4";
    public static final String DISPLAY_1_col_10 = "texto_5";
    public static final String DISPLAY_1_col_11 = "texto_6";
    public static final String DISPLAY_1_col_12 = "texto_7";
    public static final String DISPLAY_1_col_13 = "texto_8";
    public static final String DISPLAY_1_col_14 = "texto_9";
    public static final String DISPLAY_1_col_15 = "unidade_medida";
    public static final String DISPLAY_1_col_16 = "multiplicador";
    public static final String cria_display_1 = "CREATE TABLE IF NOT EXISTS "
            + DISPLAY_1_TABLE_NAME + "(" + DISPLAY_1_col_1 + " TEXT PRIMARY KEY, " + DISPLAY_1_col_2
            + " TEXT, " + DISPLAY_1_col_3 + " REAL," + DISPLAY_1_col_4
            + " REAL, " + DISPLAY_1_col_5
            + " REAL, " + DISPLAY_1_col_6
            + " TEXT, " + DISPLAY_1_col_7
            + " TEXT, " + DISPLAY_1_col_8
            + " TEXT, " + DISPLAY_1_col_9
            + " TEXT, " + DISPLAY_1_col_10
            + " TEXT, " + DISPLAY_1_col_11
            + " TEXT, " + DISPLAY_1_col_12
            + " TEXT, " + DISPLAY_1_col_13
            + " TEXT, " + DISPLAY_1_col_14
            + " TEXT, " + DISPLAY_1_col_15
            + " TEXT, " + DISPLAY_1_col_16
            + " TEXT" + ")";

    public static final String VALORES_ATUAIS_TABLE_NAME = "valores_atuais";
    public static final String valores_atuais_col_1 = "codigo";
    public static final String valores_atuais_col_2 = "label";
    public static final String valores_atuais_col_3 = "unidade";
    public static final String valores_atuais_col_4 = "valor";
    public static final String cria_valores_atuais = "CREATE TABLE IF NOT EXISTS "
            + VALORES_ATUAIS_TABLE_NAME + "("
            + valores_atuais_col_1 + " TEXT PRIMARY KEY, "
            + valores_atuais_col_2 + " TEXT,"
            + valores_atuais_col_3 + " TEXT,"
            + valores_atuais_col_4 + " TEXT"
            + ")";

    public gerenciadorBanco(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(cria_parametro_1);
        db.execSQL(cria_configuracao_1);
        db.execSQL(cria_display_1);
        db.execSQL(cria_valores_atuais);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ PARAM_1_TABLE_NAME);
        db.execSQL("DROP TABLE "+ CONFIG_1_TABLE_NAME);
        db.execSQL("DROP TABLE "+ DISPLAY_1_TABLE_NAME);
        db.execSQL("DROP TABLE "+ VALORES_ATUAIS_TABLE_NAME);
        onCreate(db);
    }

    // metodo para inserir no parametro 1
    public boolean insertData_PARAM_1(String codigo, String label, Double valor_min, Double valor_max, Double passo, String texto1, String texto2, String texto3, String texto4, String texto5, String texto6, String texto7, String texto8, String unidademedida, Double multiplicador) {
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
        contentValues.put(PARAM_1_col_15, multiplicador);
        long result_do_insert = db.insertWithOnConflict(PARAM_1_TABLE_NAME, codigo, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        if (result_do_insert == -1)
            return false;
        else
            return true;
    }

    // metodo para inserir no configuracao 1
    public boolean insertData_CONFIG_1(String codigo, String label, Double valor_min, Double valor_max, Double passo, String texto1, String texto2, String texto3, String texto4, String texto5, String texto6, String texto7, String texto8, String unidademedida, Double multiplicador) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONFIG_1_col_1, codigo);
        contentValues.put(CONFIG_1_col_2, label);
        contentValues.put(CONFIG_1_col_3, valor_min);
        contentValues.put(CONFIG_1_col_4, valor_max);
        contentValues.put(CONFIG_1_col_5, passo);
        contentValues.put(CONFIG_1_col_6, texto1);
        contentValues.put(CONFIG_1_col_7, texto2);
        contentValues.put(CONFIG_1_col_8, texto3);
        contentValues.put(CONFIG_1_col_9, texto4);
        contentValues.put(CONFIG_1_col_10, texto5);
        contentValues.put(CONFIG_1_col_11, texto6);
        contentValues.put(CONFIG_1_col_12, texto7);
        contentValues.put(CONFIG_1_col_13, texto8);
        contentValues.put(CONFIG_1_col_14, unidademedida);
        contentValues.put(CONFIG_1_col_15, multiplicador);
        long result_do_insert = db.insertWithOnConflict(CONFIG_1_TABLE_NAME, codigo, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        if (result_do_insert == -1)
            return false;
        else
            return true;
    }

    // metodo para inserir no parametro 1
    public boolean insertData_DISPLAY_1(String codigo, String label, Double valor_min, Double valor_max, Double passo, String texto1, String texto2, String texto3, String texto4, String texto5, String texto6, String texto7, String texto8, String texto9, String unidademedida, Double multiplicador) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DISPLAY_1_col_1, codigo);
        contentValues.put(DISPLAY_1_col_2, label);
        contentValues.put(DISPLAY_1_col_3, valor_min);
        contentValues.put(DISPLAY_1_col_4, valor_max);
        contentValues.put(DISPLAY_1_col_5, passo);
        contentValues.put(DISPLAY_1_col_6, texto1);
        contentValues.put(DISPLAY_1_col_7, texto2);
        contentValues.put(DISPLAY_1_col_8, texto3);
        contentValues.put(DISPLAY_1_col_9, texto4);
        contentValues.put(DISPLAY_1_col_10, texto5);
        contentValues.put(DISPLAY_1_col_11, texto6);
        contentValues.put(DISPLAY_1_col_12, texto7);
        contentValues.put(DISPLAY_1_col_13, texto8);
        contentValues.put(DISPLAY_1_col_14, texto9);
        contentValues.put(DISPLAY_1_col_15, unidademedida);
        contentValues.put(DISPLAY_1_col_16, multiplicador);
        long result_do_insert = db.insertWithOnConflict(DISPLAY_1_TABLE_NAME, codigo, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        if (result_do_insert == -1)
            return false;
        else
            return true;
    }

    public boolean insereDadosAtuais(String codigo, String label,String unidade, String valor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(valores_atuais_col_1, codigo);
        contentValues.put(valores_atuais_col_2, label);
        contentValues.put(valores_atuais_col_3, unidade);
        contentValues.put(valores_atuais_col_4, valor);

        long result_do_insert = db.insertWithOnConflict(VALORES_ATUAIS_TABLE_NAME,codigo, contentValues,SQLiteDatabase.CONFLICT_REPLACE);

        if (result_do_insert == -1)
            return false;
        else
            return true;
    }


    // obtém dados da tabela mestre de Parâmetros 1
    public String getUnidadeParametro(String codigo) {
        Cursor res;
        String unidade;
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("SELECT "+ PARAM_1_col_14 + " FROM " + PARAM_1_TABLE_NAME + " WHERE " + PARAM_1_col_1 + "=" + "'"+codigo+"'", null);
        res.moveToFirst();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(res));
        unidade = res.getString(0);
        return unidade;
    }

    // obtém dados da tabela mestre de Parâmetros 1
    public String getLabelParametro(String codigo) {
        Cursor res;
        String label;
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("SELECT "+ PARAM_1_col_2 + " FROM " + PARAM_1_TABLE_NAME + " WHERE " + PARAM_1_col_1 + "=" + "'"+codigo+"'", null);
        res.moveToFirst();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(res));
        label = res.getString(0);
        return label;
    }

    public Double getMultiplicadorParametro(String codigo) {
        Cursor res;
        String multiplicador;
        Double multiplicadornumerico;
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("SELECT "+ PARAM_1_col_15 + " FROM " + PARAM_1_TABLE_NAME + " WHERE " + PARAM_1_col_1 + "=" + "'"+codigo+"'", null);
        res.moveToFirst();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(res));
        multiplicador = res.getString(0);
        if (multiplicador==null){
            multiplicador="1.0";
            multiplicadornumerico = Double.valueOf(multiplicador);
        }
        else {
            multiplicadornumerico = Double.valueOf(multiplicador);
        }
        return multiplicadornumerico;
    }

    public String getValorParametro(String codigo, String valor, Double multiplicador) {
        Cursor res;
        SQLiteDatabase db = this.getWritableDatabase();
        //descobre se é texto ou numero
        res = db.rawQuery("SELECT "+ PARAM_1_col_15 + " FROM " + PARAM_1_TABLE_NAME + " WHERE " + PARAM_1_col_1 + "=" + "'"+codigo+"'", null);
        res.moveToFirst();
        if (res.isNull(0)){
            Integer n_texto;
            n_texto = Integer.valueOf(valor);
            n_texto = n_texto+1;
            valor = n_texto.toString();
            res = db.rawQuery("SELECT "+ "texto_" + valor + " FROM " + PARAM_1_TABLE_NAME + " WHERE " + PARAM_1_col_1 + "=" + "'"+codigo+"'", null);
            res.moveToFirst();
            valor = res.getString(0);
        }
        else {
            Double valorNumerico;
            valorNumerico = Double.valueOf(valor);
            valorNumerico = valorNumerico*multiplicador;
            valor = String.valueOf(valorNumerico);
            valor = utils.trata_valor_multiplicador(valor,multiplicador);
            }
        return valor;
    }

    // obtém unidades da tabela mestre de Parâmetros 1
    public String getUnidadeConfig(String codigo) {
        Cursor res;
        String unidade;
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("SELECT "+ CONFIG_1_col_14 + " FROM " + CONFIG_1_TABLE_NAME + " WHERE " + CONFIG_1_col_1 + "=" + "'"+codigo+"'", null);
        res.moveToFirst();
        unidade = res.getString(0);
        return unidade;
    }

    // obtém labels da tabela mestre de Configurações 1
    public String getLabelConfig(String codigo) {
        Cursor res;
        String label;
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("SELECT "+ CONFIG_1_col_2 + " FROM " + CONFIG_1_TABLE_NAME + " WHERE " + CONFIG_1_col_1 + "=" + "'"+codigo+"'", null);
        res.moveToFirst();
        label = res.getString(0);
        return label;
    }

    // obtém multiplicador da tabela mestre de Configurações 1
    public Double getMultiplicadorConfig(String codigo) {
        Cursor res;
        String multiplicador;
        Double multiplicadornumerico;
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("SELECT "+ CONFIG_1_col_15 + " FROM " + CONFIG_1_TABLE_NAME + " WHERE " + CONFIG_1_col_1 + "=" + "'"+codigo+"'", null);
        res.moveToFirst();
        multiplicador = res.getString(0);
        if (multiplicador==null){
            multiplicador="1.0";
            multiplicadornumerico = Double.valueOf(multiplicador);
        }
        else {
            multiplicadornumerico = Double.valueOf(multiplicador);
        }
        return multiplicadornumerico;
    }

    // obtém valor da tabela mestre de Configurações 1
    public String getValorConfig(String codigo, String valor, Double multiplicador) {
        Cursor res;
        SQLiteDatabase db = this.getWritableDatabase();
        //descobre se é texto ou numero
        res = db.rawQuery("SELECT "+ CONFIG_1_col_15 + " FROM " + CONFIG_1_TABLE_NAME + " WHERE " + CONFIG_1_col_1 + "=" + "'"+codigo+"'", null);
        res.moveToFirst();
        if (res.isNull(0)){
            Integer n_texto;
            n_texto = Integer.valueOf(valor);
            n_texto = n_texto+1;
            valor = n_texto.toString();
            res = db.rawQuery("SELECT "+ "texto_" + valor + " FROM " + CONFIG_1_TABLE_NAME + " WHERE " + CONFIG_1_col_1 + "=" + "'"+codigo+"'", null);
            res.moveToFirst();
            valor = res.getString(0);
        }
        else {
            Double valorNumerico;
            valorNumerico = Double.valueOf(valor);
            valorNumerico = valorNumerico*multiplicador;
            valor = String.valueOf(valorNumerico);
            valor = utils.trata_valor_multiplicador(valor,multiplicador);
        }
        return valor;
    }

    // obtém label da tabela mestre de Display 1
    public String getLabelDisplay(String codigo) {
        Cursor res;
        String label;
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("SELECT "+ DISPLAY_1_col_2 + " FROM " + DISPLAY_1_TABLE_NAME + " WHERE " + DISPLAY_1_col_1 + "=" + "'"+codigo+"'", null);
        res.moveToFirst();
        label = res.getString(0);
        return label;
    }

    // obtém unidade de medida da tabela mestre de Display 1
    public String getUnidadeDisplay(String codigo) {
        Cursor res;
        String label;
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("SELECT "+ DISPLAY_1_col_15 + " FROM " + DISPLAY_1_TABLE_NAME + " WHERE " + DISPLAY_1_col_1 + "=" + "'"+codigo+"'", null);
        res.moveToFirst();
        label = res.getString(0);
        return label;
    }

    // obtém dados da tabela mestre de Display 1
    public Double getMultiplicadorDisplay(String codigo) {
        Cursor res;
        String multiplicador;
        Double multiplicadornumerico;
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("SELECT "+ DISPLAY_1_col_16 + " FROM " + DISPLAY_1_TABLE_NAME + " WHERE " + DISPLAY_1_col_1 + "=" + "'"+codigo+"'", null);
        res.moveToFirst();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(res));
        multiplicador = res.getString(0);
        if (multiplicador==null){
            multiplicador="1.0";
            multiplicadornumerico = Double.valueOf(multiplicador);
        }
        else {
            multiplicadornumerico = Double.valueOf(multiplicador);
        }
        return multiplicadornumerico;
    }

    public String getValorDisplay(String codigo, String valor, Double multiplicador) {
        Cursor res;
        SQLiteDatabase db = this.getWritableDatabase();
        //descobre se é texto ou numero
        res = db.rawQuery("SELECT "+ DISPLAY_1_col_16 + " FROM " + DISPLAY_1_TABLE_NAME + " WHERE " + DISPLAY_1_col_1 + "=" + "'"+codigo+"'", null);
        res.moveToFirst();
        if (res.isNull(0)){
            Integer n_texto;
            n_texto = Integer.valueOf(valor);
            n_texto = n_texto+1;
            valor = n_texto.toString();
            res = db.rawQuery("SELECT "+ "texto_" + valor + " FROM " + DISPLAY_1_TABLE_NAME + " WHERE " + DISPLAY_1_col_1 + "=" + "'"+codigo+"'", null);
            res.moveToFirst();
            valor = res.getString(0);
        }
        else {
            Double valorNumerico;
            valorNumerico = Double.valueOf(valor);
            valorNumerico = valorNumerico*multiplicador;
            valor = String.valueOf(valorNumerico);
            valor = utils.trata_valor_multiplicador(valor,multiplicador);
        }
        return valor;
    }

    // Consulta genérica no banco
    public Cursor consultaBanco(String query) {
        Cursor res;
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery(query, null);
        return res;
    }





}
