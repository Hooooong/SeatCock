package sku.jvj.seatcock.Adapter;

import android.app.Activity;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

import sku.jvj.seatcock.Util.GlobalApplication;

/**
 * Created by Android Hong on 2016-06-13.
 */
public class KakaoSDKAdapter extends KakaoAdapter {

    /**
     * Session Config에 대해서는 default값들이 존재한다.
     * 필요한 상황에서만 override해서 사용하면 됨.
     * @return Session의 설정값.
     */

    @Override
    public ISessionConfig getSessionConfig() {
        return new ISessionConfig() {
            // 로그인시 인증받을 타입을 지정한다. 지정하지 않을 시 가능한 모든 옵션이 지정된다.
            @Override
            public AuthType[] getAuthTypes() {
                return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
            }

            // SDK 로그인시 사용되는 WebView에서 pause와 resume시에 Timer를 설정하여 CPU소모를 절약한다.
            // true 를 리턴할경우 webview로그인을 사용하는 화면서 모든 webview에 onPause와 onResume 시에 Timer를 설정해 주어야 한다. 지정하지 않을 시 false로 설정된다.
            @Override
            public boolean isUsingWebviewTimer() {
                return false;
            }

            // 일반 사용자가 아닌 Kakao와 제휴된 앱에서 사용되는 값으로,
            // 값을 채워주지 않을경우 ApprovalType.INDIVIDUAL 값을 사용하게 된다
            @Override
            public ApprovalType getApprovalType() {
                return ApprovalType.INDIVIDUAL;
            }

            // Kakao SDK 에서 사용되는 WebView에서 email 입력폼에서 data를 save할지여부를 결정한다.
            // Default true.
            @Override
            public boolean isSaveFormData() {
                return true;
            }
        };
    }

    @Override
    public IApplicationConfig getApplicationConfig() {
        return new IApplicationConfig() {
            @Override
            public Activity getTopActivity() {
                return GlobalApplication.getCurrentActivity();
            }

            @Override
            public Context getApplicationContext() {
                return GlobalApplication.getGlobalApplicationContext();
            }
        };
    }
}
