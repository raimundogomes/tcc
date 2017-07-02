package br.ufrn.imd.sgr.activities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.service.RequisicaoServiceImpl;
import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.model.Laboratorio;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.model.StatusRequisicao;
import br.ufrn.imd.sgr.model.TipoExame;
import br.ufrn.imd.sgr.model.TipoMaterial;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.DateUtils;

public class NovaRequisicaoActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final int DATA_COLETA_SECRECAO = 0;
    private static final int ID_HORA_COLETA_SECRECAO = 1;
    private static final int DATA_COLETA_SANGUE = 2;
    private static final int ID_HORA_COLETA_SANGUE = 3;
    private static final int DATA_COLETA_URINA = 4;
    private static final int ID_HORA_COLETA_URINA = 5;

    private static final String HORA_COLETA_SANGUE = "HORA_COLETA_SANGUE";
    private static final String HORA_COLETA_URINA = "HORA_COLETA_URINA";
    private static final String HORA_COLETA_SECRECAO = "HORA_COLETA_SECRECAO";

    private Requisicao requisicao;

    private Button buttonSalvar;

    private RequisicaoServiceImpl requisicaoService;

    private DadosPaciente dadosPaciente;
    private ExameSecrecao exameSecrecao;
    private ExameSangue exameSangue;
    private ExameUrina exameUrina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_requisicao);

        requisicaoService = new RequisicaoServiceImpl(getApplicationContext());

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

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView == exameSecrecao.getSwitchCulturaSecrecao()) {
            exameSecrecao.atualizaRadioButtons(isChecked);
        } else if (buttonView == exameSangue.getSwitchCulturaSangue()) {

            exameSangue.atualizaRadioButtons(isChecked);

        } else if (buttonView == exameUrina.getSwitchCulturaUrina()) {

            exameUrina.atualizaRadioButtons(isChecked);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(HORA_COLETA_SANGUE, DateUtils.obterHora(exameSangue.getDataColeta()));
        outState.putString(HORA_COLETA_URINA, DateUtils.obterHora(exameUrina.getDataColeta()));
        outState.putString(HORA_COLETA_SECRECAO, DateUtils.obterHora(exameSecrecao.getDataColeta()));
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        exameSangue.getBotaoHora().setText(savedInstanceState.getString(HORA_COLETA_SANGUE));
        exameUrina.getBotaoHora().setText(savedInstanceState.getString(HORA_COLETA_URINA));
        exameSecrecao.getBotaoHora().setText(savedInstanceState.getString(HORA_COLETA_SECRECAO));
    }

    public void salvar(View v) {

        montarRequisicao();

        if (requisicaoService.validarRequisicao(requisicao)) {
            requisicao = requisicaoService.salvarRequisicao(requisicao, this);

        } else {
            String mensagemErro = getString(R.string.erro_preenchimento_obrigatorio);
            Toast toast = Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void atualizarDataColetaSecrecao(View v) {
        showDialog(DATA_COLETA_SECRECAO);
        Log.d("NovaRequisicaoActivity", "atualizarDataColetaSecrecao DATA_COLETA_SECRECAO");
    }

    public void atualizarHoraColetaSecrecao(View v) {
        showDialog(ID_HORA_COLETA_SECRECAO);
    }

    public void atualizarDataColetaSangue(View v) {
        showDialog(DATA_COLETA_SANGUE);
        Log.d("NovaRequisicaoActivity", "atualizarHoraColetaUrina DATA_COLETA_SANGUE");
    }

    public void atualizarHoraColetaSangue(View v) {
        showDialog(ID_HORA_COLETA_SANGUE);
    }

    public void atualizarDataColetaUrina(View v) {
        showDialog(DATA_COLETA_URINA);
        Log.d("NovaRequisicaoActivity", "atualizarDataColetaUrina DATA_COLETA_URINA");
    }

    public void atualizarHoraColetaUrina(View v) {
        showDialog(ID_HORA_COLETA_URINA);
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

    private List<Exame> contruirExames() {
        List<Exame> listaExames = new ArrayList<>();

        if (exameSangue.getSwitchCulturaSangue().isChecked()) {
            Exame exame = new Exame();
            exame.setTipoColeta(exameSangue.getTipoColeta());
            exame.setTipoMaterial(TipoMaterial.SANGUE);
            exame.setTipoExame(TipoExame.SANGUE);
            exame.setDataColeta(exameSangue.getDataColeta());
            listaExames.add(exame);
            requisicao.setTemHemocultura(true);
        }

        if (exameUrina.getSwitchCulturaUrina().isChecked()) {
            Exame exame = new Exame();
            exame.setTipoColeta(exameUrina.getTipoColeta());
            exame.setTipoMaterial(TipoMaterial.URINA);
            exame.setDataColeta(exameUrina.getDataColeta());
            exame.setTipoExame(TipoExame.URINA);
            listaExames.add(exame);

            requisicao.setTemUrocultura(true);
        }

        if (exameSecrecao.getSwitchCulturaSecrecao().isChecked()) {
            Exame exame = new Exame();
            exame.setTipoColeta(exameSecrecao.getTipoColeta());
            exame.setTipoMaterial(exameSecrecao.obterTipoMaterial());
            exame.setDataColeta(exameSecrecao.getDataColeta());
            exame.setTipoExame(TipoExame.SECRECAO);
            listaExames.add(exame);

            requisicao.setTemSecrecao(true);
        }
        return listaExames;
    }



    @Override
    protected Dialog onCreateDialog(int id) {

        final Calendar calendario = GregorianCalendar.getInstance();

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int Hora = calendario.get(Calendar.HOUR_OF_DAY);
        int Minuto = calendario.get(Calendar.MINUTE);

        if (id == DATA_COLETA_SECRECAO) {

            return new DatePickerDialog(this, exameSecrecao.dataPickerListener, ano, mes, dia);
        }

        if (id == ID_HORA_COLETA_SECRECAO) {
            return new TimePickerDialog(this, exameSecrecao.timePickerListener, Hora, Minuto, true);
        }

        if (id == DATA_COLETA_SANGUE) {
            return new DatePickerDialog(this, exameSangue.dataPickerListener, ano, mes, dia);
        }

        if (id == ID_HORA_COLETA_SANGUE) {
            return new TimePickerDialog(this, exameSangue.timePickerListener, Hora, Minuto, true);
        }

        if (id == DATA_COLETA_URINA) {
            return new DatePickerDialog(this, exameUrina.dataPickerListener, ano, mes, dia);
        }

        if (id == ID_HORA_COLETA_URINA) {
            return new TimePickerDialog(this, exameUrina.timePickerListener, Hora, Minuto, true);
        }


        return null;

    }
}

