package br.ufrn.imd.sgr;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.ufrn.imd.sgr.utils.Constantes;

public class RequisicoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisicoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
              //  sicronizarRequisicoes(requisicoes);
                break;
            case R.id.menu_configuracoes:
            //    abrirConfiguracoes();
                break;
            case R.id.menu_desconectar:
                desconectar();
                break;
            case R.id.menu_nova_requisicao:
              //  novaRequisicao();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void desconectar() {
        SharedPreferences sp = getSharedPreferences(Constantes.PREF_NAME, MODE_PRIVATE);

        boolean b = sp.getBoolean(Constantes.CONFIGURACAO_CONECTADO, false);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();

        finish();
    }

}
