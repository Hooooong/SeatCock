package sku.jvj.seatcock.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sku.jvj.seatcock.Interface.Main.MainView;
import sku.jvj.seatcock.Model.Advertising;

/**
 * Created by Android Hong on 2016-09-09.
 */
public class AdPagerAdapter extends PagerAdapter {

    private ArrayList<Advertising> AdArrayList;
    private MainView view;

    public AdPagerAdapter(ArrayList AdArrayList,MainView view){
        this.AdArrayList = AdArrayList;
        this.view = view;
    }
    @Override
    public int getCount() {
        return AdArrayList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container,final int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(imageView.getContext())
                .load(AdArrayList.get(position).getAdPicture())
                .into(imageView);

        container.addView(imageView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                view.navigateToAdvertising(AdArrayList.get(position));
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
