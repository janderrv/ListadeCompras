package com.example.listadecompras;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtSenha;
    Button btnLogin, btnSingup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void onResume() {

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSingup = findViewById(R.id.btnSingup);


        btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SingupActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();
                Intent intent;


                if (email.isEmpty()) {
                    edtEmail.setError("Campo obrigatório");
                } else if (senha.isEmpty()) {
                    edtSenha.setError("Campo obrigatório");
                } else {
                    try {
                        ModelUser usuario = new ModelUser("", email, senha, "");
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

        super.onResume();
    }

    private void setSharedPrefs(Context context, String projectName, String key, String value) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(projectName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getSharedPrefs(Context context, String projectName, String key) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(projectName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
