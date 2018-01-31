package sku.jvj.seatcock.Interface.Store.Review;

import java.util.ArrayList;

/**
 * Created by Android Hong on 2016-10-24.
 */

public interface ReviewPresenter {
    void getReviewData(String storeId);
    void getReviewDetailData(int reviewNum);
    void deleteReview(int reviewNum);

    void showDialog(int reviewNum);

    void navigateToPicture(ArrayList<String> reviewPicture, int tag);
    String getKakaoId();

    void onSuccess(String reviewStatus);
    void failOfTimeOut(String reviewStatus);



}
