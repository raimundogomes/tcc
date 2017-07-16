package br.ufrn.imd.sgr.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.ufrn.imd.sgr.R;
import br.ufrn.imd.sgr.model.Email;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.EmailUtil;


public class PacienteActivity extends AppCompatActivity {

    private Paciente paciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dados_paciente_activity);

        Intent intent = getIntent();

        paciente= (Paciente) intent.getExtras().get(Constantes.DADOS_PACIENTE_ACTIVITY);

        atualizarTelaDadosPaciente(paciente);

    }

    private void atualizarTelaDadosPaciente(Paciente paciente) {

        TextView prontuario = (TextView) findViewById(R.id.text_prontuario);
        prontuario.setText(paciente.getProntuario().toString());

        TextView cns = (TextView) findViewById(R.id.text_cns);
        cns.setText(paciente.getCns());

        TextView nomeMae = (TextView) findViewById(R.id.text_nomeMae);
        nomeMae.setText(paciente.getNomeMae());


        TextView nome = (TextView) findViewById(R.id.text_paciente);
        nome.setText(paciente.getNome());


        TextView telefone = (TextView) findViewById(R.id.text_fone);
        telefone.setText("  " + paciente.getTelefone());

        TextView email = (TextView) findViewById(R.id.text_email);
        email.setText(paciente.getEmail());

    }

    public void telefonar(View v){

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+paciente.getTelefone()));
        startActivity(callIntent);
    }

    public void enviarEmail(View v){

        if(paciente.getEmail()!=null){
            Email email = new Email(new String[]{paciente.getEmail()}, "", "");

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
}