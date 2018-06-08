package pbs.researchmobile.com.mach;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import pbs.researchmobile.com.mach.record.AdminSQLiteOpenHelper;
import pbs.researchmobile.com.mach.record.MyAlarmReceiver;
import pbs.researchmobile.com.mach.record.vars;

public class Tab2Fragment extends Fragment {

    private EditText t4, t3,t5,t6,t7;
    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase bd;
    private ContentValues registro;
    private EditText tvDisplayDate;
    private DatePicker dpResult;
    private Button btnChangeDate;
    // date
    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;
    // hora
    private int minute;
    private int hour;
    private TimePicker timePicker;
    private TextView textViewTime;
    private Button button;
    private static final int TIME_DIALOG_ID = 998;
    Calendar calendario = Calendar.getInstance();
    int hora, min,dia,mes,ano,hora11;
    String cadenaF, cadenaH,fecha_sistema,hora_sistema;
    private EditText decripcion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);

        admin = new AdminSQLiteOpenHelper(getContext(), vars.bd, null, vars.version);
        bd = admin.getWritableDatabase();
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH)+1;
        ano = calendario.get(Calendar.YEAR);
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        min = calendario.get(Calendar.MINUTE);
        fecha_sistema=mes+"-"+dia+"-"+ano+" ";
        hora_sistema = hora+":"+min;
        setCurrentDateOnView(view);
        addListenerOnButton(view);
        // hora
        setCurrentTimeOnView(view);
        t3 = (EditText) view.findViewById(R.id.editText21);
        t4 = (EditText) view.findViewById(R.id.editText22);
        t5= (EditText) view.findViewById(R.id.eddescrip);
        t6= (EditText) view.findViewById(R.id.editText23);
        t7= (EditText) view.findViewById(R.id.editText24);
        servicio();

        Button guardar = (Button) view.findViewById(R.id.button30);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviar();
            }
        });

        t4.setMovementMethod(new ScrollingMovementMethod());

        return view;
    }

    public void servicio() {
        Intent intent = new Intent(getContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(getContext(), MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis(); //first run of alarm is immediate // aranca la palicacion
        int intervalMillis = 100; //3 segundos
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);
    }

    public void enviar() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(), vars.bd, null, vars.version);
        SQLiteDatabase bd = admin.getReadableDatabase();
        bd = admin.getWritableDatabase();
        registro = new ContentValues();
        registro.put("encabezado", t3.getText().toString());
        registro.put("mensaje", t4.getText().toString());
        registro.put("descripcion",t5.getText().toString());
        registro.put("fecha", t6.getText().toString());
        registro.put("hora", t7.getText().toString());
        bd.insert("alarma", null, registro);//nombre de la tabla
        bd.close();
        t3.setText("");
        t4.setText("");
        t5.setText("");
        t6.setText("");
        t7.setText("");
        Toast.makeText(getActivity(), "alarma registrada", Toast.LENGTH_LONG).show();
    }

    public void setCurrentTimeOnView(View view) {

        textViewTime = (EditText) view.findViewById(R.id.editText24);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

    }

    public void addListenerOnButton(View view) {

        EditText Dia = (EditText) view.findViewById(R.id.editText23);
        Dia.setInputType(InputType.TYPE_NULL);
        tvDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Time currDate = new Time(Time.getCurrentTimezone());
                currDate.setToNow();
                DatePickerDialog d = new DatePickerDialog(getActivity(), datePickerListener, currDate.year,  currDate.month, currDate.monthDay);
                d.show();
                DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
                        {
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Do as you need
                            }
                        };
            }

        });

        EditText hora = (EditText) view.findViewById(R.id.editText24);
        hora.setInputType(InputType.TYPE_NULL);
        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    TimePickerDialog time = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            hour = selectedHour;
                            minute = selectedMinute;
                            textViewTime.setText(new StringBuilder().append(hour).append(":").append(padding_str(minute)));
                        }
                    },hour,minute,false);
                    time.show();
            }

        });

    }

    public void setCurrentDateOnView(View view) {

        tvDisplayDate = (EditText) view.findViewById(R.id.editText23);
        dpResult = (DatePicker) view.findViewById(R.id.datePicker);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));

        // set current date into datepicker
        dpResult.init(year, month, day, null);

    }

    private static String padding_str(int c) {

        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            tvDisplayDate.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

            dpResult.init(year, month, day, null);

        }
    };

}
