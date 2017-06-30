package br.ufrn.imd.sgr.activities;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import br.ufrn.imd.sgr.R;

/**
 * Created by neto on 29/06/2017.
 */

public class ExameSangue {

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
        }else{
            radioVeia.setEnabled(false);
            radioArteria.setEnabled(false);
            radioCateterCentral.setEnabled(false);
            radioCateterUmbilical.setEnabled(false);
        }
    }

    public RadioGroup getRadioCulturaSangue() {
        return radioCulturaSangue;
    }
}
