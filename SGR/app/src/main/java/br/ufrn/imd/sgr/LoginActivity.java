package br.ufrn.imd.sgr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.ufrn.imd.sgr.activities.ListaRequisicaoActivity;
import br.ufrn.imd.sgr.utils.Constantes;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button  btnLogin, btnReset;

    private CheckBox manterConectado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	//	if(savedInstanceState==null) {

        SharedPreferences preferencias = getSharedPreferences(Constantes.PREF_NAME, MODE_PRIVATE);
        boolean conectado = preferencias.getBoolean(Constantes.CONFIGURACAO_CONECTADO, false);

        if (conectado) {
            finish();
            startActivity(new Intent(this, RequisicoesActivity.class));
        }
	//	}

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //if (auth.getCurrentUser() != null) {
        //    startActivity(new Intent(LoginActivity.this, MainActivity.class));
        //    finish();
        //}

        // set the view now
        setContentView(R.layout.activity_login);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        manterConectado = (CheckBox) findViewById(R.id.manterConectado);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Digite o E-mail!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Digite a Senha!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    salvarPreferenciaManterLogado();
                                    Intent intent = new Intent(LoginActivity.this, RequisicoesActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            private void salvarPreferenciaManterLogado() {
                                SharedPreferences preferencias = getSharedPreferences(Constantes.PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferencias.edit();
                                editor.putBoolean(Constantes.CONFIGURACAO_CONECTADO, manterConectado.isChecked());
                                editor.commit();

                            }
                        });
            }
        });
    }
}

