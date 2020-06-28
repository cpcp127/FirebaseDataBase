package com.example.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button join;
    private Button login;
    private Button find_btn;
    private EditText email_login;
    private EditText pwd_login;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        join = (Button) findViewById(R.id.main_join_btn);
        login = (Button) findViewById(R.id.main_login_btn);
        find_btn=(Button)findViewById(R.id.find_btn);

        email_login = (EditText) findViewById(R.id.main_email);
        pwd_login = (EditText) findViewById(R.id.main_pwd);
        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        if(firebaseUser !=null){
            Intent intent=new Intent(MainActivity.this,SuccessActivity.class);
            startActivity(intent);
            finish();
        }//자동로그인

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = email_login.getText().toString().trim();
                final String pwd = pwd_login.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "로그인 오류", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("비밀번호 재설정");
                ad.setMessage("비밀번호 재설정할 이메일을 적어주세요");

                final EditText et=new EditText(MainActivity.this);
                ad.setView(et);

                ad.setPositiveButton("보내기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String email_pwd=et.getText().toString();
                        FirebaseAuth auth=FirebaseAuth.getInstance();
                        auth.sendPasswordResetEmail(email_pwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"이메일 전송완료",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                ad.show();
            }
        });
    }
}