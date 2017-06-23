package br.ufrn.imd.sgr.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.adapter.ExameAdapter;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.DateUtils;


public class DetalheRequisicaoActivity extends PrincipalActivity {

    private Requisicao requisicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detalhe_requisicao_activity);

        Intent intent = getIntent();

        requisicao = (Requisicao)intent.getExtras().get(Constantes.REQUISICAO_DETALHE_ACTIVITY);


        criarDadosRequisicao(requisicao);

        ListView listView = (ListView) findViewById(R.id.list_exames);

        final ExameAdapter exameAdapter = new ExameAdapter(this,  requisicao.getExames());
        listView.setAdapter(exameAdapter);
    }

    private void criarDadosRequisicao(Requisicao requisicao) {

        TextView situacaoRequisicao = (TextView) findViewById(R.id.text_situacaoRequisicao);
        situacaoRequisicao.setText(requisicao.getStatus().getDescricao());


        TextView numeroRequisicao = (TextView) findViewById(R.id.text_numeroRequisicao);
        numeroRequisicao.setText(requisicao.getNumeroFormatado());

        TextView textViewDataRequesicao = (TextView) findViewById(R.id.text_dataRequisicao);
        textViewDataRequesicao.setText(DateUtils.obterData(requisicao.getDataRequisicao()));

        if(requisicao.getDataUltimaModificacao()!=null){
            TextView dataFim = (TextView) findViewById(R.id.text_dataFinal);
            dataFim.setText(DateUtils.obterData(requisicao.getDataUltimaModificacao()));
        }

        TextView nomePaciente = (TextView) findViewById(R.id.text_paciente);
        nomePaciente.setText(requisicao.getPaciente().getNome());

        TextView nomeLaboratio = (TextView) findViewById(R.id.text_laboratorio);
        nomeLaboratio.setText(requisicao.getLaboratorio().getNome());

    }

    public void telefonar(String telefone){
        if(telefone!=null) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + telefone));
            startActivity(callIntent);
        }
        else{
            Toast.makeText(this, "Telefone não cadastrado!" , Toast.LENGTH_SHORT).show();
        }
    }

    public void telefonarLaboratorio(View v){
        if(requisicao.getLaboratorio()!=null ) {
            telefonar(requisicao.getPaciente().getTelefone());
        }

    }

    public void telefonarPaciente(View v){
        if(requisicao.getPaciente()!=null) {
            telefonar(requisicao.getPaciente().getTelefone());
        }

    }

}
