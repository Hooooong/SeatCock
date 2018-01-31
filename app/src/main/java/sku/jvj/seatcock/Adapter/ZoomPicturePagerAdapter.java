package sku.jvj.seatcock.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sku.jvj.seatcock.R;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Android Hong on 2016-09-09.
 */
public class ZoomPicturePagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> reviewPicture;

    public ZoomPicturePagerAdapter(Context context, ArrayList<String> reviewPicture) {
        this.context = context;
        this.reviewPicture = reviewPicture;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return reviewPicture.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.content_picture, container, false);
        PhotoView photoView = (PhotoView) itemView.findViewById(R.id.photoView);
        Glide.with(context)
                .load(reviewPicture.get(position))
                .into(photoView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
