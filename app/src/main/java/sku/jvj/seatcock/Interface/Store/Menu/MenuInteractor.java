package sku.jvj.seatcock.Interface.Store.Menu;

/**
 * Created by Android Hong on 2016-10-24.
 */

public interface MenuInteractor {
    int MENU_NETWORK_SUCCESS = 1;
    int MENU_NETWORK_FAILER = 2;

    void getMenuData(String storeId);
}
