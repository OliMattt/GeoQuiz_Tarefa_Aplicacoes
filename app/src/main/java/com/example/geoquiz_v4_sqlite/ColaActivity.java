package com.example.geoquiz_v4_sqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ColaActivity extends AppCompatActivity {

    private static final String EXTRA_REPOSTA_E_VERDADEIRA = "com.example.geoquiz_v3.reposta_e_verdadeira";
    private static final String EXTRA_RESPOSTA_FOI_MOSTRADA= "com.example.geoquiz_v3.reposta_foi_mostrada";
    private static final String CHAVE_COLOU = "CHAVE_COLOU";

    private boolean mRespostaEVerdadeira;
    private TextView mTextViewResposta;
    private Button mBotaoMostraResposta;
    private Button mBotaoVoltar; // Novo botão
    private boolean mColou = false;  // Registra se o usuário viu ou não a resposta

    @Override
    protected void onCreate(Bundle instanciaSalva) {
        super.onCreate(instanciaSalva);
        setContentView(R.layout.activity_cola);

        // Recupera estado da activity em caso de rotação do dispositivo
        if (instanciaSalva != null) {
            mColou = instanciaSalva.getBoolean(CHAVE_COLOU);
            setRespostaFoiMostrada(mColou);
        }

        mRespostaEVerdadeira = getIntent().getBooleanExtra(EXTRA_REPOSTA_E_VERDADEIRA, false);

        mTextViewResposta = findViewById(R.id.view_texto_resposta);
        mBotaoMostraResposta = findViewById(R.id.botao_mostra_resposta);
        mBotaoVoltar = findViewById(R.id.botao_voltar); // Inicializando o botão de voltar

        mBotaoMostraResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColou = true;
                if (mRespostaEVerdadeira) {
                    mTextViewResposta.setText(R.string.botao_verdadeiro);
                } else {
                    mTextViewResposta.setText(R.string.botao_falso);
                }
                setRespostaFoiMostrada(true);
            }
        });

        // Configurando o botão Voltar
        mBotaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Finaliza a atividade e volta para a anterior
            }
        });
    }

    private void setRespostaFoiMostrada(boolean respostaFoiMostrada) {
        Intent dados = new Intent();
        dados.putExtra(EXTRA_RESPOSTA_FOI_MOSTRADA, respostaFoiMostrada);
        setResult(RESULT_OK, dados);
    }

    public static Intent novoIntent(Context packageContext, boolean respostaEVerdadeira) {
        Intent intent = new Intent(packageContext, ColaActivity.class);
        intent.putExtra(EXTRA_REPOSTA_E_VERDADEIRA, respostaEVerdadeira);
        return intent;
    }

    public static boolean foiMostradaResposta(Intent result) {
        return result.getBooleanExtra(EXTRA_RESPOSTA_FOI_MOSTRADA, false);
    }

    // Salva estado da activity em caso de rotação do dispositivo
    @Override
    public void onSaveInstanceState(Bundle instanciaSalva) {
        super.onSaveInstanceState(instanciaSalva);
        instanciaSalva.putBoolean(CHAVE_COLOU, mColou);
    }
}
