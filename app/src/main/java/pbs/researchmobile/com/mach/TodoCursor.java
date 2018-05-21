package pbs.researchmobile.com.mach;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

public class TodoCursor extends CursorAdapter {

    public TodoCursor(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView id = (TextView) view.findViewById(R.id.position);
        TextView txtTitulo = (TextView) view.findViewById(R.id.titulo);
        TextView txtMensaje = (TextView) view.findViewById(R.id.mensaje);
        TextView txtDatos = (TextView) view.findViewById(R.id.text_data);

        //String id2 = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        String titulo = cursor.getString(cursor.getColumnIndexOrThrow("encabezado"));
        String mensaje = cursor.getString(cursor.getColumnIndexOrThrow("mensaje"));
        String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));

        //id.setText(id2);
        txtTitulo.setText(titulo);
        txtMensaje.setText(mensaje);
        txtDatos.setText(descripcion);


    }



}