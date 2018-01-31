package sku.jvj.seatcock.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;


/**
 * Created by Android Hong on 2016-05-30.
 */
public class IntroActivity extends Activity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int REQUEST_PERMISSION_SETTING = 2;

    public static Context mContext;

    private ImageView introImageView;
    private int permissionCheck1;
    private int permissionCheck2;

    private int pushRequestCode = 0;
    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_intro);

        Intent pushIntent = getIntent();
        pushRequestCode = pushIntent.getIntExtra("pushRequestCode",pushRequestCode);

        introImageView = (ImageView)findViewById(R.id.introImageView);
        Glide.with(this).load(R.drawable.img_intro).into(introImageView);

        Log.i("Util.IsWifiAvailable", Boolean.toString(Util.IsWifiAvailable(this)));
        Log.i("Util.Is3GAvailable", Boolean.toString(Util.Is3GAvailable(this)));

        handler = new Handler(); //딜래이를 주기 위해 핸들러 생성
        checkLocationPermission(2000);

    }

    /**
     * Permission 검사
     * @param time
     * time은 delay 시간을 나타낸다.
     */
    private void checkLocationPermission(final int time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(time);

                    permissionCheck1 = ContextCompat.checkSelfPermission(IntroActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                    permissionCheck2 = ContextCompat.checkSelfPermission(IntroActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
                    if(permissionCheck1 == PackageManager.PERMISSION_DENIED && permissionCheck2 == PackageManager.PERMISSION_DENIED) {
                        // 이 권한을 필요한 이유를 설명해야하는가?
                        ActivityCompat.requestPermissions(IntroActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    }else{
                        handler.postDelayed(run, 1000); // 딜레이 ( 러머블 객체는 run, 시간 2초)
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handler.postDelayed(run, 1000); // 딜레이 ( 러머블 객체는 run, 시간 2초)
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(IntroActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(IntroActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        Toast.makeText(IntroActivity.this, "권한설정을 해야만 자리콕을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        showPermissionDialog();
                    }
                }
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_PERMISSION_SETTING:
                checkLocationPermission(1000);
                break;
        }
    }


    /**
     * 알람 값 확인해서 시작
     * pushRequestCode = 0   : 일반 실행
     * pushRequestCode = 100 : 예약관련
     * pushRequestCode = 200 : 대기번호 관련
     */
    Runnable run = new Runnable() {
        @Override
        public void run() {
            if (Util.IsWifiAvailable(getApplicationContext()) || Util.Is3GAvailable(getApplicationContext())) {
                if(pushRequestCode == 0){
                    Intent pushIntent = new Intent(IntroActivity.this, MainActivity.class);
                    pushIntent.putExtra("pushRequestCode",pushRequestCode);
                    // 실행되어지고 있는 모든 Activity 제거
                    pushIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(pushIntent);
                    finish();

                }else {
                    Intent pushIntent = new Intent(IntroActivity.this, MainActivity.class);
                    pushIntent.putExtra("pushRequestCode",pushRequestCode);
                    // 실행되어지고 있는 모든 Activity 제거
                    pushIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(pushIntent);
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "네크워크에 연결할 수 없습니다.", Toast.LENGTH_LONG).show();
                finish();
            }

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            //overridePendingTransition 이란 함수를 이용하여 fade in,out 효과를줌. 순서가 중요
        }
    };


    /**
     * Permission 를 바꾸는
     */
    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("권한 설정");
        builder.setMessage("[정보] -> [권한] 에서 위치 권한을 설정하시겠습니까? 설정을 하지 않은 경우 '자리 콕'을 사용할 수 없습니다.");

        String positiveText = "설정";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", IntroActivity.this.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
            }
        });

        String negativeText = "취소";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(IntroActivity.this, "권한설정을 해야만 자리콕을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
    }

    //인트로 중에 뒤로가기를 누를 경우 핸들러를 끊어버려 아무일 없게 만드는 부분
    //미 설정시 인트로 중 뒤로가기를 누르면 인트로 후에 홈화면이 나옴.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(run);
    }

}

