package sku.jvj.seatcock.Interactor;

import android.os.Handler;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sku.jvj.seatcock.Activity.WaitingTicketActivity;
import sku.jvj.seatcock.Interface.Waiting.WaitingInteractor;
import sku.jvj.seatcock.Interface.Waiting.WaitingPresenter;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Server.WaitingData;

/**
 * Created by Android Hong on 2016-10-31.
 */
public class WaitingInteractorImpl implements WaitingInteractor {

    private SimpleDateFormat dayTime;

    private WaitingPresenter waitingPresenter;
    private WaitingHandler waitingHandler;
    private WaitingData waitingData;
    private KakaoUser kakaoUser = KakaoUser.getInstance();
    private Store store;
    private String waitingNumber;
    private String waitingPerson;
    private String waitingTime;
    private int personNum;
    private String today;

    public WaitingInteractorImpl(WaitingPresenter waitingPresenter) {
        this.waitingPresenter = waitingPresenter;
        store = (Store) WaitingTicketActivity.mActivity.getIntent().getSerializableExtra("store");

        //현재시간 뽑기
        long time =  System.currentTimeMillis();
        dayTime = new SimpleDateFormat("yyyy-MM-dd");
        today = dayTime.format(new Date(time));
        personNum  = 1;
        waitingHandler = new WaitingHandler();
        waitingData = new WaitingData(waitingHandler);
    }

    @Override
    public void getWaitingData() {
        waitingData.getWaitingData(store.getStoreId());
    }

    @Override
    public void setWaitingData() {
        ArrayList<String> watingArrayList = waitingData.setWatingData();
        waitingNumber = watingArrayList.get(0);
        waitingPerson = watingArrayList.get(1);
        waitingTime = watingArrayList.get(2);
    }

    @Override
    public void issuanceWaitingTicket() {
        waitingData.issuanceWaitingTicket(store.getStoreId(),kakaoUser.getId(),Integer.toString(personNum));
    }

    @Override
    public int getPersonNum() {
        return personNum;
    }

    @Override
    public String getWaitingNumber() {
        return waitingNumber;
    }

    @Override
    public String getWaitingTime() {
        return waitingTime;
    }

    @Override
    public String getWaitingPerson() {
        return waitingPerson;
    }

    @Override
    public String getStoreName() {
        return store.getStoreName();
    }

    @Override
    public String userName() {
        return kakaoUser.getName();
    }

    @Override
    public String userPhoneNumber() {
        return kakaoUser.getPhoneNumber();
    }

    @Override
    public int setPersonNum(String type) {
        if (type.equals("plus")) {
            personNum = personNum + 1;
        } else {
            personNum = personNum - 1;
        }
        return personNum;
    }

    @Override
    public String getToday() {
        return today;
    }

    /**
     * 핸들러 클래스
     */
    public class WaitingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WAIT_NETWORK_SUCCESS:
                    waitingPresenter.onSuccess("waitingData");
                    break;
                case WAIT_NETWORK_FAILER:
                    waitingPresenter.failOfTimeOut("waitingDataFailer");
                    break;
                case WAIT_NETWORK_ISSUANCE:
                    waitingPresenter.onSuccess("waitingIssuance");
                    break;
                case WAIT_NETWORK_NOISSUANCE:
                    waitingPresenter.failOfTimeOut("waitingFailer");
                    break;
                case WAIT_NETWORK_EXIST:
                    waitingPresenter.failOfTimeOut("waitingExist");
                    break;
            }
        }
    }


}
