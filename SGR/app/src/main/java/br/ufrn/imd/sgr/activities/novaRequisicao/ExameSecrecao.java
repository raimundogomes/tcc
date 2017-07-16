package br.ufrn.imd.sgr.activities.novaRequisicao;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.util.Calendar;
import java.util.Date;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.model.TipoColeta;
import br.ufrn.imd.sgr.model.TipoMaterial;
import br.ufrn.imd.sgr.utils.DateUtils;

/**
 * Created by neto on 29/06/2017.
 */

public class ExameSecrecao extends ExameComum{

    private RadioGroup radioTipoColetaSecrecao;

    private RadioButton radioFeridaOperatoria;
    private RadioButton radioAbscessos;
    private RadioButton radioUlcera;
    private RadioButton radioFragmentoTecido;

    private RadioGroup radioCulturaSecrecao;
    private RadioButton radioSwab;
    private RadioButton radioAspiradoAgulha;

    private Switch switchCulturaSecrecao;


    public ExameSecrecao(NovaRequisicaoActivity activity) {
        //Cultura de secreção
        radioCulturaSecrecao = (RadioGroup) activity.findViewById(R.id.radio_group_secrecao);
        radioFeridaOperatoria = (RadioButton) activity.findViewById(R.id.radioFeridaOperatoria);
        radioAbscessos = (RadioButton) activity.findViewById(R.id.radioAbscessos);

        radioUlcera = (RadioButton) activity.findViewById(R.id.radioUlceras);
        radioFragmentoTecido = (RadioButton) activity.findViewById(R.id.radioFragmento);


        radioTipoColetaSecrecao = (RadioGroup) activity.findViewById(R.id.radio_group_secrecao_tipo_coleta);
        radioSwab = (RadioButton) activity.findViewById(R.id.radio_swab);
        radioAspiradoAgulha = (RadioButton) activity.findViewById(R.id.radio_agulha);

        switchCulturaSecrecao = (Switch) activity.findViewById(R.id.culturaSecrecao);

        switchCulturaSecrecao.setOnCheckedChangeListener(activity);

        botaoData = (Button) activity.findViewById(R.id.bt_data_secrecao);
        botaoData.setText(" " + DateUtils.obterDataDDMMYYY(new Date()));

        botaoHora = (Button) activity.findViewById(R.id.bt_hora_secrecao);

        dataColeta = Calendar.getInstance();
    }

    public TipoMaterial obterTipoMaterial(){
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

    public Switch getSwitchCulturaSecrecao() {
        return switchCulturaSecrecao;
    }

    public void atualizaRadioButtons(boolean isChecked) {
        if (isChecked) {
            radioFeridaOperatoria.setEnabled(true);
            radioAbscessos.setEnabled(true);
            radioUlcera.setEnabled(true);
            radioFragmentoTecido.setEnabled(true);
            radioSwab.setEnabled(true);
            radioAspiradoAgulha.setEnabled(true);

            botaoData.setEnabled(true);
            botaoHora.setEnabled(true);
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

            botaoData.setEnabled(false);
            botaoHora.setEnabled(false);
        }
    }

    public RadioGroup getRadioTipoColetaSecrecao() {
        return radioTipoColetaSecrecao;
    }

    public TipoColeta getTipoColeta(){
        switch (radioTipoColetaSecrecao.getCheckedRadioButtonId()) {
            case R.id.radio_swab:
                return TipoColeta.SWAB;
            case R.id.radio_agulha:
                return TipoColeta.ASPIRADO_AGULHA;
            default:
                return null;
        }
    }


}
