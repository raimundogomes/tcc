package br.ufrn.imd.sgr.service.impl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufrn.imd.sgr.activities.novaRequisicao.NovaRequisicaoActivity;
import br.ufrn.imd.sgr.activities.RequisicoesActivity;
import br.ufrn.imd.sgr.dao.RequisicaoDao;
import br.ufrn.imd.sgr.dao.impl.RequisicaoDaoImpl;
import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.TO.ListaRequisicaoTO;
import br.ufrn.imd.sgr.model.SituacaoRequisicao;
import br.ufrn.imd.sgr.service.ExameService;
import br.ufrn.imd.sgr.service.PacienteService;
import br.ufrn.imd.sgr.service.RequisicaoService;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.VolleyApplication;


public class RequisicaoServiceImpl implements RequisicaoService {

    private RequisicaoDao requisicaoDao;

    private PacienteService pacienteService;

    private ExameService exameService;

    private Context applicationContext;

    public RequisicaoServiceImpl(Context applicationContext){
        requisicaoDao = new RequisicaoDaoImpl(applicationContext);
        pacienteService = new PacienteServiceImpl(applicationContext) ;
        exameService = new ExameServiceImpl(applicationContext);

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


    public Requisicao persistirRequisicao(Requisicao requisicao) {

        Paciente pacienteBD = pacienteService.consultarPeloProntuario(requisicao.getPaciente().getProntuario());

        requisicao.getPaciente().setId(pacienteBD.getId());

        if(pacienteBD.getId()==null){
            pacienteBD =  pacienteService.insert(requisicao.getPaciente());
        }else{
            pacienteService.update(requisicao.getPaciente());
        }

        requisicao = requisicaoDao.insert(requisicao);

        for (Exame exame :requisicao.getExames() ) {
            exame.setIdRequisicao(requisicao.getId());
            exame = exameService.insert(exame);
        }


      return requisicao;
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
                        requisicao.setNumero(numeroRequisicao);

                        Requisicao requisicaoTemp = persistirRequisicao(requisicao);

                        Intent result = new Intent();
                        result.putExtra(Constantes.REQUISICAO_NOVA_ACTIVITY, requisicaoTemp);
                        novaRequisicaoActivity.setResult(novaRequisicaoActivity.RESULT_OK, result);
                        novaRequisicaoActivity.finish();
                    } catch (JSONException e) {
                        e.printStackTrace(); //TODO refatorar codigo. Lancar e tratar exceção.
                        Log.d("Teste", e.toString());
                        Toast.makeText(novaRequisicaoActivity,
                                "Não foi possível converter o resultado da requisição",
                                Toast.LENGTH_LONG).show();
                    }


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

    public Requisicao atualizarRequisicoes(final RequisicoesActivity contexto) {
        String url = Constantes.URL_REQUISICAO + "consultarRequisicoesAtualizadas";

        final ListaRequisicaoTO req = montarListaRequisicaoTO(contexto);

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
                    try {
                        JSONArray jsonArray = response.getJSONArray("listaRequisicoes");

                        final Gson builder = new GsonBuilder()
                                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                                    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
                                        return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
                                    }
                                })
                                .create();

                        if(jsonArray.length()==0){
                            contexto.ocultarDialog();

                            Toast.makeText(contexto,
                                    "Não houve atualização desde da última sincronização!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        for (int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject j = jsonArray.getJSONObject(i);
                            Requisicao r = builder.fromJson(j.toString(), Requisicao.class);
                            requisicaoDao.atualizarRequisicao(r);
                            exameService.atualizarExames(r.getExames());
                        }

                        contexto.ocultarDialog();

                        contexto.atualizarTela();

                        Toast.makeText(contexto,
                                "Requisições sincronizadas!",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        contexto.ocultarDialog();
                        Toast.makeText(contexto,
                                "Falha ao sincronizar!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.d("SGR-APP", error.toString());
                    contexto.ocultarDialog();
                    Toast.makeText(contexto,
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

    @NonNull
    private ListaRequisicaoTO montarListaRequisicaoTO(RequisicoesActivity novaRequisicaoActivity) {
        ListaRequisicaoTO req = new ListaRequisicaoTO();

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
        return req;
    }

    public List<Requisicao> consultarRequisicoesSolicitadas(){
        List<Requisicao> lista = requisicaoDao.consultarPorSituacao(SituacaoRequisicao.SOLICITADA.getCodigo());
        return  lista;
    }

    public void excluir(long id){
        requisicaoDao.delete(id);
        exameService.delete(id);
    }

    @Override
    public List<Requisicao> listar() {
        return requisicaoDao.listar();
    }
}
