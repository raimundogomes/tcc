package br.ufrn.imd.sgr.activities;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import br.ufrn.imd.sgr.R;

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

    public ExameUrina(NovaRequisicaoActivity activity) {

        switchCulturaUrina = (Switch) activity.findViewById(R.id.txtCulturaUrina);

        switchCulturaUrina.setOnCheckedChangeListener(activity);

        radioCulturaUrina = (RadioGroup) activity.findViewById(R.id.radio_group_urucultura);
        radioJatoMedio = (RadioButton) activity.findViewById(R.id.radio_urucultura_jato);
        radioSacoColetor = (RadioButton) activity.findViewById(R.id.radio_urucultura_saco_coletor);
        radioSonda = (RadioButton) activity.findViewById(R.id.radio_urucultura_sonda);
        radioSvd = (RadioButton) activity.findViewById(R.id.radio_urucultura_svd);
        radioPuncao = (RadioButton) activity.findViewById(R.id.radio_urucultura_puncao);

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
        } else {
            radioJatoMedio.setEnabled(false);
            radioSvd.setEnabled(false);
            radioSonda.setEnabled(false);
            radioPuncao.setEnabled(false);
            radioSacoColetor.setEnabled(false);
        }
    }

    public RadioGroup getRadioCulturaUrina() {
        return radioCulturaUrina;
    }
}
