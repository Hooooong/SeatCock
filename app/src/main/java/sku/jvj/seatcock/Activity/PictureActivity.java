package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import sku.jvj.seatcock.Adapter.ZoomPicturePagerAdapter;
import sku.jvj.seatcock.R;

/**
 * Created by Android Hong on 2016-09-08.
 */
public class PictureActivity extends Activity implements View.OnClickListener{
    private static int index;

    private TextView pageCountTextView;
    private TextView closeBtn;
    private PictureViewPager pictureViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_picture);

        Intent reviewPic = getIntent();
        ArrayList<String> reviewPicture = (ArrayList<String>) reviewPic.getSerializableExtra("picture");
        index = reviewPic.getExtras().getInt("index");

        initLayout(reviewPicture);
    }

    private void initLayout(final ArrayList<String> reviewPicture){
        closeBtn = (TextView) findViewById(R.id.closeBtn);
        pageCountTextView = (TextView) findViewById(R.id.pageCountTextView);
        pageCountTextView.setText((index + 1) + " / " + reviewPicture.size());

        pictureViewPager = (PictureViewPager) findViewById(R.id.picture_view_pager);
        pictureViewPager.setAdapter(new ZoomPicturePagerAdapter(this, reviewPicture));
        pictureViewPager.setCurrentItem(index);

        pictureViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int number) {
                pageCountTextView.setText((number + 1) + " /  " + reviewPicture.size());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int num) {

            }
        });
        closeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId){
            case R.id.closeBtn:
                finish();
                break;
        }
    }
}

class PictureViewPager extends ViewPager {

    public PictureViewPager(Context context) {
        super(context);
    }

    public PictureViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
