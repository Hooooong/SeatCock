package sku.jvj.seatcock.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sku.jvj.seatcock.Interface.Store.StorePresenter;

/**
 * Created by Android Hong on 2016-09-09.
 */
public  class StorePicturePagerAdapter extends PagerAdapter {

    public static String statePicture;
    private StorePresenter storePresenter;
    private ArrayList<String> pictureArrayList;

    public StorePicturePagerAdapter(ArrayList pictureArrayList, StorePresenter storePresenter){
        this.pictureArrayList = pictureArrayList;
        this.storePresenter = storePresenter;
    }
    @Override
    public int getCount() {
        return pictureArrayList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container,final int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(imageView.getContext())
                .load(pictureArrayList.get(position))
                .into(imageView);


        // 현재위치 링크가지고오기
        statePicture = pictureArrayList.get(position);

        container.addView(imageView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storePresenter.navigateToStorePicture(position);

            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
