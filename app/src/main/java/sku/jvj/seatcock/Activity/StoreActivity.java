package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Toast;

import com.matthewtamlin.sliding_intro_screen_library.DotIndicator;

import java.util.ArrayList;

import sku.jvj.seatcock.Adapter.StorePagerAdapter;
import sku.jvj.seatcock.Adapter.StorePicturePagerAdapter;
import sku.jvj.seatcock.Interface.Store.StorePresenter;
import sku.jvj.seatcock.Interface.Store.StoreView;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Presenter.StorePresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

import static android.view.animation.AnimationUtils.loadAnimation;

/**
 * Created by Android Hong on 2016-10-22.
 */

public class StoreActivity extends AppCompatActivity implements StoreView, View.OnClickListener {

    public static Activity mActivity;

    private StorePresenter storePresenter;
    private ProgressDialog progressDialog;
    private Context mContext;
    private Toolbar toolbar;
    private FloatingActionButton fab, fab1, fab2, reviewFab;
    private Boolean isFabOpen = false;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private TabLayout tabLayout;

    private ViewPager storePictureViewPager;
    private DotIndicator indicator;
    private ViewPager storeViewPager;
    private StorePagerAdapter storePagerAdapter;
    private CollapsingToolbarLayout ctl;
    private StorePicturePagerAdapter storePicturePagerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        mContext = this;
        mActivity = this;

        storePresenter = new StorePresenterImpl(this);

        initLayout();
        setUpViewPager();
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {


        toolbar = (Toolbar) findViewById(R.id.storeToolbar);
        ctl = (CollapsingToolbarLayout) findViewById(R.id.storeCollapsingToolbar);
        fab = (FloatingActionButton) findViewById(R.id.storeFab);
        fab1 = (FloatingActionButton) findViewById(R.id.storeFab1);
        fab2 = (FloatingActionButton) findViewById(R.id.storeFab2);
        reviewFab = (FloatingActionButton) findViewById(R.id.storeReviewFab);
        fab_open = loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        storePictureViewPager = (ViewPager)findViewById(R.id.storePictureViewPager);
        indicator = (DotIndicator)findViewById( R.id.store_indicator_ad );
        storeViewPager = (ViewPager) findViewById(R.id.storeViewPager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ctl.setExpandedTitleColor(Color.WHITE);
        ctl.setTitle(storePresenter.getStoreName());

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText("소개"));
        tabLayout.addTab(tabLayout.newTab().setText("메뉴"));
        tabLayout.addTab(tabLayout.newTab().setText("리뷰"));

        // 도트 색 지정
        indicator.setSelectedDotColor( Color.parseColor("#FFB300"));
        indicator.setUnselectedDotColor( Color.parseColor("#CFCFCF"));

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        reviewFab.setOnClickListener(this);
    }

    @Override
    public void showProgress() {
        progressDialog = Util.createProgressDialog(this);
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }


    @Override
    public void displayStorePictre(ArrayList arrayList) {
        storePicturePagerAdapter = new StorePicturePagerAdapter(arrayList, storePresenter);
        storePictureViewPager.setAdapter(storePicturePagerAdapter);
        indicator.setNumberOfItems(arrayList.size());

        storePictureViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setSelectedItem( storePictureViewPager.getCurrentItem(), true );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void navigateToPicture(ArrayList arrayList, int position) {
        Intent pic = new Intent(this, PictureActivity.class);
        pic.putExtra("picture", arrayList);
        pic.putExtra("index", position);
        startActivity(pic);
    }

    @Override
    public void navigateToMap(double x, double y) {
        Intent map = new Intent(this, MapActivity.class);
        map.putExtra("x", x);
        map.putExtra("y", y);
        startActivity(map);
    }

    @Override
    public void navigateToSeat(Store store) {
        Intent storeIntent = new Intent(this, SeatActivity.class);
        storeIntent.putExtra("store", store);
        startActivity(storeIntent);
    }


    @Override
    public void navigateToReviewWrite(String storeId) {
        Intent reviewWrite = new Intent(this, ReviewWriteActivity.class);
        reviewWrite.putExtra("storeId", storeId);
        startActivity(reviewWrite);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Information, Menu, Review ViewPager 초기화
     */
    private void setUpViewPager() {
        storePagerAdapter = new StorePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), storePresenter.getStoreId());
        storeViewPager.setAdapter(storePagerAdapter);
        storeViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                storeViewPager.setCurrentItem(tab.getPosition());
                changeFab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //Activity 에 관한 이벤트 처리 -----------------------------------------------------------------------------
    /**
     * 뒤로가기 버튼 눌렀을 시
     */
    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    /**
     * 기기 버튼(뒤로가기) 눌렀을 시
     */
    @Override
    public void onBackPressed() {
        this.finish();
    }

    /**
     * Appbar 메뉴 생성 초기화
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store, menu);
        return true;
    }

    /**
     * Appbar 메뉴 선택 이벤트
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //메뉴 추가
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            Toast.makeText(mContext, "공유하기 누름", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.action_favorite) {

            Toast.makeText(mContext, "즐겨찾기 누름", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * FAB 애니메이션
     * + 눌렀을 시 FAB 보여주기
     */
    public void animateFAB() {
        if (isFabOpen) {
            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);

            fab1.setClickable(false);
            fab2.setClickable(false);

            isFabOpen = false;

        } else {
            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);

            fab1.setClickable(true);
            fab2.setClickable(true);

            isFabOpen = true;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        }
    }

    /**
     * FAB 애니메이션
     * ReviewFragment 에서 변경
     */
    private void changeFab(int position) {
        switch (position) {
            case 0:
                fab.show();
                reviewFab.hide();
                break;
            case 1:
                fab.show();
                reviewFab.hide();
                break;
            case 2:
                fab.hide();
                if (isFabOpen) {
                    animateFAB();
                }
                reviewFab.show();
                break;
            default:
                fab.show();
                reviewFab.hide();
                break;
        }
    }

    // onClick 이벤트처리----------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId) {
            case R.id.storeFab:
                animateFAB();
                break;
            case R.id.storeFab1:
                storePresenter.navigateToSeat();
                break;
            case R.id.storeFab2:
                storePresenter.navigateToStoreMap();
                break;
            case R.id.storeReviewFab:

                storePresenter.navigateToReviewWrite();
                break;

        }
    }
}
