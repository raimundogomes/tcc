package br.ufrn.imd.sgr.business;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.ufrn.imd.sgr.activities.NovaRequisicaoActivity;
import br.ufrn.imd.sgr.dao.ExameDao;
import br.ufrn.imd.sgr.dao.PacienteDao;
import br.ufrn.imd.sgr.dao.RequisicaoDao;
import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.VolleyApplication;


public class RequisicaoBusiness {

    private RequisicaoDao requisicaoDao;

    private PacienteDao pacienteDao;

    private ExameDao exameDao;

    private Context applicationContext;

    public RequisicaoBusiness(Context applicationContext){
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
        return requisicao.getPaciente() != null && requisicao.getLaboratorio()!= null;
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

}
