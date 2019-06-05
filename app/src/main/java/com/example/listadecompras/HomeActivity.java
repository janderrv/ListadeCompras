package com.example.listadecompras;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HomeActivity extends AppCompatActivity {

    EditText edtNome;
    Button btnCreateList, btnSearchList, btnComprar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onResume() {


        edtNome = findViewById(R.id.edtUsuario);
        btnCreateList = findViewById(R.id.btnUpdate);
        btnSearchList = findViewById(R.id.btnSearchList);
        btnComprar = findViewById(R.id.btnComprar);

        String nome, email;

        final Bundle dados = getIntent().getExtras();
        email = dados.getString("email");

        ModelUsuario usuario = new ModelUsuario("", email, "", "");
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        nome = db.pegarNome(usuario);

        edtNome.setText(nome);

        btnCreateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewListActivity.class);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });
        btnSearchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListsActivity.class);
                intent.putExtras(dados);
                startActivity(intent);

            }
        });
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectListActivity.class);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });

        super.onResume();
    }

    @Override
    public void onBackPressed() {

    }
}
