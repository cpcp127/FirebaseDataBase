package com.example.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private EditText email_join;
    private EditText pwd_join;
    private EditText age_join,name_join;
    private EditText pwd_ck;
    private Button btn;
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        email_join = (EditText) findViewById(R.id.sign_up_email);
        pwd_join = (EditText) findViewById(R.id.sign_up_pwd);
        pwd_ck=(EditText)findViewById(R.id.sign_up_pwd_ck);
        age_join=(EditText)findViewById(R.id.sign_up_age);
        name_join=(EditText)findViewById(R.id.sign_up_name);
        btn = (Button) findViewById(R.id.sign_up_btn);


        firebaseAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final String email = email_join.getText().toString().trim();
                    final String pwd = pwd_join.getText().toString().trim();
                    final String pwdck=pwd_ck.getText().toString().trim();
                    final String age = age_join.getText().toString().trim();
                    final String event="";
                    final String name = name_join.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(Main2Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {


                                    if (task.isSuccessful()) {
                                        if(pwd.equals(pwdck)){
                                        Map<String, Object> taskMap = new HashMap<String, Object>();

                                        taskMap.put("회원정보",new UserData(email,age,name,event));
                                        UserData userData=new UserData(email,age,name,event);
                                        databaseReference.child("회원정보").child(email.replace(".",",")).setValue(userData);
                                        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        }else{
                                            Toast.makeText(Main2Activity.this, "비번 에러", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(Main2Activity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
            }
        });


    }

}