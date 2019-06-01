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
    private static final String DATABASE_NAME = "abc";
    private static final int DATABASE_VERSION = 1;
    // Tabelas
    private static final String TB_LISTA_DE_COMPRAS = "listadecompras";
    private static final String TB_USUARIOS = "usuarios";
    private static final String TB_PRODUTOS = "produtos";
    private static final String TB_LISTADECOMPRA_HAS_PRODUTO = "listadecompra_has_produto";
    // Lista de compras colunas
    private static final String KEY_LISTA_DE_COMPRAS_ID = "idLista";
    private static final String KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK = "usuarioId";
    private static final String KEY_LISTA_DE_COMPRAS_NOME = "nomeLista";
    private static final String KEY_LISTA_DE_COMPRAS_PRODUTO_NOME = "nomeProduto";
    private static final String KEY_LISTA_DE_COMPRAS_PRODUTO_ID_FK = "produtoId";
    // ModelUsuario colunas
    private static final String KEY_USUARIO_ID = "usuarioid";
    private static final String KEY_USUARIO_NOME = "nomeUsuario";
    private static final String KEY_USUARIO_EMAIL = "email";
    private static final String KEY_USUARIO_SENHA = "senha";
    // ModelProduto colunas
    private static final String KEY_PRODUTO_ID = "produtoid";
    private static final String KEY_PRODUTO_NOME = "nomeProduto";
    private static final String KEY_PRODUTO_USUARIO_ID_FK = "usuarioId";


    private static final String TAG = "DataBAse";
    private static DatabaseHelper sInstance;
    // Lista de compra has produto colunas
    private static final String listadecompra_idlistadecompra = "listadecompra_idlistadecompra";
    private static final String produto_idproduto = "produto_idproduto";

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

        String index1 = " CREATE INDEX " + TB_LISTA_DE_COMPRAS + "."
                + "fk_listadecompra_usuario_idx" + " ON " +
                TB_LISTA_DE_COMPRAS + "(" + KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK + ")";

        String index2 = " CREATE INDEX " + TB_LISTADECOMPRA_HAS_PRODUTO + "."
                + "fk_listadecompra_has_produto_produto1_idx" + " ON " +
                TB_LISTADECOMPRA_HAS_PRODUTO + "(" + produto_idproduto + ")";
        String index3 = " CREATE INDEX " + TB_LISTADECOMPRA_HAS_PRODUTO + "."
                + "fk_listadecompra_has_produto_listadecompra1_idx" + " ON " +
                TB_LISTADECOMPRA_HAS_PRODUTO + "(" + listadecompra_idlistadecompra + ")";


        db.execSQL(usuario);
        db.execSQL(listadecompra);
        db.execSQL(produto);
        db.execSQL(hasproduto);
        // db.execSQL(index1);
        // db.execSQL(index2);
        // db.execSQL(index3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_LISTADECOMPRA_HAS_PRODUTO);
            db.execSQL("DROP TABLE IF EXISTS " + TB_PRODUTOS);
            db.execSQL("DROP TABLE IF EXISTS " + TB_LISTA_DE_COMPRAS);
            db.execSQL("DROP TABLE IF EXISTS " + TB_USUARIOS);
            onCreate(db);
        }
    }

    // Insert usuario no db
    public void addUsuario(ModelUsuario usuario) {
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
    public boolean consultaEmail(ModelUsuario usuario) {
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
    public boolean validarLogin(ModelUsuario usuario) {
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
    public String pegarId(ModelUsuario usuario) {
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
    public String pegarNome(ModelUsuario usuario) {
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
    public int addListaDeCompras(ModelListaDeCompras lista) {
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

    //Listar listas de compras
    public List<ModelListaDeCompras> listarListas(String id) {
        List<ModelListaDeCompras> listaListas = new ArrayList<ModelListaDeCompras>();

        String query = "SELECT * FROM " + TB_LISTA_DE_COMPRAS + " WHERE " + KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK +
                " = " + id;

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ModelListaDeCompras lista = new ModelListaDeCompras();
                lista.setIdLista(cursor.getString(0));
                lista.setNomeLista(cursor.getString(1));
                lista.setIdUsuario(cursor.getString(2));

                listaListas.add(lista);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.endTransaction();

        return listaListas;

    }

    // Insert produtos no db
    public int addProduto(ModelProduto produto, ModelListaDeCompras lista) {
        SQLiteDatabase db = getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(KEY_PRODUTO_NOME, produto.getProdutoNome());
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
                // String sql = "INSERT INTO " + TB_PRODUTOS + " VALUES (null,'" +
                // produto.getProdutoNome() + "'," + produto.getIdUsuario()+")";
                // db.execSQL(sql);
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

    //Listar produtos da lista
    public List<ModelProduto> listarProduto(String idLista, String idUsuario) {
        List<ModelProduto> listaProduto = new ArrayList<ModelProduto>();

        String query = "select " + KEY_PRODUTO_ID + "," + KEY_PRODUTO_NOME + " from " + TB_PRODUTOS + " p join "
                + TB_LISTADECOMPRA_HAS_PRODUTO + " lh on p." + KEY_PRODUTO_ID + "= lh." + produto_idproduto +
                " where " + KEY_PRODUTO_USUARIO_ID_FK + " = " + idUsuario;

        //String query = "SELECT DISTINCT lt." + KEY_LISTA_DE_COMPRAS_ID + " FROM " + TB_LISTA_DE_COMPRAS + " lt " +
        //      "JOIN " + TB_LISTADECOMPRA_HAS_PRODUTO + " hp" + " ON lt." + KEY_LISTA_DE_COMPRAS_ID + " = " +
        //    "hp." + produto_idproduto + " JOIN " + TB_PRODUTOS + " pd" + " ON pd." + KEY_PRODUTO_ID +
        //  " = hp." + listadecompra_idlistadecompra + " WHERE " + KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK + " LIKE ?" + idLista;

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ModelProduto produtos = new ModelProduto();
                    produtos.setIdProduto(cursor.getString(0));
                    produtos.setProdutoNome(cursor.getString(1));
                    listaProduto.add(produtos);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cursor.close();
        db.endTransaction();

        return listaProduto;

    }
}