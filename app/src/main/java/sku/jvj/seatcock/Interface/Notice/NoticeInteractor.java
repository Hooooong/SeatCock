package sku.jvj.seatcock.Interface.Notice;

/**
 * Created by Android Hong on 2016-10-26.
 */

public interface NoticeInteractor {
    int NOTICE_NETWORK_SUCCESS = 1;
    int NOTICE_NETWORK_FAILER = 2;

    void getNoticeData();
    void setAllStatusUpdate();

}
