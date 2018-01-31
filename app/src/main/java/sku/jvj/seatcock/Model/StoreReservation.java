package sku.jvj.seatcock.Model;

/**
 * Created by Android Hong on 2016-09-06.
 */
public class StoreReservation {

    //예약 날짜
    private String date;
    // 예약 시간
    private String time;
    // 점포 ID
    private String storeId;
    // 점포 이름
    private String storeName;
    // 점포 주소
    private String storeAddress;
    // 점포 전화번호
    private String storePhoneNumber;
    // 예약 좌석 번호
    private String seatNum;
    // 현재, 지난 예약 확인
    private boolean complete;
    // 존, 노말 예약 확인
    private String znCheck;
    //인원수
    private int person;

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getStoreId() {
        return storeId;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
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
    public String getSeatNum() {
        return seatNum;
    }
    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }
    public boolean isComplete() {
        return complete;
    }
    public void setComplete(String complete) {
        this.complete = checkBoolean(complete);
    }
    public String getZnCheck() {
        return znCheck;
    }
    public void setZnCheck(String znCheck) {
        this.znCheck = znCheck;
    }
    public String getStorePhoneNumber() {
        return storePhoneNumber;
    }
    public void setStorePhoneNumber(String storePhoneNumber) {
        this.storePhoneNumber = storePhoneNumber;
    }
    public int getPerson() {
        return person;
    }
    public void setPerson(int person) {
        this.person = person;
    }

    /**
     * 현재 , 지난 예약 확인
     * 0이면 현재(True), 1이면 지난(False)
     * @param check
     * @return
     */
    private boolean checkBoolean(String check) {
        boolean result;
        if (check.equals("0")) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }


}
