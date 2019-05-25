package com.example.listadecompras;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostsDatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "db_listadecompras";
    private static final int DATABASE_VERSION = 1;
    // Tabelas
    private static final String TB_LISTA_DE_COMPRAS = "listaDeCompras";
    private static final String TB_USUARIOS = "usuarios";
    private static final String TB_PRODUTOS = "produtos";
    // Lista de compras colunas
    private static final String KEY_LISTA_DE_COMPRAS_ID = "idLista";
    private static final String KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK = "usuarioId";
    private static final String KEY_LISTA_DE_COMPRAS_NOME = "nomeLista";
    private static final String KEY_LISTA_DE_COMPRAS_PRODUTO_NOME = "nomeProduto";
    private static final String KEY_LISTA_DE_COMPRAS_PRODUTO_ID_FK = "produtoId";
    // Usuario colunas
    private static final String KEY_USUARIO_ID = "idUsuario";
    private static final String KEY_USUARIO_NOME = "nomeUsuario";
    private static final String KEY_USUARIO_EMAIL = "email";
    private static final String KEY_USUARIO_SENHA = "senha";
    // Produto colunas
    private static final String KEY_PRODUTO_ID = "idProduto";
    private static final String KEY_PRODUTO_NOME = "nomeProduto";
    private static PostsDatabaseHelper sInstance;


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private PostsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Padrao singleton
    public static synchronized PostsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new PostsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USUARIOS_TABLE = "CREATE TABLE " + TB_USUARIOS +
                "(" +
                KEY_USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Define a primary key
                KEY_USUARIO_NOME + " VARCHAR(50) NOT NULL, " +
                KEY_USUARIO_EMAIL + " VARCHAR(50) NOT NULL, " +
                KEY_USUARIO_SENHA + " VARCHAR(20) NOT NULL" +
                ")";


        String CREATE_PRODUTOS_TABLE = "CREATE TABLE " + TB_LISTA_DE_COMPRAS +
                "(" +
                KEY_PRODUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_PRODUTO_NOME + " VARCHAR (50) NOT NULL" +
                ")";

        String CREATE_LISTA_DE_COMPRAS_TABLE = "CREATE TABLE " + TB_LISTA_DE_COMPRAS +
                "(" +
                KEY_LISTA_DE_COMPRAS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_LISTA_DE_COMPRAS_USUARIO_ID_FK + " INTEGER REFERENCES KEY_USUARIO_ID, " +
                KEY_LISTA_DE_COMPRAS_NOME + " VARCHAR(50) NOT NULL UNIQUE ON CONFLICT FAIL, " +
                KEY_LISTA_DE_COMPRAS_PRODUTO_ID_FK + " INTEGER REFERENCES KEY_PRODUTOS_ID" +
                ")";

        db.execSQL(CREATE_USUARIOS_TABLE);
        db.execSQL(CREATE_PRODUTOS_TABLE);
        db.execSQL(CREATE_LISTA_DE_COMPRAS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TB_USUARIOS);
            db.execSQL("DROP TABLE IF EXISTS " + TB_PRODUTOS);
            db.execSQL("DROP TABLE IF EXISTS " + TB_LISTA_DE_COMPRAS);
            onCreate(db);
        }
    }
}