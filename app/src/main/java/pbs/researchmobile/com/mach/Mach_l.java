package pbs.researchmobile.com.mach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.anshul.libray.PasswordEditText;

public class Mach_l extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Match";
    private FirebaseAuth mAuth;
    private Button iniciar;
    private EditText etUsername;
    private PasswordEditText etPassword;
    private Session session;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mach_l);
        session = new Session(this);
        mAuth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(this);

        etUsername = (EditText)findViewById(R.id.us);
        etPassword = (PasswordEditText)findViewById(R.id.pas);

        iniciar = (Button)findViewById(R.id.entrar);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSecion();
            }
        });

        LinearLayout registrar = (LinearLayout)findViewById(R.id.registro);
        registrar.setOnClickListener(this);

        LinearLayout recuperar = (LinearLayout)findViewById(R.id.re_contraseña);
        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperar();
            }
        });

        if(session.loggedin()){
            startActivity(new Intent(Mach_l.this,Paint.class));
            finish();
        }

    }

    private void iniciarSecion() {
        String usuraio = etUsername.getText().toString();
        String contraseña = etPassword.getText().toString();
        if (!usuraio.isEmpty() && !contraseña.isEmpty()){
            progress.setTitle("Iniciar");
            progress.setMessage("Iniciando Sesión...");
            progress.setCancelable(false);
            progress.show();
            LoginUsuario();
        }else {
            Toast.makeText(Mach_l.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
        }

    }

    private void recuperar() {
        startActivity(new Intent(Mach_l.this,Recuperar.class));
        finish();
    }

    private void LoginUsuario() {
        String email = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "inicioCorrectamente:Completo:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "inicioCorrectamente", task.getException());
                            Toast.makeText(Mach_l.this, "Autenticación fallida.", Toast.LENGTH_SHORT).show();
                        }else {
                            session.setLoggedin(true);
                            startActivity(new Intent(Mach_l.this,Paint.class));
                            finish();
                        }
                        progress.dismiss();
                    }
                });

    }

    public boolean emailValidator(String email){
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();


    }

    @Override
    public void onClick(View view) {
        Intent z = new Intent(getApplicationContext(),Registro.class);
        startActivity(z);
        this.finish();
    }

}
