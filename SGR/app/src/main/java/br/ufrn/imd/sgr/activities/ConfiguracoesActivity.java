package br.ufrn.imd.sgr.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.utils.Constantes;

public class ConfiguracoesActivity extends AppCompatActivity implements
        View.OnClickListener{

	private RadioGroup radioOrdenacaoGroup;

	private RadioButton radioNumeroRequisicaoButton;
	private RadioButton radioDataRequisicaoButton;
	private RadioButton radioNomePacienteButton;
	private RadioButton radioSituacaoButton;

	private int criterioSelecionado;

    private Button buttonSalvar;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_configuracoes);

		Intent intent = getIntent();

        SharedPreferences preferencias = getSharedPreferences(Constantes.PREF_NAME, MODE_PRIVATE);

        criterioSelecionado = preferencias.getInt(Constantes.CONFIGURACAO_CRITERIO_SELECIONADO, Constantes.CRITERIO_DATA_REQUISICAO);

		radioOrdenacaoGroup = (RadioGroup) findViewById(R.id.radio_group_ordenacao);

        //salvar
        buttonSalvar = (Button) findViewById(R.id.botao_salvar_configuracao);
        buttonSalvar.setOnClickListener(this);

		radioNumeroRequisicaoButton = (RadioButton) findViewById(R.id.radio_numero_requisicao);
		radioDataRequisicaoButton = (RadioButton) findViewById(R.id.radio_data_requisicao);
		radioNomePacienteButton = (RadioButton) findViewById(R.id.radio_nome_paciente);
		radioSituacaoButton = (RadioButton) findViewById(R.id.radio_status_requisicao);

		switch (criterioSelecionado){
			case Constantes.CRITERIO_DATA_REQUISICAO:
				radioDataRequisicaoButton.setChecked(true);
				break;
			case Constantes.CRITERIO_NOME_PACIENTE:
				radioNomePacienteButton.setChecked(true);
				break;
			case Constantes.CRITERIO_NUMERO_REQUISICAO:
				radioNumeroRequisicaoButton.setChecked(true);
				break;
			case Constantes.CRITERIO_STATUS_REQUISICAO:
				radioSituacaoButton.setChecked(true);
				break;
			default:
				radioNomePacienteButton.setChecked(true);
				break;
		}
    }


    @Override
    public void onClick(View v) {

		switch(radioOrdenacaoGroup.getCheckedRadioButtonId()) {
			case R.id.radio_data_requisicao:
					criterioSelecionado = Constantes.CRITERIO_DATA_REQUISICAO;
					break;
			case R.id.radio_nome_paciente:
					criterioSelecionado = Constantes.CRITERIO_NOME_PACIENTE;
					break;
			case R.id.radio_numero_requisicao:
					criterioSelecionado = Constantes.CRITERIO_NUMERO_REQUISICAO;
					break;
			case R.id.radio_status_requisicao:
					criterioSelecionado = Constantes.CRITERIO_STATUS_REQUISICAO;
					break;
			default:
				break;
		}

        SharedPreferences preferencias = getSharedPreferences(Constantes.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putInt(Constantes.CONFIGURACAO_CRITERIO_SELECIONADO, criterioSelecionado);
        editor.commit();

        Intent result = new Intent();
        result.putExtra(Constantes.CONFIGURACAO_ACTIVITY, criterioSelecionado);
        setResult(RESULT_OK, result);
        finish();

    }
}