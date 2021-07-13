package OpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.tree.MainActivity;
import com.example.tree.globales;

public class SQLite_OpenHelper extends SQLiteOpenHelper {

    public SQLite_OpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String usuarios="CREATE TABLE usuarios (id_i INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre_t TEXT," +
                "usuario_t TEXT," +
                "contrasena_t TEXT," +
                "perfil_i INTEGER DEFAULT 1," +
                "activo_i INTEGER DEFAULT 0)";
        db.execSQL(usuarios);

        for(int i = 1; i <= 10; i++)
        {
            String insUsu="INSERT INTO usuarios (nombre_t, usuario_t, contrasena_t) " +
                    "VALUES ('Usuario "+i+"', 'usuario"+i+"', '1234' )";
            db.execSQL(insUsu);
        }

        String zona="create table zona(id_i integer primary key autoincrement , " +
                "lote_i integer, " +
                "track_t integer, " +
                "area_t integer, " +
                "arboles_i integer, " +
                "usuario_id_i integer)";
        db.execSQL(zona);

        String arbol="create table arbol(id_i integer primary key autoincrement , " +
                "zona_id_i integer, " +
                "usuario_id_i integer, " +
                "arbol_i integer, " +
                "altura_i integer, " +
                "diametro_i integer, " +
                "calidad_i integer)";
        db.execSQL(arbol);
    }

    public Cursor datosExportar() throws SQLException {
        Cursor mcursor;
        mcursor=getReadableDatabase().rawQuery("select * from arbol left join zona on zona.id_i=arbol.zona_id_i",null);

        return mcursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void abrirBD(){
        this.getWritableDatabase();
    }

    public void cerrarBD() {
        this.close();
    }

    public void insertaUsuariosBD(String nombre, String usuario, String contrasena, String perfil){
        ContentValues val=new ContentValues();
        val.put("nombre_t", nombre);
        val.put("usuario_t", usuario);
        val.put("contrasena_t", contrasena);
        val.put("perfil_i", perfil);
        this.getWritableDatabase().insert("usuarios", null, val);
    }

    public void insertaZonaBD(String lote, Integer track, Integer area, Integer arboles, Integer usuario){
        ContentValues val=new ContentValues();
        val.put("lote_i", lote);
        val.put("track_t", track);
        val.put("area_t", area);
        val.put("arboles_i", arboles);
        val.put("usuario_id_i", usuario);

        Long lonUltimaZona=this.getWritableDatabase().insert("zona", null, val);
        globales.intZonaId_g=lonUltimaZona;
    }

    public void insertaArbolBD(Integer arbol, Integer alto, Integer diametro, Integer calidad){
        ContentValues val=new ContentValues();
        val.put("arbol_i", arbol);
        val.put("altura_i", alto);
        val.put("diametro_i", diametro);
        val.put("calidad_i", calidad);
        val.put("zona_id_i", globales.intZonaId_g);
        val.put("usuario_id_i", globales.intUsuarioId_g);

        this.getWritableDatabase().insert("arbol", null, val);

    }

    public String buscaCredenciales(String usu, String pass) throws SQLException {
        String resp="no";
        Cursor mcursor;
        mcursor=getReadableDatabase().query("usuarios", new String[]{"id_i", "nombre_t", "usuario_t"},
                "usuario_t='"+usu+"' and contrasena_t='"+pass+"'", null,null,null,null);
        if (mcursor.getCount()>0){
            ContentValues valores = new ContentValues();
            valores.put("activo_i",1);
            getWritableDatabase().update("usuarios", valores, "usuario_t='"+usu+"' and contrasena_t='"+pass+"'", null);
            resp="ok";
        }
        return resp;
    }

    public String buscaSesion() throws SQLException {
        String resp="no";
        Cursor mcursor;
        mcursor=getReadableDatabase().query("usuarios", new String[]{"id_i", "nombre_t", "usuario_t"},
                "activo_i=1", null,null,null,null);
        if (mcursor.getCount()>0){
            resp="ok";
        }
        return resp;
    }

}