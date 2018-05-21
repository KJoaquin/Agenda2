package pbs.researchmobile.com.mach.record;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import pbs.researchmobile.com.mach.Paint;
import pbs.researchmobile.com.mach.R;

public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    private NotificationManager notificationManager;
    private final int NOTIFICATION_ID = 1010;
    private AdminSQLiteOpenHelper admin;
    private Cursor fila;
    private SQLiteDatabase bd;
    private String alarma,descripcion,titulo,mensaje;
    private long id;


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyTestService.class);
        context.startService(i);
        Calendar calendario = Calendar.getInstance();
        int hora, min,dia,mes,ano;
        String cadenaF, cadenaH,fecha_sistema,hora_sistema;

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH)+1;
        ano = calendario.get(Calendar.YEAR);
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        min = calendario.get(Calendar.MINUTE);
        fecha_sistema=mes+"-"+dia+"-"+ano+" ";
        hora_sistema=hora+":"+min;
        admin = new AdminSQLiteOpenHelper(context, vars.bd, null, vars.version);
        bd = admin.getWritableDatabase();

        if(bd!=null) {
            fila = bd.rawQuery("SELECT * FROM alarma WHERE fecha='"+fecha_sistema+"' AND hora= '"+hora_sistema+"'", null);
            if(fila.moveToFirst()){
                alarma=fila.getString(0);
                titulo=fila.getString(1);
                descripcion =fila.getString(2);
                mensaje =fila.getString(3);
                //triggerNotification(context,titulo,descripcion,mensaje);
                notificacion(context,alarma,titulo,descripcion,mensaje);
            }
        }
        bd.close();
    }

    private void notificacion(Context context, String alarma, String titulo, String mensaje, String descripcion) {

        Bitmap icono1 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher);

        Intent notificationIntent = new Intent(context, Paint.class);
        notificationIntent.putExtra("NOT",2);
        notificationIntent.putExtra("ID",alarma);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = new long[]{2000, 1000, 2000};

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(descripcion);
        bigText.setBigContentTitle(mensaje);
        bigText.setSummaryText(titulo);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context);
        mBuilder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setLargeIcon(icono1)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setSound(defaultSound)
                .setStyle(bigText);

        NotificationManager mNotificationMAnager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationMAnager.notify(0, mBuilder.build());


    }

    private void triggerNotification(Context contexto, String titulo, String mensje, String descripcion) {
        Intent notificationIntent = new Intent(contexto, Paint.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(contexto, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = new long[]{2000, 1000, 2000};

        NotificationCompat.Builder builder = new NotificationCompat.Builder(contexto);
        builder.setContentIntent(contentIntent)

                .setTicker("")
                .setContentTitle("alarma")
                .setContentTitle("")
                .setContentText(titulo)
                .setContentInfo("Info")
                .setContentIntent(contentIntent)
                .setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(), R.drawable.ic_messaging))
                .setSmallIcon(R.drawable.ic_messaging)
                .setAutoCancel(true) //Cuando se pulsa la notificación ésta desaparece
                .setSound(defaultSound)
                .setVibrate(pattern);

        Notification notificacion = new NotificationCompat.BigTextStyle(builder)
                .bigText(titulo)
                .setBigContentTitle(titulo)
                .setSummaryText(titulo)
                .build();

        notificationManager = (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificacion);
    }

}
