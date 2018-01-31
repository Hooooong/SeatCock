package sku.jvj.seatcock.Interface.MyInfo;

import java.util.ArrayList;

/**
 * Created by Android Hong on 2016-10-27.
 */

public interface MyInfoPresenter {


    void getMyReviewData();
    void getMyInfo();


    void refreshMyProfile();

    void getReviewDetailData(int reviewNum);
    void deleteMyReview(int reviewNum);

    void showDialog(int reviewNum);

    void onSuccess(String reviewStatus);
    void failOfTimeOut(String reviewStatus);

    void navigateToPicture(ArrayList<String> reviewPicture, int i);



}
