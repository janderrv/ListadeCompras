package com.example.listadecompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewProductActivity extends AppCompatActivity {

    EditText edtProductName;
    Button btnCreateProdutct, btnCancelProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

    }


    @Override
    protected void onResume() {

        edtProductName = findViewById(R.id.edtNewName);
        btnCreateProdutct = findViewById(R.id.btnUpdate);
        btnCancelProduct = findViewById(R.id.btnCancel);


        btnCreateProdutct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String productName = edtProductName.getText().toString();

                DatabaseHelper bd = DatabaseHelper.getInstance(getApplicationContext());
                Bundle dados = getIntent().getExtras();
                if (productName.isEmpty()) {
                    edtProductName.setError("Campo obrigatório");
                } else {
                    ModelProduto produto = new ModelProduto(productName, "", dados.getString("id"));
                    Log.d("idusuario", dados.getString("id"));
                    Log.d("idlista", dados.getString("idLista"));
                    ModelListaDeCompras lista = new ModelListaDeCompras(dados.getString("id"), "", "", dados.getString("idLista"));
                    int i = 0;
                    i = bd.addProduto(produto, lista);
                    if (i == 0) {
                        Toast.makeText(getApplicationContext(), "Produto já existe!", Toast.LENGTH_LONG).show();
                    } else if (i == 1) {
                        Log.d("IDproduto", produto.getIdProduto());
                        Toast.makeText(getApplicationContext(), "Produto adicionado!", Toast.LENGTH_LONG).show();
                        limparCampos();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro ao adicionar produto!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnCancelProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        super.onResume();
    }

    public void limparCampos() {
        edtProductName.setText("");
    }
}
