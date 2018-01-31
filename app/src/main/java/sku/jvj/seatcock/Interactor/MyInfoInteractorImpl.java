package sku.jvj.seatcock.Interactor;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.kakaotalk.KakaoTalkService;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sku.jvj.seatcock.Interface.MyInfo.MyInfoInteractor;
import sku.jvj.seatcock.Interface.MyInfo.MyInfoPresenter;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.StoreReview;
import sku.jvj.seatcock.Server.MyReviewData;

/**
 * Created by Android Hong on 2016-10-27.
 */

public class MyInfoInteractorImpl implements MyInfoInteractor {


    private MyInfoPresenter myInfoPresenter;
    private MyReviewData myReviewData;
    private MyInfo myInfo;
    private KakaoUser kakaoUser = KakaoUser.getInstance();

    public MyInfoInteractorImpl(MyInfoPresenter myInfoPresenter) {
        this.myInfoPresenter = myInfoPresenter;
        myInfo = new MyInfo();
        myReviewData = new MyReviewData(myInfo);
    }

    @Override
    public void getMyReviewData() {
        myReviewData.getData(kakaoUser.getId());
    }

    @Override
    public ArrayList<StoreReview> setMyReviewData() {
        return myReviewData.setData();
    }

    @Override
    public StoreReview setDetailReviewData() {
        return null;
    }

    @Override
    public KakaoUser getMyInfo() {
        Log.e("kakaoUser",kakaoUser.getProfile_image());
      return kakaoUser;
    }

    @Override
    public void getReviewDetailData(int reviewNum) {
    }

    @Override
    public void deleteMyReviewData(int reviewNum) {
        myReviewData.deleteToReview(reviewNum);
    }

    @Override
    public void refreshMyProfile() {
        KakaoTalkService.requestProfile(new TalkResponseCallback<KakaoTalkProfile>() {
            @Override
            public void onNotKakaoTalkUser() {

            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(KakaoTalkProfile talkProfile) {
                String profileUrl;
                String thumbnailUrl;
                if (talkProfile.getProfileImageUrl().isEmpty() || talkProfile.getProfileImageUrl() == null) {
                    profileUrl = "noimage";
                    thumbnailUrl = "noimage";
                } else {
                    profileUrl = talkProfile.getProfileImageUrl();
                    thumbnailUrl = talkProfile.getThumbnailUrl();
                }
                changeProfileImageUrl(profileUrl, thumbnailUrl);
            }
        });
    }

    /**
     * 프로필 변경 메소드
     * @param profileImageURL
     */
    public void changeProfileImageUrl(final String profileImageURL, final String thumbnailURL){
        final Map<String, String> properties = new HashMap<>();

        properties.put("profile_image",profileImageURL);
        properties.put("thumbnail_image",thumbnailURL);

        UserManagement.requestUpdateProfile(new ApiResponseCallback<Long>() {
            @Override
            public void onSuccess(Long userId) {
                UserProfile userProfile = UserProfile.loadFromCache();

                // 카카오 서버 프로필 변경
                userProfile.updateUserProfile(properties);

                if (userProfile != null) {
                    userProfile.saveUserToCache();
                }

                kakaoUser.setProfile_image(profileImageURL);

                // DB 프로필 변경
                myReviewData.profileUpdateData(kakaoUser.getId(),profileImageURL);
            }

            @Override
            public void onNotSignedUp() {
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

        },properties);

    }

    /**
     * 핸들러 클래스
     */
    public class MyInfo extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MYREVIEW_NETWORK_SUCCESS:
                    myInfoPresenter.onSuccess("reviewData");
                    break;
                case MYREVIEW_NETWORK_FAILER:
                    myInfoPresenter.failOfTimeOut("reviewData");
                    break;
                case MYREVIEWDEL_NETWORK_SUCCESS:
                    myInfoPresenter.onSuccess("reviewDeleteData");
                    break;
                case MYREVIEWDEL_NETWORK_FAILER:
                    myInfoPresenter.failOfTimeOut("reviewDeleteData");
                    break;
                case PROFILE_NETWORK_SUCCESS:
                    myInfoPresenter.onSuccess("profileUpdate");
                    break;
                case PROFILE_NETWORK_FAILER:
                    myInfoPresenter.failOfTimeOut("profileUpdate");
                    break;
            }
        }
    }




}
