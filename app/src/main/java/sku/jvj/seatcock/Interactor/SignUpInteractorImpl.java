package sku.jvj.seatcock.Interactor;


import android.os.Handler;
import android.os.Message;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.HashMap;
import java.util.Map;

import sku.jvj.seatcock.Interface.SignUp.SignUpInteractor;
import sku.jvj.seatcock.Interface.SignUp.SignUpPresenter;
import sku.jvj.seatcock.Server.UserData;

/**
 * Created by Android Hong on 2016-10-26.
 */

public class SignUpInteractorImpl implements SignUpInteractor {

    private SignUpPresenter signUpPresenter;
    private SignUpHandler signUpHandler;
    private UserData userData;

    public SignUpInteractorImpl(SignUpPresenter signUpPresenter) {
        this.signUpPresenter = signUpPresenter;
        signUpHandler = new SignUpHandler();
        userData = new UserData(signUpHandler);

    }

    @Override
    public void signUpUser(final String name,final String jubun,final String phoneNumber) {
        final Map<String, String> properties = new HashMap<String, String>();

        properties.put("name", name);
        properties.put("jubun", jubun);
        properties.put("phoneNumber", phoneNumber);

        UserManagement.requestSignup(new ApiResponseCallback<Long>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                signUpPresenter.failOfTimeOut();
            }

            @Override
            public void onNotSignedUp() {
                signUpPresenter.failOfTimeOut();
            }

            @Override
            public void onSuccess(Long result) {
                String userid = Long.toString(result);
                setProfilePath(userid,name,jubun,phoneNumber);
            }
        }, properties);
    }

    /**
     * 프로필 경로 가지고오기
     */
    private void setProfilePath(final String userid, final String name,final String jubun,final String phoneNumber) {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                String userProfilePath = null;
                if (userProfile.getProfileImagePath().isEmpty() || userProfile.getProfileImagePath() == null) {
                    // 이미지 존재하지 않음.
                     userProfilePath = "noimage";
                } else {
                    // 이미지 존재
                    userProfilePath = userProfile.getProfileImagePath();
                }
                userData.insertToUser(userid,name,jubun,phoneNumber,userProfilePath);
            }
        });
    }

    /**
     * 핸들러 클래스
     */
    public class SignUpHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case USER_NETWORK_SUCCESS:
                    signUpPresenter.onSuccess();
                    break;
                case USER_NETWORK_FAILER:
                    signUpPresenter.failOfTimeOut();
                    break;
            }
        }
    }
}
