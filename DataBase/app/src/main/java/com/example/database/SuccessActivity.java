package com.example.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.common.api.Response;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class SuccessActivity extends AppCompatActivity {
    Toolbar myToolbar;

    private CalendarView calendarView;
    private Button cha_Btn,del_Btn,save_Btn;
    private TextView diaryTextView,textView2,textView3;
    private EditText contextEditText;
    private DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
    private DatabaseReference monstafa;
    private FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
    private String userId=user.getEmail().replace(".",",");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        calendarView=findViewById(R.id.calendarView);
        diaryTextView=findViewById(R.id.diaryTextView);
        save_Btn=findViewById(R.id.save_Btn);
        del_Btn=findViewById(R.id.del_Btn);
        cha_Btn=findViewById(R.id.cha_Btn);
        textView2=findViewById(R.id.textView2);
        textView3=findViewById(R.id.textView3);

        contextEditText=findViewById(R.id.contextEditText);
        //현재 로그인 사용자 이름 불러오기
        monstafa=ref.child("회원정보").child(userId).child("name");
       //경로의 전체 내용을 읽고 변경사항을 수신 대기하는 메서드
        monstafa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.getValue(String.class);
                textView3.setText(name+"님의 달력일기장");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //달력 날짜 클릭시 이벤트 처리
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                diaryTextView.setText(String.format("%d / %d / %d",year,month+1,dayOfMonth));
                 final String eveDate;
                eveDate=String.format("%d,%d,%d",year,month+1,dayOfMonth);

                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);

                cha_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        contextEditText.setVisibility(View.VISIBLE);
                        textView2.setVisibility(View.INVISIBLE);
                        save_Btn.setVisibility(View.VISIBLE);
                        cha_Btn.setVisibility(View.INVISIBLE);
                        del_Btn.setVisibility(View.INVISIBLE);
                        textView2.setText(contextEditText.getText());
                        monstafa=ref.child("회원정보").child(userId).child("event").child(eveDate);
                        monstafa.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String setText=dataSnapshot.getValue(String.class);
                                contextEditText.setText(setText);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                });
                del_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textView2.setVisibility(View.INVISIBLE);
                        contextEditText.setText("");
                        ref.child("회원정보").child(userId).child("event").child(eveDate).removeValue();
                        contextEditText.setVisibility(View.VISIBLE);
                        save_Btn.setVisibility(View.VISIBLE);
                        cha_Btn.setVisibility(View.INVISIBLE);
                        del_Btn.setVisibility(View.INVISIBLE);

                    }
                });
                save_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String userEvent=contextEditText.getText().toString();
                        ref.child("회원정보").child(userId).child("event").child(eveDate).setValue(userEvent);

                        save_Btn.setVisibility(View.INVISIBLE);
                        cha_Btn.setVisibility(View.VISIBLE);
                        del_Btn.setVisibility(View.VISIBLE);
                        contextEditText.setVisibility(View.INVISIBLE);
                        textView2.setVisibility(View.VISIBLE);

                    }
                });
                monstafa=ref.child("회원정보").child(userId).child("event").child(eveDate);
                monstafa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String eventt=dataSnapshot.getValue(String.class);
                        textView2.setText(eventt);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수

    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings2:
                FirebaseAuth.getInstance().signOut();//로그아웃
                finish();
                //finishAffinity(); //어플리케이션을 종료
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_LONG).show();
                return true;

            default:

                long now=System.currentTimeMillis();//현재 시간 가져오기
                Date mDate = new Date(now);//date 형식으로 고친다
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy,M,dd");//가져오고 싶은 형태로 날짜 포멧
                String getTime=simpleDateFormat.format(mDate);
                Toast.makeText(getApplicationContext(), getTime+"에할 일", Toast.LENGTH_LONG).show();
                monstafa=ref.child("회원정보").child(userId).child("event").child(getTime);
                monstafa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String today=dataSnapshot.getValue(String.class);
                        final AlertDialog.Builder builder=new AlertDialog.Builder(SuccessActivity.this);
                        builder.setTitle("오늘할일");
                        builder.setMessage(today);
                        builder.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return super.onOptionsItemSelected(item);
        }



        }
    }

