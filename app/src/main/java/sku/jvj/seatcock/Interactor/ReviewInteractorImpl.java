package sku.jvj.seatcock.Interactor;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import sku.jvj.seatcock.Interface.Store.Review.ReviewInteractor;
import sku.jvj.seatcock.Interface.Store.Review.ReviewPresenter;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.StoreReview;
import sku.jvj.seatcock.Server.StoreReviewData;


/**
 * Created by Android Hong on 2016-10-24.
 */

public class ReviewInteractorImpl implements ReviewInteractor {

    private StoreReviewHandler storeReviewHandler;
    private ReviewPresenter reviewPresenter;
    private StoreReviewData storeReviewData;
    private KakaoUser kakaoUser = KakaoUser.getInstance();

    public ReviewInteractorImpl(ReviewPresenter reviewPresenter){
        this.reviewPresenter = reviewPresenter;
        storeReviewHandler = new StoreReviewHandler();
        storeReviewData = new StoreReviewData(storeReviewHandler);
    }

    @Override
    public void getReviewData(String storeId) {
        storeReviewData.getTotalData(storeId);
    }

    @Override
    public ArrayList<StoreReview> setReviewData() {
        return storeReviewData.setTotalData();
    }

    @Override
    public void getReviewDetailData(int reviewNum) {

    }

    @Override
    public StoreReview setDetailReviewData() {
        return storeReviewData.setDetailData();
    }

    @Override
    public void deleteReviewData(int reviewNum) {
        storeReviewData.deleteToReview(reviewNum);
    }

    @Override
    public String getKakaoId() {
        return kakaoUser.getId();
    }

    /**
     * 핸들러 클래스
     */
    public class StoreReviewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REVIEW_NETWORK_SUCCESS:
                    reviewPresenter.onSuccess("reviewData");
                    break;
                case REVIEW_NETWORK_FAILER:
                    reviewPresenter.failOfTimeOut("reviewData");
                    break;
                case REVIEW_DETAIL_NETWORK_SUCCESS:
                    reviewPresenter.onSuccess("reviewData");
                    break;
                case REVIEW_DETAIL_NETWORK_FAILER:
                    reviewPresenter.failOfTimeOut("reviewDetailData");
                    break;

                case REVIEW_DELETE_NETWORK_SUCCESS:
                    reviewPresenter.onSuccess("reviewDeleteData");
                    break;
                case REVIEW_DELETE_NETWORK_FAILER:
                    reviewPresenter.failOfTimeOut("reviewDeleteData");
                    break;
            }
        }
    }
}
