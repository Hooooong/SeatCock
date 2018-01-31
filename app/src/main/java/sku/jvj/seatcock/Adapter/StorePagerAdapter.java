package sku.jvj.seatcock.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import sku.jvj.seatcock.Activity.ContentsFragment;
import sku.jvj.seatcock.Activity.MenuFragment;
import sku.jvj.seatcock.Activity.ReviewFragment;
import sku.jvj.seatcock.Model.Store;

public class StorePagerAdapter extends FragmentPagerAdapter {

    private int _numOfTabs;
    private String storeId;

    public StorePagerAdapter(FragmentManager fm, int numOfTabs, String storeId) {
        super(fm);
        this.storeId = storeId;
        this._numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ContentsFragment.create(storeId);
            case 1:
                return MenuFragment.create(storeId);
            case 2:
                return ReviewFragment.create(storeId);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return _numOfTabs;
    }
}
