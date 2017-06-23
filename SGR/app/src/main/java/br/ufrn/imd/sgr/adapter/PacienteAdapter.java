package br.ufrn.imd.sgr.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.model.Paciente;

public class PacienteAdapter extends ArrayAdapter<Paciente> {

    private Context context;
    private List<Paciente> pacientes = null;

    public PacienteAdapter(Context context, List<Paciente> pacientes) {
        super(context,0, pacientes);
        this.pacientes = pacientes;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Paciente paciente = pacientes.get(position);

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_pacientes, null);
        }

        //prontuário
        TextView textViewProntuario = (TextView) view.findViewById(R.id.text_prontuario);
        textViewProntuario.setText(String.valueOf(paciente.getProntuario()));

        //cpf
        TextView textViewCPF = (TextView) view.findViewById(R.id.text_cpf);
        textViewCPF.setText(paciente.getCpf());

        //nome do paciente
        TextView textViewNome = (TextView) view.findViewById(R.id.text_paciente);
        textViewNome.setText(paciente.getNome());

        //nome da mãe
        TextView textViewNomeMae = (TextView) view.findViewById(R.id.text_nomeMae);
        textViewNomeMae.setText(paciente.getNomeMae());

        //data de nascimento
        TextView textViewDataNascimento = (TextView) view.findViewById(R.id.text_dataNascimento);
        textViewDataNascimento.setText(paciente.getDataNascimento());

        return view;
    }
}
