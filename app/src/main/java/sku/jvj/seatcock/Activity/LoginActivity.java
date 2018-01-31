package sku.jvj.seatcock.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import sku.jvj.seatcock.R;

/**
 * Created by Android Hong on 2016-06-14.
 */
public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SessionCallback callback;

    /**
     * 로그인 버튼을 클릭 했을시 access token을 요청하도록 설정한다.
     *
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.loginToolbar);
        toolbar.setTitle("로그인");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        callback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 뒤로가기 버튼 눌렀을 시
     */
    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    /**
     * 기기 버튼(뒤로가기) 눌렀을 시
     */
    @Override
    public void onBackPressed() {
        this.finish();
    }

    // 로그인 activity를 이용하여 sdk에서 필요로 하는 activity를 띄우기 때문에
    // 해당 결과를 세션에도 넘겨줘서 처리할 수 있도록 Session#handleActivityResult를 호출해 줍니다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 세션의 상태가 변경될 때 불리는 세션 콜백을 삭제합니다.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        // access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태.
        // 일반적으로 로그인 후의 다음 activity로 이동한다.
        @Override
        public void onSessionOpened() {
            Log.i("Login : onSessionOpen ", "세션연결완료");
            requestSave();
        }

        // memory와 cache에 session 정보가 전혀 없는 상태.
        // 일반적으로 로그인 버튼이 보이고 사용자가 클릭시 동의를 받아 access token 요청을 시도한다
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.i("세션연결실패", "세션연결실패");
            if (exception != null) {
                Logger.e(exception);
            } else {
                Logger.e(exception);
                setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
            }                                            // 로그인화면을 다시 불러옴
        }
    }

    /**
     * 사용자의 상태를 알아보고 저장하기 위해 me API 호출을 한다.
     */
    protected void requestSave() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
                Log.i("String", errorResult.toString());

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onNotSignedUp() {
                redirectSignupActivity();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                redirectMainActivity();
            }
        });
    }

    /**
     * 세션 연결 시
     * 회원이 아닐 경우 회원가입 화면으로 넘어가기 위한 메소드
     */
    protected void redirectSignupActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * 세션 연결 시
     * 회원일 경우 정보 저장 후 메인화면으로 넘어가기 위한 메소드
     */
    protected void redirectMainActivity() {
        Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
