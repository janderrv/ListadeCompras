package com.example.listadecompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewListActivity extends AppCompatActivity {

    EditText edtListName;
    Button btnCreateList, btnCancelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        edtListName = findViewById(R.id.edtListName);
        btnCreateList = findViewById(R.id.btnCreateList);
        btnCancelList = findViewById(R.id.btnCancelList);

        Bundle dados = getIntent().getExtras();
        String nome = dados.getString("nome");
        final String id = dados.getString("id");

        btnCreateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper bd = DatabaseHelper.getInstance(getApplicationContext());
                String listName = edtListName.getText().toString();

                if (listName.isEmpty()) {
                    edtListName.setError("Campo obrigatório!");
                } else {
                    Usuario usuario = new Usuario("", "", "", id);
                    ListaDeCompras lista = new ListaDeCompras(usuario, null, listName);
                    int i = 0;
                    i = bd.addListaDeCompras(lista);
                    if (i == 0) {
                        Toast.makeText(getApplicationContext(), "Lista já existe!", Toast.LENGTH_LONG).show();
                    } else if (i == 1) {
                        Toast.makeText(getApplicationContext(), "Lista criada!", Toast.LENGTH_LONG).show();
                        limpaCampo();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro ao criar lista!", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        btnCancelList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampo();
            }
        });


    }

    private void limpaCampo() {
        edtListName.setText("");
    }
}
