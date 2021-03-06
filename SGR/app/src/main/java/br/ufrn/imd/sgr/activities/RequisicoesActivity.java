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
import br.ufrn.imd.sgr.service.RequisicaoService;
import br.ufrn.imd.sgr.service.impl.RequisicaoServiceImpl;
import br.ufrn.imd.sgr.comparator.RequisicaoComparator;
import br.ufrn.imd.sgr.model.Email;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.model.SituacaoRequisicao;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.DateUtils;
import br.ufrn.imd.sgr.utils.DetectaConexao;
import br.ufrn.imd.sgr.utils.EmailUtil;

public class RequisicoesActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,

        TextWatcher {

    public static final String SUBJECT_EMAIL = "[MEJC][SGR] - Requisição N°: /%s";

    private ListView listView;

    private List<Requisicao> requisicoes = new ArrayList<Requisicao>();

    private List<Requisicao> requisicoesfiltradas;

    private Requisicao requisicaoSelecionada = null;

    private RequisicaoAdapter requisicaoAdapter;

    private int criterioOrdenacaoSelecionado = Constantes.CRITERIO_DATA_REQUISICAO;

    private RequisicaoService requisicaoServiceImpl;

    private  ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisicoes);

        requisicaoServiceImpl = new RequisicaoServiceImpl(this);

        requisicaoServiceImpl = new RequisicaoServiceImpl(getApplicationContext());

        if(requisicoes.size()==0){
            requisicoes = requisicaoServiceImpl.listar();
        }

        requisicoesfiltradas = ((List) ((ArrayList) requisicoes).clone());

        ordenarComBaseConfiguracao();

        listView = (ListView) findViewById(R.id.list_requisicao);

        registerForContextMenu(listView);

        requisicaoAdapter = new RequisicaoAdapter(this,  requisicoesfiltradas);

        listView.setAdapter(requisicaoAdapter);

        listView.setOnItemClickListener(this);

        listView.setOnItemLongClickListener(this);

        //edit search
        EditText editSearch = (EditText) findViewById(R.id.edit_search);

        editSearch.addTextChangedListener(this);

        dialog = new ProgressDialog(this);

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
//            case R.id.menu_encaminhar_requisicao:
//                encaminharRequisicao();
//                break;

            case R.id.menu_dados_paciente:
                visualizarDadosPaciente();
                break;
            case R.id.menu_excluir_requisicao:
                excluirRequisicao();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void excluirRequisicao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirme a exclusão da requisição de número " + requisicaoSelecionada.getNumeroFormatado() + "?");
        builder.setPositiveButton("Sim" ,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                excluir();
            }
        });
        builder.setNegativeButton("Não", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void excluir() {
        requisicaoServiceImpl.excluir(requisicaoSelecionada.getId());
        requisicoes.remove(requisicaoSelecionada);
        requisicoesfiltradas.remove(requisicaoSelecionada);
        requisicaoAdapter.notifyDataSetChanged();
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
                requisicaoServiceImpl.desconectar(this);
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

        exibirDialog("Sincronizando as requisições...", "Aguarde, por favor.");

        requisicaoServiceImpl.atualizarRequisicoes( this);

    }

    private void exibirDialog(String titulo, String mensagem){
        dialog.setTitle(titulo);
        dialog.setMessage(mensagem);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
    }
    public void ocultarDialog(){
        dialog.dismiss();
    }

    public void atualizarTela() {
        requisicoes = requisicaoServiceImpl.listar();

        requisicoesfiltradas = ((List) ((ArrayList) requisicoes).clone());

        ordenarComBaseConfiguracao();
        requisicaoAdapter = new RequisicaoAdapter(this,  requisicoesfiltradas);

        listView.setAdapter(requisicaoAdapter);
        requisicaoAdapter.notifyDataSetChanged();
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
            Email email = new Email(new String[]{requisicaoSelecionada.getPaciente().getEmail()},
                    String.format(SUBJECT_EMAIL, requisicaoSelecionada.getNumero()), montarCorpoEmail());
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
                "Situação da requisição: " + requisicaoSelecionada.getStatus().getDescricao() + "\n";
        return corpoEmail;
    }

    private void cancelarRequisicao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirme o cancelamento da requisição de número " + requisicaoSelecionada.getNumeroFormatado() + "?");
        builder.setPositiveButton("Sim" ,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                       cancelar();
                    }
                });
        builder.setNegativeButton("Não", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void cancelar() {

        //realizar o cancelamento da requisição pelo serviço
        requisicaoServiceImpl.cancelarRequisicaoServico(requisicaoSelecionada, getApplicationContext());
        requisicaoSelecionada.setStatus(SituacaoRequisicao.CANCELADA);
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
