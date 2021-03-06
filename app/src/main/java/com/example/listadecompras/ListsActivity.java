package com.example.listadecompras;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class ListsActivity extends AppCompatActivity {

    ListView listLista;
    Button btnCreateList1, btnCanceList1;
    SearchView searchList;

    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList, arrayListId, arrayListNome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
    }

    @Override
    protected void onResume() {
        super.onResume();

        listLista = findViewById(R.id.list);
        btnCreateList1 = findViewById(R.id.btnUpdate);
        btnCanceList1 = findViewById(R.id.btnCancel);
        searchList = findViewById(R.id.searchList);
        Bundle dados = getIntent().getExtras();
        assert dados != null;
        final String idUsuario = dados.getString("id");

        listarListas(idUsuario);

        searchList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        listLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle dadosx = getIntent().getExtras();
                String idLista = arrayListId.get(position);
                String nomeLista = arrayListNome.get(position);
                assert dadosx != null;
                dadosx.putString("id", idUsuario);
                dadosx.putString("idLista", idLista);
                dadosx.putString("nomeLista", nomeLista);
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtras(dadosx);
                startActivity(intent);

            }
        });
    }

    public void listarListas(String id) {
        DatabaseHelper bd = DatabaseHelper.getInstance(this);

        List<ModelPurchaseList> lista = bd.listarListas(id);

        arrayList = new ArrayList<>();
        arrayListId = new ArrayList<>();
        arrayListNome = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);


        listLista.setAdapter(adapter);


        for (ModelPurchaseList c : lista) {
            // Log.d("Lista", "\nID: " + c.getCodigo() + " Nome: " + c.getNome() + " Horas: " +c.getHoras());
            arrayList.add(c.getNomeLista());
            arrayListId.add(c.getIdLista());
            arrayListNome.add(c.getNomeLista());
            adapter.notifyDataSetChanged();
        }
    }
}
