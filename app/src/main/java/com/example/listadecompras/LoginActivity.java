package com.example.listadecompras;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtSenha;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);


        final DatabaseHelper db = DatabaseHelper.getInstance(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();
                Intent intent;


                if (email.isEmpty()) {
                    edtEmail.setError("Campo obrigatório");
                } else if (senha.isEmpty()) {
                    edtSenha.setError("Campo obrigatório");
                } else {
                    try {
                        Usuario usuario = new Usuario("", email, senha, "");
                        if (db.validarLogin(usuario)) {
                            Bundle dados = new Bundle();
                            dados.putString("id", db.pegarId(usuario));
                            dados.putString("email", usuario.getEmail());
                            dados.putString("nome", usuario.getNome());
                            intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.putExtras(dados);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Usuário ou senha inválido!", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.d("Erro login", "Erro login");
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Erro ao fazer login", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    public void abrirTelaCadastro(View view) {
        Intent intent = new Intent(this, SingupActivity.class);
        startActivity(intent);
    }
}
