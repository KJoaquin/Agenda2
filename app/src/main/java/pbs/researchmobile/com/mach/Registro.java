package pbs.researchmobile.com.mach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pbs.researchmobile.com.mach.users.add.AddUserContract;
import pbs.researchmobile.com.mach.users.add.AddUserPresenter;

public class Registro extends AppCompatActivity implements AddUserContract.View {

    private TextView entrada;
    private View viewl;
    private TextInputEditText nombre,user1,password1,cpassword1;
    private TextInputLayout a0,a1,a2,a3;
    private FirebaseAuth firebaseAuth;
    private AddUserPresenter mAddUserPresenter;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        firebaseAuth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(this);
        mAddUserPresenter = new AddUserPresenter(this);

        nombre     =(TextInputEditText)findViewById(R.id.nom);
        user1      =(TextInputEditText)findViewById(R.id.us1);
        password1  =(TextInputEditText)findViewById(R.id.pas1);
        cpassword1 =(TextInputEditText)findViewById(R.id.conf_pas1);
        a0 = (TextInputLayout)findViewById(R.id.nombre);
        a1 = (TextInputLayout)findViewById(R.id.usuario1);
        a2 = (TextInputLayout)findViewById(R.id.password1);
        a3 = (TextInputLayout)findViewById(R.id.passwordR);
        entrada = (TextView)findViewById(R.id.inicio1);

        LayoutInflater layoutInflater = getLayoutInflater();
        viewl = layoutInflater.inflate(R.layout.menss,(ViewGroup)findViewById(R.id.layout_m));

        Button iniciar = (Button) findViewById(R.id.entrarx);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();

            }
        });

    }

    private void registrarUsuario() {
        final String unombre = nombre.getText().toString();
        String username = user1.getText().toString();
        String password_t = password1.getText().toString();
        String password_c = cpassword1.getText().toString();
        int ap1 = 0,ap2 = 0,ap3 = 0,ap4 = 0,ap0=0;

        if (unombre.isEmpty()) {
            a0.setError("Campo Obligatorio");
            ap0 = 0;
        }else {
            a0.setError(null);
            ap0 = 1;
        }if (username.isEmpty()) {
            a1.setError("Campo Obligatorio");
            ap1 = 0;
        }else {
            if(!emailValidator(username)){
                a1.setError("Ingrese un Email Valido");
                ap1 = 0;
            }else {
                a1.setError(null);
                ap1 = 1;
            }
        }if(password_t.isEmpty()){
            a2.setError("Campo Obligatorio");
            ap2 = 0;
        }else{
            ap2 = 1;
            a2.setError(null);
        }if(password_c.isEmpty()){
            a3.setError("Campo Obligatorio");
            ap3 = 0;
        }else {
            a3.setError(null);
            ap3 = 1;
        }if ( ap2 == 1){
            if (password_t.equals(password_c)){
                ap4 = 1;
                a3.setError(null);
            }else {
                ap4 = 0;
                a3.setError("La contraseña no coniside");
            }
        }

        if (ap1 == 1 && ap3 == 1 && ap4 == 1 && ap0 == 1 ){
            progress.setTitle("Registrando");
            progress.setMessage("Realizando registro...");
            progress.setCancelable(false);
            progress.show();
            firebaseAuth.createUserWithEmailAndPassword(username, password_c).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //checking if success
                            if(task.isSuccessful()){
                                Toast toast1 = Toast.makeText(getApplicationContext(),"Toast:Gravity.TOP",Toast.LENGTH_SHORT);
                                toast1.setGravity(Gravity.CENTER,0,0);
                                toast1.setView(viewl);
                                toast1.show();

                                mAddUserPresenter.addUser(getApplicationContext(), task.getResult().getUser(),unombre);

                                Intent intent = new Intent(Registro.this, Mach_l.class);
                                Registro.this.startActivity(intent);
                                Registro.this.finish();

                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                                builder.setMessage(task.getException().getMessage())
                                        .setNegativeButton("Reintentar", null)
                                        .create()
                                        .show();
                            }
                            progress.dismiss();
                        }
                    });
        }
    }

    public void dato(View view) {
        Intent aa = new Intent(this,Mach_l.class);
        startActivity(aa);
        finish();

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
        startActivity(new Intent(Registro.this,Mach_l.class));
        finish();
    }

    @Override
    public void onAddUserSuccess(String message) {
        progress.dismiss();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAddUserFailure(String message) {
        progress.dismiss();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }
}
