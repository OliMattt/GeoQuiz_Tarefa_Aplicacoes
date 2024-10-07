package com.example.geoquiz_v4_sqlite;

import java.util.UUID;

public class Resposta {

    private String mUUIDQuestao;
    private boolean mRespostaCorreta;
    private String mRespostaOferecida;
    private boolean mColou;

    // Construtor da classe Resposta
    public Resposta(String textoRespostaID, boolean respostaCorreta, String respostaOferecida, boolean colou) {

        this.mUUIDQuestao = textoRespostaID;
        this.mRespostaCorreta = respostaCorreta;
        this.mRespostaOferecida = respostaOferecida;
        this.mColou = colou;
    }

    public String getUUIDQuestao(){
        return mUUIDQuestao;
    }

    public boolean isRespostaCorreta(){
        return  mColou;
    }

    public String getRespostaOferecida() {
        return mRespostaOferecida;
    }

    public boolean isColou() {
        return mColou;
    }
}

/*

 @Override
            public void onClick(View view) {

                  Acesso ao SQLite

                if (mQuestoesDb == null) {
                mQuestoesDb = new QuestaoDB(getBaseContext());
        }
i          nt indice = 0;
                mQuestoesDb.addQuestao(mBancoDeQuestoes[indice++]);
                mQuestoesDb.addQuestao(mBancoDeQuestoes[indice++]);
            }

 */
