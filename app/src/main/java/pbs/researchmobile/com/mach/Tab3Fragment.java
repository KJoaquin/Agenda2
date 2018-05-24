package pbs.researchmobile.com.mach;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;

import pbs.researchmobile.com.mach.record.AdminSQLiteOpenHelper;
import pbs.researchmobile.com.mach.record.vars;
import pbs.researchmobile.com.mach.users.ItemClickSupport;

public class Tab3Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    ArrayList<String> lista;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_fragment,container,false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mListView = (ListView) view.findViewById(R.id.lista);
        cargaCompleta();

        return view;
    }

    private void cargaCompleta() {
        AdminSQLiteOpenHelper andminSQL;
        andminSQL = new AdminSQLiteOpenHelper(getActivity(), vars.bd, null, vars.version);
        SQLiteDatabase db = andminSQL.getWritableDatabase();
        Cursor cursor = andminSQL.listarpersonas();

        TodoCursor todoAdapter = new TodoCursor(getActivity(), cursor);
        mListView.setAdapter(todoAdapter);
        accionSwipe();

    }

    private void accionSwipe() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                ((SwipeLayout)(mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);
                final SwipeLayout swipeLayout = (SwipeLayout)view.findViewById(R.id.swipe);
                swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                    @Override
                    public void onOpen(SwipeLayout layout) {
                        YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                    }
                });
                swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                    @Override
                    public void onDoubleClick(SwipeLayout layout, boolean surface) {
                        swipeLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeLayout.close();
                            }
                        }, 50);
                    }
                });
                view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(getActivity(), vars.bd, null, vars.version);
                        String mensaje =db.eliminar(id);
                        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
                        cargaCompleta();
                    }
                });

            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        cargaCompleta();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }, 1500);
    }

}
