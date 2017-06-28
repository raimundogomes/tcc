package br.ufrn.imd.sgr.activities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
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
import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.model.Laboratorio;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.model.StatusRequisicao;
import br.ufrn.imd.sgr.model.TipoColeta;
import br.ufrn.imd.sgr.model.TipoExame;
import br.ufrn.imd.sgr.model.TipoMaterial;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.DateUtils;
import br.ufrn.imd.sgr.utils.VolleyApplication;

public class NovaRequisicaoActivity extends AppCompatActivity implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private Requisicao requisicao;

    private RadioButton radioFeridaOperatoria;
    private RadioButton radioAbscessos;
    private RadioButton radioUlcera;
    private RadioButton radioFragmentoTecido;
    private RadioButton radioSwab;
    private RadioButton radioAspiradoAgulha;

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

    private Button botaoDataSecrecao;

    private Button botaoHoraSecrecao;

    private static String dataColetaSecrecao;

    private DatePickerDialog datePickerDialogSangue;

    private Button buttonSalvar;

    private RequisicaoBusiness requisicaoService;
    private RadioGroup radioTipoColetaSecrecao;
    private RadioGroup radioCulturaSecrecao;

    DadosPaciente dadosPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_requisicao);

        requisicaoService = new RequisicaoBusiness(getApplicationContext());

        iniciarRequisicao();

        botaoDataSecrecao = (Button) findViewById(R.id.bt_data_secrecao);
        botaoDataSecrecao.setText(" " +DateUtils.obterDataDDMMYYY(new Date()));
        botaoDataSecrecao.setOnClickListener(this);

        botaoHoraSecrecao = (Button) findViewById(R.id.bt_hora_secrecao);
        botaoHoraSecrecao.setOnClickListener(this);

        dadosPaciente = new DadosPaciente(this);


        //salvar
        buttonSalvar = (Button) findViewById(R.id.buttonSalvar);
        buttonSalvar.setOnClickListener(this);

        //Cultura de secreção
        radioCulturaSecrecao = (RadioGroup) findViewById(R.id.radio_group_secrecao);
        radioFeridaOperatoria = (RadioButton) findViewById(R.id.radioFeridaOperatoria);
        radioAbscessos = (RadioButton) findViewById(R.id.radioAbscessos);

        radioUlcera = (RadioButton) findViewById(R.id.radioUlceras);
        radioFragmentoTecido = (RadioButton) findViewById(R.id.radioFragmento);


        radioTipoColetaSecrecao = (RadioGroup) findViewById(R.id.radio_group_secrecao_tipo_coleta); 
        radioSwab = (RadioButton) findViewById(R.id.radio_swab);
        radioAspiradoAgulha = (RadioButton) findViewById(R.id.radio_agulha);


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

    }

    private void iniciarRequisicao() {
        //paciente
        Intent intent = getIntent();

        Paciente paciente = (Paciente) intent.getExtras().get(Constantes.DADOS_PACIENTE_REQUISICAO_NOVA_ACTIVITY);

        requisicao = new Requisicao();

        requisicao.setPaciente(paciente);
    }

    public void onCheckedChanged(CompoundButton buttonView,  boolean isChecked) {

        if(buttonView == switchCulturaSecrecao) {
            if (isChecked) {
                radioFeridaOperatoria.setEnabled(true);
                radioAbscessos.setEnabled(true);
                radioUlcera.setEnabled(true);
                radioFragmentoTecido.setEnabled(true);
                radioSwab.setEnabled(true);
                radioAspiradoAgulha.setEnabled(true);
            } else {
                radioFeridaOperatoria.setEnabled(false);
                radioFeridaOperatoria.setChecked(false);

                radioAbscessos.setEnabled(false);
                radioAbscessos.setChecked(false);

                radioUlcera.setEnabled(false);
                radioUlcera.setChecked(false);

                radioFragmentoTecido.setEnabled(false);
                radioFragmentoTecido.setChecked(false);

                radioSwab.setEnabled(false);
                radioAspiradoAgulha.setEnabled(false);
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

        if(v == botaoDataSecrecao){
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
        }else if(v== botaoHoraSecrecao){
            showDialog(1);
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
                      e.printStackTrace(); //TODO refatorar codigo. Lancar e tratar exceção.
                    }

                    requisicao.setNumero(numeroRequisicao);

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

        SharedPreferences preferencias = getSharedPreferences(Constantes.PREF_NAME, MODE_PRIVATE);
        String email = preferencias.getString(Constantes.EMAIL, "");

        requisicao.setEmailSolicitante(email);

        requisicao.setStatus(StatusRequisicao.SOLICITADA);
        requisicao.setDataRequisicao(new Date());

        requisicao.setInternadoUltimas72Horas(dadosPaciente.internadoUltimas72Horas());

        requisicao.setSubmetidoProcedimentoInvasivo(dadosPaciente.submetidoProcedimentoIvasivo());

        requisicao.setFezUsoAntibiotico(dadosPaciente.usouAntibiotico());

        requisicao.setLaboratorio(Laboratorio.MICROBIOLOGIA);

        List<Exame> listaExames = new ArrayList<>();
        
        if(switchCulturaSangue.isChecked()){
            Exame exame = new Exame();
            exame.setTipoColeta(obterTipoColeta(radioCulturaSangue.getCheckedRadioButtonId()));
            exame.setTipoMaterial(TipoMaterial.SANGUE);
            exame.setTipoExame(TipoExame.SANGUE);
            exame.setDataColeta(dataColetaSecrecao);
            listaExames.add(exame);
        }

        if(switchCulturaUrina.isChecked()){
            Exame exame = new Exame();
            exame.setTipoColeta(obterTipoColeta(radioCulturaUrina.getCheckedRadioButtonId()));
            exame.setTipoMaterial(TipoMaterial.URINA);
            exame.setTipoExame(TipoExame.URINA);
            listaExames.add(exame);
        }

        if(switchCulturaSecrecao.isChecked()){
            Exame exame = new Exame();
            exame.setTipoColeta(obterTipoColeta(radioTipoColetaSecrecao.getCheckedRadioButtonId()));
            exame.setTipoMaterial(obterTipoMaterial());
            exame.setTipoExame(TipoExame.SECRECAO);
            listaExames.add(exame);
        }

        requisicao.setExames(listaExames);
    }

    private TipoMaterial obterTipoMaterial(){
        switch(radioCulturaSecrecao.getCheckedRadioButtonId()) {
            case R.id.radioFeridaOperatoria:
                return TipoMaterial.FERIDA_OPERATORIA;
            case R.id.radioAbscessos:
                return TipoMaterial.ABSCESSO;
            case R.id.radioUlceras:
                return TipoMaterial.ULCERA;
            case R.id.radioFragmento:
                return TipoMaterial.FRAGMENTO_TECIDO;
            default:
                return null;
        }
    }

    private TipoColeta obterTipoColeta(int idTipo) {
        
        switch(idTipo) {
            case R.id.radio_urucultura_jato:
                return TipoColeta.JATO_MEDIO;
            case R.id.radio_urucultura_saco_coletor:
                return TipoColeta.SACO_COLETOR;
            case R.id.radio_urucultura_sonda:
                return TipoColeta.SONDA_ALIVIO;
            case R.id.radio_urucultura_svd:
                return TipoColeta.SVD;
            case R.id.radio_urucultura_puncao:
                return TipoColeta.PUNCAO_SUBPUBICA;
            case R.id.radio_hemocultura_veia:
                return TipoColeta.VEIA;
            case R.id.radio_hemocultura_cateter_umbilical:
                return TipoColeta.CATETER_UMBILICAL;
            case R.id.radio_hemocultura_cateter_central:
                return TipoColeta.CATETER_CENTRAL;
            case R.id.radio_hemocultura_arteria:
                return TipoColeta.ARTERIA;
            case R.id.radio_swab:
                return TipoColeta.CATETER_CENTRAL;
            case R.id.radio_agulha:
                return TipoColeta.ARTERIA;
            default:
                return null;
        }

    }


    @Override
    protected Dialog onCreateDialog(int id) {

        if(id==0) {
            Calendar calendario = GregorianCalendar.getInstance();

            int ano = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);


            return new DatePickerDialog(this, mDateSetListenerSangue, ano, mes, dia);
        }

        final Calendar c = Calendar.getInstance();
        int Hora = c.get(Calendar.HOUR_OF_DAY);
        int  Minuto = c.get(Calendar.MINUTE);
        return new TimePickerDialog(
                this,
                timePickerListener,
                Hora,
                Minuto,
                true  );

    }


    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    botaoHoraSecrecao.setText(" " + selectedHour + ":"+selectedMinute);

                }
            };


    private DatePickerDialog.OnDateSetListener mDateSetListenerSangue = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dataColetaSecrecao = " " +dayOfMonth + "/" + monthOfYear  + "/" +year;
            botaoDataSecrecao.setText(dataColetaSecrecao);
        }
    };


}
