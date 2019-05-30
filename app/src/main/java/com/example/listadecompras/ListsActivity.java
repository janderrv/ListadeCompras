package com.example.listadecompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        listLista = findViewById(R.id.list);
        btnCreateList1 = findViewById(R.id.btnCreateList1);
        btnCanceList1 = findViewById(R.id.btnCancelList1);
        searchList = findViewById(R.id.searchList);

        listarListas();

        searchList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            DatabaseHelper bd = DatabaseHelper.getInstance(getApplicationContext());

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                //bd.filtrarListas(newText);
                return false;
            }
        });
    }

    public void listarListas() {
        DatabaseHelper bd = DatabaseHelper.getInstance(this);

        List<ListaDeCompras> lista = bd.listarListas();

        arrayList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);


        listLista.setAdapter(adapter);


        for (ListaDeCompras c : lista) {
            // Log.d("Lista", "\nID: " + c.getCodigo() + " Nome: " + c.getNome() + " Horas: " +c.getHoras());
            arrayList.add(c.getIdLista() + " - " + c.getNomeLista());
            adapter.notifyDataSetChanged();
        }
    }
}
