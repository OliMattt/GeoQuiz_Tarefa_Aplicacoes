package com.example.geoquiz_v4_sqlite;

public class RespostaDbSchema {

    public static final class RespostasTbl {
        public static final String NOME = "Respostas";

        public static final class Cols {
            public static final String UUID_DA_QUESTAO = "uuid"; // Identificação da questão respondida.
            public static final String RESPOSTA_CORRETA = "resposta_correta"; // Se a resposta do usuário foi correta (0 ou 1).
            public static final String RESPOSTA_OFERECIDA = "resposta_oferecida"; // Resposta apresentada pelo usuário (Verdadeiro ou Falso).
            public static final String COLOU = "colou"; // Se o usuário colou antes de responder (Sim ou Não).
        }
    }
}
