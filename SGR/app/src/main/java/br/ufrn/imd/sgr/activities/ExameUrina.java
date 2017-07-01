package br.ufrn.imd.sgr.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Date;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.utils.DateUtils;

/**
 * Created by neto on 29/06/2017.
 */

public class ExameUrina {

    private RadioGroup radioCulturaUrina;
    private RadioButton radioJatoMedio;
    private RadioButton radioPuncao;
    private RadioButton radioSacoColetor;
    private RadioButton radioSonda;
    private RadioButton radioSvd;

    private Switch switchCulturaUrina;

    private Button botaoData;

    private Button botaoHora;

    public ExameUrina(NovaRequisicaoActivity activity) {

        switchCulturaUrina = (Switch) activity.findViewById(R.id.txtCulturaUrina);

        switchCulturaUrina.setOnCheckedChangeListener(activity);

        radioCulturaUrina = (RadioGroup) activity.findViewById(R.id.radio_group_urucultura);
        radioJatoMedio = (RadioButton) activity.findViewById(R.id.radio_urucultura_jato);
        radioSacoColetor = (RadioButton) activity.findViewById(R.id.radio_urucultura_saco_coletor);
        radioSonda = (RadioButton) activity.findViewById(R.id.radio_urucultura_sonda);
        radioSvd = (RadioButton) activity.findViewById(R.id.radio_urucultura_svd);
        radioPuncao = (RadioButton) activity.findViewById(R.id.radio_urucultura_puncao);

        botaoData = (Button) activity.findViewById(R.id.bt_data_urina);
        botaoData.setText(" " + DateUtils.obterDataDDMMYYY(new Date()));

        botaoHora = (Button) activity.findViewById(R.id.bt_hora_urina);

    }

    public Switch getSwitchCulturaUrina() {
        return switchCulturaUrina;
    }

    public void atualizaRadioButtons(boolean isChecked) {

        if (isChecked) {
            radioJatoMedio.setEnabled(true);
            radioSvd.setEnabled(true);
            radioSonda.setEnabled(true);
            radioPuncao.setEnabled(true);
            radioSacoColetor.setEnabled(true);
            botaoData.setEnabled(true);
            botaoHora.setEnabled(true);
        } else {
            radioJatoMedio.setEnabled(false);
            radioSvd.setEnabled(false);
            radioSonda.setEnabled(false);
            radioPuncao.setEnabled(false);
            radioSacoColetor.setEnabled(false);
            botaoData.setEnabled(false);
            botaoHora.setEnabled(false);
        }
    }

    public String getDataColeta() {
        return botaoData.getText() + " " + botaoHora.getText();
    }

    public RadioGroup getRadioCulturaUrina() {
        return radioCulturaUrina;
    }

    public DatePickerDialog.OnDateSetListener dataPickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String dataColeta = " " +dayOfMonth + "/" + (monthOfYear+1)  + "/" +year;
            botaoData.setText(dataColeta);
        }
    };

    public TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    botaoHora.setText(selectedHour + ":"+selectedMinute);

                }
            };
}
