package sku.jvj.seatcock.Interactor;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import sku.jvj.seatcock.Activity.ReviewWriteActivity;
import sku.jvj.seatcock.Interface.Store.Review.ReviewWriteInteractor;
import sku.jvj.seatcock.Interface.Store.Review.ReviewWritePresenter;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Server.StoreReiviewWriteData;
import sku.jvj.seatcock.Util.Util;

/**
 * Created by Android Hong on 2016-10-25.
 */

public class ReviewWriteInteractorImpl implements ReviewWriteInteractor {

    // 사진경로
    private ArrayList<String> pictureArrayList;
    // 사진 실제 경로
    private ArrayList<String> pictureRealPathArrayList;

    private String storeId;
    private ReviewWritePresenter reviewWritePresenter;
    private StoreReiviewWriteData storeReiviewWriteData;

    private KakaoUser kakaoUser = KakaoUser.getInstance();

    private ReviewWrite reviewWrite;

    public ReviewWriteInteractorImpl(ReviewWritePresenter reviewWritePresenter){
        this.reviewWritePresenter = reviewWritePresenter;

        storeId =  ReviewWriteActivity.mActivity.getIntent().getStringExtra("storeId");

        pictureArrayList = new ArrayList<>();
        pictureRealPathArrayList = new ArrayList<>();

        reviewWrite = new ReviewWrite();
        storeReiviewWriteData = new StoreReiviewWriteData(reviewWrite);
    }

    @Override
    public void insertToReviewData(String rating1, String rating2, String rating3, String reviewText) {
        /*storeReiviewWriteData.insertToReview(storeId, reviewText,rating1,rating2,rating3, kakaoUser.getid(), pictureRealPathArrayList);*/
        storeReiviewWriteData.insertToReview(storeId, reviewText,rating1,rating2,rating3,kakaoUser.getId(),pictureRealPathArrayList);
    }

    @Override
    public void insertToReviewRealPictureData() {
        for (int i = 0; i < pictureRealPathArrayList.size(); i++) {
            storeReiviewWriteData.uploadImage(Util.getFilePath(pictureRealPathArrayList.get(i)) + "" + Util.getFileName(pictureRealPathArrayList.get(i)),i+1, pictureRealPathArrayList.size());
        }
    }

    @Override
    public void insertToReviewPicture(Uri uri, String path) {
        pictureArrayList.add(uri.toString());
        pictureRealPathArrayList.add(path);
    }

    @Override
    public ArrayList deleteToReviewPictture(int pictureNum) {
        pictureArrayList.remove(pictureNum-1);
        pictureRealPathArrayList.remove(pictureNum-1);
        return pictureArrayList;
    }

    @Override
    public void modifyToReviewPicture(Uri uri,String path,int pictureNum) {
        pictureArrayList.set(pictureNum, uri.toString());
        pictureRealPathArrayList.set(pictureNum, path);

    }

    @Override
    public int getPictureSize() {
        return pictureArrayList.size();
    }

    /**
     * 핸들러 클래스
     */
    public class ReviewWrite extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REVIEW_INSERT_NETWORK_SUCCESS:
                    if(pictureRealPathArrayList.size()>0){
                        insertToReviewRealPictureData();
                    }else{
                        reviewWritePresenter.onSuccess();
                    }
                    break;
                case REVIEW_INSERT_NETWORK_FAILER:
                    reviewWritePresenter.failOfTImeOut();
                    break;
                case REVIEW_INSERT_PICTURE_SUCCESS:
                    reviewWritePresenter.onSuccess();
                    break;
                case REVIEW_INSERT_PICTURE_FAILER:
                    reviewWritePresenter.failOfTImeOut();
                    break;
            }
        }
    }


}
