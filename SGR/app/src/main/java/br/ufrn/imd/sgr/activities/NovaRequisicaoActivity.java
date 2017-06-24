package br.ufrn.imd.sgr.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.ufrn.imd.sgr.R;
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
import br.ufrn.imd.sgr.utils.DateUtils;

public class NovaRequisicaoActivity extends AppCompatActivity implements
        View.OnClickListener{

    public static final int ID_DATA_REQUISICAO_SANGUE = 0;

    public static final int ID_DATA_REQUISICAO_URINA = 1;

    private static final String EDIT_SANGUE = "Sangue";
    private static final String EDIT_URINA = "Urina";
    private static final String LABORATRIO = "Laboratorio";
    private RequestQueue queue;

    private Paciente paciente;

    private CheckBox checkBoxSangue;

    private ImageButton imgBtnCalendarioSangue;

    private static TextView txtDataAmostraExameSangue;

    private static Date dataAmostraExameSangue;

    private DatePickerDialog datePickerDialogSangue;

    private CheckBox checkBoxUrina;

    private ImageButton imgBtnCalendarioUrina;

    private static TextView txtDataAmostraExameUrina;

    private static Date dataAmostraExameUrina;

    private Button buttonSalvar;

    RequisicaoDao requisicaoDao;
    PacienteDao pacienteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_requisicao);

        queue = Volley.newRequestQueue(NovaRequisicaoActivity.this);

        //paciente
        Intent intent = getIntent();

        paciente = (Paciente) intent.getExtras().get(Constantes.DADOS_PACIENTE_REQUISICAO_NOVA_ACTIVITY);

        atualizarDadosPaciente(paciente);

        //exame de sangue
        checkBoxSangue = (CheckBox) findViewById(R.id.checkBoxExameSangue);
        checkBoxSangue.setOnClickListener(this);

        txtDataAmostraExameSangue = (TextView) findViewById(R.id.editTextDataAmostraExameSangue);

        imgBtnCalendarioSangue = (ImageButton) findViewById(R.id.imageButtonCalendarioSangue);
        imgBtnCalendarioSangue.setOnClickListener(this);

        //exame de urina
        checkBoxUrina = (CheckBox) findViewById(R.id.checkBoxExameUrina);
        checkBoxUrina.setOnClickListener(this);

        txtDataAmostraExameUrina = (TextView) findViewById(R.id.editTextDataAmostraExameUrina);

        imgBtnCalendarioUrina = (ImageButton) findViewById(R.id.imageButtonCalendarioUrina);
        imgBtnCalendarioUrina.setOnClickListener(this);

        //salvar
        buttonSalvar = (Button) findViewById(R.id.buttonSalvar);
        buttonSalvar.setOnClickListener(this);

        /////////////////
        requisicaoDao = new RequisicaoDao(this);
        pacienteDao = new PacienteDao(this);
    }

    private void atualizarDadosPaciente(Paciente paciente) {

        TextView prontuario = (TextView) findViewById(R.id.txtProntuario);
        prontuario.setText(paciente.getProntuario().toString());

        TextView nome = (TextView) findViewById(R.id.txtNomePaciente);
        nome.setText(paciente.getNome());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EDIT_SANGUE, (String) txtDataAmostraExameSangue.getText().toString());
        outState.putString(EDIT_URINA, (String) txtDataAmostraExameUrina.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        txtDataAmostraExameSangue.setText(savedInstanceState.getString(EDIT_SANGUE));
        txtDataAmostraExameUrina.setText(savedInstanceState.getString(EDIT_URINA));
    }

    @Override
    public void onClick(View v) {

        if(v == checkBoxSangue && !checkBoxSangue.isChecked()){
            txtDataAmostraExameSangue.setText("");
        }
        else if(v == checkBoxUrina && !checkBoxUrina.isChecked()){
            txtDataAmostraExameUrina.setText("");
        } else if(v == imgBtnCalendarioSangue){
            showDialog(ID_DATA_REQUISICAO_SANGUE);
        } else if(v == imgBtnCalendarioUrina){
            showDialog(ID_DATA_REQUISICAO_URINA);
        } else if(v == buttonSalvar){
            Requisicao requisicao = montarRequisicao();
            if(validarRequisicao(requisicao)){
                salvarRequisicao(requisicao);
            }else{
                String mensagemErro = getString(R.string.erro_preenchimento_obrigatorio);
                Toast toast = Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private boolean validarRequisicao(Requisicao requisicao){
        return requisicao.getPaciente() != null && requisicao.getLaboratorio()!= null;
    }

    private void salvarRequisicao(final Requisicao requisicao) {
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

                   try {
                       Long numeroRequisicao = response.getLong("numero");

                       requisicao.setNumero(numeroRequisicao);

                    } catch (JSONException e) {// refatorar codigo. Lancar e tratar exceção.
                       e.printStackTrace();
                        Log.d("Teste", e.getMessage());
                    }
                    
                    persistirRequisicao(requisicao);
                    
                    Intent result = new Intent();
                    result.putExtra(Constantes.REQUISICAO_NOVA_ACTIVITY, requisicao);
                    setResult(RESULT_OK, result);
                    finish();
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("Teste", error.toString());
                    Toast.makeText(getApplicationContext(),
                            "Não foi possível estabelecer conexão para inserir a requisição.",
                            Toast.LENGTH_LONG).show();
                }
            });

            queue.add(jsObjRequest);

        } catch (JSONException e) {
            Log.d("Teste", e.toString());;
        }

    }

    private void persistirRequisicao(Requisicao requisicao) {

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

    private Requisicao montarRequisicao() {
        Requisicao requisicao = new Requisicao();

        requisicao.setStatus(StatusRequisicao.SOLICITADA);
        requisicao.setDataRequisicao(new Date());

        requisicao.setPaciente(paciente);
        requisicao.setLaboratorio(Laboratorio.MICROBIOLOGIA);

        List<Exame> listExames = new ArrayList<>();
        if(checkBoxSangue.isChecked()){
            Exame exameSangue = new Exame(TipoExame.SANGUE);
            Amostra amostraSangue = new Amostra();
            amostraSangue.setDataColeta(dataAmostraExameSangue);
            exameSangue.setAmostra(amostraSangue);
            listExames.add(exameSangue);
        }
        if(checkBoxUrina.isChecked()){
            Exame exameUrina = new Exame(TipoExame.URINA);
            Amostra amostraUrina = new Amostra();
            amostraUrina.setDataColeta(dataAmostraExameUrina);
            exameUrina.setAmostra(amostraUrina);
            listExames.add(exameUrina);
        }
        requisicao.setExames(listExames);

        return requisicao;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendario = GregorianCalendar.getInstance();

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case ID_DATA_REQUISICAO_SANGUE:
                return new DatePickerDialog(this, mDateSetListenerSangue, ano, mes, dia);
            case ID_DATA_REQUISICAO_URINA:
                return new DatePickerDialog(this, mDateSetListenerUrina, ano, mes, dia);
            default:
                break;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListenerSangue = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dataAmostraExameSangue = DateUtils.obterData(year, monthOfYear, dayOfMonth);
            NovaRequisicaoActivity.txtDataAmostraExameSangue.setText(DateUtils.obterDataPorExtenso(year, monthOfYear, dayOfMonth));
        }
    };

    private DatePickerDialog.OnDateSetListener mDateSetListenerUrina = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dataAmostraExameUrina = DateUtils.obterData(year, monthOfYear, dayOfMonth);
            NovaRequisicaoActivity.txtDataAmostraExameUrina.setText(DateUtils.obterDataPorExtenso(year, monthOfYear, dayOfMonth));
        }
    };

}
