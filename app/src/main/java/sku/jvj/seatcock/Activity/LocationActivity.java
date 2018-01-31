package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import sku.jvj.seatcock.Interface.Location.LocationPresenter;
import sku.jvj.seatcock.Interface.Location.LocationView;
import sku.jvj.seatcock.Presenter.LocationPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

/**
 * Created by Android Hong on 2016-09-20.
 */
public class LocationActivity extends AppCompatActivity implements LocationView, View.OnClickListener,MapView.MapViewEventListener {

    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int REQUEST_SELECT_PLACE = 10;

    public static Context mContext;
    public static Activity mActivity;

    private ViewGroup mapViewContainer;
    private MapView mapView;
    private Toolbar toolbar;
    private ImageView locationRefreshImageView;
    private Button locationBtn,locationAddressSearchButton;
    private TextView locationTextView;
    private LocationPresenter locationPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mContext = this;
        mActivity = this;
        
        locationPresenter = new LocationPresenterImpl(this);
        initLayout();
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {

        toolbar = (Toolbar) findViewById(R.id.locationToolbar);
        locationTextView = (TextView)findViewById(R.id.locationTextView);
        mapViewContainer = (ViewGroup) findViewById(R.id.mapView);
        locationBtn = (Button)findViewById(R.id.locationBtn);

        locationAddressSearchButton = (Button)findViewById(R.id.locationAddressSearchButton);
        locationRefreshImageView = (ImageView)findViewById(R.id.locationRefreshImageView);

        toolbar.setTitle("위치 설정");
        setSupportActionBar(toolbar);

        mapView = new MapView(this);
        mapView.setMapTilePersistentCacheEnabled(true);
        mapView.setHDMapTileEnabled(true);
        mapView.setDaumMapApiKey("eded91d546d8ade935e3fb3674e0782c");
        mapViewContainer.addView(mapView);

        this.displayLocationMap(locationPresenter.getLatitude(),locationPresenter.getLongitude());

        mapView.setMapViewEventListener(this);
        locationBtn.setOnClickListener(this);
        locationRefreshImageView.setOnClickListener(this);
        locationAddressSearchButton.setOnClickListener(this);
    }

    @Override
    public void displayLocationText(double latitude, double longitude) {
        locationTextView.setText(Util.getChangeDetailAddress(this,latitude,longitude));
    }

    @Override
    public void displayLocationMap(double latitude, double longitude) {
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 1, false);
    }

    @Override
    public void navigationToMain(double latitude, double longitude) {
        Intent location = new Intent();
        location.putExtra("latitude", latitude);
        location.putExtra("longitude", longitude);
        setResult(RESULT_OK, location);//RESULT_OK를 돌려주면 MainActivity 에서 받는다.
        finish();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //메뉴 추가
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_back) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                locationPresenter.gpsRefresh();
            } else {
                locationTextView.setText("현재 위치를 사용할 수 없습니다.");
            }
        }else if(requestCode == REQUEST_SELECT_PLACE){
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng latLng = place.getLatLng();
                locationPresenter.updateLocation(latLng.latitude,latLng.longitude);
                this.displayLocationMap(latLng.latitude,latLng.longitude);
            }
        }
    }

    /**
     * onDestroy 호출 - Activity가 종료될 때
     */
    @Override
    protected void onDestroy() {
        locationPresenter.googleApiDisconnect();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId){
            case R.id.locationBtn:
                locationPresenter.presentLocation();
                break;
            case R.id.locationRefreshImageView:
                locationPresenter.googleApiConnect();
                break;
            case R.id.locationAddressSearchButton:
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
                            .build();
                    LatLng southwest = new LatLng(33.06,125.04);
                    LatLng northeast = new LatLng(38.27,131.52);

                    LatLngBounds southKoreaArea = new LatLngBounds(southwest,northeast);

                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_OVERLAY)
                            .setBoundsBias(southKoreaArea)
                            .setFilter(typeFilter)
                            .build(this);
                    startActivityForResult(intent, REQUEST_SELECT_PLACE);

                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    // MapView 이벤트--------------------------------------------------------------------------------------------------------
    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        final  MapPoint.GeoCoordinate geoCoordinate = mapPoint.getMapPointGeoCoord();
        double latitude = geoCoordinate.latitude;
        double longitude = geoCoordinate.longitude;

        locationPresenter.updateLocation(latitude,longitude);
        this.displayLocationText(latitude,longitude);
    }
}
