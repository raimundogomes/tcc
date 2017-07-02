package br.ufrn.imd.sgr.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufrn.imd.sgr.activities.NovaRequisicaoActivity;
import br.ufrn.imd.sgr.activities.RequisicoesActivity;
import br.ufrn.imd.sgr.dao.ExameDao;
import br.ufrn.imd.sgr.dao.PacienteDao;
import br.ufrn.imd.sgr.dao.RequisicaoDao;
import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.model.ListaRequisicaoTO;
import br.ufrn.imd.sgr.model.StatusRequisicao;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.VolleyApplication;


public class RequisicaoServiceImpl {

    private RequisicaoDao requisicaoDao;

    private PacienteDao pacienteDao;

    private ExameDao exameDao;

    private Context applicationContext;

    public RequisicaoServiceImpl(Context applicationContext){
        requisicaoDao = new RequisicaoDao(applicationContext);
        pacienteDao = new PacienteDao(applicationContext);
        exameDao = new ExameDao(applicationContext);


    }

    public void cancelarRequisicaoServico(final Requisicao requisicao, final Context applicationContext) {

        String url = Constantes.URL_REQUISICAO + "cancelarRequisicao";

        final JSONObject jsonBody;
        try {
            jsonBody = new JSONObject("{\"numeroRequisicao\":\"" + requisicao.getNumeroFormatado() +  "\"}");

            Log.d("Teste", jsonBody.get("numeroRequisicao").toString());

            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response
                    .Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Teste", response.toString());
                    requisicaoDao.cancelar(requisicao);

                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Teste", error.toString());
                    Toast.makeText(applicationContext,
                            "Não foi possível estabelecer conexão para cancelar a requisição.",
                            Toast.LENGTH_LONG).show();
                }
            });

            VolleyApplication.getInstance().getRequestQueue().add(jsObjRequest);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void desconectar(final Context applicationContext) {
        SharedPreferences sp = applicationContext.getSharedPreferences(Constantes.PREF_NAME, applicationContext.MODE_PRIVATE);

        boolean b = sp.getBoolean(Constantes.CONFIGURACAO_CONECTADO, false);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();

    }


    public void persistirRequisicao(Requisicao requisicao) {
        requisicao.setNumero(1l);
        Paciente pacienteBD = pacienteDao.consultarPeloProntuario(requisicao.getPaciente().getProntuario());

        if(pacienteBD.getId()==null){
            pacienteBD =  pacienteDao.insert(requisicao.getPaciente());
        }else{
            pacienteDao.update(requisicao.getPaciente());
        }

        requisicao.getPaciente().setId(pacienteBD.getId());

        requisicao = requisicaoDao.insert(requisicao);



        for (Exame exame :requisicao.getExames() ) {
            exame.setIdRequisicao(requisicao.getId());
            exameDao.insert(exame);
        }


        // Log.d("Teste", requisicaoDao.listar().toString());
    }

    public boolean validarRequisicao(final Requisicao requisicao){
        if(requisicao.getInternadoUltimas72Horas()==null){
            return false;
        }

        if(requisicao.getSubmetidoProcedimentoInvasivo() == null){
            return false;
        }

        if(requisicao.getUsouAntibiotico() ==null){
            return false;
        }

        if(requisicao.getExames().size()== 0){
            return false;
        }

        for (Exame e: requisicao.getExames()) {
            if( e.getTipoColeta()== null){
                return false;
            }
            if(e.getTipoMaterial()==null){
                return false;
            }
            if(e.getDataColeta()==null){
                return false;
            }
        }

        return true;
    }

    public Requisicao salvarRequisicao(final Requisicao requisicao,final NovaRequisicaoActivity novaRequisicaoActivity) {
        String url = Constantes.URL_REQUISICAO + "inserirRequisicao";

        final JSONObject jsonBody;
        try {

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String jsonInString = gson.toJson(requisicao);

            jsonBody = new JSONObject(jsonInString);


            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response
                    .Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Teste", response.toString());


                    Long numeroRequisicao = null;
                    try {
                        numeroRequisicao = response.getLong("numero");
                    } catch (JSONException e) {
                        e.printStackTrace(); //TODO refatorar codigo. Lancar e tratar exceção.
                    }

                    requisicao.setNumero(numeroRequisicao);

                    persistirRequisicao(requisicao);

                    Intent result = new Intent();
                    result.putExtra(Constantes.REQUISICAO_NOVA_ACTIVITY, requisicao);
                    novaRequisicaoActivity.setResult(novaRequisicaoActivity.RESULT_OK, result);
                    novaRequisicaoActivity.finish();


                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("Teste", error.toString());
                    Toast.makeText(novaRequisicaoActivity,
                            "Não foi possível estabelecer conexão para inserir a requisição.",
                            Toast.LENGTH_LONG).show();

                }
            });

            VolleyApplication.getInstance().getRequestQueue().add(jsObjRequest);

        } catch (JSONException e) {
            Log.d("Teste", e.toString());
        }

        return requisicao;

    }

    public Requisicao salvarRequisicaoSemInternet(final Requisicao requisicao,final NovaRequisicaoActivity novaRequisicaoActivity) {

        persistirRequisicao(requisicao);

        Intent result = new Intent();
        result.putExtra(Constantes.REQUISICAO_NOVA_ACTIVITY, requisicao);
        novaRequisicaoActivity.setResult(novaRequisicaoActivity.RESULT_OK, result);
        novaRequisicaoActivity.finish();

        return requisicao;

    }

    public Requisicao atualizarRequisicoes( final RequisicoesActivity novaRequisicaoActivity) {
        String url = Constantes.URL_REQUISICAO + "consultarRequisicoesAtualizadas";

        ListaRequisicaoTO req= new ListaRequisicaoTO();

        SharedPreferences preferencias = novaRequisicaoActivity.getSharedPreferences(Constantes.PREF_NAME, novaRequisicaoActivity.MODE_PRIVATE);
        String email = preferencias.getString(Constantes.EMAIL, "");
        req.setEmailSolicitante(email);
        req.setDataUltimaAtualizacao(new Date());

        Map map = new HashMap<Long, Date>();

        List<Requisicao> lista = consultarRequisicoesSolicitadas();

        for (Requisicao r: lista) {
            map.put(r.getId(), r.getDataUltimaModificacao());
        }

        req.setMapIdDataUltimaAtualizao(map);

        final JSONObject jsonBody;
        try {

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String jsonInString = gson.toJson(req);

            Log.d("SGR", jsonInString);

            jsonBody = new JSONObject(jsonInString);


            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response
                    .Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Teste", response.toString());

                    String resultado = response.toString();

                    Type listType = new TypeToken<List<Requisicao>>() {}.getType();

                    List<String> yourList = new Gson().fromJson(resultado, listType);

                    Intent result = new Intent();
                    result.putExtra(Constantes.REQUISICAO_NOVA_ACTIVITY, resultado);
                    novaRequisicaoActivity.setResult(RequisicoesActivity.RESULT_OK, result);
                    novaRequisicaoActivity.finish();


                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.d("Teste", error.toString());
                    Toast.makeText(novaRequisicaoActivity,
                            "Não foi possível estabelecer conexão para inserir a requisição.",
                            Toast.LENGTH_LONG).show();

                }
            });

            VolleyApplication.getInstance().getRequestQueue().add(jsObjRequest);

        } catch (JSONException e) {
            Log.d("Teste", e.toString());
        }

        return new Requisicao();

    }

    public List<Requisicao> consultarRequisicoesSolicitadas(){
        List<Requisicao> lista = requisicaoDao.consultarPorSituacao(StatusRequisicao.SOLICITADA.getCodigo());
        return  lista;
    }
}
