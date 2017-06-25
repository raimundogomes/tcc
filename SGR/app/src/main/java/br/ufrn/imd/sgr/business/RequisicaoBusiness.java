package br.ufrn.imd.sgr.business;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufrn.imd.sgr.RequisicoesActivity;
import br.ufrn.imd.sgr.activities.NovaRequisicaoActivity;
import br.ufrn.imd.sgr.activities.PesquisarPacienteActivity;
import br.ufrn.imd.sgr.dao.PacienteDao;
import br.ufrn.imd.sgr.dao.RequisicaoDao;
import br.ufrn.imd.sgr.model.Amostra;
import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.model.Laboratorio;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.model.StatusRequisicao;
import br.ufrn.imd.sgr.model.TipoExame;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.DetectaConexao;
import br.ufrn.imd.sgr.utils.VolleyApplication;


public class RequisicaoBusiness {

    RequisicaoDao requisicaoDao;

    PacienteDao pacienteDao;

    public RequisicaoBusiness(Context applicationContext){
        requisicaoDao = new RequisicaoDao(applicationContext);
        pacienteDao = new PacienteDao(applicationContext);
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

        requisicaoDao.insert(requisicao);

        // Log.d("Teste", requisicaoDao.listar().toString());
    }

}
