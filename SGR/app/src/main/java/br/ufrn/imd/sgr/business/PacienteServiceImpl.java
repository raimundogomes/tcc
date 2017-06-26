package br.ufrn.imd.sgr.business;

import android.app.MediaRouteButton;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.service.PacienteService;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.VolleyApplication;

/**
 * Created by netou on 25/06/2017.
 */

public class PacienteServiceImpl implements PacienteService {

    public List<Paciente> pesquisarPaciente(String prontuario, final ProgressBar progressBar) {
        String urlPaciente = Constantes.URL_PACIENTE;

        if(!"".equals(prontuario)){
            urlPaciente += "prontuario/"+prontuario;
        }
        final List<Paciente> list = new ArrayList<Paciente>();
        Log.d("Teste", urlPaciente);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, urlPaciente, null, new Response
                .Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Teste", response.toString());
                Gson gson = new Gson();

                Paciente paciente = gson.fromJson(response.toString(), Paciente.class);
                progressBar.setVisibility(View.GONE);

                list.add(paciente);

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
               // throw new Exception(error.getMessage());
            }
        });
        VolleyApplication.getInstance().getRequestQueue().add(jsObjRequest);
        return list;
    }

    public List<Paciente> pesquisarPacientesPeloNome(String nome) {

        final List<Paciente> listaPaciente = new ArrayList<Paciente>();

        String urlNome = Constantes.URL_PACIENTE + "nome/" + nome;
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, urlNome, null, new Response
                .Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    Gson gson = new Gson();
                    Log.i("Teste", "teste 1");

                    for (int i = 0; i < response.length(); ++i) {
                        Paciente paciente = gson.fromJson(response.getJSONObject(i).toString(), Paciente.class);
                        Log.i("Teste", paciente.getNome());
                        listaPaciente.add(paciente);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        jsObjRequest.setTag("REST");

        VolleyApplication.getInstance().getRequestQueue().add(jsObjRequest);

        return listaPaciente;
    }



}
