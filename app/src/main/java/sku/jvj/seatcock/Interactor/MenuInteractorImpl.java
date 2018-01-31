package sku.jvj.seatcock.Interactor;

import android.os.Handler;
import android.os.Message;

import sku.jvj.seatcock.Interface.Store.Menu.MenuInteractor;
import sku.jvj.seatcock.Interface.Store.Menu.MenuPresenter;
import sku.jvj.seatcock.Server.StoreMenuData;

/**
 * Created by Android Hong on 2016-10-24.
 */

public class MenuInteractorImpl implements MenuInteractor {

    private StoreMenu storeMenu;
    private StoreMenuData storeMenuData;
    private MenuPresenter menuPresenter;

    public MenuInteractorImpl(MenuPresenter menuPresenter){
        this.menuPresenter = menuPresenter;
        storeMenu = new StoreMenu();

    }

    @Override
    public void getMenuData(String storeId) {
        storeMenuData = new StoreMenuData(storeMenu);
        storeMenuData.getData(storeId);
    }


    /**
     * 핸들러 클래스
     */
    public class StoreMenu extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MENU_NETWORK_SUCCESS:
                    menuPresenter.onSuccess(storeMenuData.setData());
                    break;
                case MENU_NETWORK_FAILER:
                    menuPresenter.failOfTimeOut();
                    break;
            }
        }
    }
}
