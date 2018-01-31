package sku.jvj.seatcock.Presenter;

import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.ReviewInteractorImpl;
import sku.jvj.seatcock.Interface.Store.Review.ReviewInteractor;
import sku.jvj.seatcock.Interface.Store.Review.ReviewPresenter;
import sku.jvj.seatcock.Interface.Store.Review.ReviewView;

/**
 * Created by Android Hong on 2016-10-24.
 */

public class ReviewPresenterImpl implements ReviewPresenter {

    private ReviewInteractor reviewInteractor;
    private ReviewView view;

    public ReviewPresenterImpl(ReviewView view){
        this.view = view;
        reviewInteractor = new ReviewInteractorImpl(this);
    }

    @Override
    public void getReviewData(String storeId) {
        if(view != null){
            view.showProgress();
        }
        reviewInteractor.getReviewData(storeId);

    }

    @Override
    public void getReviewDetailData(int reviewNum) {
        if(view != null){
            view.showProgress();
        }
        reviewInteractor.getReviewDetailData(reviewNum);
    }

    @Override
    public void deleteReview(int reviewNum) {
        if(view != null){
            view.showProgress();
        }
        reviewInteractor.deleteReviewData(reviewNum);
    }

    @Override
    public void showDialog(int reviewNum) {
        if(view != null){
            view.showChoiceDialog(reviewNum);
        }
    }

    @Override
    public String getKakaoId() {
        return reviewInteractor.getKakaoId();
    }

    @Override
    public void navigateToPicture(ArrayList<String> reviewPicture, int tag) {
        if(view != null){
            view.navigateToPicture(reviewPicture, tag);
        }

    }

    @Override
    public void onSuccess(String reviewStatus) {
        if(view != null){
            view.hideProgress();
            switch (reviewStatus){
                case "reviewData":
                    view.displayReview(reviewInteractor.setReviewData());
                    break;
                case "reviewDetailData":
                    view.navigateToDetailReview(reviewInteractor.setDetailReviewData());
                    break;
                case "reviewDeleteData":
                    view.showToast("리뷰 삭제가 성공하였습니다.");
                    break;
            }
        }
    }

    @Override
    public void failOfTimeOut(String reviewStatus) {
        if(view != null){
            view.hideProgress();
            switch (reviewStatus){
                case "reviewData":
                    break;
                case "reviewDetailData":
                    break;
                case "reviewDeleteData":
                    view.showToast("리뷰 삭제가 실패하였습니다");
                    break;
            }
        }

    }


}
