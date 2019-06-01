package com.example.listadecompras;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    EditText nomeLista;
    ListView productList;
    Button btnAddProduct, btnRemoveProduct, btnDeletList;

    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    @Override
    protected void onResume() {
        nomeLista = findViewById(R.id.edtProductName);
        productList = findViewById(R.id.productList);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnRemoveProduct = findViewById(R.id.btnRemoveProduct);
        btnDeletList = findViewById(R.id.btnDeleteList);

        final Bundle dados = getIntent().getExtras();
        String idLista = dados.getString("idLista");
        final String idUsuario = dados.getString("id");

        listarProdutos(idLista, idUsuario);

        nomeLista.setText(dados.getString("nomeLista"));

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewProductActivity.class);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String produto1 = (String) productList.getItemAtPosition(position);

                DatabaseHelper bd = DatabaseHelper.getInstance(getApplicationContext());
                ModelProduto produto = new ModelProduto(produto1, "", idUsuario);
                Bundle produtos = new Bundle();
                produtos.putString("idProduto", bd.pegarDadosProduto(produto).getIdProduto());
                produtos.putString("idUsuario", bd.pegarDadosProduto(produto).getIdUsuario());
                produtos.putString("nomeProduto", bd.pegarDadosProduto(produto).getProdutoNome());
                Intent intent = new Intent(getApplicationContext(), EditProductActivity.class);
                intent.putExtras(produtos);
                startActivity(intent);
            }
        });
        super.onResume();
    }



    public void listarProdutos(String idLista, String idUsuario) {
        DatabaseHelper bd = DatabaseHelper.getInstance(this);

        List<ModelProduto> produtos = bd.listarProduto(idLista, idUsuario);

        arrayList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);


        productList.setAdapter(adapter);


        for (ModelProduto c : produtos) {
            // Log.d("Lista", "\nID: " + c.getCodigo() + " Nome: " + c.getNome() + " Horas: " +c.getHoras());
            arrayList.add(c.getProdutoNome());
            adapter.notifyDataSetChanged();
        }
    }
}
