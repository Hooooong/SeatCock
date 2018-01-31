package sku.jvj.seatcock.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sku.jvj.seatcock.Adapter.StoreMenuAdapter;
import sku.jvj.seatcock.Interface.Store.Menu.MenuView;
import sku.jvj.seatcock.Model.StoreMenu;
import sku.jvj.seatcock.Presenter.MenuPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

/**
 * Created by Android Hong on 2016-06-08.
 */
public class MenuFragment extends Fragment implements MenuView {

    private String storeId;
    private View view;

    private ProgressDialog progressDialog;
    private MenuPresenterImpl menuPresenter;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private StoreMenuAdapter mAdapter;

    /**
     * 생성자
     * @param storeId
     * @return
     */
    public static MenuFragment create(String storeId) {
        MenuFragment fragment = new MenuFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("storeId",storeId);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store_menu, container, false);
        storeId = (String)getArguments().getSerializable("storeId");
        initLayout();

        menuPresenter = new MenuPresenterImpl(this);
        menuPresenter.getMenuData(storeId);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initLayout(){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.menuCardView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new StaggeredGridLayoutManager(2, 1);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mLayoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StoreMenuAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showProgress() {
        progressDialog = Util.createProgressDialog(getActivity());
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void displayMenuData(ArrayList<StoreMenu> storeMenuArrayList) {
        mAdapter.addData(storeMenuArrayList);
        mAdapter.notifyDataSetChanged();
    }
}
