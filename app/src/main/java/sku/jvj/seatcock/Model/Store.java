package sku.jvj.seatcock.Model;

import java.io.Serializable;

/**
 * Created by Android Hong on 2016-06-08.
 */
public class Store implements Serializable {

    // 점포 x 좌표
    private String x;
    // 점표 y 자표
    private String y;
    // 점포 ID;
    private String storeId;
    // 점포 이름
    private String storeName;
    // 점포 전화번호
    private String phoneNumber;
    // 점포 간단한 소개
    private String storeIntro;
    // 점포 시작시간
    private String storeStartTime;
    // 점포 종료시간
    private String storeFinishTime;
    // 예약 MAXTIME
    private String storeMaxTime;
    //점포 상표 이미지(tradeMark);
    private String storeTrademark;
    // 점포와의 거리
    private int storeDistance;
    // 점포 사용 좌석
    private int seatUseCountSituation;
    // 점포 총 좌석
    private int seatTotalCountSituation;
    // 점포 이미지
    private String imageResourceId;
    // 점포 주소
    private String storeAddress;
    // 평점
    private float gpa;
    // 평점 5점 인원
    private int gpa_five;
    // 평점 4점 인원
    private int gpa_four;
    // 평점 3점 인원
    private int gpa_three;
    // 평점 2점 인원
    private int gpa_two;
    // 평점 1점 인원
    private int gpa_one;
    // 홈페이지
    private String homepage;
    // 리뷰 갯수
    private int reviewConunt;
    //Normal 예약이 가능한지 여부
    private boolean isNormalReservation;
    //Zone 예약이 가능한지 여부
    private boolean isZoneReservation;


    public String getStoreId() {
        return storeId;
    }
    public String getStoreName() {
        return storeName;
    }
    public int getStoreDistance() {
        return storeDistance;
    }
    public String getImageResourceId() {
        return imageResourceId;
    }
    public float getGpa() {
        return gpa;
    }
    public int getReviewConunt(){ return reviewConunt;}
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getStoreIntro() {
        return storeIntro;
    }
    public String getStoreTrademark() {
        return storeTrademark;
    }
    public void setStoreTrademark(String storeTrademark) {
        this.storeTrademark = trademarkCheck(storeTrademark);
    }
    public void setStoreIntro(String storeIntro) {
        this.storeIntro = storeIntro;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setStoreId(String storeId){
        this.storeId = storeId;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public void setStoreDistance(int storeDistance) {
        this.storeDistance = storeDistance;
    }
    public void setImageResourceId(String imageResourceId) {
        this.imageResourceId = pictureCheck(imageResourceId);
    }
    public void setGpa(float gpa) {
        this.gpa = gpa;
    }
    public void setReviewConunt(int reviewConunt){
        this.reviewConunt = reviewConunt;
    }
    public void setNormalReservation(String normalReservation) {
        isNormalReservation = reservationCheck(normalReservation);
    }
    public void setZoneReservation(String zoneReservation) {
        isZoneReservation = reservationCheck(zoneReservation);
    }
    public boolean isZoneReservation() {
        return isZoneReservation;
    }
    public boolean isNormalReservation() {
        return isNormalReservation;
    }
    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }
    public String getStoreAddress() {
        return storeAddress;
    }
    public String getY() {
        return y;
    }
    public void setY(String y) {
        this.y = y;
    }
    public String getX() {
        return x;
    }
    public void setX(String x) {
        this.x = x;
    }
    public String getHomepage() {
        return homepage;
    }
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
    public int getGpa_five() {
        return gpa_five;
    }
    public void setGpa_five(int gpa_five) {
        this.gpa_five = gpa_five;
    }
    public int getGpa_four() {
        return gpa_four;
    }
    public void setGpa_four(int gpa_four) {
        this.gpa_four = gpa_four;
    }
    public int getGpa_three() {
        return gpa_three;
    }
    public void setGpa_three(int gpa_three) {
        this.gpa_three = gpa_three;
    }
    public int getGpa_two() {
        return gpa_two;
    }
    public void setGpa_two(int gpa_two) {
        this.gpa_two = gpa_two;
    }
    public int getGpa_one() {
        return gpa_one;
    }
    public void setGpa_one(int gpa_one) {
        this.gpa_one = gpa_one;
    }
    public void setStoreFinishTime(String storeFinalTime) {
        this.storeFinishTime = storeFinalTime;
    }
    public String getStoreStartTime() {
        return storeStartTime;
    }
    public void setStoreStartTime(String storeStartTime) {
        this.storeStartTime = storeStartTime;
    }
    public String getStoreFinishTime() {
        return storeFinishTime;
    }
    public int getSeatUseCountSituation() {
        return seatUseCountSituation;
    }
    public void setSeatUseCountSituation(int seatUseCountSituation) {
        this.seatUseCountSituation = seatUseCountSituation;
    }
    public int getSeatTotalCountSituation() {
        return seatTotalCountSituation;
    }
    public void setSeatTotalCountSituation(int seatTotalCountSituation) {
        this.seatTotalCountSituation = seatTotalCountSituation;
    }
    public String getStoreMaxTime() {
        return storeMaxTime;
    }
    public void setStoreMaxTime(String storeMaxTime) {
        this.storeMaxTime = storeMaxTime;
    }

    /**
     * 일반, 존 예약 체크
     * @param check
     * @return
     */
    private boolean reservationCheck(String check) {
        boolean result;
        if (check.equals("1")) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /**
     * 대기번호
     * @param wating
     * @return
     */
    private boolean watingTicketCheck(String wating) {
        boolean result = false;
        if(wating.equals("0")){
            result = false;
        }else{
            result = true;
        }
        return result;
    }

    /**
     * 사진 유무 체크
     * @param check
     * @return
     */
    private String pictureCheck(String check) {
        String result;
        if (check.equals("0")) {
            result = "0";
        } else {
            result = "http://14.63.213.157/dongimg/storeimg/" + check;
        }
        return result;
    }

    /**
     * TreadMark 가 있는지 체크
     * @param check
     * @return
     */
    private String trademarkCheck(String check) {
        String result;
        result = "http://14.63.213.157/dongimg/trademark/" + check;
        return result;
    }

}
