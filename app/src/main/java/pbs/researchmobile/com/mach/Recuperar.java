package pbs.researchmobile.com.mach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.anshul.libray.PasswordEditText;

public class Recuperar extends AppCompatActivity {

    private static final String TAG = "Match";
    private FirebaseAuth mAuth;
    private TextInputEditText etUsername;
    private TextInputLayout error1;
    private Button iniciar;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);
        mAuth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(this);

        etUsername = (TextInputEditText)findViewById(R.id.us);
        error1 = (TextInputLayout)findViewById(R.id.email);
        iniciar = (Button)findViewById(R.id.enviar);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarDatos();
            }
        });

    }

    private void enviarDatos() {
        final String username = etUsername.getText().toString();

        if (username.isEmpty()) {
            error1.setError("Campo Obligatorio");
        }else {
            if(!emailValidator(username)){
                error1.setError("Ingrese un Email Valido");
            }else {
                progress.setTitle("Recuperar");
                progress.setMessage("Enviando Correo...");
                progress.show();
                error1.setError(null);
                mAuth.sendPasswordResetEmail(username)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(),"Enviamos un correo electrónico a "+
                                            username+" con un enlace para que recuperes el acceso a tu cuenta",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }


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
    public void onBackPressed() {
        startActivity(new Intent(Recuperar.this,Mach_l.class));
        finish();
    }
}
