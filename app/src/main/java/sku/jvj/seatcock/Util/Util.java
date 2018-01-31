package sku.jvj.seatcock.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sku.jvj.seatcock.Activity.IntroActivity;
import sku.jvj.seatcock.Model.SeatTime;
import sku.jvj.seatcock.R;

/**
 * Created by Android Hong on 2016-08-11.
 */
public class Util {


    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    /**
     * WI-FI를 사용하고 있는지에 대한 메소드
     *
     * @param context
     * @return
     */
    public static boolean IsWifiAvailable(Context context) {
        boolean bConnect = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager m_NetConnectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = m_NetConnectMgr.getActiveNetworkInfo();

            if (info != null) {
                if (info.getType() == m_NetConnectMgr.TYPE_WIFI) {
                    bConnect = true;
                }
            }
        } else {
            ConnectivityManager m_NetConnectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = m_NetConnectMgr.getActiveNetworkInfo();

            if (info != null) {
                if (info.getType() == m_NetConnectMgr.TYPE_WIFI) {
                    bConnect = true;
                }
            }
        }
        return bConnect;
    }

    /***
     * 3G 데이터를 사용하고 있는지에 대한 메소드
     *
     * @param context
     * @return
     */
    public static boolean Is3GAvailable(Context context) {
        boolean bConnect = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager m_NetConnectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = m_NetConnectMgr.getActiveNetworkInfo();

            if (info != null) {
                if (info.getType() == m_NetConnectMgr.TYPE_MOBILE) {
                    bConnect = true;
                }
            }
        } else {
            ConnectivityManager m_NetConnectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = m_NetConnectMgr.getActiveNetworkInfo();

            if (info != null) {
                if (info.getType() == m_NetConnectMgr.TYPE_MOBILE) {
                    bConnect = true;
                }
            }
        }
        return bConnect;
    }


    /**
     * 돈 환산
     * @param str_money
     * @return
     */
    public static String getMoney(String str_money) {

        int money = Integer.parseInt(str_money);
        DecimalFormat df = new DecimalFormat("#,###");

        String strMoney = df.format(money) + "원";
        return strMoney;
    }

    /**
     * 지난시간 표시하기
     * @param source
     * @return
     */
    public static String formatTimeString(String source) {
        String stringDate = source;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(stringDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg ;
        if (diffTime < SEC) {
            // sec
            msg = "방금 전";
        } else if ((diffTime /= SEC) < MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= MIN) < HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= HOUR) < DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= DAY) < MONTH) {
            // month
            msg = (diffTime) + "달 전";
        } else {
            // year
            diffTime = (diffTime/MONTH);
            msg = (diffTime) + "년 전";
        }
        return msg;
    }

    /**
     * ProgressDialog
     *
     * @param mContext
     * @return
     */
    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        return dialog;
    }

    /**
     * 자세한 주소
     *
     * @param lat
     * @param lng
     * @return
     */
    public static String getChangeDetailAddress(final Context context, double lat, double lng) {
        String str = "현재 위치를 사용할 수 없습니다.";
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0) {
                    Address addr = address.get(0);
                    str = addr.getAdminArea() == null ? addr.getLocality() : addr.getAdminArea() + " "
                            + addr.getLocality() + " "
                            + addr.getThoroughfare() + " " + addr.getFeatureName();
                }
            }
        } catch (IOException e) {
            Log.e("MainActivity", "주소를 찾지 못하였습니다.");
            e.printStackTrace();
        }

        return str;
    }

    /**
     * 간단한주소 주소
     *
     * @param lat
     * @param lng
     * @return
     */
    public static String getChangeAddress(Context context, double lat, double lng) {
        String str = "현재 위치를 사용할 수 없습니다.";
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);

        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0) {

                    Address addr = address.get(0);
                    // 서을특별시 or 인천광역시 ~~구 ~~동 까지만
                    str = addr.getAdminArea() == null ? addr.getLocality() : addr.getAdminArea() + " "
                            + addr.getLocality() + " "
                            + addr.getThoroughfare() + " ";
                }
            }
        } catch (IOException e) {
            Log.e("MainActivity", "주소를 찾지 못하였습니다.");
            e.printStackTrace();
        }

        return str;
    }

    /**
     * 내위치에서 얼마나 걸리는지 체크
     *
     * @param check
     * @return
     */
    public static String distanceCheck(int check) {
        String result;
        int distance = check;
        if (distance >= 1000) {
            result = Integer.toString((int) Math.round(distance / 1000.0)) + "km";
        } else {
            result = check + "m";
        }
        return result;
    }

    /**
     * 최대값 메소드
     * @param count5
     * @param count4
     * @param count3
     * @param count2
     * @param count1
     * @return
     */
    public static int checkMax(int count5, int count4, int count3, int count2, int count1) {
        List<Integer> i = Arrays.asList(count5, count4, count3, count2, count1);
        return Collections.max(i);
    }

    /**
     * 파일 이름 때어내기
     * @param Path
     * @return
     */
    public static String getFilePath(String Path) {
        String str = Path.substring(0,Path.lastIndexOf("/")+1);
        return str;
    }

    /**
     * 파일 경로 때어내기
     * @param Path
     * @return
     */
    public static String getFileName(String Path) {
        String str = Path.substring((Path.lastIndexOf("/")+1),Path.length());
        return str;
    }

    /**
     * 사용 시간 리스트 만들기
     * @return
     */
    public static CharSequence[]    charUseTime(String storeMaxTime) {
        int timeSize = Integer.parseInt(storeMaxTime);
        CharSequence[] useTime = new CharSequence[timeSize];
        String time;

        int sh = 0;
        int sm = 0;

        for( int i =0; i < timeSize; i++) {

            if(sm == 30 ) {
                sh++;
                sm = 0;
            }else {
                sm += 30;
            }

            if(sh == 0){
                time =  sm +"분";
            }else if(sm == 0){
                time =  sh +"시간";
            }else{
                time =  sh +"시간 " + sm + "분";
            }

            useTime[i] = time;
        }

        return useTime;
    }


    /**
     * 시간 사이즈 구하기
     * @param start
     * @param finish
     * @return
     */
    public static int timeSize(String start, String finish ) {

        int timeSize = 0;

        // 시작 시간 및 분
        int sh = Integer.parseInt(start.substring(0,2));
        int sm = Integer.parseInt(start.substring(3,5));

        // 종료 시작 및 분
        int fh = Integer.parseInt(finish.substring(0,2));
        int fm = Integer.parseInt(finish.substring(3,5));

        // 시간 및 분을 저장하는 변수
        int hsize = 0;
        int msize = 0;

        hsize = fh - sh ;

        if(fm > sm) {
            msize++;
        }else if(fm < sm) {
            hsize--;
            msize++;
        }else {
        }

        timeSize = (hsize *2)  + 1 + msize - 4;     // 4에 점포 최대 예약 시간 빼기임
        return timeSize;
    }

    /**
     * 점포 시간 테이블 만들기
     * @param start
     * @param timeSize
     * @return
     */
    public static ArrayList<SeatTime> storeTime(String start, int timeSize) {
        ArrayList<SeatTime> storeTime =  new ArrayList<>();

        String time = start;
        // 시작 시간 및 분
        int sh = Integer.parseInt(time.substring(0,2));
        int sm = Integer.parseInt(time.substring(3,5));

        String setshsm ;

        for( int i =0; i < timeSize; i++) {
            SeatTime seatTime = new SeatTime();
            if (sh < 10 && sm != 30) {
                setshsm = "0" + sh + ":0" + sm;
            } else if (sh >= 10 && sm == 30) {
                setshsm = "" + sh + ":" + sm;
            } else if (sh < 10 && sm == 30) {
                setshsm = "0" + sh + ":" + sm;
            } else {
                setshsm = "" + sh + ":0" + sm;
            }
            seatTime.setTime(setshsm);

            storeTime.add(seatTime);
            if(sm == 30 ) {
                sh++;
                sm = 0;
            }else {
                sm += 30;
            }
        }

        return storeTime;
    }


    /**
     * 끝나는 시간 계산
     * @param time
     * @param useTime
     * @return
     */
    public static String timeChoice(String time, String useTime){
        String result;

        // 선택한 시작 시간 및 분
        int ch = Integer.parseInt(time.substring(0,2));
        int cm = Integer.parseInt(time.substring(3,5));

        // 이용 시간 시간 및 분
        int uh = Integer.parseInt(useTime.substring(0,2));
        int um = Integer.parseInt(useTime.substring(3,5));

        int hour  = ch+uh;
        int min = cm+um;

        // 분 합친게 60분이면 시간 1 올려주기
        if(min == 60){
            hour ++;
            min = 0;
        }

        if (hour < 10 && min != 30) {
            result = "0" + hour + ":0" + min;
        } else if (hour >= 10 && min == 30) {
            result = "" + hour + ":" + min;
        } else if (hour < 10 && min == 30) {
            result = "0" + hour + ":" + min;
        } else {
            result = "" + hour + ":0" + min;
        }

        return result;
    }


    /**
     * 예약시 PUSH 날리기
     * @param context
     * @param storeName
     * @param reservationDate
     * @param reservationTime
     * @param person
     * @param checkNum
     */
    public static void reservationPush(Context context,String storeName,String storeId, String reservationDate, String reservationTime, String person, String checkNum){

        Intent pushIntent = new Intent(context, IntroActivity.class);
        pushIntent.putExtra("pushRequestCode",100);

        // 모든 Activity 제거
        pushIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, pushIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(context);
        mBuilder.setSmallIcon(R.drawable.app_icon);
        mBuilder.setTicker("'"+storeName + "' 의 예약이 완료되었습니다.");
        mBuilder.setContentTitle("예약 완료");
        mBuilder.setContentText("'"+storeName + "' 의 예약이 완료되었습니다.");
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.InboxStyle style = new Notification.InboxStyle(mBuilder);
        style.addLine("점포 : " +storeName);

        if(checkNum != null) {
            style.addLine("Table No. " + checkNum);
        }

        style.addLine("날짜 : " + reservationDate);
        style.addLine("시간 : " + reservationTime);
        style.addLine("인원수 : " + person);

        //style.setSummaryText("+ 3 More");

        mBuilder.setStyle(style);

        nm.notify(Integer.parseInt(storeId), mBuilder.build());
    }


    /**
     * 대기번호 발급시 PUSH 날리기
     * @param context
     * @param storeName
     * @param waitingNum
     * @param waitingTime
     * @param person
     */
    public static void waitingPush(Context context,String storeName,String waitingNum, String waitingTime, String person){

        Intent pushIntent = new Intent(context, IntroActivity.class);
        pushIntent.putExtra("pushRequestCode",200);

        // 모든 Activity 제거
        pushIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, pushIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(context);
        mBuilder.setSmallIcon(R.drawable.app_icon);
        mBuilder.setTicker("'"+storeName + "'의 대기번호가 발급되었습니다.");
        mBuilder.setContentTitle("대기번호 발급 완료");
        mBuilder.setContentText("'"+storeName + "'의 대기번호 발급이 완료되었습니다.");
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.InboxStyle style = new Notification.InboxStyle(mBuilder);
        style.addLine("점포 : " +storeName);
        style.addLine("대기번호 :  " + waitingNum);
        style.addLine("예상 대기시간 : 약 " + waitingTime);
        style.addLine("인원수 : " + person + " 명");

        //style.setSummaryText("+ 3 More");

        mBuilder.setStyle(style);

        nm.notify(200, mBuilder.build());
    }
}


