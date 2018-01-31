package sku.jvj.seatcock.Interactor;

import android.os.Handler;
import android.os.Message;

import sku.jvj.seatcock.Interface.Store.Contents.ContentsInteractor;
import sku.jvj.seatcock.Interface.Store.Contents.ContentsPresenter;
import sku.jvj.seatcock.Server.StoreContentsData;

/**
 * Created by Android Hong on 2016-10-24.
 */

public class ContentsInteractorImpl implements ContentsInteractor {

    private StoreContents storeContents;
    private ContentsPresenter contentsPresenter;
    private StoreContentsData storeContentsData;

    public ContentsInteractorImpl(ContentsPresenter contentsPresenter){
        this.contentsPresenter = contentsPresenter;
        storeContents = new StoreContents();
    }


    @Override
    public void getContentsData(String storeId) {
        storeContentsData = new StoreContentsData(storeContents);
        storeContentsData.getData(storeId);
    }

    /**
     * 핸들러 클래스
     */
    public class StoreContents extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CON_NETWORK_SUCCESS:
                    contentsPresenter.onSuccess(storeContentsData.setData());
                    break;
                case CON_NETWORK_FAILER:
                    contentsPresenter.failOfTimeOut();
                    break;
            }
        }
    }
}
