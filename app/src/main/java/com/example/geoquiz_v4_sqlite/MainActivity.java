package com.example.geoquiz_v4_sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

/*
  Modelo de projeto para a Atividade 1.
  Será preciso adicionar o cadastro das respostas do usuário ao Quiz, conforme
  definido no Canvas.

  GitHub: https://github.com/udofritzke/GeoQuiz
 */

public class MainActivity extends AppCompatActivity {

    private Button mBotaoVerdadeiro;
    private Button mBotaoFalso;
    private Button mBotaoProximo;
    private Button mBotaoMostra;
    private Button mBotaoDeleta;


    private Button mBotaoCola;

    private TextView mTextViewQuestao;
    private TextView mTextViewQuestoesArmazenadas;

    private static final String TAG = "QuizActivity";
    private static final String CHAVE_INDICE = "INDICE";
    private static final int CODIGO_REQUISICAO_COLA = 0;

    private final Questao[] mBancoDeQuestoes = new Questao[]{
            new Questao(R.string.questao_suez, true),
            new Questao(R.string.questao_alemanha, false)
    };

    QuestaoDB mQuestoesDb;
    RespostaDB mRespostaDb;

    private int mIndiceAtual = 0;
    private boolean mEhColador;

    @Override
    protected void onCreate(Bundle instanciaSalva) {
        super.onCreate(instanciaSalva);
        setContentView(R.layout.activity_main);
        //Log.d(TAG, "onCreate()");
        if (instanciaSalva != null) {
            mIndiceAtual = instanciaSalva.getInt(CHAVE_INDICE, 0);
        }

        mTextViewQuestao = (TextView) findViewById(R.id.view_texto_da_questao);
        atualizaQuestao();

        mBotaoVerdadeiro = (Button) findViewById(R.id.botao_verdadeiro);
        // utilização de classe anônima interna
        mBotaoVerdadeiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaResposta(true);

                if (mQuestoesDb == null) {
                    mQuestoesDb = new QuestaoDB(getBaseContext());
                }
                int indice = 0;
                mQuestoesDb.addQuestao(mBancoDeQuestoes[indice++]);

                salvaResposta(true);
            }
        });

        mBotaoFalso = (Button) findViewById(R.id.botao_falso);
        mBotaoFalso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaResposta(false);

                if (mQuestoesDb == null) {
                    mQuestoesDb = new QuestaoDB(getBaseContext());
                }
                int indice = 0;
                mQuestoesDb.addQuestao(mBancoDeQuestoes[indice++]);

                salvaResposta(false);
            }
        });

        mBotaoProximo = (Button) findViewById(R.id.botao_proximo);
        mBotaoProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIndiceAtual = (mIndiceAtual + 1) % mBancoDeQuestoes.length;
                mEhColador = false;
                atualizaQuestao();
            }
        });

        mBotaoCola = (Button) findViewById(R.id.botao_cola);
        mBotaoCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inicia ColaActivity
                // Intent intent = new Intent(MainActivity.this, ColaActivity.class);
                boolean respostaEVerdadeira = mBancoDeQuestoes[mIndiceAtual].isRespostaCorreta();
                Intent intent = ColaActivity.novoIntent(MainActivity.this, respostaEVerdadeira);
                //startActivity(intent);
                startActivityForResult(intent, CODIGO_REQUISICAO_COLA);
            }
        });


        //Cursor cur = mQuestoesDb.queryQuestao ("_id = ?", val);////(null, null);
        //String [] val = {"1"};
        mBotaoMostra = (Button) findViewById(R.id.botao_mostra_questoes);
        mBotaoMostra.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                /*
                  Acesso ao SQLite
                */
                if (mQuestoesDb == null) return;
                if (mTextViewQuestoesArmazenadas == null) {
                    mTextViewQuestoesArmazenadas = (TextView) findViewById(R.id.texto_questoes_a_apresentar);
                } else {
                    mTextViewQuestoesArmazenadas.setText("");
                }

               /*Cursor cursor = mQuestoesDb.queryQuestao(null, null);
                if (cursor != null) {
                    if (cursor.getCount() == 0) {
                        mTextViewQuestoesArmazenadas.setText("Nada a apresentar");
                        Log.i("MSGS", "Nenhum resultado");
                    }
                    //Log.i("MSGS", Integer.toString(cursor.getCount()));
                    //Log.i("MSGS", "cursor não nulo!");
                    try {
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            String texto = cursor.getString(cursor.getColumnIndex(QuestoesDbSchema.QuestoesTbl.Cols.TEXTO_QUESTAO));
                            Log.i("MSGS", texto);

                            mTextViewQuestoesArmazenadas.append(texto + "\n");
                            cursor.moveToNext();
                        }
                    } finally {
                        cursor.close();
                    }
                } else
                    Log.i("MSGS", "cursor nulo!");*/

                Cursor cursor = mRespostaDb.queryResposta(null, null);

                if (cursor != null) {
                    if (cursor.getCount() == 0) {
                        mTextViewQuestoesArmazenadas.setText("Nenhuma resposta no banco.");
                        Log.i("MSGS", "Nenhum resultado");
                    } else {
                        try {
                            cursor.moveToFirst();
                            while (!cursor.isAfterLast()) {

                                @SuppressLint("Range") String uuidQuestao = cursor.getString(cursor.getColumnIndex(RespostaDbSchema.RespostasTbl.Cols.UUID_DA_QUESTAO));
                                @SuppressLint("Range") String respostaCorreta = cursor.getString(cursor.getColumnIndex(RespostaDbSchema.RespostasTbl.Cols.RESPOSTA_CORRETA));
                                @SuppressLint("Range") String respostaOferecida = cursor.getString(cursor.getColumnIndex(RespostaDbSchema.RespostasTbl.Cols.RESPOSTA_OFERECIDA));
                                @SuppressLint("Range") int colou = cursor.getInt(cursor.getColumnIndex(RespostaDbSchema.RespostasTbl.Cols.COLOU));

                                String texto = "UUID: " + uuidQuestao + "\nResposta correta: " + respostaCorreta +
                                        "\nResposta oferecida: " + respostaOferecida +
                                        "\nColou: " + (colou == 1 ? "Sim" : "Não");

                                mTextViewQuestoesArmazenadas.append(texto + "\n\n\n");
                                cursor.moveToNext();
                            }
                        } catch (Exception e) {
                            Log.e("MSGS", "Erro ao processar cursor: " + e.getMessage());
                        } finally {
                            cursor.close();
                        }
                    }
                } else {
                    Log.i("MSGS", "cursor nulo!");
                }
            }
        });
        mBotaoDeleta = (Button) findViewById(R.id.botao_deleta);
        mBotaoDeleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                  Acesso ao SQLite
                */
                if (mQuestoesDb != null) {
                    mQuestoesDb.removeBanco();
                    if (mTextViewQuestoesArmazenadas == null) {
                        mTextViewQuestoesArmazenadas = (TextView) findViewById(R.id.texto_questoes_a_apresentar);
                    }
                    mTextViewQuestoesArmazenadas.setText("");
                }

                if (mRespostaDb != null) {
                    mRespostaDb.removeRespostas();  // Remove todas as respostas cadastradas
                    if (mTextViewQuestoesArmazenadas == null) {
                        mTextViewQuestoesArmazenadas = (TextView) findViewById(R.id.texto_questoes_a_apresentar);
                    }
                    mTextViewQuestoesArmazenadas.setText("");
                }
            }
        });

    }

    private void salvaResposta(boolean respostaOferecida){


        if (mRespostaDb == null) {
            mRespostaDb = new RespostaDB(getBaseContext());
        }

        //BANCO DE RESPOSTAS
        Questao questaoAtual = mBancoDeQuestoes[mIndiceAtual];
        String uuidQuestao = UUID.randomUUID().toString();
        boolean respostaCorreta = questaoAtual.isRespostaCorreta();
        int respostaCorretaInt = respostaCorreta ? 1 : 0; // Converte boolean para int (1 para correta, 0 para incorreta)

        mRespostaDb.addResposta(
                uuidQuestao,                                 // UUID da questão
                respostaCorretaInt,                          // 1 se correta, 0 se incorreta
                respostaOferecida ? "verdadeiro" : "falso",  // Resposta oferecida (verdadeiro ou falso)
                mEhColador                                   // Indica se o usuário colou
        );
    }

    private void atualizaQuestao() {
        int questao = mBancoDeQuestoes[mIndiceAtual].getTextoRespostaId();
        mTextViewQuestao.setText(questao);
    }

    private void verificaResposta(boolean respostaPressionada) {
        boolean respostaCorreta = mBancoDeQuestoes[mIndiceAtual].isRespostaCorreta();
        int idMensagemResposta = 0;

        if (mEhColador) {
            idMensagemResposta = R.string.toast_julgamento;
        } else {
            if (respostaPressionada == respostaCorreta) {
                idMensagemResposta = R.string.toast_correto;
            } else
                idMensagemResposta = R.string.toast_incorreto;
        }
        Toast.makeText(this, idMensagemResposta, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle instanciaSalva) {
        super.onSaveInstanceState(instanciaSalva);
        Log.i(TAG, "onSaveInstanceState()");
        instanciaSalva.putInt(CHAVE_INDICE, mIndiceAtual);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int codigoRequisicao, int codigoResultado, Intent dados) {
        if (codigoResultado != Activity.RESULT_OK) {
            return;
        }
        if (codigoRequisicao == CODIGO_REQUISICAO_COLA) {
            if (dados == null) {
                return;
            }
            mEhColador = ColaActivity.foiMostradaResposta(dados);
        }
    }
}