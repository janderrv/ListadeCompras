package com.example.listadecompras;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "db_listadecompras";
    private static final int DATABASE_VERSION = 1;
    // Tabelas
    private static final String TB_LISTA_DE_COMPRAS = "listaDeCompras";
    private static final String TB_USUARIOS = "usuarios";
    private static final String TB_PRODUTOS = "produtos";
    private static final String TB_LISTADECOMPRA_HAS_PRODUTO = "listadecompra_has_produto";
    // Lista de compras colunas
    private static final String KEY_LISTA_DE_COMPRAS_ID = "idLista";
    private static final String KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK = "usuarioId";
    private static final String KEY_LISTA_DE_COMPRAS_NOME = "nomeLista";
    private static final String KEY_LISTA_DE_COMPRAS_PRODUTO_NOME = "nomeProduto";
    private static final String KEY_LISTA_DE_COMPRAS_PRODUTO_ID_FK = "produtoId";
    // Usuario colunas
    private static final String KEY_USUARIO_ID = "usuarioid";
    private static final String KEY_USUARIO_NOME = "nomeUsuario";
    private static final String KEY_USUARIO_EMAIL = "email";
    private static final String KEY_USUARIO_SENHA = "senha";
    // Produto colunas
    private static final String KEY_PRODUTO_ID = "produtoid";
    private static final String KEY_PRODUTO_NOME = "nomeProduto";
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


        String produto = "CREATE TABLE " + TB_PRODUTOS +
                "(" +
                KEY_PRODUTO_ID + " INTEGER PRIMARY KEY NOT NULL," +
                KEY_PRODUTO_NOME + " VARCHAR(45) NOT NULL" + ")";

        String usuario = "CREATE TABLE " + TB_USUARIOS +
                "(" +
                KEY_USUARIO_ID + " INTEGER PRIMARY KEY NOT NULL," +
                KEY_USUARIO_NOME + " VARCHAR(45) NOT NULL," +
                KEY_USUARIO_EMAIL + " VARCHAR(45) NOT NULL," +
                KEY_USUARIO_SENHA + " VARCHAR(45) NOT NULL," +
                " CONSTRAINT email_UNIQUE " +
                " UNIQUE (" + KEY_USUARIO_EMAIL + ") " + ")";

        String listadecompra = "CREATE TABLE " + TB_LISTA_DE_COMPRAS +
                "(" +
                KEY_LISTA_DE_COMPRAS_ID + " INTEGER PRIMARY KEY NOT NULL," +
                KEY_LISTA_DE_COMPRAS_NOME + " VARCHAR(45) NOT NULL," +
                KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK + " INTEGER NOT NULL," +
                " CONSTRAINT nome_UNIQUE " +
                " UNIQUE(" + KEY_LISTA_DE_COMPRAS_NOME + ")," +
                " CONSTRAINT fk_listadecompra_usuario " +
                " FOREIGN KEY(" + KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK + ")" +
                " REFERENCES " + TB_USUARIOS + "(" + KEY_USUARIO_ID + ")" + ")";


        String hasproduto = "CREATE TABLE " + TB_LISTADECOMPRA_HAS_PRODUTO +
                "(" +
                listadecompra_idlistadecompra + " INTEGER NOT NULL," +
                produto_idproduto + " INTEGER NOT NULL," +
                " PRIMARY KEY(" + listadecompra_idlistadecompra + "," + produto_idproduto + ")," +
                " CONSTRAINT fk_listadecompra_has_produto_listadecompra1" +
                " FOREIGN KEY(" + listadecompra_idlistadecompra + ")" +
                " REFERENCES " + TB_LISTA_DE_COMPRAS + "(" + KEY_LISTA_DE_COMPRAS_ID + ")," +
                " CONSTRAINT fk_listadecompra_has_produto_produto1" +
                " FOREIGN KEY(" + produto_idproduto + ")" +
                " REFERENCES " + TB_PRODUTOS + "(" + KEY_PRODUTO_ID + ")" + ")";

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
        db.execSQL(produto);
        db.execSQL(listadecompra);
        db.execSQL(hasproduto);
        // db.execSQL(index1);
        // db.execSQL(index2);
        // db.execSQL(index3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_USUARIOS);
            db.execSQL("DROP TABLE IF EXISTS " + TB_PRODUTOS);
            db.execSQL("DROP TABLE IF EXISTS " + TB_LISTA_DE_COMPRAS);
            db.execSQL("DROP TABLE IF EXISTS " + TB_LISTADECOMPRA_HAS_PRODUTO);
            onCreate(db);
        }
    }

    // Insert usuario no db
    public void addUsuario(Usuario usuario) {
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
    public boolean consultaEmail(Usuario usuario) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        Cursor cursor = db.rawQuery("select * from " + TB_USUARIOS + " where " + KEY_USUARIO_EMAIL +
                " like ?", new String[]{usuario.getEmail()});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    // Validar login
    public boolean validarLogin(Usuario usuario) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();


        Cursor cursor = db.rawQuery("select * from " + TB_USUARIOS + " where " + KEY_USUARIO_EMAIL +
                " like ?" + " AND " + KEY_USUARIO_SENHA + " like ?", new String[]{usuario.getEmail(), usuario.getSenha()});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    //Pegar id
    public String pegarId(Usuario usuario) {
        SQLiteDatabase db = getWritableDatabase();
        String id = "null";

        db.beginTransaction();

        Cursor cursor = db.rawQuery("select * from " + TB_USUARIOS + " where " + KEY_USUARIO_EMAIL +
                " like ?" + " AND " + KEY_USUARIO_SENHA + " like ?", new String[]{usuario.getEmail(), usuario.getSenha()});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getString(0);
            cursor.close();
            return id;
        }
        return id;
    }

    //Pegar nome
    public String pegarNome(Usuario usuario) {
        SQLiteDatabase db = getWritableDatabase();
        String nome = "null";

        db.beginTransaction();

        Cursor cursor = db.rawQuery("select * from " + TB_USUARIOS + " where " + KEY_USUARIO_EMAIL +
                " like ?", new String[]{usuario.getEmail()});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            nome = cursor.getString(1);
            cursor.close();
            return nome;
        }
        return nome;
    }

    // Insert lista de compras no db
    public void addListaDeCompras(Usuario usuario, ListaDeCompras lista) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK, usuario.getId());
        values.put(KEY_USUARIO_EMAIL, usuario.getEmail());
        values.put(KEY_USUARIO_SENHA, usuario.getSenha());

        try {
            Cursor cursor = db.rawQuery("select * from " + TB_USUARIOS + " where " + KEY_USUARIO_EMAIL +
                    " like ?", new String[]{usuario.getEmail()});
            if (cursor.moveToFirst()) {
                Log.e(TAG, "Email já existe");
            } else {
                db.insertOrThrow(TB_USUARIOS, null, values);
                db.setTransactionSuccessful();
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao adicionar usuário");
        } finally {
            db.endTransaction();
        }
    }
}