package sku.jvj.seatcock.Model;

import java.io.Serializable;

/**
 * Created by Android Hong on 2016-09-04.
 */
public class StoreSeat implements Serializable {

    // 좌표 x
    private int x;
    // 좌표 y
    private int y;
    // 좌석 번호
    private int num;
    // 노말 예약 체크
    private boolean seatNormalCheck;
    // 존 예약 체크
    private boolean seatZoneCheck;
    //미사용중 : false, 사용중 : true
    private boolean status;
    //미 체크 : false, 체크 : true
    private boolean checkStatus;

    public boolean isStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = checkBoolean(status);
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setSeatNormalCheck(String seatNormalCheck) {
        this.seatNormalCheck = checkBoolean(seatNormalCheck);
    }
    public boolean isSeatNormalCheck() {
        return seatNormalCheck;
    }
    public boolean isSeatZoneCheck() {
        return seatZoneCheck;
    }
    public void setSeatZoneCheck(String seatZoneCheck) {
        this.seatZoneCheck = checkBoolean(seatZoneCheck);
    }
    public boolean isCheckStatus() {
        return checkStatus;
    }
    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
    }

    /**
     * 사용중인지
     * @param check
     * @return
     */
    private boolean checkBoolean(String check) {
        boolean result;
        if (check.equals("0")) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }
}
