package pbs.researchmobile.com.mach.record;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {


    public AdminSQLiteOpenHelper(Context context, String nombre, CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table alarma( _id INTEGER PRIMARY KEY AUTOINCREMENT,encabezado text, mensaje text, descripcion text,fecha date, hora time)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        db.execSQL("drop table if exists alarma" );
        db.execSQL(" create table alarma(  _id INTEGER PRIMARY KEY AUTOINCREMENT,encabezado text, mensaje text,fecha date, hora time)");

    }

    public Cursor listarpersonas(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM alarma";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    public String eliminar(long id){
        String mensaje ="";
        SQLiteDatabase database = this.getWritableDatabase();
        int cantidad =database.delete("alarma", "_id='" + id + "'", null);
        if (cantidad !=0){
            mensaje="Eliminado Correctamente";

        }
        else{
            mensaje = "No existe";
        }
        database.close();
        return mensaje;
    }

}