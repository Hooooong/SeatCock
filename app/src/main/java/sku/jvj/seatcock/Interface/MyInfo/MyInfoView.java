package sku.jvj.seatcock.Interface.MyInfo;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.StoreReview;

/**
 * Created by Android Hong on 2016-10-27.
 */

public interface MyInfoView {
    void showProgress();
    void hideProgress();

    void showToast(String message);

    void displayMyInfo(KakaoUser kakaoUser);
    void dislpayMyReview(ArrayList<StoreReview> storeReviewArrayList);

    void showChoiceDialog(int reviewNum);
    void showDeleteDialog(int reviewNum);

    void navigateToPicture(ArrayList<String> pictureArrayList, int index);
    void navigateToDetailReview(StoreReview storeReview);
}
