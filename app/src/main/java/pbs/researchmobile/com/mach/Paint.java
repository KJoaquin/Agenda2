package pbs.researchmobile.com.mach;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import pbs.researchmobile.com.mach.record.AdminSQLiteOpenHelper;
import pbs.researchmobile.com.mach.record.MyAlarmReceiver;
import pbs.researchmobile.com.mach.record.vars;

public class Paint extends AppCompatActivity {

    private static final String TAG = "Paint";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private Session session;
    private ImageView cerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager_user_listing);
        setupViewPager(mViewPager);
        servicio();
        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }

        Bundle bunde = getIntent().getExtras();
        if (bunde!=null && bunde.containsKey("NOT")){
            mViewPager.setCurrentItem(1);
        AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(getApplicationContext(), vars.bd, null, vars.version);
        String mensaj2e =db.eliminar(Integer.parseInt(bunde.getString("ID")));
        Toast.makeText(getApplicationContext(),"Recordatorio Finalizado",Toast.LENGTH_SHORT).show();
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        cerrar = (ImageView)toolbar.findViewById(R.id.img_usuario);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        TextView bc = (TextView)toolbar.findViewById(R.id.txt_buscar);
        bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirBusca();
            }
        });
        ImageView imgbc = (ImageView)toolbar.findViewById(R.id.img_buscar);
        imgbc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirBusca();
            }
        });



    }

    private void abrirBusca() {
        startActivity(new Intent(Paint.this,BusacarList.class));
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new UsersFragment(), "MENSAJES");
        adapter.addFragment(new Tab2Fragment(),  "NOTAS");
        adapter.addFragment(new Tab3Fragment(),  "AGENDA");
        viewPager.setAdapter(adapter);
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        session.setLoggedin(false);
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Paint.this,Mach_l.class));
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    public void servicio() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis(); //first run of alarm is immediate // aranca la palicacion
        int intervalMillis = 100; //3 segundos
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);
    }
}
