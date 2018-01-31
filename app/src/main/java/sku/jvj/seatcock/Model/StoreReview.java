package sku.jvj.seatcock.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Android Hong on 2016-08-31.
 */
public class StoreReview implements Serializable{

    // 점포ID
    private String storeId;
    // 리뷰 번호
    private int reviewNum;
    // 리뷰 평점
    private float gpa;
    // 리뷰 내용
    private String content;
    // 리뷰 날짜
    private String date;
    // 작성자 ID
    private String kakaoId;
    // 작성자
    private String kakaoName;
    // 작성자 프로필 사진
    private String kakaoProfile;
    // 점포 이름
    private String storeName;
    // 점포 주소
    private String storeAddress;
    // 점포 사진
    private ArrayList<String> reviewPicture;

    public int getReviewNum() {
        return reviewNum;
    }
    public void setReviewNum(int reviewNum) {
        this.reviewNum = reviewNum;
    }
    public float getGpa() {
        return gpa;
    }
    public void setGpa(float gpa) {
        this.gpa = gpa;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getKakaoName() {
        return kakaoName;
    }
    public void setKakaoName(String kakaoName) {
        this.kakaoName = kakaoName;
    }
    public String getKakaoProfile() {
        return kakaoProfile;
    }
    public void setKakaoProfile(String kakaoProfile) {
        this.kakaoProfile = kakaoProfile;
    }
    public ArrayList<String> getReviewPicture() {
        return reviewPicture;
    }
    public void setReviewPicture(ArrayList<String> reviewPicture) {
        this.reviewPicture = reviewPicture;
    }
    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public String getStoreAddress() {
        return storeAddress;
    }
    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }
    public String getKakaoId() {
        return kakaoId;
    }
    public void setKakaoId(String kakaoId) {
        this.kakaoId = kakaoId;
    }
    public String getStoreId() {
        return storeId;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
