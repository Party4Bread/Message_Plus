package com.example.david0926.messageplus.Auth;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.david0926.messageplus.Chat.RecycleModel_ChatPage;
import com.example.david0926.messageplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    //회원가입 기능을 수행하는 Activity

    //EditText, ProgressBar, Button 선언
    EditText id, password, passwordcheck, nickname, intro;
    ProgressBar regiProgress;
    Button regiBtn;

    //Firebase Authentication, Database 가져오기
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //StatusBar 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //전체화면
        setContentView(R.layout.activity_register);

        //EditText findViewById
        id = findViewById(R.id.reg_id);
        nickname = findViewById(R.id.reg_nickname);
        intro = findViewById(R.id.reg_intro);
        password = findViewById(R.id.reg_password);
        passwordcheck = findViewById(R.id.reg_passwordcheck);

        firebaseAuth = FirebaseAuth.getInstance();// FireBase 현재 Auth 정보 가져오기

        regiBtn = findViewById(R.id.btn_register); //Button findViewById
        regiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //회원가입 버튼이 클릭되었을 때

                if (!nickname.getText().toString().equals("") && !intro.getText().toString().equals("")
                        && !id.getText().toString().equals("") && !password.getText().toString().equals("")
                        && !passwordcheck.getText().toString().equals("")) { //모든 항목이 기입되었을 경우
                    createAccount(id.getText().toString(), password.getText().toString(), passwordcheck.getText().toString(), nickname.getText().toString(), intro.getText().toString()); //기입한 정보로 회원가입 진행
                }
                else { //채워지지 않은 항목이 있을 경우
                    Toast.makeText(getApplicationContext(), "빈칸을 채워주세요", Toast.LENGTH_SHORT).show(); //빈칸 기입 요청 토스트
                }
            }
        });
    }

    private boolean isValidPasswd(String target) { //패스워드 유효성 검사 함수
        Pattern p = Pattern.compile("(^.*(?=.{8,50})(?=.*[0-9])(?=.*[a-z]).*$)"); //패스워드 검사 정규식. 8~50자, 알파벳+숫자
        Matcher m = p.matcher(target); //정규식 대입 검사
        return m.find() && !target.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"); //정규식 검사값이 옳고 한글이 포함되지 않았다면 true 반환
    }

    private boolean isValidEmail(String target) { //Email 유효성 검사 함수

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches(); //Email 유효성 검사 결과 반환
    }

    private void createAccount(final String email, final String password, final String passwordcheck, final String nickname, final String intro) { //회원가입 함수

        //ProgressBar findViewById
        regiProgress = findViewById(R.id.progress_regi);

        regiProgress.setVisibility(View.VISIBLE); //ProgressBar 표시

        if (!isValidEmail(email)) { //이메일 유효성 검사에 실패할 경우
            Toast.makeText(getApplicationContext(), "이메일이 유효하지 않습니다", Toast.LENGTH_SHORT).show(); //이메일 유효성검사 실패 토스트
            regiProgress.setVisibility(View.INVISIBLE); //ProgressBar 숨기기
            return;
        }

        if (!isValidPasswd(password)) { //패스워드 유효성 검사에 실패할 경우
            Toast.makeText(getApplicationContext(), "패스워드가 유효하지 않습니다", Toast.LENGTH_SHORT).show(); //패스워드 유효성 검사 실패 토스트
            regiProgress.setVisibility(View.INVISIBLE); //ProgressBar 숨기기
            return;
        }

        if (!password.equals(passwordcheck)) { //패스워드와 패스워드 재입력이 일치하지 않는 경우
            Toast.makeText(getApplicationContext(), "패스워드가 일치하지 않습니다", Toast.LENGTH_SHORT).show(); //재입력 불일치 토스트
            regiProgress.setVisibility(View.INVISIBLE); //ProgressBar 숨기기
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password) //Firebase에 회원가입 요청
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { //작업 완료 리스너
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //회원가입에 성공했다면
                            FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 유저 정보 가져오기
                            String[] profileColorList = getApplicationContext().getResources().getStringArray(R.array.colors); //colors 목록에서 프로필 색상 리스트 가져오기
                            int profileColor = Color.parseColor(profileColorList[new Random().nextInt(profileColorList.length)]); //가져온 리스트에서 랜덤 색상 선택
                            UserModel model = new UserModel(nickname, email, user.getUid(), "#" + intro, profileColor); //UserModel 양식에 회원가입 정보 추가
                            databaseReference.child("user").push().setValue(model); //작성한 model 양식을 Firebase DB에 등록
                            Toast.makeText(getApplicationContext(), "환영합니다, " + id.getText().toString() + "!", Toast.LENGTH_SHORT).show(); //회원가입 성공 토스트
                            finish();
                        }
                        else { //회원가입에 실패했다면
                            Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show(); //화원가입 실패 토스트
                            regiProgress.setVisibility(View.INVISIBLE); //ProgressBar 숨기기
                        }
                    }
                });


    }

}
