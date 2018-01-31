package sku.jvj.seatcock.Util;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import sku.jvj.seatcock.Activity.IntroActivity;
import sku.jvj.seatcock.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    NoticeDBHelper noticeDbHelper = new NoticeDBHelper(this, "tbl_noti.db",null, 1);

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        Log.d(TAG, "From: " + remoteMessage.getFrom().toString());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            /**
             * 알람 값 확인해서 시작
             * pushRequestCode = 0   : 일반 실행
             * pushRequestCode = 100 : 예약관련
             * pushRequestCode = 200 : 대기번호 관련
             * pushRequsetCode = 300 : 공지사항 관련
             */
            int pushRequestCode = Integer.parseInt(remoteMessage.getData().get("pushRequestCode"));

            /**
             * 조건 걸어서 100. 예약 푸시, 200. 대기번호 푸시, 300:공지사항
             */
            if(pushRequestCode == 100){
                String title = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("message");
                String storeId = remoteMessage.getData().get("storeId");
                String reservationDate = remoteMessage.getData().get("reservationDate");
                String reservationTime = remoteMessage.getData().get("reservationTime");
                String person = remoteMessage.getData().get("person");
                String checkNum = remoteMessage.getData().get("checkNum");

                reservationPush(pushRequestCode, title,message, storeId,reservationDate,reservationTime, person,checkNum);

                Log.e("type",pushRequestCode + ", " + title + ", "+ message +", " + ", " + storeId +", " + reservationDate +", " +reservationTime +", " +person+", " +checkNum);
            }else if(pushRequestCode == 200){
                String title = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("message");
                String storeId = remoteMessage.getData().get("storeId");
                String waitnum = remoteMessage.getData().get("waitnum");

                waitingPush(pushRequestCode,title,message,waitnum,storeId);

                Log.e("type",pushRequestCode + ", " + title + ", "+ message +", " + ", " + waitnum  +", " + storeId);
            }else if(pushRequestCode == 300){
                String title = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("message");
                String idx = remoteMessage.getData().get("idx");

                sendNotice(title,message,idx); // 제목과 내용
                Log.e("type",pushRequestCode + ", " + title + ", "+ message +", " + idx);

            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    /**
     * 공지사항 PUSH
     * @param title
     * @param messageBody
     * @param idx
     */
    private void sendNotice(String title, String messageBody, String idx) {
        Intent noticeIntent = new Intent(this, IntroActivity.class);
        /**
         * 공지사항일 경우 ( 앞에 있던 내용들 그대로 유지 )
         */
        noticeDbHelper.insertnoti(idx);

        noticeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, noticeIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(300 , notificationBuilder.build());
    }

    /**
     * 예약 PUSH
     * @param pushRequestCode
     * @param title
     * @param messageBody
     * @param storeId
     * @param reservationDate
     * @param reservationTime
     * @param person
     * @param checkNum
     */
    public  void reservationPush(int pushRequestCode, String title, String messageBody, String storeId, String reservationDate, String reservationTime, String person, String checkNum){

        Intent noticeIntent = new Intent(this, IntroActivity.class);
        noticeIntent.putExtra("pushRequestCode",pushRequestCode);

        // 모든 Activity 제거
        noticeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, noticeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setSmallIcon(R.drawable.app_icon);
        mBuilder.setTicker("예약시간 " + messageBody + "전 입니다.");
        mBuilder.setContentTitle(title + " 알림");
        mBuilder.setContentText("예약시간 " + messageBody + "전 입니다.");
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.InboxStyle style = new Notification.InboxStyle(mBuilder);
        style.addLine("점포 : " + title);

        if(checkNum != null) {
            style.addLine("Table No. " + checkNum);
        }

        style.addLine("날짜 : " + reservationDate);
        style.addLine("시간 : " + reservationTime);
        style.addLine("인원수 : " + person);

        mBuilder.setStyle(style);
        nm.notify(Integer.parseInt(storeId), mBuilder.build());
    }

    /**
     * 대기번호 PUSH
     * @param pushRequestCode
     * @param title
     * @param messageBody
     * @param waitnum
     * @param storeId
     */
    public  void waitingPush(int pushRequestCode, String title, String messageBody, String waitnum, String storeId){
        Intent noticeIntent = new Intent(this, IntroActivity.class);
        noticeIntent.putExtra("pushRequestCode",pushRequestCode);
        Notification.Builder mBuilder;
        // 모든 Activity 제거
        noticeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, noticeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(Integer.parseInt(messageBody) == 0){
            mBuilder = new Notification.Builder(this);
            mBuilder.setSmallIcon(R.drawable.app_icon);
            mBuilder.setTicker("현재 대기번호가 회원님 차례입니다.");
            mBuilder.setContentTitle(title + " 알림");
            mBuilder.setContentText("현재 대기번호가 회원님 차례입니다. 3 ~ 5분안에 오시기 바랍니다.");
            mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setAutoCancel(true);
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
        }else{
            mBuilder = new Notification.Builder(this);
            mBuilder.setSmallIcon(R.drawable.app_icon);
            mBuilder.setTicker("대기번호 " + messageBody + "번째 전 입니다.");
            mBuilder.setContentTitle(title + " 알림");
            mBuilder.setContentText("대기번호 " + messageBody + "번째 전 입니다.");
            mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setAutoCancel(true);
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.InboxStyle style = new Notification.InboxStyle(mBuilder);
        style.addLine("점포 : " + title);
        style.addLine("현재 대기번호 : " + waitnum + " 번");
        style.addLine("나의 대기번호 : " + Integer.toString((Integer.parseInt(messageBody)+Integer.parseInt(waitnum))) + " 번");

        mBuilder.setStyle(style);
        nm.notify(Integer.parseInt(storeId), mBuilder.build());
    }


}




























