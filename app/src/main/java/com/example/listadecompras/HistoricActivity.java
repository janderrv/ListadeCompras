package com.example.listadecompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoricActivity extends AppCompatActivity {
    private ListView listHistoric;
    private ArrayList<String> idCompra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        listHistoric = findViewById(R.id.listHistoric);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Bundle dados = getIntent().getExtras();
        assert dados != null;
        final String idUsuario = dados.getString("id");
        listarCompras(idUsuario);
    }

    public void listarCompras(String idUsuario) {
        DatabaseHelper bd = DatabaseHelper.getInstance(this);
        idCompra = new ArrayList<>();
        double valor = 0;

        List<ModelPurchase> compras = bd.listarCompras(idUsuario);

        HashMap<String, String> compra = new HashMap<>();

        for (ModelPurchase c : compras) {
            compra.put(c.getNomeCompra(), "R$ " + c.getValor());
        }
        List<HashMap<String, String>> listItens = new ArrayList<>();

        SimpleAdapter adapter = new SimpleAdapter(this, listItens, R.layout.list_historic_item,
                new String[]{"Compra", "Valor"},
                new int[]{R.id.txtPurchaseName, R.id.txtPurchaseValue});

        for (Map.Entry<String, String> stringStringEntry : compra.entrySet()) {
            HashMap<String, String> resultMap = new HashMap<>();
            resultMap.put("Compra", ((Map.Entry) stringStringEntry).getKey().toString());
            resultMap.put("Valor", ((Map.Entry) stringStringEntry).getValue().toString());
            listItens.add(resultMap);
        }

        listHistoric.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

