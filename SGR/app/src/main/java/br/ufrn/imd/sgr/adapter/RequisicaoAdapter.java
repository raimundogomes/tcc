package br.ufrn.imd.sgr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.model.TipoExame;
import br.ufrn.imd.sgr.utils.DateUtils;


public class RequisicaoAdapter extends ArrayAdapter<Requisicao> {

    private Context context;
    private List<Requisicao> requisicoes = null;

    public RequisicaoAdapter(Context context, List<Requisicao> requisicoes) {
        super(context,0, requisicoes);
        this.requisicoes = requisicoes;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Requisicao requisicao = requisicoes.get(position);

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_requisicoes, null);
        }

        //data de requisição
        TextView textViewNumeroRequesicao = (TextView) view.findViewById(R.id.numeroRequisicao);
        textViewNumeroRequesicao.setText(requisicao.getNumeroFormatado());


        //data de requisição
        TextView textViewDataRequesicao = (TextView) view.findViewById(R.id.text_dataRequisicao);
        textViewDataRequesicao.setText(DateUtils.obterDataPorExtenso(requisicao.getDataRequisicao()));

        //status
        TextView textViewStatus = (TextView) view.findViewById(R.id.text_status);
        textViewStatus.setText(requisicao.getStatus().getDescricao());

        //paciente
        TextView textViewPaciente = (TextView) view.findViewById(R.id.text_paciente);
        if(requisicao.getPaciente()!=null){
            textViewPaciente.setText(requisicao.getPaciente().getNome());
        }

        TextView textViewExames = (TextView) view.findViewById(R.id.text_exames);

        List<String> exames = new ArrayList<String>();

        if(requisicao.getTemHemocultura()){
            exames.add(TipoExame.SANGUE.getDescricao());
        }

        if(requisicao.getTemUrocultura()){
            exames.add(TipoExame.URINA.getDescricao());
        }

        if(requisicao.getTemSecrecao()){
            exames.add(TipoExame.SECRECAO.getDescricao());
        }

        textViewExames.setText(exames.toString());

        return view;
    }

}
