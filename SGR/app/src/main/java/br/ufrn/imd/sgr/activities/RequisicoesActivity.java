package br.ufrn.imd.sgr.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.adapter.RequisicaoAdapter;
import br.ufrn.imd.sgr.business.RequisicaoBusiness;
import br.ufrn.imd.sgr.comparator.RequisicaoComparator;
import br.ufrn.imd.sgr.dao.RequisicaoDao;
import br.ufrn.imd.sgr.model.Email;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.model.StatusRequisicao;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.DateUtils;
import br.ufrn.imd.sgr.utils.DetectaConexao;
import br.ufrn.imd.sgr.utils.EmailUtil;

public class RequisicoesActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        DialogInterface.OnClickListener,
        TextWatcher {

    public static final String SUBJECT_EMAIL = "[SGR] - Encaminhamento de Requisição";

    private List<Requisicao> requisicoes = new ArrayList<Requisicao>();

    private List<Requisicao> requisicoesfiltradas;

    private Requisicao requisicaoSelecionada = null;

    private RequisicaoAdapter requisicaoAdapter;

    private int criterioOrdenacaoSelecionado = Constantes.CRITERIO_DATA_REQUISICAO;

    private RequisicaoDao requisicaoDao;
    private RequisicaoBusiness requisicaoBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisicoes);

        requisicaoDao = new RequisicaoDao(this);

        requisicaoBusiness = new RequisicaoBusiness(getApplicationContext());

        if(requisicoes.size()==0){
            requisicoes = requisicaoDao.listar();
        }



        ListView listView = (ListView) findViewById(R.id.list_requisicao);

        registerForContextMenu(listView);

        requisicoesfiltradas = ((List) ((ArrayList) requisicoes).clone());

        ordenarComBaseConfiguracao();

        requisicaoAdapter = new RequisicaoAdapter(this,  requisicoesfiltradas);

        listView.setAdapter(requisicaoAdapter);

        listView.setOnItemClickListener(this);

        listView.setOnItemLongClickListener(this);

        //edit search
        EditText editSearch = (EditText) findViewById(R.id.edit_search);

        editSearch.addTextChangedListener(this);

    }

    private void sicronizarRequisicoes(List<Requisicao> requisicoes) {

        DetectaConexao detectaConexao = new DetectaConexao(getApplicationContext());
        if(detectaConexao.existeConexao()){

            exibirMensagemSicronizacao();
        }
        else{
            Toast toast = Toast.makeText(this, DetectaConexao.FALHA_CONEXAO,
                    Toast.LENGTH_LONG);
            toast.show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view,
                            int position, long id) {

        Requisicao requisicaSelecionada = requisicoes.get(position);

        Intent acao = new Intent(RequisicoesActivity.this, DetalheRequisicaoActivity.class);

        acao.putExtra(Constantes.REQUISICAO_DETALHE_ACTIVITY, requisicaSelecionada);

        startActivity(acao);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        requisicaoSelecionada = (Requisicao) parent.getItemAtPosition(position);
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contexto_requisicao, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_cancelar_requisicao:
                cancelarRequisicao();
                break;
            case R.id.menu_encaminhar_requisicao:
                encaminharRequisicao();
                break;

            case R.id.menu_dados_paciente:
                visualizarDadosPaciente();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_sair:
                finish();
                break;
            case R.id.menu_sincronizar:
                sicronizarRequisicoes(requisicoes);
                break;
            case R.id.menu_configuracoes:
                abrirConfiguracoes();
                break;
            case R.id.menu_desconectar:
                requisicaoBusiness.desconectar(this);
                finish();
                break;
            case R.id.menu_nova_requisicao:
                novaRequisicao();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    public void exibirMensagemSicronizacao() {

        final ProgressDialog dialog = new ProgressDialog(RequisicoesActivity.this);
        dialog.setTitle("Sincronizando as requisições...");
        dialog.setMessage("Aguarde, por favor.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();

        long delayInMillis = 2000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delayInMillis);

    }

    public void novaRequisicao() {

        DetectaConexao detectaConexao = new DetectaConexao(getApplicationContext());
        if(detectaConexao.existeConexao()){
            Intent intent = new Intent(this, PesquisarPacienteActivity.class);
            startActivityForResult(intent, Constantes.INDICE_ACTIVITY_NOVA_REQUISICAO);
        }
        else{
            Toast toast = Toast.makeText(this, DetectaConexao.FALHA_CONEXAO,
                    Toast.LENGTH_LONG);
            toast.show();
        }


    }

    private void ordenarComBaseConfiguracao() {
        SharedPreferences preferencias = getSharedPreferences(Constantes.PREF_NAME, MODE_PRIVATE);

        int configuracaoOrdenacao = preferencias.getInt(Constantes.CONFIGURACAO_CRITERIO_SELECIONADO, criterioOrdenacaoSelecionado);

        Collections.sort(requisicoesfiltradas, new RequisicaoComparator(configuracaoOrdenacao));
    }

    private void visualizarDadosPaciente() {
        Intent acao = new Intent(RequisicoesActivity.this, PacienteActivity.class);

        acao.putExtra(Constantes.DADOS_PACIENTE_ACTIVITY, requisicaoSelecionada.getPaciente());

        startActivity(acao);

    }

    private void encaminharRequisicao() {
        if(requisicaoSelecionada.getPaciente().getEmail()!=null){
            Email email = new Email(new String[]{requisicaoSelecionada.getPaciente().getEmail()}, SUBJECT_EMAIL, montarCorpoEmail());
            EmailUtil emailUtil = new EmailUtil();
            Intent intentEmail =  emailUtil.enviarEmail(email);

            try{
                startActivity(intentEmail);
            } catch (android.content.ActivityNotFoundException ex) {

                Toast.makeText(this,emailUtil.getMensagemFalha() , Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Paciente não possui e-mail." , Toast.LENGTH_SHORT).show();
        }
    }

    private String montarCorpoEmail() {
        String corpoEmail = new String();
        corpoEmail = "Prezado(a) " + requisicaoSelecionada.getPaciente().getNome() + " segue as informações da sua requisição:" + "\n" +
                "Número da requisição: " + requisicaoSelecionada.getNumeroFormatado() + "\n" +
                "Data da requisição: " + DateUtils.obterDataPorExtenso(requisicaoSelecionada.getDataRequisicao()) + "\n" +
                "Status da requisição: " + requisicaoSelecionada.getStatus().getDescricao() + "\n";
        return corpoEmail;
    }

    private void cancelarRequisicao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirma o cancelamento da requisção de número " + requisicaoSelecionada.getNumeroFormatado() + "?");
        builder.setPositiveButton("Sim", this);
        builder.setNegativeButton("Não", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        //realizar o cancelamento da requisição pelo serviço
        requisicaoBusiness.cancelarRequisicaoServico(requisicaoSelecionada, getApplicationContext());
        requisicaoSelecionada.setStatus(StatusRequisicao.CANCELADA);
        requisicaoAdapter.notifyDataSetChanged();
        Toast.makeText(this,
                "Requisição cancelada com sucesso.",
                Toast.LENGTH_SHORT).show();

    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //TODO: pesquisar forma mais eficiente de realizar a consulta - ver Filterable

        //texto digitado
        String nomePaciente = s.toString().toLowerCase();

        //limpa a lista
        requisicoesfiltradas.clear();

        //atualiza a lista
        for(Requisicao requisicao: requisicoes){
            if(requisicao.getPaciente().getNome().toLowerCase().contains(nomePaciente)){
                requisicoesfiltradas.add(requisicao);
            }
        }

        requisicaoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constantes.INDICE_ACTIVITY_CONFIGURACOES:
                if(resultCode == RESULT_OK){
                    criterioOrdenacaoSelecionado = (int) data.getSerializableExtra(Constantes.CONFIGURACAO_ACTIVITY);
                    Collections.sort(requisicoesfiltradas, new RequisicaoComparator(criterioOrdenacaoSelecionado));

                    salvarConfiguracaoOrdenacao();

                    requisicaoAdapter.notifyDataSetChanged();
                }
                break;
            case Constantes.INDICE_ACTIVITY_NOVA_REQUISICAO:
                if(resultCode == RESULT_OK){
                    Requisicao novaRequisicao = (Requisicao) data.getSerializableExtra(Constantes.REQUISICAO_NOVA_ACTIVITY);
                    requisicoes.add(novaRequisicao);
                    requisicoesfiltradas.add(novaRequisicao);
                    Collections.sort(requisicoes, new RequisicaoComparator(criterioOrdenacaoSelecionado));
                    Collections.sort(requisicoesfiltradas, new RequisicaoComparator(criterioOrdenacaoSelecionado));
                    requisicaoAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Nova requisição inserida. Nº: " + novaRequisicao.getNumeroFormatado() , Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void salvarConfiguracaoOrdenacao() {
        SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putInt (Constantes.CONFIGURACAO_CRITERIO_SELECIONADO, criterioOrdenacaoSelecionado);
        editor.commit();
    }

    @Override
    public void afterTextChanged(Editable s) {}

    private void abrirConfiguracoes() {
        Intent intent = new Intent(this, ConfiguracoesActivity.class);
        intent.putExtra(Constantes.CONFIGURACAO_ACTIVITY, criterioOrdenacaoSelecionado);
        startActivityForResult(intent, Constantes.INDICE_ACTIVITY_CONFIGURACOES);
    }
}
