package sku.jvj.seatcock.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.Session;

import sku.jvj.seatcock.Interface.SignUp.SignUpPresenter;
import sku.jvj.seatcock.Interface.SignUp.SignUpView;
import sku.jvj.seatcock.Presenter.SignUpPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

/**
 * Created by Android Hong on 2016-08-20.
 */

public class SignUpActivity extends AppCompatActivity implements SignUpView,TextView.OnEditorActionListener,View.OnClickListener,AdapterView.OnItemSelectedListener {

    public static Context mContext;

    private Toolbar toolbar;
    private TextView userJoinTextView;
    private EditText userNameEditText, userPhoneNumberMedileEditText, userPhoneNumberBackEditText, userJubunFrontEditText, userJubunBackEditText;
    private CheckBox checkbox1, checkbox2;
    private Spinner userPhoneNumberFrontSpinner;
    private String userPhoneNumberFront;
    private String name;
    private String jubun;
    private String phoneNumber;
    private ProgressDialog progressDialog;
    private SignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpPresenter = new SignUpPresenterImpl(this);

        initLayout();
    }

    /**
     * 레이아웃 초기화 및 리스너
     */
    public void initLayout() {
        mContext = this;

        toolbar = (Toolbar) findViewById(R.id.loginToolbar);
        toolbar.setTitle("회원 가입");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.phonenumber, android.R.layout.simple_spinner_dropdown_item);
        userPhoneNumberFrontSpinner = (Spinner) findViewById(R.id.userPhoneNumberFrontSpinner);
        userPhoneNumberFrontSpinner.setAdapter(adapter);

        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        userPhoneNumberMedileEditText = (EditText) findViewById(R.id.userPhoneNumberMedileEditText);
        userPhoneNumberBackEditText = (EditText) findViewById(R.id.userPhoneNumberBackEditText);
        userJubunFrontEditText = (EditText) findViewById(R.id.userJubunFrontEditText);
        userJubunBackEditText = (EditText) findViewById(R.id.userJubunBackEditText);
        userJoinTextView = (TextView) findViewById(R.id.userJoinTextView);

        userNameEditText.setOnEditorActionListener(this);
        userPhoneNumberMedileEditText.setOnEditorActionListener(this);
        userPhoneNumberBackEditText.setOnEditorActionListener(this);
        userJubunFrontEditText.setOnEditorActionListener(this);
        userJubunBackEditText.setOnEditorActionListener(this);
        userJoinTextView.setOnClickListener(this);
        userPhoneNumberFrontSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Appbar 메뉴 생성 초기화
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    /**
     * Appbar 메뉴 선택 이벤트
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //메뉴 추가
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_back) {
            showBackDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 뒤로가기 버튼 눌렀을 시
     */
    @Override
    public boolean onSupportNavigateUp() {
        showBackDialog();
        return true;
    }

    /**
     * 기기 버튼(뒤로가기) 눌렀을 시
     */
    @Override
    public void onBackPressed() {
        showBackDialog();
    }

    /**
     * 뒤로가기 다이얼로그
     */
    private void showBackDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입");
        builder.setMessage("회원가입 창을 나가시겠습니까? \n입력하신 모든 정보는 저장되지 않습니다.");

        String positiveText = "OK";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // 세션 로그아웃
                if (Session.getCurrentSession().isOpened()) {
                    Session.getCurrentSession().close();
                }
                finish();

                Log.e("Sign up 화면 전환", "sign up 화면 전환");
            }
        });

        String negativeText = "Cancel";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        // 세션 로그아웃
    }

    /**
     * 회원가입 다이얼로그
     */
    private void showRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입");
        builder.setMessage("회원가입을 하시겠습니까? \n입력하신 정보는 일부 수정할 수 없습니다.");

        String positiveText = "OK";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name =userNameEditText.getText().toString();
                jubun = userJubunFrontEditText.getText().toString()+userJubunBackEditText.getText().toString();
                phoneNumber = userPhoneNumberFront + "-" + userPhoneNumberMedileEditText.getText().toString() + "-" + userPhoneNumberBackEditText.getText().toString();

                setSignUp(name,jubun,phoneNumber);
                Log.e("회원가입 완료", "회원가입 완료");
            }
        });

        String negativeText = "Cancel";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        // 세션 로그아웃
    }

    /**
     * 포커스 이동
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.userNameEditText && actionId == EditorInfo.IME_ACTION_GO) {
            userNameEditText.setNextFocusDownId(R.id.userPhoneNumberFrontSpinner);
        } else if (v.getId() == R.id.userPhoneNumberMedileEditText && actionId == EditorInfo.IME_ACTION_GO) {
            userPhoneNumberMedileEditText.setNextFocusDownId(R.id.userPhoneNumberBackEditText);
        } else if (v.getId() == R.id.userPhoneNumberBackEditText && actionId == EditorInfo.IME_ACTION_GO) {
            userPhoneNumberBackEditText.setNextFocusDownId(R.id.userJubunFrontEditText);
        } else if (v.getId() == R.id.userJubunFrontEditText && actionId == EditorInfo.IME_ACTION_GO) {
            userJubunFrontEditText.setNextFocusDownId(R.id.userJubunBackEditText);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId){
            case R.id.userJoinTextView:
                if(userNameEditText.getText().toString().trim().length() == 0 ){
                    Toast.makeText(SignUpActivity.this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    userNameEditText.requestFocus();
                }else if(userPhoneNumberMedileEditText.getText().toString().trim().length() == 0 ||  userPhoneNumberMedileEditText.getText().toString().trim().length() < 2){
                    Toast.makeText(SignUpActivity.this, "전화번호를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    userPhoneNumberMedileEditText.requestFocus();
                }else if(userPhoneNumberBackEditText.getText().toString().trim().length() == 0  ||  userPhoneNumberMedileEditText.getText().toString().trim().length() < 4){
                    Toast.makeText(SignUpActivity.this, "전화번호를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    userPhoneNumberBackEditText.requestFocus();
                }else if(userJubunFrontEditText.getText().toString().trim().length() == 0 ){
                    Toast.makeText(SignUpActivity.this, "생년월일을 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    userJubunFrontEditText.requestFocus();
                }else if(userJubunBackEditText.getText().toString().trim().length() == 0){
                    Toast.makeText(SignUpActivity.this, "성별(1 or 2)을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    userJubunBackEditText.requestFocus();
                }else{
                    this.showRequestDialog();
                }
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userPhoneNumberFront = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void showProgress() {
        progressDialog = Util.createProgressDialog(this);
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSignUp(String name, String jubun, String phoneNumber) {
       signUpPresenter.setSignUp(name,jubun,phoneNumber);
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
