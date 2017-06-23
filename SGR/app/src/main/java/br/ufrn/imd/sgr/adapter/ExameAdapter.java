package br.ufrn.imd.sgr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.utils.DateUtils;

/**
 * Created by Neto on 30/05/2016.
 */
public class ExameAdapter extends ArrayAdapter<Exame> {

    private Context context;
    private List<Exame> exames = null;

    public ExameAdapter(Context context, List<Exame> exames) {
        super(context,0, exames);
        this.exames = exames;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Exame exame = exames.get(position);

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_exames, null);
        }

        TextView descricaoExame = (TextView) view.findViewById(R.id.text_exame);
        descricaoExame.setText(exame.getDescricao());

        if(exame.getDataColeta()!=null){
            TextView dataColeta = (TextView) view.findViewById(R.id.text_data_coleta);
            dataColeta.setText(DateUtils.obterData(exame.getDataColeta()));
        }

        TextView situacaoExame = (TextView) view.findViewById(R.id.text_situacao_exame);
        situacaoExame.setText(exame.getSituacaoAmostra());

        TextView resultadoExame = (TextView) view.findViewById(R.id.text_resultado_exame);
        resultadoExame.setText(exame.getResultado());

        return view;
    }
}

