package com.example.geoquiz_v4_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RespostaDB {

    private Context mContext;
    private static Context mStaticContext;
    private SQLiteDatabase mDatabase;

    public RespostaDB(Context context) {
        mContext = context.getApplicationContext();
        mStaticContext = mContext;
        mDatabase = new RespostaDBHelper(mContext).getWritableDatabase();
    }

    public long addResposta(String uuidQuestao, int respostaCorreta, String respostaOferecida, boolean colou) {
        ContentValues valores = new ContentValues();

        // Inserindo os valores nos atributos
        valores.put(RespostaDbSchema.RespostasTbl.Cols.UUID_DA_QUESTAO, uuidQuestao);
        valores.put(RespostaDbSchema.RespostasTbl.Cols.RESPOSTA_CORRETA, respostaCorreta);
        valores.put(RespostaDbSchema.RespostasTbl.Cols.RESPOSTA_OFERECIDA, respostaOferecida);
        valores.put(RespostaDbSchema.RespostasTbl.Cols.COLOU, colou ? 1 : 0);

        // Inserindo os dados no banco
        return mDatabase.insert(RespostaDbSchema.RespostasTbl.NOME, null, valores);
    }

    public Cursor queryResposta(String clausulaWhere, String[] argsWhere) {
        return mDatabase.query(
                RespostaDbSchema.RespostasTbl.NOME,
                null, // Retorna todas as colunas
                clausulaWhere,
                argsWhere,
                null, // Group by
                null, // Having
                null  // Order by
        );
    }

    public int removeRespostas() {
        // Deleta todas as respostas da tabela e retorna o numero de linhas deletadas
        return mDatabase.delete(
                RespostaDbSchema.RespostasTbl.NOME,
                null,
                null
        );
    }
}
