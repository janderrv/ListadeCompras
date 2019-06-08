package com.example.listadecompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditListActivity extends AppCompatActivity {

    private EditText edtCurrentName, edtNewName;
    private Button btnUpdate, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        edtCurrentName = findViewById(R.id.edtCurrentName);
        edtNewName = findViewById(R.id.edtNewName);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);

        Bundle dados = getIntent().getExtras();

        assert dados != null;
        edtCurrentName.setText(dados.getString("nomeLista"));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            Bundle dados = getIntent().getExtras();

            @Override
            public void onClick(View v) {
                DatabaseHelper bd = DatabaseHelper.getInstance(getApplicationContext());

                String listName = edtNewName.getText().toString();

                if (listName.isEmpty()) {
                    edtCurrentName.setError("Campo obrigatório");
                } else {
                    ModelPurchaseList lista = new ModelPurchaseList(dados.getString("id"),
                            dados.getString("idProduto"), dados.getString("nomeLista"), dados.getString("idLista"));
                    Log.d("idlista", dados.getString("idLista"));
                    Log.d("idusuario", dados.getString("id"));
                    int i = bd.updateList(lista, listName);
                    if (i == 0) {
                        Toast.makeText(getApplicationContext(), "Lista não existe!", Toast.LENGTH_LONG).show();
                    } else if (i == 1) {
                        Toast.makeText(getApplicationContext(), "Lista atualizada!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro ao atualizar lista!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
