package br.ufrn.imd.sgr.activities;

import android.widget.RadioGroup;

import br.ufrn.imd.sgr.R;

/**
 * Created by neto on 26/06/2017.
 */

public class DadosPaciente {

    private final RadioGroup radioGroupAntibiotico;

    RadioGroup radioGroupInternado;

    RadioGroup radioGroupProcedimentoIvasivo;

    public DadosPaciente(NovaRequisicaoActivity activity){
        radioGroupInternado = (RadioGroup) activity.findViewById(R.id.radio_group_internado);

        radioGroupProcedimentoIvasivo = (RadioGroup) activity.findViewById(R.id.radio_group_procedimento_invasivo);

        radioGroupAntibiotico = (RadioGroup) activity.findViewById(R.id.radio_group_antibiotico);


    }

    public Boolean internadoUltimas72Horas(){
        switch (radioGroupInternado .getCheckedRadioButtonId()) {
            case R.id.radio_internado_sim:
                return true;
            case R.id.radio_internado_nao:
                return false;
        }
        return null;
    }

    public Boolean submetidoProcedimentoIvasivo(){

        switch (radioGroupProcedimentoIvasivo .getCheckedRadioButtonId()) {
                case R.id.radio_proced_invasivo_sim:
                    return true;
                case R.id.radio_proced_invasivo_nao:
                    return false;
            }
            return null;
        }

    public Boolean usouAntibiotico(){
        switch (radioGroupAntibiotico.getCheckedRadioButtonId()) {
            case R.id.radio_antibiotico_sim:
                return true;
            case R.id.radio_antibiotico_nao:
                return false;
        }
        return null;
    }

}
