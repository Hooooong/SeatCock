package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.util.helper.log.Logger;
import com.matthewtamlin.sliding_intro_screen_library.DotIndicator;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import sku.jvj.seatcock.Adapter.AdPagerAdapter;
import sku.jvj.seatcock.Adapter.StoreMainAdapter;
import sku.jvj.seatcock.Interface.Main.MainPresenter;
import sku.jvj.seatcock.Interface.Main.MainView;
import sku.jvj.seatcock.Model.Advertising;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Presenter.MainPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

import static sku.jvj.seatcock.Interface.Main.MainInteractor.REQUEST_CHECK_SETTINGS;


public class MainActivity extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    private long backKeyPressedTime = 0;

    public static Context mContext;
    public static Activity mActivity;

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private StoreMainAdapter storeMainAdapter;
    private FloatingActionButton fab;
    private ProgressDialog progressDialog;
    private MainPresenter mainPresenter;
    private ObservableScrollView observaleScrollView;
    private AdPagerAdapter adPagerAdapter;
    private Timer swipeTimer;
    private ViewPager advertisingViewPager;
    private DotIndicator indicator;
    private TextView myGPSAddress,  locationSearchTextView;
    public ActionBarDrawerToggle toggle;
    private RelativeLayout noSearchLayout;
    private Toast toast;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView userName, userPhone, noticeBellTextView;
    private ImageView userImage, notice;
    private LinearLayout noLoginLayout, LoginLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mActivity = this;

        mainPresenter = new MainPresenterImpl(this);
        initLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.googleApiDisconnect();
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        myGPSAddress = (TextView) findViewById(R.id.myGPSAddress);
        locationSearchTextView = (TextView) findViewById(R.id.locationSearchTextView);
        noSearchLayout = (RelativeLayout) findViewById(R.id.noSearchLayout);
        recyclerView = (RecyclerView) findViewById(R.id.storeRecyclerView);
        observaleScrollView = (ObservableScrollView) findViewById(R.id.observaleScrollView);
        advertisingViewPager = (ViewPager) findViewById(R.id.advertisingViewPager);
        indicator = (DotIndicator) findViewById(R.id.Ad_indicator_ad);
        fab.attachToScrollView(observaleScrollView);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                int noticeNum = mainPresenter.getNotiCount();
                if (noticeNum > 0) {
                    noticeBellTextView.setVisibility(View.VISIBLE);
                    noticeBellTextView.setText(Integer.toString(noticeNum));
                } else {
                    noticeBellTextView.setVisibility(View.GONE);
                }
                if (Session.getCurrentSession().isOpened()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getApplicationContext())
                                    .load(mainPresenter.getKakaoPrifile())
                                    .into(userImage);

                        }
                    });
                }
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // navigation Drawer 메뉴 생성
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View HeaderView = navigationView.getHeaderView(0);

        // navigationView 바인딩
        userName = (TextView) HeaderView.findViewById(R.id.userNameTextView);
        userImage = (ImageView) HeaderView.findViewById(R.id.userImageView);
        userPhone = (TextView) HeaderView.findViewById(R.id.userPhoneNumberTextView);
        notice = (ImageView) HeaderView.findViewById(R.id.noticeImageView);
        noticeBellTextView = (TextView) HeaderView.findViewById(R.id.noticeBellTextView);
        noLoginLayout = (LinearLayout) HeaderView.findViewById(R.id.noLoginLayout);
        LoginLayout = (LinearLayout) HeaderView.findViewById(R.id.LoginLayout);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        storeMainAdapter = new StoreMainAdapter(this);
        recyclerView.setAdapter(storeMainAdapter);


        navigationView.setNavigationItemSelectedListener(this);
        notice.setOnClickListener(this);
        navigationView.setOnClickListener(this);
        locationSearchTextView.setOnClickListener(this);
        fab.setOnClickListener(this);

    }

    // Interface Override -------------------------------------------------------------------------------
    @Override
    public void showProgress() {
        progressDialog = Util.createProgressDialog(this);
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showToast(String massage) {
        Toast.makeText(mContext, massage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isLogin(final KakaoUser kakaoUser) {
        if (kakaoUser != null) {
            // 로그인 한 경우
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    noLoginLayout.setVisibility(View.GONE);
                    LoginLayout.setVisibility(View.VISIBLE);
                    userName.setText(kakaoUser.getName());
                    userPhone.setText(kakaoUser.getPhoneNumber());

                    if ("noimage".equals(kakaoUser.getProfile_image())) {
                        userImage.setImageResource(R.drawable.user_icon);
                    } else {
                        Glide.with(getApplicationContext())
                                .load(kakaoUser.getProfile_image())
                                .into(userImage);
                    }
                    navigationView.getMenu().findItem(R.id.nav_user).setTitle("로그아웃");
                }
            });
        } else {
            // 로그인 안한 경우
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    noLoginLayout.setVisibility(View.VISIBLE);
                    LoginLayout.setVisibility(View.GONE);
                    navigationView.getMenu().findItem(R.id.nav_user).setTitle("로그인");
                }
            });

        }
    }

    @Override
    public void displayLocationText(double latitude, double longitude) {
        myGPSAddress.setText(Util.getChangeAddress(this, latitude, longitude));
    }

    @Override
    public void displayStore(ArrayList<Store> storeArrayList) {
        if (storeArrayList.size() > 0) {
            noSearchLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            storeMainAdapter.addData(storeArrayList);
            storeMainAdapter.notifyDataSetChanged();

            recyclerView.setNestedScrollingEnabled(false);
        } else {
            noSearchLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void displayAdvertising(ArrayList<Advertising> advertisingArrayList) {
        final int pageCount = advertisingArrayList.size();
        // 도트 색 지정
        indicator.setSelectedDotColor(Color.parseColor("#FFB300"));
        indicator.setUnselectedDotColor(Color.parseColor("#CFCFCF"));

        adPagerAdapter = new AdPagerAdapter(advertisingArrayList, this);
        advertisingViewPager.setAdapter(adPagerAdapter);

        indicator.setNumberOfItems(advertisingArrayList.size());

        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int currentPage = advertisingViewPager.getCurrentItem();
                                if (currentPage >= pageCount - 1) {
                                    advertisingViewPager.setCurrentItem(0, true);
                                } else {
                                    advertisingViewPager.setCurrentItem(currentPage + 1, true);
                                }
                                indicator.setSelectedItem((currentPage + 1 == pageCount) ? 0 : currentPage + 1, true);
                            }
                });
            }
        }, 1000, 5000);

        // viewPager 이동할 때 이벤트
        advertisingViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setSelectedItem(advertisingViewPager.getCurrentItem(), true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void navigateToStore(Store store) {
        Intent storeIntent = new Intent(this, StoreActivity.class);
        storeIntent.putExtra("store", store);
        startActivity(storeIntent);
    }

    @Override
    public void navigateToSeat(Store store) {
        Intent storeIntent = new Intent(this, SeatActivity.class);
        storeIntent.putExtra("store", store);
        startActivity(storeIntent);
    }

    @Override
    public void navigateToAdvertising(Advertising advertising) {
        Intent Ad = new Intent(this, AdActivity.class);
        Ad.putExtra("Advertising", advertising);
        startActivity(Ad);
    }

    @Override
    public void navigateToLocation(double latitude, double longitude) {
        Intent locationIntent = new Intent(this, LocationActivity.class);
        locationIntent.putExtra("latitude", latitude);
        locationIntent.putExtra("longitude", longitude);
        startActivityForResult(locationIntent, LOCATION_DATA);
    }

    @Override
    public void navigateToMyReservation() {
        Intent myReservationIntent = new Intent(MainActivity.mContext, MyReservationActivity.class);
        myReservationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(myReservationIntent);
    }

    @Override
    public void navigateToSearch() {
        Intent searchIntent = new Intent(this, SearchActivity.class);
        searchIntent.putExtra("latitude",mainPresenter.getLatitude());
        searchIntent.putExtra("longitude",mainPresenter.getLongitude());
        startActivity(searchIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                this.hideProgress();
                // 예 버튼 눌렀을 때 GPS 사용 가능
                mainPresenter.gpsConnect();
            } else {
                // 아니오 버튼 눌렀을 때
                this.hideProgress();
                Toast.makeText(getApplicationContext(), "위치 사용안하니까 다른 화면 보여줘야해", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == LOCATION_DATA) {
            if (resultCode == RESULT_OK) {
                double changeLatitude = 0;
                double changeLongitude = 0;
                changeLatitude = data.getDoubleExtra("latitude", changeLatitude);
                changeLongitude = data.getDoubleExtra("longitude", changeLongitude);
                mainPresenter.updateLocation(changeLatitude, changeLongitude);
                mainPresenter.getStoreData(changeLatitude, changeLongitude);
            }
        }
    }

    // onClick 메소드-------------------------------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId) {
            case R.id.locationSearchTextView:
                mainPresenter.presentLocation();
                break;
            case R.id.fab:
                this.navigateToSearch();
                break;
            case R.id.nav_view:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.noticeImageView:
                /*NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(300);*/
                Intent noticeIntent = new Intent(getApplicationContext(), NoticeActivity.class);
                startActivity(noticeIntent);
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
    }

    // navi onClick 메소드
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean checked = false;    //눌렀을 때 체크가 되어있어야하는지에 대한 변수
        if (id == R.id.nav_camera) {
            if (Session.getCurrentSession().isOpened()) {
                Intent myInfoIntent = new Intent(getApplicationContext(), MyInfoActivity.class);
                startActivity(myInfoIntent);
            } else {
                this.showToast("로그인을 먼저 해주세요");
            }
        } else if (id == R.id.nav_gallery) {
            if (Session.getCurrentSession().isOpened()) {
                final String appendMessage = getString(R.string.com_kakao_confirm_unlink);
                new AlertDialog.Builder(this)
                        .setMessage(appendMessage)
                        .setPositiveButton(getString(R.string.com_kakao_ok_button),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UserManagement.requestUnlink(new UnLinkResponseCallback() {
                                            @Override
                                            public void onFailure(ErrorResult errorResult) {
                                                Logger.e(errorResult.toString());
                                            }

                                            @Override
                                            public void onSessionClosed(ErrorResult errorResult) {

                                            }

                                            @Override
                                            public void onNotSignedUp() {
                                            }

                                            @Override
                                            public void onSuccess(Long userId) {
                                            }
                                        });
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
            } else {
                this.showToast("로그인을 먼저 해주세요");
                checked = false;
            }
        } else if (id == R.id.nav_slideshow) {
            if (Session.getCurrentSession().isOpened()) {
                /* 예약 정보에 들어갔을 때 PUSH 삭제하는 것 생각해보기.....

                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(100);*/

                navigateToMyReservation();
            } else {
                this.showToast("로그인을 먼저 해주세요");
                checked = false;
            }
        } else if (id == R.id.nav_manage) {
            if (Session.getCurrentSession().isOpened()) {
                checked = true;
            } else {
                this.showToast("로그인을 먼저 해주세요");
                checked = false;
            }
        } else if (id == R.id.nav_share) {
            if (Session.getCurrentSession().isOpened()) {
                checked = true;
            } else {
                this.showToast("로그인을 먼저 해주세요");
                checked = false;
            }
        } else if (id == R.id.nav_user) {
            if (Session.getCurrentSession().isOpened()) {
                mainPresenter.logout();
            } else {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return checked;
    }

    /**
     * 기기 버튼(뒤로가기) 눌렀을 시
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish();
                toast.cancel();
            }
        }
    }

    /**
     * 뒤로가기 버튼 이벤트
     */
    private void showGuide() {
        toast = Toast.makeText(this, "한번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT);
        toast.show();
    }

}


