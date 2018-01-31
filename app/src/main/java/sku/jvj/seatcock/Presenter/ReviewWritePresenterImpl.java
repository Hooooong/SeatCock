package sku.jvj.seatcock.Presenter;

import android.Manifest;
import android.net.Uri;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import sku.jvj.seatcock.Activity.ReviewWriteActivity;
import sku.jvj.seatcock.Interactor.ReviewWriteInteractorImpl;
import sku.jvj.seatcock.Interface.Store.Review.ReviewWriteInteractor;
import sku.jvj.seatcock.Interface.Store.Review.ReviewWritePresenter;
import sku.jvj.seatcock.Interface.Store.Review.ReviewWriteView;

/**
 * Created by Android Hong on 2016-10-25.
 */

public class ReviewWritePresenterImpl implements ReviewWritePresenter {

    private ReviewWriteView view;
    private ReviewWriteInteractor reviewWriteInteractor;

    public ReviewWritePresenterImpl(ReviewWriteView view){
        this.view = view;
        reviewWriteInteractor = new ReviewWriteInteractorImpl(this);
    }

    @Override
    public void insertReviewData(String rating1, String rating2, String rating3, String reviewText) {
        if(reviewText.trim().length() < 10){
            if(view != null){
                view.showToast("리뷰작성은 최소 10자 이상의 글자가 필요합니다");
            }
        }else{
            if(view != null){
                view.showProgress();
                reviewWriteInteractor.insertToReviewData(rating1,rating2,rating3,reviewText);
            }
        }
    }

    @Override
    public void checkPermission() {
        new TedPermission(ReviewWriteActivity.mActivity)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("접근권한을 거부하셨습니다.\n[설정] > [권한] 에서 권한을 허용하여야만 사진을 등록할 수 있습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    public int getPictureSize() {
        return reviewWriteInteractor.getPictureSize();
    }

    @Override
    public void addPicture(Uri uri, String path) {
        reviewWriteInteractor.insertToReviewPicture(uri,path);
        view.setPicture(uri);
    }

    @Override
    public void modifyPicture(Uri uri, String path ,int pictureNum) {
        reviewWriteInteractor.modifyToReviewPicture(uri, path, pictureNum);
        view.laterModifyPicture(uri,pictureNum);
    }

    @Override
    public void deletePicture(int pictureNum) {
        view.laterDeletePictrue(reviewWriteInteractor.deleteToReviewPictture(pictureNum));
    }

    @Override
    public void onSuccess() {
        if(view != null){
            view.showToast("리뷰 작성이 완료되었습니다");
            view.hideProgressAndFinish();
        }
    }

    @Override
    public void failOfTImeOut() {
        if(view != null){
            view.hideProgress();
            view.showToast("리뷰 작성이 실패하였습니다");
        }
    }

    /**
     * 권한허가(WRITE_EXTERNAL_STORAGE)
     */
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(reviewWriteInteractor.getPictureSize() < 3) {
                view.navigateToGallery(ReviewWriteView.REQ_CODE_IMAGE);
            }else{
                view.showToast("최대 사진갯수(3개)를 초과할 수 없습니다.");
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

        }
    };
}
