package sku.jvj.seatcock.Interface.Store;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.Store;

/**
 * Created by Android Hong on 2016-10-22.
 */

public interface StoreView {

    /**
     * Progress를 보여주는 메소드
     */
    void showProgress();

    /**
     * Progress를 없애는 메소드
     */
    void hideProgress();

    /**
     * Toast보여주는 화면
     * @param message
     */
    void showToast(String message);

    /**
     * Store 사진을 보여주는 메소드
     * @param arrayList
     */
    void displayStorePictre(ArrayList arrayList);

    /**
     * 사진 화면(PictureActivity)으로 넘어가는 메소드
     * @param arrayList
     * @param position
     */
    void navigateToPicture(ArrayList arrayList, int position);

    /**
     * 점포 지도(MapActivity) 으로 넘어가는 메소드
     * @param x
     * @param y
     */
    void navigateToMap(double x,double y);

    /**
     * 좌석 화면(SeatActivity)으로 넘어가는 메소드
     */
    void navigateToSeat(Store store);

    /**
     * 리뷰 작성화면(ReviewWriteActivity)으로 넘어가는 메소드
     * @param storeId
     */
    void navigateToReviewWrite(String storeId);

}