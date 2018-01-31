package sku.jvj.seatcock.Interface.Store;

import java.util.ArrayList;

/**
 * Created by Android Hong on 2016-10-22.
 */

public interface StorePresenter {

    /**
     * Store 사진을 불러오는 메소드
     */
    void getStorePictureData();

    /**
     * 점포이름
     * @return
     */
    String getStoreName();

    /**
     * StoreId 정보를 불러오는 메소드
     */
    String getStoreId();

    /**
     * PictureActivity로 넘어가기 위한 메소드
     * @param position
     */
    void navigateToStorePicture(int position);

    /**
     * Store 정보(x,y)를 불러오는 메소드
     */
    void navigateToStoreMap();

    /**
     * StoreId 를 가져와 SeatActivity 로 넘어가기 위한 메소드
     */
    void navigateToSeat();

    /**
     * StoreId 를 가져와 ReviewWriteActivty 로 넘어가기 위한 메소드
     */
    void navigateToReviewWrite();

    /**
     * 성공적으로 데이터 불러옴
     * @param arrayList
     */
    void onSuccess(ArrayList arrayList);

    /**
     * 데이터 불러오기 실패
     */
    void failOfTimeOut();

}
