package br.ufrn.imd.sgr.utils;

/**
 * Created by Neto on 30/05/2016.
 */
public final class Constantes {

    public final static String SGR = "SGR";

    public final static String REQUISICAO_DETALHE_ACTIVITY = "REQUISICAO_DETALHE_ACTIVITY";
    public static final String DADOS_PACIENTE_ACTIVITY = "DADOS_PACIENTE_ACTIVITY";
    public final static String CONFIGURACAO_ACTIVITY = "CONFIGURACAO_ACTIVITY";
    public final static String REQUISICAO_NOVA_ACTIVITY = "NOVA_REQUISICAO_ACTIVITY";
    public final static String DADOS_PACIENTE_REQUISICAO_NOVA_ACTIVITY = "DADOS_PACIENTE_REQUISICAO_NOVA_ACTIVITY";

    public static final String CONFIGURACAO_CRITERIO_SELECIONADO = "CONFIGURACAO_CRITERIO_SELECIONADO";

    public static final String CONFIGURACAO_CONECTADO = "CONFIGURACAO_CONECTADO";

    public static final String EMAIL = "EMAIL";


    public static final int CRITERIO_NUMERO_REQUISICAO = 0;
    public static final int CRITERIO_DATA_REQUISICAO = 1;
    public static final int CRITERIO_NOME_PACIENTE = 2;
    public static final int CRITERIO_STATUS_REQUISICAO = 3;

    public static final int INDICE_ACTIVITY_CONFIGURACOES = 0;
    public static final int INDICE_ACTIVITY_NOVA_REQUISICAO = 1;

    public static final String PREF_NAME = "LoginActivityPreferences";

    public static final String URL_PACIENTE = "http://192.168.0.31/sgr/service/paciente/";
    public static final String URL_REQUISICAO = "http://192.168.0.31/sgr/service/requisicao/";

    //public static final String URL_PACIENTE = "http://192.168.56.1/sgr/service/paciente/";
    //public static final String URL_REQUISICAO = "http://192.168.56.1/sgr/service/requisicao/";
    public static final String PRENCHIMENTO_OBRIGATORIO = "Campo %s é obrigatório";
}
