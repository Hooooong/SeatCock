package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import sku.jvj.seatcock.Interface.Map.MapPresenter;
import sku.jvj.seatcock.Presenter.MapPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

import static sku.jvj.seatcock.Interface.Map.MapInteractor.REQUEST_CHECK_SETTINGS;

/**
 * Created by Android Hong on 2016-06-13.
 */
public class MapActivity extends AppCompatActivity implements sku.jvj.seatcock.Interface.Map.MapView {

    private ViewGroup mapViewContainer;
    private MapView mapView;
    private Toolbar toolbar;
    private TextView locationTextView;

    private MapPresenter mapPresenter;
    public static Activity mActivity;
    public static Context mContext;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initLayout();

        mapPresenter = new MapPresenterImpl(this);
        mapPresenter.createGoogleApi();
    }

    private void initLayout() {
        mActivity = this;
        mContext = this;

        locationTextView = (TextView) findViewById(R.id.locationTextView);
        toolbar = (Toolbar) findViewById(R.id.mapToolbar);
        mapViewContainer = (ViewGroup) findViewById(R.id.mapView);

        toolbar.setTitle("지도 보기");
        setSupportActionBar(toolbar);
    }

    /**
     * onDestroy 호출 - Activity가 종료될 때
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapPresenter.googleApiDisconnect();
    }

    /**
     * Appbar 메뉴 생성 초기화
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    /**
     * Appbar 메뉴 선택 이벤트
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_back) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 기기 버튼(뒤로가기) 눌렀을 시
     */
    @Override
    public void onBackPressed() {
        this.finish();
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
    public void displayLocationText(double latitude, double longitude) {
           locationTextView.setText(Util.getChangeDetailAddress(this,latitude, longitude));
    }

    @Override
    public void displayLocationMap(double storeLatitude, double storeLongitude, double myLatitude, double myLongitude) {
        double centerlati = (myLatitude + storeLatitude) / 2.0;
        double centerlong = (myLongitude + storeLongitude) / 2.0;

        mapView = new MapView(this);
        mapView.setDaumMapApiKey("eded91d546d8ade935e3fb3674e0782c");
        mapView.setHDMapTileEnabled(true);

        mapViewContainer.addView(mapView);

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(centerlati, centerlong), zoomLevel(myLatitude,myLongitude,storeLatitude,storeLongitude), false);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("나의 위치");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(myLatitude, myLongitude));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin); // 마커를 클릭했을때, 기본으로 제공하는 Blue 마커 모양.
        mapView.addPOIItem(marker);

        marker = new MapPOIItem();
        marker.setItemName("점포 위치");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(storeLatitude, storeLongitude));
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);

    }

    /**
     * GPS 설정 다이얼로그 확인/취소 눌렀을 시 결과
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // 예 버튼 눌렀을 때 GPS 사용 가능
                mapPresenter.gpsConnect();
            } else {
                // 아니오 버튼 눌렀을 때
                Toast.makeText(mActivity, "위치를 키지 않은 경우 내위치와 점포위치를 보실수 없습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    /**
     * 줌 레벨 구하기
     *
     * @return
     */
    public int zoomLevel(double myLatitude, double myLongitude, double storeLatitude, double storeLongitude) {
        double distance = calDistance(myLatitude, myLongitude, storeLatitude, storeLongitude);
        int zoomrevel;

        if (distance < 1000) {
            zoomrevel = 0;
        } else if (distance < 5000) {
            zoomrevel = 3;
        } else if (distance < 10000) {
            zoomrevel = 5;
        } else if (distance < 20000) {
            zoomrevel = 6;
        } else if (distance < 30000) {
            zoomrevel = 7;
        } else if (distance < 40000) {
            zoomrevel = 7;
        } else if (distance < 50000) {
            zoomrevel = 8;
        } else if (distance < 80000) {
            zoomrevel = 9;
        } else {
            zoomrevel = 10;
        }
        return zoomrevel;
    }

    /**
     * 내 위치와 점포 위치 좌표 비교
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    private double calDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta, dist;
        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

        return dist;
    }

    // 주어진 도(degree) 값을 라디언으로 변환
    private double deg2rad(double deg) {
        return (double) (deg * Math.PI / (double) 180d);
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad) {
        return (double) (rad * (double) 180d / Math.PI);
    }


}
