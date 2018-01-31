package sku.jvj.seatcock.Presenter;

import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.MenuInteractorImpl;
import sku.jvj.seatcock.Interface.Store.Menu.MenuInteractor;
import sku.jvj.seatcock.Interface.Store.Menu.MenuPresenter;
import sku.jvj.seatcock.Interface.Store.Menu.MenuView;
import sku.jvj.seatcock.Model.StoreMenu;

/**
 * Created by Android Hong on 2016-10-24.
 */

public class MenuPresenterImpl implements MenuPresenter {

    private MenuInteractor menuInteractor;
    private MenuView view;

    public MenuPresenterImpl(MenuView view){
        this.view = view;
        menuInteractor = new MenuInteractorImpl(this);
    }

    @Override
    public void getMenuData(String storeId) {
        if(view != null){
            view.showProgress();
        }
        menuInteractor.getMenuData(storeId);
    }

    @Override
    public void onSuccess(ArrayList<StoreMenu> storeMenuArrayList) {
        if(view != null){
            view.hideProgress();
            view.displayMenuData(storeMenuArrayList);
        }

    }

    @Override
    public void failOfTimeOut() {
        if(view != null){
            view.hideProgress();
        }
    }
}
