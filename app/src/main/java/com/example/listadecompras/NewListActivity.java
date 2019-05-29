package com.example.listadecompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewListActivity extends AppCompatActivity {

    EditText edtListName;
    Button btnCreate, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        edtListName = findViewById(R.id.edtListName);
        btnCreate = findViewById(R.id.btnCreate);
        btnCancel = findViewById(R.id.btnCancel);

        Bundle dados = getIntent().getExtras();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = edtListName.getText().toString();
                if (listName.isEmpty()) {
                    edtListName.setError("Campo obrigatório!");
                } else {
                    ListaDeCompras lista = new ListaDeCompras(listName);
                }

            }
        });
    }
}
