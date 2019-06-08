package com.example.listadecompras;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SelectedListActivity extends AppCompatActivity {
    private EditText nomeLista, value;
    private ListView productList;
    private Double valueTotal;
    private Button btnFinish, btnCancel;
    private ArrayList<String> idProduto;
    private ArrayList<String> valorProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_list);

        nomeLista = findViewById(R.id.edtNewName);
        productList = findViewById(R.id.productList);
        btnFinish = findViewById(R.id.btnFinish);
        btnCancel = findViewById(R.id.btnCancel);
        value = findViewById(R.id.edtValue);
        valueTotal = 0.0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Bundle dados = getIntent().getExtras();
        assert dados != null;
        final String idLista = dados.getString("idLista");
        final String idUsuario = dados.getString("id");

        listarProdutos(idLista, idUsuario);

        value.setText(String.format("R$ %s", valueTotal));
        final Double[] valor = {0.0};

        nomeLista.setText(dados.getString("nomeLista"));
        final AlertDialog.Builder dialog = new AlertDialog.Builder(SelectedListActivity.this);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                Date data = new Date();
                String dataFormatada = formataData.format(data);
                DatabaseHelper bd = DatabaseHelper.getInstance(getApplicationContext());
                ModelPurchase compra = new ModelPurchase("", valueTotal, dataFormatada, idUsuario, idLista, "");
                bd.addCompra(compra);
                Log.d("compra", "compra finalizada");
                finish();
            }
        });

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText edittext = new EditText(getApplicationContext());
                edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                edittext.setRawInputType(Configuration.KEYBOARD_12KEY);
                dialog.setTitle("Inserir valor do produto");
                dialog.setView(edittext);

                edittext.addTextChangedListener(new TextWatcher() {
                    private String current = "";

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!s.toString().equals(current)) {
                            edittext.removeTextChangedListener(this);

                            String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
                            String cleanString = s.toString().replaceAll(replaceable, "");

                            double parsed;
                            try {
                                parsed = Double.parseDouble(cleanString);
                            } catch (NumberFormatException e) {
                                parsed = 0.00;
                            }
                            String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));

                            current = formatted;
                            edittext.setText(formatted);
                            edittext.setSelection(formatted.length());
                            edittext.addTextChangedListener(this);
                            valor[0] = parsed / 100;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }

                });

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper bd = DatabaseHelper.getInstance(getApplicationContext());
                        HashMap produto1;
                        produto1 = (HashMap) productList.getItemAtPosition(position);
                        String produtox = Objects.requireNonNull(produto1.get("Produto")).toString();
                        ModelProduct produto = new ModelProduct(produtox, "", idUsuario);
                        String id = bd.pegarDadosProduto(produto).getIdProduto();
                        Log.d("PRODUTO", produto1.toString());

                        ModelProduct produtoz = new ModelProduct("", id, idUsuario);
                        Log.d("idproduto", id);
                        Log.d("idusuario", idUsuario);
                        Log.d("valor", String.valueOf(valor[0]));
                        bd.updateValorProduto(produtoz, valor[0]);
                        listarProdutos(idLista, idUsuario);
                        value.setText(String.format("R$ %.2f", valueTotal));
                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void listarProdutos(String idLista, String idUsuario) {
        DatabaseHelper bd = DatabaseHelper.getInstance(this);
        idProduto = new ArrayList<>();
        valorProduto = new ArrayList<>();
        double valor = 0;

        final List<ModelProduct> produtos = bd.listarProduto(idLista, idUsuario);

        HashMap<String, String> productValue = new HashMap<>();

        for (ModelProduct c : produtos) {
            productValue.put(c.getProdutoNome(), "R$ " + c.getProdutoValor());
            idProduto.add(c.getIdProduto());
            valor = valor + (c.getProdutoValor());
        }
        valueTotal = valor;

        List<HashMap<String, String>> listItens = new ArrayList<>();

        SimpleAdapter adapter = new SimpleAdapter(this, listItens, R.layout.list_product_item,
                new String[]{"Produto", "Valor"},
                new int[]{R.id.txtProductName, R.id.txtProductValue});

        for (Map.Entry<String, String> stringStringEntry : productValue.entrySet()) {
            HashMap<String, String> resultMap = new HashMap<>();
            resultMap.put("Produto", ((Map.Entry) stringStringEntry).getKey().toString());
            resultMap.put("Valor", ((Map.Entry) stringStringEntry).getValue().toString());
            listItens.add(resultMap);
        }

        productList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
