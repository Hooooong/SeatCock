package sku.jvj.seatcock.Interface.MyInfo;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.StoreReview;

/**
 * Created by Android Hong on 2016-10-27.
 */

public interface MyInfoInteractor {

    int MYREVIEW_NETWORK_SUCCESS = 1;
    int MYREVIEW_NETWORK_FAILER = 2;

    int MYREVIEWDEL_NETWORK_SUCCESS = 3;
    int MYREVIEWDEL_NETWORK_FAILER = 4;

    int PROFILE_NETWORK_SUCCESS = 5;
    int PROFILE_NETWORK_FAILER = 6;

    void getMyReviewData();
    ArrayList<StoreReview> setMyReviewData();
    StoreReview setDetailReviewData();
    KakaoUser getMyInfo();

    void getReviewDetailData(int reviewNum);
    void deleteMyReviewData(int reviewNum);

    void refreshMyProfile();



}
