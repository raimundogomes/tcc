package br.ufrn.imd.sgr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.adapter.PacienteAdapter;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.utils.Constantes;

public class PesquisarPacienteActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private RequestQueue queue;

    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_paciente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("sljakljklj");
        setSupportActionBar(toolbar);

        Button button = (Button) findViewById(R.id.botao_pesquisar_paciente);

        queue = Volley.newRequestQueue(this);

        listview = (ListView) findViewById(R.id.listView_paciente);

        button.setOnClickListener(this);

        listview.setOnItemClickListener(this);

    }

    public boolean camposEstaoPreenchidos(){
        String prontuario =  ((TextView)findViewById(R.id.text_prontuario)).getText().toString();
        String nome = ((TextView) findViewById(R.id.text_nome_paciente)).getText().toString();

        if( !"".equals(prontuario) || !"".equals(nome) ){

            return true;

        }
        else{
            Toast toast = Toast.makeText(this, "Preencha o prontuário ou nome do paciente!", Toast.LENGTH_SHORT);
            toast.show();

            return false;
        }

    }

    @Override
    public void onClick(final View view) {
        //enviando informação

        if(camposEstaoPreenchidos()) {
            String prontuario =  ((TextView)findViewById(R.id.text_prontuario)).getText().toString();

            if(!"".equals(prontuario)){
                Log.d("Teste", "pesquisar por prontuário");
                pesquisarPaciente(prontuario, view);
            }
            else{

                pesquisarPacientesPeloNome(view);
            }

        }
    }

    private void pesquisarPaciente(String prontuario, final View view) {
        String urlPaciente = Constantes.URL_PACIENTE;

       if(!"".equals(prontuario)){
            urlPaciente += "prontuario/"+prontuario;
        }

        Log.d("Teste", urlPaciente);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, urlPaciente, null, new Response
                .Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Teste", response.toString());
                Gson gson = new Gson();

                    Paciente paciente = gson.fromJson(response.toString(), Paciente.class);
                    final List<Paciente> list = new ArrayList<Paciente>();
                    list.add(paciente);

                montarListaPacientes(list, view);

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsObjRequest);
    }

    private void montarListaPacientes(List<Paciente> list, View view) {
        TextView txt_lista_paciente = (TextView) findViewById(R.id.text_selecao_paciente);

        if(list.isEmpty()) {
            txt_lista_paciente.setText("Nenhum paciente encontrado para os parâmetros informados.");
        }else{
            txt_lista_paciente.setText("Selecione um paciente da lista:");
            txt_lista_paciente.setVisibility(View.VISIBLE);
        }

        final PacienteAdapter adapter = new PacienteAdapter(view.getContext(), list);
        listview.setAdapter(adapter);
    }

    private void pesquisarPacientesPeloNome(final View view) {
        try {
            String nome = ((TextView) findViewById(R.id.text_nome_paciente)).getText().toString();
            String urlNome = Constantes.URL_PACIENTE + "nome/" + nome;
            JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, urlNome, null, new Response
                    .Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    try {
                        Gson gson = new Gson();
                        Log.i("Teste", "teste 1");
                        final List<Paciente> list = new ArrayList<Paciente>();
                        for (int i = 0; i < response.length(); ++i) {
                            Paciente paciente = gson.fromJson(response.getJSONObject(i).toString(), Paciente.class);
                            Log.i("Teste", paciente.getNome());
                            list.add(paciente);
                        }
                        montarListaPacientes(list, view);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(),
                            "ERRO ", Toast.LENGTH_LONG)
                            .show();
                }
            });

            jsObjRequest.setTag("REST");

            queue.add(jsObjRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        Paciente paciente = (Paciente) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, NovaRequisicaoActivity.class);
        intent.putExtra(Constantes.DADOS_PACIENTE_REQUISICAO_NOVA_ACTIVITY, paciente);
        startActivityForResult(intent, Constantes.INDICE_ACTIVITY_NOVA_REQUISICAO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constantes.INDICE_ACTIVITY_NOVA_REQUISICAO:
                if(resultCode == RESULT_OK){
                    Requisicao novaRequisicao = (Requisicao) data.getSerializableExtra(Constantes.REQUISICAO_NOVA_ACTIVITY);
                    Intent result = new Intent();
                    result.putExtra(Constantes.REQUISICAO_NOVA_ACTIVITY, novaRequisicao);
                    setResult(RESULT_OK, result);
                    finish();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
