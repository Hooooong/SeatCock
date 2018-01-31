package sku.jvj.seatcock.Interface.Store.Review;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.StoreReview;

/**
 * Created by Android Hong on 2016-10-24.
 */
public interface ReviewView {
    void showProgress();
    void hideProgress();
    void showToast(String message);

    void showChoiceDialog(int reviewNum);
    void showDeleteDialog(int reviewNum);

    void displayReview(ArrayList<StoreReview> storeReviewArrayList);
    void navigateToPicture(ArrayList<String> arrayList, int index);
    void navigateToDetailReview(StoreReview storeReview);
}
