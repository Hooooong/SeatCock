package sku.jvj.seatcock.Interface.Store.Menu;


import java.util.ArrayList;
import sku.jvj.seatcock.Model.StoreMenu;

/**
 * Created by Android Hong on 2016-10-24.
 */

public interface MenuView {

    void showProgress();
    void hideProgress();

    void displayMenuData(ArrayList<StoreMenu> storeMenuArrayList);
}
