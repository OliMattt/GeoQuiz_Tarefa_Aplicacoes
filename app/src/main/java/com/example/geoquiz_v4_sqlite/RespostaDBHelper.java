package com.example.geoquiz_v4_sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RespostaDBHelper extends SQLiteOpenHelper {

    private static final int VERSAO = 1;
    private static final String NOME_DATABASE = "respostaDB";

    public RespostaDBHelper(Context context){
        super(context,NOME_DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + RespostaDbSchema.RespostasTbl.NOME + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RespostaDbSchema.RespostasTbl.Cols.UUID_DA_QUESTAO + " TEXT, " +
                RespostaDbSchema.RespostasTbl.Cols.RESPOSTA_CORRETA + " INTEGER, " +
                RespostaDbSchema.RespostasTbl.Cols.RESPOSTA_OFERECIDA + " TEXT, " +
                RespostaDbSchema.RespostasTbl.Cols.COLOU + " INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Política de upgrade é simplesmente descartar o conteúdo e começar novamente
        db.execSQL("DROP TABLE IF EXISTS " + RespostaDbSchema.RespostasTbl.NOME);
        onCreate(db);
    }

}
