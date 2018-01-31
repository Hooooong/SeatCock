package sku.jvj.seatcock.Model;

/**
 * Created by Android Hong on 2016-09-13.
 */
public class SeatTime {

    // 시간
    private String time;
    // 선택한 시간
    private boolean checkTime;
    // 사용중인 시간
    private boolean useTime;

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public boolean isCheckTime() {
        return checkTime;
    }
    public void setCheckTime(boolean checkTime) {
        this.checkTime = checkTime;
    }
    public boolean isUseTime() {
        return useTime;
    }
    public void setUseTime(boolean useTime) {
        this.useTime = useTime;
    }

    @Override
    public String toString() {
        return "SeatTime{" +
                "time='" + time + '\'' +
                ", checkTime=" + checkTime +
                ", useTime=" + useTime +
                '}';
    }
}
