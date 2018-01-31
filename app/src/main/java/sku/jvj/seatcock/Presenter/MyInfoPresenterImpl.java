package sku.jvj.seatcock.Presenter;

import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.MyInfoInteractorImpl;
import sku.jvj.seatcock.Interface.MyInfo.MyInfoInteractor;
import sku.jvj.seatcock.Interface.MyInfo.MyInfoPresenter;
import sku.jvj.seatcock.Interface.MyInfo.MyInfoView;

/**
 * Created by Android Hong on 2016-10-27.
 */

public class MyInfoPresenterImpl implements MyInfoPresenter {

    private MyInfoView view;
    private MyInfoInteractor myInfoInteractor;

    public MyInfoPresenterImpl(MyInfoView view) {
        this.view = view;
        myInfoInteractor = new MyInfoInteractorImpl(this);

    }

    @Override
    public void getMyReviewData() {
        if (view != null) {
            view.showProgress();
        }
        myInfoInteractor.getMyReviewData();
    }

    @Override
    public void getMyInfo() {
        view.displayMyInfo(myInfoInteractor.getMyInfo());
    }


    @Override
    public void refreshMyProfile() {
        myInfoInteractor.refreshMyProfile();
    }

    @Override
    public void getReviewDetailData(int reviewNum) {
        if (view != null) {
            view.showProgress();
        }
        myInfoInteractor.getReviewDetailData(reviewNum);
    }

    @Override
    public void deleteMyReview(int reviewNum) {
        if (view != null) {
            view.showProgress();
        }
        myInfoInteractor.deleteMyReviewData(reviewNum);
    }

    @Override
    public void showDialog(int reviewNum) {
        if (view != null) {
            view.showChoiceDialog(reviewNum);
        }
    }

    @Override
    public void navigateToPicture(ArrayList<String> reviewPicture, int tag) {
        if (view != null) {
            view.navigateToPicture(reviewPicture, tag);
        }
    }

    @Override
    public void onSuccess(String reviewStatus) {
        if (view != null) {
            view.hideProgress();
            switch (reviewStatus) {
                case "reviewData":
                    view.dislpayMyReview(myInfoInteractor.setMyReviewData());
                    break;
                case "reviewDetailData":
                    view.navigateToDetailReview(myInfoInteractor.setDetailReviewData());
                    break;
                case "reviewDeleteData":
                    view.showToast("리뷰 삭제가 성공하였습니다.");
                    break;
                case "profileUpdate":
                    getMyInfo();
                    view.showToast("프로필 업데이트 성공");
                    break;
            }
        }
    }

    @Override
    public void failOfTimeOut(String reviewStatus) {
        if (view != null) {
            view.hideProgress();
            switch (reviewStatus) {
                case "reviewData":
                    break;
                case "reviewDetailData":
                    break;
                case "reviewDeleteData":
                    view.showToast("리뷰 삭제가 실패하였습니다.");
                    break;
                case "profileUpdate" :
                    view.showToast("프로필 업데이트 실패");
                    break;
            }


        }
    }
}
