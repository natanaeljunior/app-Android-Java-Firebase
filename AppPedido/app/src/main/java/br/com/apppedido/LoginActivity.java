package br.com.apppedido;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.apppedido.Model.Usuario;

public class LoginActivity extends AppCompatActivity {

    ImageButton btnEntrar;
    EditText editUsuario, editSenha;
    ProgressBar progressBarLogin;

    private Usuario user;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verificarUsuarioLogado();
        inicializarComponentes();

        progressBarLogin.setVisibility(View.GONE);
        btnEntrar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (!editUsuario.getText().toString().isEmpty()) {
                    if (!editSenha.getText().toString().isEmpty()) {
                        user = new Usuario(editUsuario.getText().toString(), editSenha.getText().toString());
                        validarlogin(user);

                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Preencha a senha!",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Preencha o Email!: ",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void validarlogin(Usuario user) {
        progressBarLogin.setVisibility(View.VISIBLE);

        autenticacao = FirebaseAuth.getInstance();
        autenticacao.signInWithEmailAndPassword(
                user.getUser(),
                user.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBarLogin.setVisibility(View.GONE);
                    startActivity(new Intent(   getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Erro ao fazer Login!!",Toast.LENGTH_SHORT).show();
                    progressBarLogin.setVisibility(View.GONE);
                }
            }
        });
    }

    public void verificarUsuarioLogado(){
        autenticacao =  FirebaseAuth.getInstance();
        if(autenticacao.getCurrentUser() != null){
            startActivity(new Intent(   getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    public void inicializarComponentes() {

        editUsuario = findViewById(R.id.editUsuario);
        editSenha = findViewById(R.id.editSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        progressBarLogin = findViewById(R.id.progressBarLogin);

        editUsuario.requestFocus();

    }

}
