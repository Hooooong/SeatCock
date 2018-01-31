package sku.jvj.seatcock.Interactor;

import android.os.Handler;
import android.os.Message;

import sku.jvj.seatcock.Interface.Notice.NoticeInteractor;
import sku.jvj.seatcock.Interface.Notice.NoticePresenter;
import sku.jvj.seatcock.Server.NoticeData;

/**
 * Created by Android Hong on 2016-10-26.
 */

public class NoticeInteractorImpl implements NoticeInteractor {


    private NoticePresenter noticePresenter;
    private NoticeHandler noticeHandler;
    private NoticeData noticeData;

    public NoticeInteractorImpl(NoticePresenter noticePresenter) {
        this.noticePresenter = noticePresenter;
        noticeHandler = new NoticeHandler();
        noticeData = new NoticeData(noticeHandler);
    }

    @Override
    public void getNoticeData() {
        noticeData.noticeGetData();
    }

    @Override
    public void setAllStatusUpdate() {
        noticeData.setAllStatusUpdate();
    }

    /**
     * 핸들러 클래스
     */
    public class NoticeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NOTICE_NETWORK_SUCCESS:
                    noticePresenter.onSuccess(noticeData.setData());
                    break;
                case NOTICE_NETWORK_FAILER:
                    noticePresenter.failOfTimeOut();
                    break;
            }
        }
    }
}
