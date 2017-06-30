package br.ufrn.imd.sgr.activities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import android.widget.TimePicker;
import android.widget.Toast;

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

public class NovaRequisicaoActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private Requisicao requisicao;

    private Button buttonSalvar;

    private RequisicaoBusiness requisicaoService;

    private DadosPaciente dadosPaciente;
    private ExameSecrecao exameSecrecao;
    private ExameSangue exameSangue;
    private ExameUrina exameUrina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_requisicao);

        requisicaoService = new RequisicaoBusiness(getApplicationContext());

        iniciarRequisicao();

        dadosPaciente = new DadosPaciente(this);

        exameSecrecao = new ExameSecrecao(this);
        exameSangue = new ExameSangue(this);
        exameUrina = new ExameUrina(this);

        buttonSalvar = (Button) findViewById(R.id.buttonSalvar);

    }

    private void iniciarRequisicao() {
        //paciente
        Intent intent = getIntent();

        Paciente paciente = (Paciente) intent.getExtras().get(Constantes.DADOS_PACIENTE_REQUISICAO_NOVA_ACTIVITY);

        requisicao = new Requisicao();

        requisicao.setPaciente(paciente);
    }

    public void onCheckedChanged(CompoundButton buttonView,  boolean isChecked) {

        if(buttonView == exameSecrecao.getSwitchCulturaSecrecao()) {
            exameSecrecao.atualizaRadioButtons(isChecked);
        }else if(buttonView== exameSangue.getSwitchCulturaSangue()){

            exameSangue.atualizaRadioButtons(isChecked);

        }else if(buttonView == exameUrina.getSwitchCulturaUrina()){

            exameUrina.atualizaRadioButtons(isChecked);

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

    public void salvar(View v) {

        montarRequisicao();

        if(requisicaoService.validarRequisicao(requisicao)){
            requisicao = requisicaoService.salvarRequisicao(requisicao, this);

        }else{
            String mensagemErro = getString(R.string.erro_preenchimento_obrigatorio);
            Toast toast = Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void atualizarDataColetaSecrecao(View v){
        showDialog(0);
    }

    public void atualizarHoraColetaSecrecao(View v){
        showDialog(1);
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

        List<Exame> listaExames = contruirExames();

        requisicao.setExames(listaExames);
    }

    @NonNull
    private List<Exame> contruirExames() {
        List<Exame> listaExames = new ArrayList<>();

        if(exameSangue.getSwitchCulturaSangue().isChecked()){
            Exame exame = new Exame();
            exame.setTipoColeta(obterTipoColeta(exameSangue.getRadioCulturaSangue().getCheckedRadioButtonId()));
            exame.setTipoMaterial(TipoMaterial.SANGUE);
            exame.setTipoExame(TipoExame.SANGUE);
            exame.setDataColeta(exameSecrecao.getDataColeta());
            listaExames.add(exame);
        }

        if(exameUrina.getSwitchCulturaUrina().isChecked()){
            Exame exame = new Exame();
            exame.setTipoColeta(obterTipoColeta(exameUrina.getRadioCulturaUrina().getCheckedRadioButtonId()));
            exame.setTipoMaterial(TipoMaterial.URINA);
            exame.setTipoExame(TipoExame.URINA);
            listaExames.add(exame);
        }

        if(exameSecrecao.getSwitchCulturaSecrecao().isChecked()){
            Exame exame = new Exame();
            exame.setTipoColeta(obterTipoColeta(exameSecrecao.getRadioTipoColetaSecrecao().getCheckedRadioButtonId()));
            exame.setTipoMaterial(exameSecrecao.obterTipoMaterial());
            exame.setTipoExame(TipoExame.SECRECAO);
            listaExames.add(exame);
        }
        return listaExames;
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
                    exameSecrecao.getBotaoHoraSecrecao().setText(" " + selectedHour + ":"+selectedMinute);

                }
            };


    private DatePickerDialog.OnDateSetListener mDateSetListenerSangue = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String dataColetaSecrecao = " " +dayOfMonth + "/" + (monthOfYear+1)  + "/" +year;
            exameSecrecao.getBotaoDataSecrecao().setText(dataColetaSecrecao);
        }
    };


}
