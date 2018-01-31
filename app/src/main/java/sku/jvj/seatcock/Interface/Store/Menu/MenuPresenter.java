package sku.jvj.seatcock.Interface.Store.Menu;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.StoreMenu;

/**
 * Created by Android Hong on 2016-10-24.
 */

public interface MenuPresenter {
    void getMenuData(String storeId);

    void onSuccess(ArrayList<StoreMenu> storeMenuArrayList);
    void failOfTimeOut();
}
