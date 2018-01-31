package sku.jvj.seatcock.Presenter;

import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.NoticeInteractorImpl;
import sku.jvj.seatcock.Interface.Notice.NoticeInteractor;
import sku.jvj.seatcock.Interface.Notice.NoticePresenter;
import sku.jvj.seatcock.Interface.Notice.NoticeView;
import sku.jvj.seatcock.Model.Notice;

/**
 * Created by Android Hong on 2016-10-26.
 */

public class NoticePresenterImpl implements NoticePresenter {

    private NoticeView view;
    private NoticeInteractor noticeInteractor;

    public NoticePresenterImpl(NoticeView view) {
        this.view = view;
        noticeInteractor = new NoticeInteractorImpl(this);
        this.getNoticeData();
    }

    @Override
    public void getNoticeData() {
        if(view != null){
            view.showProgress();
        }
        noticeInteractor.getNoticeData();
    }

    @Override
    public void setAllStatusUpdate() {
        noticeInteractor.setAllStatusUpdate();
    }

    @Override
    public void onSuccess(ArrayList<Notice> noticeArrayList) {
        if(view != null){
            view.hideProgress();
            view.displayNotice(noticeArrayList);
        }
    }


    @Override
    public void failOfTimeOut() {
        if(view != null){
            view.hideProgress();
        }
    }
}
