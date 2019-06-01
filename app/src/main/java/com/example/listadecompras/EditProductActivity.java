package com.example.listadecompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProductActivity extends AppCompatActivity {

    Button btnUpdateProduct, btnCancelUpdate;
    EditText edtProductName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
    }

    @Override
    protected void onResume() {


        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnCancelUpdate = findViewById(R.id.btnCancelUpdate);
        edtProductName = findViewById(R.id.edtProductName);


        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = edtProductName.getText().toString();

                DatabaseHelper bd = DatabaseHelper.getInstance(getApplicationContext());
                Bundle dados = getIntent().getExtras();

                if (productName.isEmpty()) {
                    edtProductName.setError("Campo obrigatório");
                } else {
                    ModelProduto produto = new ModelProduto(dados.getString("nomeProduto"),
                            dados.getString("idProduto"), dados.getString("idUsuario"));
                    Log.d("idproduto", dados.getString("idProduto"));
                    Log.d("idusuario", dados.getString("idUsuario"));
                    int i = bd.updateProduto(produto, productName);
                    if (i == 0) {
                        Toast.makeText(getApplicationContext(), "Produto não existe!", Toast.LENGTH_LONG).show();
                    } else if (i == 1) {
                        Toast.makeText(getApplicationContext(), "Produto atualizado!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro ao atualizar produto!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });

        btnCancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        super.onResume();
    }
}
