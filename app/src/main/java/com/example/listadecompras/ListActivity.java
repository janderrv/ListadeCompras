package com.example.listadecompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        nomeLista = findViewById(R.id.edtListName);
        productList = findViewById(R.id.productList);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnRemoveProduct = findViewById(R.id.btnRemoveProduct);
        btnDeletList = findViewById(R.id.btnDeleteList);

        Bundle dados = getIntent().getExtras();
        String idLista = dados.getString("idLista");

        listarProdutos(idLista);

        nomeLista.setText(dados.getString("nomeLista"));
    }

    public void listarProdutos(String idLista) {
        DatabaseHelper bd = DatabaseHelper.getInstance(this);

        List<ModelProduto> produtos = bd.listarProduto(idLista);

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
