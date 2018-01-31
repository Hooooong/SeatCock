package sku.jvj.seatcock.Model;

/**
 * Created by Android Hong on 2016-10-12.
 */

public class Notice {

    private int noticeId;
    private String noticeTitle;
    private String noticeDate;
    private String noticeContets;

    // 상태 ( 0 이 읽은것, 1 이 않읽은것 )
    private int status = 0;

    public Notice(int noticeId, int status) {
        this.noticeId = noticeId;
        this.status = status;
    }

    public Notice(int noticeId, String noticeTitle, String noticeDate, String noticeContets, int status) {
        this.noticeId = noticeId;
        this.noticeTitle = noticeTitle;
        this.noticeDate = noticeDate;
        this.noticeContets = noticeContets;
        this.status = status;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public String getNoticeContets() {
        return noticeContets;
    }

    public int getStatus() {
        return status;
    }
}

