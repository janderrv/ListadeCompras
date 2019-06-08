package com.example.listadecompras;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "db_lista_de_compra";
    private static final int DATABASE_VERSION = 1;
    // Tabelas
    private static final String TB_LISTA_DE_COMPRAS = "listadecompras";
    private static final String TB_USUARIOS = "usuarios";
    private static final String TB_PRODUTOS = "produtos";
    private static final String TB_LISTADECOMPRA_HAS_PRODUTO = "listadecompra_has_produto";
    private static final String TB_COMPRAS = "compras";
    // Lista de compras colunas
    private static final String KEY_LISTA_DE_COMPRAS_ID = "idLista";
    private static final String KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK = "usuarioId";
    private static final String KEY_LISTA_DE_COMPRAS_NOME = "nomeLista";

    // Usuario colunas
    private static final String KEY_USUARIO_ID = "usuarioid";
    private static final String KEY_USUARIO_NOME = "nomeUsuario";
    private static final String KEY_USUARIO_EMAIL = "email";
    private static final String KEY_USUARIO_SENHA = "senha";
    // Produto colunas
    private static final String KEY_PRODUTO_ID = "produtoid";
    private static final String KEY_PRODUTO_NOME = "nomeProduto";
    private static final String KEY_PRODUTO_USUARIO_ID_FK = "usuarioId";
    private static final String KEY_PRODUTO_VALOR = "valorProduto";
    //Compras coluna
    private static final String KEY_COMPRA_ID = "idCompra";
    private static final String KEY_COMPRA_NOME = "nomeCompra";
    private static final String KEY_COMPRA_VALOR = "valorCompra";
    private static final String KEY_COMPRA_USUARIO_ID_FK = "idUsuario";
    private static final String KEY_COMPRA_LISTA_DE_COMPRAS_FK = "idListaDeCompras";
    private static final String KEY_COMPRA_DATA = "dataCompra";


    private static final String TAG = "DataBAse";
    // Lista de compra has produto colunas
    private static final String listadecompra_idlistadecompra = "listadecompra_idlistadecompra";
    private static final String produto_idproduto = "produto_idproduto";
    private static DatabaseHelper sInstance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Usando padrao singleton
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String usuario = "CREATE TABLE " + TB_USUARIOS +
                "(" +
                KEY_USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_USUARIO_NOME + " VARCHAR(45) NOT NULL," +
                KEY_USUARIO_EMAIL + " VARCHAR(45) NOT NULL," +
                KEY_USUARIO_SENHA + " VARCHAR(45) NOT NULL," +
                " CONSTRAINT email_UNIQUE " +
                " UNIQUE (" + KEY_USUARIO_EMAIL + ") " + ")";

        String listadecompra = "CREATE TABLE " + TB_LISTA_DE_COMPRAS +
                "(" +
                KEY_LISTA_DE_COMPRAS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_LISTA_DE_COMPRAS_NOME + " VARCHAR(45) NOT NULL," +
                KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK + " INTEGER NOT NULL," +
                " CONSTRAINT fk_listadecompra_usuario" +
                " FOREIGN KEY(" + KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK + ")" +
                " REFERENCES " + TB_USUARIOS + "(" + KEY_USUARIO_ID + ")" +
                " ON DELETE CASCADE" +
                " ON UPDATE CASCADE" +
                ")";

        String produto = "CREATE TABLE " + TB_PRODUTOS +
                "(" +
                KEY_PRODUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_PRODUTO_NOME + " VARCHAR(45) NOT NULL," +
                KEY_PRODUTO_VALOR + " DECIMAL(2,5) NOT NULL," +
                KEY_PRODUTO_USUARIO_ID_FK + " INTEGER NOT NULL," +
                " CONSTRAINT fk_produto_usuario1" +
                " FOREIGN KEY(" + KEY_PRODUTO_USUARIO_ID_FK + ")" +
                " REFERENCES " + TB_USUARIOS + "(" + KEY_USUARIO_ID + ")" +
                " ON DELETE CASCADE" +
                " ON UPDATE CASCADE" +
                ")";

        String hasproduto = "CREATE TABLE " + TB_LISTADECOMPRA_HAS_PRODUTO +
                "(" +
                listadecompra_idlistadecompra + " INTEGER NOT NULL," +
                produto_idproduto + " INTEGER NOT NULL" + ")";

        String compra = "CREATE TABLE " + TB_COMPRAS +
                "(" +
                KEY_COMPRA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_COMPRA_NOME + " VARCHAR(50) NOT NULL," +
                KEY_COMPRA_VALOR + " DECIMAL NOT NULL," +
                KEY_COMPRA_DATA + " DATE NOT NULL," +
                KEY_COMPRA_USUARIO_ID_FK + " INTEGER NOT NULL," +
                " CONSTRAINT fk_compra_usuario" +
                " FOREIGN KEY(" + KEY_COMPRA_USUARIO_ID_FK + ")" +
                " REFERENCES " + TB_USUARIOS + "(" + KEY_USUARIO_ID + ")" +
                ")";

        db.execSQL(usuario);
        db.execSQL(listadecompra);
        db.execSQL(produto);
        db.execSQL(hasproduto);
        db.execSQL(compra);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_LISTADECOMPRA_HAS_PRODUTO);
            db.execSQL("DROP TABLE IF EXISTS " + TB_PRODUTOS);
            db.execSQL("DROP TABLE IF EXISTS " + TB_LISTA_DE_COMPRAS);
            db.execSQL("DROP TABLE IF EXISTS " + TB_USUARIOS);
            db.execSQL("DROP TABLE IF EXISTS " + TB_COMPRAS);
            onCreate(db);
        }
    }

    // Insert usuario no db
    void addUsuario(ModelUser usuario) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_USUARIO_NOME, usuario.getNome());
        values.put(KEY_USUARIO_EMAIL, usuario.getEmail());
        values.put(KEY_USUARIO_SENHA, usuario.getSenha());

        try {
            Cursor cursor = db.rawQuery("select * from " + TB_USUARIOS + " where " + KEY_USUARIO_EMAIL +
                    " like ?", new String[]{usuario.getEmail()});
            if (cursor.moveToFirst()) {
                Log.e(TAG, "Email já existe");
            } else {
                db.insertOrThrow(TB_USUARIOS, null, values);
                Log.d(TAG, "Usuário criado");
                db.setTransactionSuccessful();
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao adicionar usuário");
        } finally {
            db.endTransaction();
        }
    }

    //Consulta se email já está no banco de dados
    boolean consultaEmail(ModelUser usuario) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        Cursor cursor = db.rawQuery("select * from " + TB_USUARIOS + " where " + KEY_USUARIO_EMAIL +
                " like ?", new String[]{usuario.getEmail()});
        if (cursor.moveToFirst()) {
            cursor.close();
            db.endTransaction();
            return true;
        }
        db.endTransaction();
        return false;
    }

    // Validar login
    boolean validarLogin(ModelUser usuario) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();


        Cursor cursor = db.rawQuery("select * from " + TB_USUARIOS + " where " + KEY_USUARIO_EMAIL +
                " like ?" + " AND " + KEY_USUARIO_SENHA + " like ?", new String[]{usuario.getEmail(), usuario.getSenha()});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.endTransaction();
            return true;
        }
        cursor.close();
        db.endTransaction();
        return false;
    }

    //Pegar id
    String pegarId(ModelUser usuario) {
        SQLiteDatabase db = getWritableDatabase();
        String id = "null";

        db.beginTransaction();

        Cursor cursor = db.rawQuery("select * from " + TB_USUARIOS + " where " + KEY_USUARIO_EMAIL +
                " like ?" + " AND " + KEY_USUARIO_SENHA + " like ?", new String[]{usuario.getEmail(), usuario.getSenha()});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getString(0);
            cursor.close();
            db.endTransaction();
            return id;
        }
        db.endTransaction();
        return id;
    }

    //Pegar nome
    String pegarNome(ModelUser usuario) {
        SQLiteDatabase db = getWritableDatabase();
        String nome = "null";

        db.beginTransaction();

        Cursor cursor = db.rawQuery("select * from " + TB_USUARIOS + " where " + KEY_USUARIO_EMAIL +
                " like ?", new String[]{usuario.getEmail()});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            nome = cursor.getString(1);
            cursor.close();
            db.endTransaction();
            return nome;
        }
        db.endTransaction();
        return nome;
    }

    // Insert lista de compras no db
    int addListaDeCompras(ModelPurchaseList lista) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_LISTA_DE_COMPRAS_NOME, lista.getNomeLista());
        values.put(KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK, lista.getIdUsuario());


        String query = "select * from " + TB_LISTA_DE_COMPRAS + " where " + KEY_LISTA_DE_COMPRAS_NOME +
                " like '" + lista.getNomeLista() + "' AND " + KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK + " like " + lista.getIdUsuario();


        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                Log.e(TAG, "Lista já existe");
                cursor.close();
                return 0;
            } else {
                db.insertOrThrow(TB_LISTA_DE_COMPRAS, null, values);
                Log.d(TAG, "Lista adicionada");
                db.setTransactionSuccessful();
                cursor.close();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao criar lista");
            return 2;
        } finally {
            db.endTransaction();
        }
    }

    int deleteList(ModelPurchaseList lista) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_LISTA_DE_COMPRAS_NOME, lista.getNomeLista());
        values.put(KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK, lista.getIdUsuario());

        String query = "select * from " + TB_LISTA_DE_COMPRAS + " where " + KEY_LISTA_DE_COMPRAS_NOME +
                " like '" + lista.getNomeLista() + "' AND " + KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK + " like " + lista.getIdUsuario();


        try {
            Cursor cursor = db.rawQuery(query, null);
            if (!(cursor.moveToFirst())) {
                Log.e(TAG, "Lista não existe");
                Log.e("idlista", lista.getIdLista());
                Log.e("nomelista", lista.getNomeLista());
                Log.e("idUsuario", lista.getIdUsuario());
                cursor.close();
                return 0;
            } else {
                db.execSQL("DELETE FROM " + TB_LISTA_DE_COMPRAS + " WHERE " + KEY_LISTA_DE_COMPRAS_ID +
                        " LIKE " + lista.getIdLista() + " AND " + KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK +
                        " = " + lista.getIdUsuario());
                db.execSQL("DELETE FROM " + TB_LISTADECOMPRA_HAS_PRODUTO + " WHERE " + listadecompra_idlistadecompra +
                        " LIKE " + lista.getIdLista());
                Log.d(TAG, "Lista excluida");
                db.setTransactionSuccessful();
                cursor.close();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao deletar lista");
            return 2;
        } finally {
            db.endTransaction();
        }
    }

    //Listar listas de compras
    List<ModelPurchaseList> listarListas(String id) {
        List<ModelPurchaseList> listaListas = new ArrayList<>();

        String query = "SELECT * FROM " + TB_LISTA_DE_COMPRAS + " WHERE " + KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK +
                " = " + id + " ORDER BY " + KEY_LISTA_DE_COMPRAS_NOME + " ASC ";

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ModelPurchaseList lista = new ModelPurchaseList();
                lista.setIdLista(cursor.getString(0));
                lista.setNomeLista(cursor.getString(1));
                lista.setIdUsuario(cursor.getString(2));

                listaListas.add(lista);
            } while (cursor.moveToNext());
        }
        assert cursor != null;
        cursor.close();
        db.endTransaction();

        return listaListas;

    }

    // Insert produtos no db
    int addProduto(ModelProduct produto, ModelPurchaseList lista) {
        SQLiteDatabase db = getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(KEY_PRODUTO_NOME, produto.getProdutoNome());
        values.put(KEY_PRODUTO_VALOR, produto.getProdutoValor());
        values.put(KEY_PRODUTO_USUARIO_ID_FK, produto.getIdUsuario());
        ContentValues values1 = new ContentValues();
        values1.put(listadecompra_idlistadecompra, lista.getIdLista());


        String query = "select * from " + TB_PRODUTOS + " where " + KEY_PRODUTO_NOME +
                " = '" + produto.getProdutoNome() + "' AND " + KEY_PRODUTO_USUARIO_ID_FK + " = " + produto.getIdUsuario();

        db.beginTransaction();

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                Log.e(TAG, "Produto já existe");
                cursor.close();
                return 0;
            } else {
                db.insertOrThrow(TB_PRODUTOS, null, values);
                Cursor cursorx = db.rawQuery("select * from " + TB_PRODUTOS + " where " + KEY_PRODUTO_NOME +
                        " like ?" + " AND " + KEY_USUARIO_ID + " like ?", new String[]{produto.getProdutoNome(), produto.getIdUsuario()});

                String idproduto = "";
                if (cursorx.getCount() > 0) {
                    cursorx.moveToFirst();
                    idproduto = cursorx.getString(0);
                    cursorx.close();
                }
                values1.put(produto_idproduto, idproduto);
                db.insertOrThrow(TB_LISTADECOMPRA_HAS_PRODUTO, null, values1);
                Log.d(TAG, "Produto adicionado");
                db.setTransactionSuccessful();
                assert cursor != null;
                cursor.close();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao criar produto");
            return 2;
        } finally {
            db.endTransaction();
        }
    }

    // Update produtos no db
    int updateProduto(ModelProduct produto, String nome) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "select * from " + TB_PRODUTOS + " where " + KEY_PRODUTO_NOME +
                " = '" + produto.getProdutoNome() + "' AND " + KEY_PRODUTO_USUARIO_ID_FK + " = " + produto.getIdUsuario();

        db.beginTransaction();

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (!(cursor != null && cursor.moveToFirst())) {
                Log.e(TAG, "Produto não existe");
                assert cursor != null;
                cursor.close();
                return 0;
            } else {
                query = "select * from " + TB_PRODUTOS + " where " + KEY_PRODUTO_NOME +
                        " = '" + produto.getProdutoNome() + "' AND " + KEY_USUARIO_ID + " = " + produto.getIdUsuario();
                Cursor cursorx = db.rawQuery(query, null);

                String idproduto = "a";
                String idusuario = "a";

                if (cursorx.getCount() > 0) {
                    cursorx.moveToFirst();
                    idproduto = cursorx.getString(0);
                    idusuario = cursorx.getString(2);
                    cursorx.close();
                }

                db.execSQL("UPDATE " + TB_PRODUTOS + " SET " + KEY_PRODUTO_NOME + " = '" + nome +
                        "' WHERE " + KEY_PRODUTO_ID +
                        " = " + idproduto + " AND " + KEY_PRODUTO_USUARIO_ID_FK + " = " + idusuario);

                Log.d(TAG, "Produto atualizado");
                db.setTransactionSuccessful();
                cursor.close();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao atualizar produto");
            return 2;
        } finally {
            db.endTransaction();
        }
    }

    //Setar valor no produto
    int updateValorProduto(ModelProduct produto, Double valor) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "select * from " + TB_PRODUTOS + " where " + KEY_PRODUTO_ID +
                " = '" + produto.getIdProduto() + "' AND " + KEY_PRODUTO_USUARIO_ID_FK + " = " + produto.getIdUsuario();

        db.beginTransaction();

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (!(cursor != null && cursor.moveToFirst())) {
                Log.e(TAG, "Produto não existe");
                assert cursor != null;
                cursor.close();
                return 0;
            } else {
                query = "select * from " + TB_PRODUTOS + " where " + KEY_PRODUTO_ID +
                        " = '" + produto.getIdProduto() + "' AND " + KEY_PRODUTO_USUARIO_ID_FK + " = " + produto.getIdUsuario();
                Cursor cursorx = db.rawQuery(query, null);

                String idproduto = "a";
                String idusuario = "a";

                if (cursorx.getCount() > 0) {
                    cursorx.moveToFirst();
                    idproduto = cursorx.getString(0);
                    idusuario = cursorx.getString(3);
                    cursorx.close();
                }

                db.execSQL("UPDATE " + TB_PRODUTOS + " SET " + KEY_PRODUTO_VALOR + " = " + valor +
                        " WHERE " + KEY_PRODUTO_ID +
                        " = " + idproduto + " AND " + KEY_PRODUTO_USUARIO_ID_FK + " = " + idusuario);

                Log.d(TAG, "Valor atualizado");
                db.setTransactionSuccessful();
                cursor.close();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao atualizar valor do produto");
            return 2;
        } finally {
            db.endTransaction();
        }
    }

    //Excluir produto
    int deleteProduct(ModelProduct produto, String idLista) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "select * from " + TB_PRODUTOS + " where " + KEY_PRODUTO_NOME +
                " = '" + produto.getProdutoNome() + "' AND " + KEY_PRODUTO_USUARIO_ID_FK + " = " + produto.getIdUsuario();

        db.beginTransaction();

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (!(cursor != null && cursor.moveToFirst())) {
                Log.e(TAG, "Produto não existe");
                assert cursor != null;
                cursor.close();
                return 0;
            } else {
                query = "select * from " + TB_PRODUTOS + " where " + KEY_PRODUTO_NOME +
                        " = '" + produto.getProdutoNome() + "' AND " + KEY_PRODUTO_USUARIO_ID_FK + " = " + produto.getIdUsuario();
                Cursor cursorx = db.rawQuery(query, null);

                String idproduto = "a";
                String idusuario = "a";

                if (cursorx.getCount() > 0) {
                    cursorx.moveToFirst();
                    idproduto = cursorx.getString(0);
                    idusuario = cursorx.getString(2);
                    cursorx.close();
                }

                db.execSQL("DELETE FROM " + TB_PRODUTOS + " where " + KEY_PRODUTO_ID + " = " +
                        idproduto + " AND " + KEY_PRODUTO_USUARIO_ID_FK + " = " + idusuario);
                db.execSQL("DELETE FROM " + TB_LISTADECOMPRA_HAS_PRODUTO + " where " + produto_idproduto + " = " +
                        idproduto + " AND " + listadecompra_idlistadecompra + " = " + idLista);

                Log.d(TAG, "Produto excluido");
                db.setTransactionSuccessful();
                cursor.close();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao excluir produto");
            return 2;
        } finally {
            db.endTransaction();
        }
    }

    //Pegar produtoid nome e fk
    ModelProduct pegarDadosProduto(ModelProduct produto) {
        String query = "select * from " + TB_PRODUTOS + " where " + KEY_PRODUTO_NOME + " = '" + produto.getProdutoNome() +
                "' and " + KEY_PRODUTO_USUARIO_ID_FK + " = " + produto.getIdUsuario();

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        Cursor cursor = db.rawQuery(query, null);
        ModelProduct produto1 = new ModelProduct();

        try {
            if (cursor != null && cursor.moveToFirst()) {
                produto1.setIdProduto(cursor.getString(0));
                produto1.setProdutoNome(cursor.getString(1));
                produto1.setIdUsuario(produto.getIdUsuario());
                db.setTransactionSuccessful();
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return produto1;
    }

    //Listar produtos da lista
    List<ModelProduct> listarProduto(String idLista, String idUsuario) {
        List<ModelProduct> listaProduto = new ArrayList<>();

        String query = "select " + KEY_PRODUTO_ID + "," + KEY_PRODUTO_NOME + "," + KEY_PRODUTO_VALOR + " from " + TB_PRODUTOS + " p join "
                + TB_LISTADECOMPRA_HAS_PRODUTO + " lh on p." + KEY_PRODUTO_ID + "= lh." + produto_idproduto +
                " where " + KEY_PRODUTO_USUARIO_ID_FK + " = " + idUsuario +
                " AND " + listadecompra_idlistadecompra + " = " + idLista + " ORDER BY " + KEY_PRODUTO_NOME + " ASC ";

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ModelProduct produtos = new ModelProduct();
                    produtos.setIdProduto(cursor.getString(0));
                    produtos.setProdutoNome(cursor.getString(1));
                    produtos.setProdutoValor(Double.valueOf(cursor.getString(2)));
                    listaProduto.add(produtos);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert cursor != null;
        cursor.close();
        db.endTransaction();

        return listaProduto;
    }

    // Update lista no db
    int updateList(ModelPurchaseList lista, String nome) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "select * from " + TB_LISTA_DE_COMPRAS + " where " + KEY_LISTA_DE_COMPRAS_NOME +
                " = '" + lista.getNomeLista() + "' AND " + KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK + " = " +
                lista.getIdUsuario();

        db.beginTransaction();

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (!(cursor != null && cursor.moveToFirst())) {
                Log.e(TAG, "Lista não existe");
                assert cursor != null;
                cursor.close();
                return 0;
            } else {
                query = "select * from " + TB_LISTA_DE_COMPRAS + " where " + KEY_LISTA_DE_COMPRAS_NOME +
                        " = '" + lista.getNomeLista() + "' AND " + KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK + " = " +
                        lista.getIdUsuario();
                Cursor cursorx = db.rawQuery(query, null);

                String idlista = "a";
                String idusuario = "a";

                if (cursorx.getCount() > 0) {
                    cursorx.moveToFirst();
                    idlista = cursorx.getString(0);
                    idusuario = cursorx.getString(2);
                    cursorx.close();
                }

                db.execSQL("UPDATE " + TB_LISTA_DE_COMPRAS + " SET " + KEY_LISTA_DE_COMPRAS_NOME + " = '" + nome +
                        "' WHERE " + KEY_LISTA_DE_COMPRAS_ID +
                        " = " + idlista + " AND " + KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK + " = " + idusuario);

                Log.d(TAG, "Lista atualizada");
                db.setTransactionSuccessful();
                cursor.close();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao atualizar lista");
            return 2;
        } finally {
            db.endTransaction();
        }
    }

    //Insert tabela compra
    void addCompra(ModelPurchase compra) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        String sql = "SELECT MAX(" + KEY_COMPRA_ID + ")+1 FROM " + TB_COMPRAS;
        db.beginTransaction();
        Cursor cursor = db.rawQuery(sql, null);
        String max = "1";
        try {
            if (cursor.moveToFirst() && (!cursor.getString(0).equals(null))) {
                max = cursor.getString(0);
                cursor.close();
            } else
                max = "1";
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        values.put(KEY_COMPRA_NOME, "Compra " + max + " dia: " + compra.getData());
        values.put(KEY_COMPRA_VALOR, compra.getValor());
        values.put(KEY_COMPRA_DATA, compra.getData());
        values.put(KEY_COMPRA_USUARIO_ID_FK, compra.getIdUsuario());


        try {
            db.insert(TB_COMPRAS, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //Listar compras
    List<ModelPurchase> listarCompras(String idUsuario) {
        List<ModelPurchase> listaCompra = new ArrayList<>();

        String query = "select " + KEY_COMPRA_ID + "," + KEY_COMPRA_NOME + "," + KEY_COMPRA_VALOR + " from " + TB_COMPRAS +
                " where " + KEY_COMPRA_USUARIO_ID_FK + " = " + idUsuario + " ORDER BY " + KEY_COMPRA_NOME + " ASC ";

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ModelPurchase compra = new ModelPurchase();
                    compra.setIdCompra(cursor.getString(0));
                    compra.setNomeCompra(cursor.getString(1));
                    compra.setValor(Double.valueOf(cursor.getString(2)));
                    listaCompra.add(compra);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert cursor != null;
        cursor.close();
        db.endTransaction();

        return listaCompra;
    }

}