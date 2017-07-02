package br.ufrn.imd.sgr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.adapter.PacienteAdapter;
import br.ufrn.imd.sgr.service.PacienteServiceImpl;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.service.PacienteService;
import br.ufrn.imd.sgr.utils.Constantes;

public class PesquisarPacienteActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView listview;

    private ProgressBar progressBar;

    private PacienteService pacienteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_paciente);

        Button button = (Button) findViewById(R.id.botao_pesquisar_paciente);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        listview = (ListView) findViewById(R.id.listView_paciente);

        button.setOnClickListener(this);

        listview.setOnItemClickListener(this);

        pacienteService = new PacienteServiceImpl(this);

    }

    public boolean camposEstaoPreenchidos(){
        String prontuario =  ((TextView)findViewById(R.id.text_prontuario)).getText().toString();
        String nome = ((TextView) findViewById(R.id.text_nome_paciente)).getText().toString();

        if( !"".equals(prontuario) || !"".equals(nome) ){

            return true;

        }
        else{
            Toast toast = Toast.makeText(this, "Preencha o prontuário ou nome do paciente!", Toast.LENGTH_SHORT);
            toast.show();

            return false;
        }

    }

    @Override
    public void onClick(final View view) {
        //enviando informação

        if(camposEstaoPreenchidos()) {
            String prontuario =  ((TextView)findViewById(R.id.text_prontuario)).getText().toString();
            List<Paciente> lista;

            progressBar.setVisibility(View.VISIBLE);

            if(!"".equals(prontuario)){

                lista = pacienteService.pesquisarPaciente(prontuario, progressBar);
            }
            else{

                String nome = ((TextView) findViewById(R.id.text_nome_paciente)).getText().toString();

                lista = pacienteService.pesquisarPacientesPeloNome(nome, progressBar);


            }

            montarListaPacientes(lista, view);


        }
    }

    private void montarListaPacientes(List<Paciente> list, View view) {
        TextView txt_lista_paciente = (TextView) findViewById(R.id.text_selecao_paciente);

        if(list.isEmpty()) {
            txt_lista_paciente.setText("Nenhum paciente encontrado para os parâmetros informados.");
        }else{
            txt_lista_paciente.setText("Selecione um paciente da lista:");
            txt_lista_paciente.setVisibility(View.VISIBLE);
        }

        PacienteAdapter adapter = new PacienteAdapter(view.getContext(), list);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        Paciente paciente = (Paciente) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, NovaRequisicaoActivity.class);
        intent.putExtra(Constantes.DADOS_PACIENTE_REQUISICAO_NOVA_ACTIVITY, paciente);
        startActivityForResult(intent, Constantes.INDICE_ACTIVITY_NOVA_REQUISICAO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constantes.INDICE_ACTIVITY_NOVA_REQUISICAO:
                if(resultCode == RESULT_OK){
                    Requisicao novaRequisicao = (Requisicao) data.getSerializableExtra(Constantes.REQUISICAO_NOVA_ACTIVITY);
                    Intent result = new Intent();
                    result.putExtra(Constantes.REQUISICAO_NOVA_ACTIVITY, novaRequisicao);
                    setResult(RESULT_OK, result);
                    finish();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
