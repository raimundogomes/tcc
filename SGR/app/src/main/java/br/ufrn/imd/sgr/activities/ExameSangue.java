package br.ufrn.imd.sgr.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.model.TipoColeta;
import br.ufrn.imd.sgr.utils.DateUtils;

/**
 * Created by neto on 29/06/2017.
 */

public class ExameSangue extends ExameComum {

    private RadioGroup radioCulturaSangue;
    private RadioButton radioVeia;
    private RadioButton radioArteria;
    private RadioButton radioCateterCentral;
    private RadioButton radioCateterUmbilical;

    private Switch switchCulturaSangue;

    public ExameSangue(NovaRequisicaoActivity activity) {
        switchCulturaSangue = (Switch) activity.findViewById(R.id.txtCulturaSangue);

        switchCulturaSangue.setOnCheckedChangeListener(activity);

        radioCulturaSangue = (RadioGroup) activity.findViewById(R.id.radio_group_hemocultura);
        radioVeia = (RadioButton) activity.findViewById(R.id.radio_hemocultura_veia);
        radioArteria = (RadioButton) activity.findViewById(R.id.radio_hemocultura_arteria);
        radioCateterCentral = (RadioButton) activity.findViewById(R.id.radio_hemocultura_cateter_central);
        radioCateterUmbilical = (RadioButton) activity.findViewById(R.id.radio_hemocultura_cateter_umbilical);

        botaoData = (Button) activity.findViewById(R.id.bt_data_sangue);
        botaoData.setText(" " + DateUtils.obterDataDDMMYYY(new Date()));

        botaoHora = (Button) activity.findViewById(R.id.bt_hora_sangue);

        dataColeta = GregorianCalendar.getInstance();
    }


    public Switch getSwitchCulturaSangue() {
        return switchCulturaSangue;
    }


    public void atualizaRadioButtons(boolean isChecked) {
        if (isChecked) {
            radioVeia.setEnabled(true);
            radioArteria.setEnabled(true);
            radioCateterCentral.setEnabled(true);
            radioCateterUmbilical.setEnabled(true);
            botaoData.setEnabled(true);
            botaoHora.setEnabled(true);
        }else{
            radioVeia.setEnabled(false);
            radioArteria.setEnabled(false);
            radioCateterCentral.setEnabled(false);
            radioCateterUmbilical.setEnabled(false);
            botaoData.setEnabled(false);
            botaoHora.setEnabled(false);
        }
    }

    public TipoColeta getTipoColeta() {

        switch (radioCulturaSangue.getCheckedRadioButtonId()) {
            case R.id.radio_hemocultura_veia:
                return TipoColeta.VEIA;
            case R.id.radio_hemocultura_cateter_umbilical:
                return TipoColeta.CATETER_UMBILICAL;
            case R.id.radio_hemocultura_cateter_central:
                return TipoColeta.CATETER_CENTRAL;
            case R.id.radio_hemocultura_arteria:
                return TipoColeta.ARTERIA;
            default:
                return null;
        }

    }

    public RadioGroup getRadioCulturaSangue() {
        return radioCulturaSangue;
    }
}
