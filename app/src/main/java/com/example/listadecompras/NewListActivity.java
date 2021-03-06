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
    }

    @Override
    protected void onResume() {

        edtListName = findViewById(R.id.edtNewName);
        btnCreateList = findViewById(R.id.btnUpdate);
        btnCancelList = findViewById(R.id.btnCancel);

        Bundle dados = getIntent().getExtras();
        assert dados != null;
        final String id = dados.getString("id");

        btnCreateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper bd = DatabaseHelper.getInstance(getApplicationContext());
                String listName = edtListName.getText().toString();

                if (listName.isEmpty()) {
                    edtListName.setError("Campo obrigatório!");
                } else {
                    ModelUser usuario = new ModelUser("", "", "", id);
                    ModelPurchaseList lista = new ModelPurchaseList(usuario.getId(), null, listName, "");
                    int i = bd.addListaDeCompras(lista);
                    if (i == 0) {
                        Toast.makeText(getApplicationContext(), "Lista já existe!", Toast.LENGTH_LONG).show();
                    } else if (i == 1) {
                        Toast.makeText(getApplicationContext(), "Lista criada!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro ao criar lista!", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        btnCancelList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        super.onResume();
    }

    private void limpaCampo() {
        edtListName.setText("");
    }
}
