package br.ufrn.imd.sgr.activities.novaRequisicao;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.util.Calendar;
import java.util.Date;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.model.TipoColeta;
import br.ufrn.imd.sgr.utils.DateUtils;

/**
 * Created by neto on 29/06/2017.
 */

public class ExameUrina extends ExameComum{

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

        botaoData = (Button) activity.findViewById(R.id.bt_data_urina);
        botaoData.setText(" " + DateUtils.obterDataDDMMYYY(new Date()));

        botaoHora = (Button) activity.findViewById(R.id.bt_hora_urina);

        dataColeta = Calendar.getInstance();

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

    public TipoColeta getTipoColeta(){

        switch (radioCulturaUrina.getCheckedRadioButtonId()) {
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
        }

        return null;
    }

    public RadioGroup getRadioCulturaUrina() {
        return radioCulturaUrina;
    }


}
