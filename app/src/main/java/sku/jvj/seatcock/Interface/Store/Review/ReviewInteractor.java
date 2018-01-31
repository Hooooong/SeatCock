package sku.jvj.seatcock.Interface.Store.Review;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.StoreReview;


/**
 * Created by Android Hong on 2016-10-24.
 */

public interface ReviewInteractor {
    int REVIEW_NETWORK_SUCCESS = 1;
    int REVIEW_NETWORK_FAILER = 2;

    int REVIEW_DETAIL_NETWORK_SUCCESS = 3;
    int REVIEW_DETAIL_NETWORK_FAILER = 4;

    int REVIEW_DELETE_NETWORK_SUCCESS = 5;
    int REVIEW_DELETE_NETWORK_FAILER = 6;


    void getReviewData(String storeId);
    ArrayList<StoreReview> setReviewData();
    void getReviewDetailData(int reviewNum);
    StoreReview setDetailReviewData();
    void deleteReviewData(int reviewNum);


    String getKakaoId();
}
