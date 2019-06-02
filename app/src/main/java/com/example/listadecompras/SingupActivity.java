package com.example.listadecompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SingupActivity extends AppCompatActivity {

    EditText edtNome, edtEmail, edtSenha, edtCSenha;
    Button btnCadastrar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
    }

    @Override
    protected void onResume() {

        edtNome = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtPassword);
        edtCSenha = findViewById(R.id.edtCPassword);

        btnCadastrar = findViewById(R.id.btnSingup);
        btnCancelar = findViewById(R.id.btnCancel);

        final DatabaseHelper db = DatabaseHelper.getInstance(this);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edtNome.getText().toString();
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();
                String cSenha = edtCSenha.getText().toString();

                if (nome.isEmpty()) {
                    edtNome.setError("Nome é obrigatório");
                } else if (email.isEmpty()) {
                    edtEmail.setError("E-mail é obrigatório");
                } else if (senha.isEmpty()) {
                    edtSenha.setError("Senha é obrigatório");
                } else if (cSenha.isEmpty()) {
                    edtCSenha.setError("Confirmação de senha é obrigatório");
                } else if (!(senha.equals(cSenha))) {
                    edtCSenha.setError("Senhas diferem");
                } else {
                    try {
                        ModelUsuario usuario = new ModelUsuario(nome, email, senha);
                        if (db.consultaEmail(usuario)) {
                            edtEmail.setError("E-mail já utilizado");
                        } else {
                            db.addUsuario(usuario);
                            limparCampos();
                        }
                    } catch (Exception e) {
                        Log.d("singup", "erro cadastro");
                        e.printStackTrace();
                        Toast.makeText(SingupActivity.this, "Erro ao cadastrar usuário", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampos();
            }
        });

        super.onResume();
    }

    public void limparCampos() {
        edtNome.setText("");
        edtEmail.setText("");
        edtSenha.setText("");
        edtCSenha.setText("");
        edtNome.requestFocus();
    }
}

