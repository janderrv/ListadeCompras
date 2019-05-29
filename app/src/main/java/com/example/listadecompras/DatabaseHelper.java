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
    private static final String TAG = "DataBAse";
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
        String CREATE_USUARIOS_TABLE = "CREATE TABLE " + TB_USUARIOS +
                "(" +
                KEY_USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_USUARIO_NOME + " VARCHAR(50) NOT NULL, " +
                KEY_USUARIO_EMAIL + " VARCHAR(50) NOT NULL, " +
                KEY_USUARIO_SENHA + " VARCHAR(20) NOT NULL" +
                ")";


        String CREATE_PRODUTOS_TABLE = "CREATE TABLE " + TB_PRODUTOS +
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_USUARIOS);
            db.execSQL("DROP TABLE IF EXISTS " + TB_PRODUTOS);
            db.execSQL("DROP TABLE IF EXISTS " + TB_LISTA_DE_COMPRAS);
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
}