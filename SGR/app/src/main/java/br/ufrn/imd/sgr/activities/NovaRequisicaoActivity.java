package br.ufrn.imd.sgr.activities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import br.ufrn.imd.sgr.business.RequisicaoBusiness;
import br.ufrn.imd.sgr.dao.PacienteDao;
import br.ufrn.imd.sgr.dao.RequisicaoDao;
import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.model.Laboratorio;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.model.StatusRequisicao;
import br.ufrn.imd.sgr.model.TipoColetaSangue;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.DateUtils;
import br.ufrn.imd.sgr.utils.VolleyApplication;

public class NovaRequisicaoActivity extends AppCompatActivity implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private Requisicao requisicao;

    private CheckBox checkBoxFeridaOperatoria;
    private CheckBox checkBoxAbscessos;
    private CheckBox checkBoxUlceras;
    private CheckBox checkBoxFragmento;

    private RadioGroup radioCulturaSangue;
    private RadioButton radioVeia;
    private RadioButton radioArteria;
    private RadioButton radioCateterCentral;
    private RadioButton radioCateterUmbilical;

    private RadioGroup radioCulturaUrina;
    private RadioButton radioJatoMedio;
    private RadioButton radioPuncao;
    private RadioButton radioSacoColetor;
    private RadioButton radioSonda;
    private RadioButton radioSvd;

    private Switch switchCulturaSecrecao;
    private Switch switchCulturaSangue;
    private Switch switchCulturaUrina;

    private ImageButton imgBtnCalendarioSangue;

    private static TextView txtDataColeta;

    private static Date dataAmostraExameSangue;

    private DatePickerDialog datePickerDialogSangue;

    private Button buttonSalvar;

    private  TipoColetaSangue tipoColetaSangue;

    RequisicaoBusiness requisicaoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_requisicao);

        requisicaoService = new RequisicaoBusiness(getApplicationContext());

        //paciente
        Intent intent = getIntent();

        Paciente paciente = (Paciente) intent.getExtras().get(Constantes.DADOS_PACIENTE_REQUISICAO_NOVA_ACTIVITY);

        requisicao = new Requisicao();

        requisicao.setPaciente(paciente);


        //Cultura de secreção
        checkBoxFeridaOperatoria = (CheckBox) findViewById(R.id.checkBoxFeridaOperatoria);
        checkBoxFeridaOperatoria.setEnabled(false);

        checkBoxAbscessos = (CheckBox) findViewById(R.id.checkBoxAbscessos);
        checkBoxAbscessos.setEnabled(false);

        checkBoxUlceras = (CheckBox) findViewById(R.id.checkBoxUlceras);
        checkBoxUlceras.setEnabled(false);

        checkBoxFragmento = (CheckBox) findViewById(R.id.checkBoxFragmento);
        checkBoxFragmento.setEnabled(false);

        txtDataColeta = (TextView) findViewById(R.id.editTextDataColeta);

        imgBtnCalendarioSangue = (ImageButton) findViewById(R.id.imageButtonCalendarioSangue);
        imgBtnCalendarioSangue.setOnClickListener(this);

        //salvar
        buttonSalvar = (Button) findViewById(R.id.buttonSalvar);
        buttonSalvar.setOnClickListener(this);

        switchCulturaSangue = (Switch) findViewById(R.id.txtCulturaSangue);

        switchCulturaUrina = (Switch) findViewById(R.id.txtCulturaUrina);

        switchCulturaSecrecao = (Switch) findViewById(R.id.culturaSecrecao);

        switchCulturaSecrecao.setOnCheckedChangeListener(this);

        switchCulturaUrina.setOnCheckedChangeListener(this);

        switchCulturaSangue.setOnCheckedChangeListener(this);


        radioCulturaSangue = (RadioGroup) findViewById(R.id.radio_group_hemocultura);
        radioVeia = (RadioButton) findViewById(R.id.radio_hemocultura_veia);
        radioArteria = (RadioButton) findViewById(R.id.radio_hemocultura_arteria);
        radioCateterCentral = (RadioButton) findViewById(R.id.radio_hemocultura_cateter_central);
        radioCateterUmbilical = (RadioButton) findViewById(R.id.radio_hemocultura_cateter_umbilical);

        radioCulturaUrina = (RadioGroup) findViewById(R.id.radio_group_urucultura);
        radioJatoMedio = (RadioButton) findViewById(R.id.radio_urucultura_jato);
        radioSacoColetor = (RadioButton) findViewById(R.id.radio_urucultura_saco_coletor);
        radioSonda = (RadioButton) findViewById(R.id.radio_urucultura_sonda);
        radioSvd = (RadioButton) findViewById(R.id.radio_urucultura_svd);
        radioPuncao = (RadioButton) findViewById(R.id.radio_urucultura_puncao);

        radioCulturaSangue.setOnClickListener(this);

        radioCulturaUrina.setOnClickListener(this);

    }

    public void atualizarTipoColetaSangue(View v){
        switch(radioCulturaSangue.getCheckedRadioButtonId()) {
            case R.id.radio_hemocultura_veia:
                tipoColetaSangue = TipoColetaSangue.VEIA;
                break;
            case R.id.radio_hemocultura_cateter_umbilical:
                tipoColetaSangue = TipoColetaSangue.CATETER_UMBILICAL;
                break;
            case R.id.radio_hemocultura_cateter_central:
                tipoColetaSangue = TipoColetaSangue.CATETER_CENTRAL;
                break;
            case R.id.radio_hemocultura_arteria:
                tipoColetaSangue = TipoColetaSangue.ARTERIA;
                break;
            default:
                break;
        }
    }

    public void onCheckedChanged(CompoundButton buttonView,  boolean isChecked) {

        if(buttonView == switchCulturaSecrecao) {
            if (isChecked) {
                checkBoxFeridaOperatoria.setEnabled(true);
                checkBoxAbscessos.setEnabled(true);
                checkBoxUlceras.setEnabled(true);
                checkBoxFragmento.setEnabled(true);
            } else {
                checkBoxFeridaOperatoria.setEnabled(false);
                checkBoxFeridaOperatoria.setChecked(false);

                checkBoxAbscessos.setEnabled(false);
                checkBoxAbscessos.setChecked(false);

                checkBoxUlceras.setEnabled(false);
                checkBoxUlceras.setChecked(false);

                checkBoxFragmento.setEnabled(false);
                checkBoxFragmento.setChecked(false);
            }
        }else if(buttonView== switchCulturaSangue){
            if (isChecked) {
                radioVeia.setEnabled(true);
                radioArteria.setEnabled(true);
                radioCateterCentral.setEnabled(true);
                radioCateterUmbilical.setEnabled(true);
            }else{
                radioVeia.setEnabled(false);
                radioArteria.setEnabled(false);
                radioCateterCentral.setEnabled(false);
                radioCateterUmbilical.setEnabled(false);
            }

        }else if(buttonView == switchCulturaUrina){
            if (isChecked) {
                radioJatoMedio.setEnabled(true);
                radioSvd.setEnabled(true);
                radioSonda.setEnabled(true);
                radioPuncao.setEnabled(true);
                radioSacoColetor.setEnabled(true);
            }else{
                radioJatoMedio.setEnabled(false);
                radioSvd.setEnabled(false);
                radioSonda.setEnabled(false);
                radioPuncao.setEnabled(false);
                radioSacoColetor.setEnabled(false);
            }


        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putString(EDIT_SANGUE, (String) txtDataColeta.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       // txtDataColeta.setText(savedInstanceState.getString(EDIT_SANGUE));
    }

    @Override
    public void onClick(View v) {

        if(v==radioCulturaSangue){
            atualizarTipoColetaSangue(v);
        }
        else if(v == imgBtnCalendarioSangue){
            showDialog(0);
        } else if(v == buttonSalvar){

             montarRequisicao();
            if(validarRequisicao(requisicao)){
                requisicao = salvarRequisicao();

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

    private Requisicao salvarRequisicao() {
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
                      e.printStackTrace(); //TODO ?????
                    }

                    requisicao.setNumero(numeroRequisicao);


//                    } catch (JSONException e) {//TODO refatorar codigo. Lancar e tratar exceção.
//                        e.printStackTrace();
//                        Log.d("Teste", e.getMessage());
//                    }

                    requisicaoService.persistirRequisicao(requisicao);

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

            VolleyApplication.getInstance().getRequestQueue().add(jsObjRequest);

        } catch (JSONException e) {
            Log.d("Teste", e.toString());
        }

        return requisicao;

    }


    private void montarRequisicao() {
        //Requisicao requisicao = new Requisicao();

        requisicao.setStatus(StatusRequisicao.SOLICITADA);
        requisicao.setDataRequisicao(new Date());


        requisicao.setLaboratorio(Laboratorio.MICROBIOLOGIA);

        List<Exame> listExames = new ArrayList<>();


    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendario = GregorianCalendar.getInstance();

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(this, mDateSetListenerSangue, ano, mes, dia);

    }

    private DatePickerDialog.OnDateSetListener mDateSetListenerSangue = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dataAmostraExameSangue = DateUtils.obterData(year, monthOfYear, dayOfMonth);
            NovaRequisicaoActivity.txtDataColeta.setText(DateUtils.obterDataPorExtenso(year, monthOfYear, dayOfMonth));
        }
    };


}
