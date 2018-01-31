package sku.jvj.seatcock.Interface.Main;

import android.view.View;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.Advertising;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.Store;

/**
 * Created by Android Hong on 2016-10-21.
 */

public interface MainView extends  View.OnClickListener{

    int LOCATION_DATA = 2;
    /**
     * Progress를 보여주는 메소드
     */
    void showProgress();

    /**
     * Progress를 없애는 메소드
     */
    void hideProgress();

    /**
     * 알람 띄워주는 메소드
     * @param massage
     */
    void showToast(String massage);
    /**
     * 로그인이 되어 있을 경우 or 안되어 있을 경우
     */
    void isLogin(KakaoUser kakaoUser);

    /**
     * 내 위치(TextView)를 보여주는 메소드
     * @param latitude
     * @param longitude
     */
    void displayLocationText(double latitude, double longitude);

    /**
     * 점포 정보 보여주는 메소드
     * @param storeArrayList
     */
    void displayStore(ArrayList<Store> storeArrayList);

    /**
     * 광고 정보 보여주는 메소드
     * @param advertisingArrayList
     */
    void displayAdvertising(ArrayList<Advertising> advertisingArrayList);

    /**
     * Store 화면(StoreActivity)으로 넘어가는 메소드
     * @param store
     */
    void navigateToStore(Store store);

    /**
     * 좌석 화면(SeatActivity)으로 넘어가는 메소드
     * @param store
     */
    void navigateToSeat(Store store);

    /**
     * 광고 화면(AdActivity)으로 넘어가는 메소드
     * @param advertising
     */
    void navigateToAdvertising(Advertising advertising);

    /**
     * 내위치 변경하는 화면(LocationActivity)으로 넘어가는 메소드
     * @param latitude
     * @param longitude
     */
    void navigateToLocation(double latitude, double longitude);

    /**
     * 내 예약정보 화면(MyReservationActivity)으로 넘어가는 메소드
     */
    void navigateToMyReservation();

    /**
     * 검색화면(SearchActivity)으로 넘어가는 메소드
     */
    void navigateToSearch();

}
