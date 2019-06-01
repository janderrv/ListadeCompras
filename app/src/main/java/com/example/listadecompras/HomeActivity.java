package com.example.listadecompras;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HomeActivity extends AppCompatActivity {

    EditText edtNome;
    Button btnCreateList, btnSearchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        edtNome = findViewById(R.id.edtUsuario);
        btnCreateList = findViewById(R.id.btnCreateList1);
        btnSearchList = findViewById(R.id.btnSearchList);

        String id, nome, email;

        final Bundle dados = getIntent().getExtras();
        id = dados.getString("id");
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
    }
}
