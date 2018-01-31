package sku.jvj.seatcock.Server;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import sku.jvj.seatcock.Interactor.BillInteractorImpl;
import sku.jvj.seatcock.Interactor.ReservationInteractorImpl;

import static sku.jvj.seatcock.Interface.Reservation.ReservationInteractor.RES_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Reservation.ReservationInteractor.RES_NETWORK_SUCCESS;

/**
 * Created by Android Hong on 2016-09-12.
 */
public class ReservationData  {


    public static final int RESERVATION_ZONE_SUCCESS = 0;
    public static final int RESERVATION_NORMAL_SUCCESS = 1;
    public static final int RESERVATION_FAILURE = 3;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private static final String GETURL = "http://14.63.213.157/dongimg/seat_rev_v4.0.php";
    private static final String INSERTZONEURL = "http://14.63.213.157/dongimg/insert_zone_v4.php";
    private static final String INSERTNORMALURL = "http://14.63.213.157/dongimg/insert_normal.php";

    //php변수를 입력
    private static final String TAG_RESULTS = "result";
    private static final String TAG_TIME = "time";
    private static final String TAG_END_TIME = "end_time";

    // 받아온 데이터
    private ArrayList<String> reservationTimeArrayList;
    private JSONArray jsonArray = null;

    private ReservationInteractorImpl.ReservationHandler handler;
    private String myJSONString;

    /**
     * 생성자
     */
    public ReservationData(ReservationInteractorImpl.ReservationHandler handler) {
        this.handler = handler;

    }

    /**
     * store_id, date, seat_num 으로 값 불러오기
     *
     * @param store_id
     * @param date
     * @param seat_num
     */
    public void getData(String store_id, String date, String seat_num) {
        class GetData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String store_id = (String) params[0];
                    String date = (String) params[1];
                    String seat_num = (String) params[2];

                    String data = URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");
                    data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                    data += "&" + URLEncoder.encode("seatnum", "UTF-8") + "=" + URLEncoder.encode(seat_num, "UTF-8");

                    URL url = new URL(GETURL);

                    URLConnection conn = url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setDoOutput(true);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (IllegalArgumentException e) {
                    return new String("failOfTimeOut");
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                myJSONString = result;
                if (result.equals("failOfTimeOut")) {
                    handler.sendEmptyMessage(RES_NETWORK_FAILER);
                } else {
                    handler.sendEmptyMessage(RES_NETWORK_SUCCESS);
                }
            }
        }

        GetData getData = new GetData();
        getData.execute(store_id, date, seat_num);
    }

    /**
     * getData()에서 파싱한 데이터를
     * storeArrayList 에 데이터를 저장하기
     */
    public ArrayList<String> setData() {
        Log.e("myJSONString",myJSONString);
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            reservationTimeArrayList = new ArrayList<>();
            reservationTimeArrayList.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                String time = c.getString(TAG_TIME);
                String endTime = c.getString(TAG_END_TIME);

                reservationTimeArrayList.add(time);

                String addTime = time;

                int hourTime = Integer.parseInt(endTime.substring(0, 2)) - Integer.parseInt(time.substring(0, 2));
                int minitTime = Integer.parseInt(endTime.substring(3, 5)) -  Integer.parseInt(time.substring(3, 5));

                if(minitTime == 0){
                    hourTime = hourTime * 2;
                }else{
                    if(minitTime < 0 ){
                        // hourTime 1 or 2
                        if(hourTime == 2) {
                            hourTime = hourTime + 1;
                        }
                    }else{
                        //hourTime 0 or 1
                        if(hourTime == 0){
                            hourTime  = hourTime + 1;
                        }else {
                            hourTime = hourTime + 2;
                        }
                    }
                }

                // j < x(여기서 x은 (시 * 2) - 1 이다.
                for (int j = 0; j < hourTime - 1 ; j++) {
                    int sh = Integer.parseInt(addTime.substring(0, 2));
                    int sm = Integer.parseInt(addTime.substring(3, 5));

                    if (sm == 30) {
                        sh++;
                        sm = 0;
                    } else {
                        sm += 30;
                    }
                    if (sh < 10 && sm != 30) {
                        addTime = "0" + sh + ":0" + sm;
                    } else if (sh >= 10 && sm == 30) {
                        addTime = "" + sh + ":" + sm;
                    } else if (sh < 10 && sm == 30) {
                        addTime = "0" + sh + ":" + sm;
                    } else {
                        addTime = "" + sh + ":0" + sm;
                    }
                    reservationTimeArrayList.add(addTime);
                }
            }

            //중복제거 시퀀스( ArrayList 는 중복 가능이라 HashSet 에 넣고 다시 뽑아온다. )
            HashSet hashSet = new HashSet();
            hashSet.addAll(reservationTimeArrayList);
            reservationTimeArrayList.clear();
            reservationTimeArrayList.addAll(hashSet);
            Collections.sort(reservationTimeArrayList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reservationTimeArrayList;
    }



    /**
     * 좌석 예약
     * @param store_id
     * @param seat_num
     * @param res_date
     * @param res_time
     * @param kakao_id
     * @param res_person
     */

    public static void insertToZoneReservation(String store_id, String seat_num, String res_date, String res_time, String kakao_id, String res_person, String res_endtime, final BillInteractorImpl.ReservationHandler reservationHandler) {
        class InsertData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if("success".equalsIgnoreCase(result.trim())){
                    //예약이 잘되면 핸들러에 메세지 보내기
                    reservationHandler.sendEmptyMessage(RESERVATION_ZONE_SUCCESS);
                }else{
                    //예약이 실패하면 핸들러에 메세지 보내기
                    reservationHandler.sendEmptyMessage(RESERVATION_FAILURE);
                }
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String store_id = (String) params[0];
                    String seat_num = (String) params[1];
                    String res_date = (String) params[2];
                    String res_time = (String) params[3];
                    String kakao_id = (String) params[4];
                    String res_person = (String) params[5];
                    String res_endtime = (String) params[6];

                    String data = URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");
                    data += "&" + URLEncoder.encode("seat_num", "UTF-8") + "=" + URLEncoder.encode(seat_num, "UTF-8");
                    data += "&" + URLEncoder.encode("res_date", "UTF-8") + "=" + URLEncoder.encode(res_date, "UTF-8");
                    data += "&" + URLEncoder.encode("res_time", "UTF-8") + "=" + URLEncoder.encode(res_time, "UTF-8");
                    data += "&" + URLEncoder.encode("kakao_id", "UTF-8") + "=" + URLEncoder.encode(kakao_id, "UTF-8");
                    data += "&" + URLEncoder.encode("res_person", "UTF-8") + "=" + URLEncoder.encode(res_person, "UTF-8");
                    data += "&" + URLEncoder.encode("res_endtime", "UTF-8") + "=" + URLEncoder.encode(res_endtime, "UTF-8");

                    URL url = new URL(INSERTZONEURL);
                    URLConnection conn = url.openConnection();

                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        InsertData insertData = new InsertData();
        insertData.execute(store_id,seat_num,res_date,res_time,kakao_id,res_person, res_endtime);

    }


    /**
     * 일반 예약
     * @param store_id
     * @param res_date
     * @param res_time
     * @param kakao_id
     * @param res_person
     */

    public static void insertToNormalReservation(String store_id, String res_date, String res_time, String kakao_id, String res_person,String res_endtime,final BillInteractorImpl.ReservationHandler reservationHandler) {
        class InsertData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if("success".equals(result.trim())){
                    //예약이 잘되면
                    reservationHandler.sendEmptyMessage(RESERVATION_NORMAL_SUCCESS);
                }else{
                    //예약이 실패하면
                    reservationHandler.sendEmptyMessage(RESERVATION_FAILURE);
                }
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String store_id = (String) params[0];
                    String res_date = (String) params[1];
                    String res_time = (String) params[2];
                    String kakao_id = (String) params[3];
                    String res_person = (String) params[4];
                    String res_endtime = (String) params[5];

                    String data = URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");
                    data += "&" + URLEncoder.encode("res_date", "UTF-8") + "=" + URLEncoder.encode(res_date, "UTF-8");
                    data += "&" + URLEncoder.encode("res_time", "UTF-8") + "=" + URLEncoder.encode(res_time, "UTF-8");
                    data += "&" + URLEncoder.encode("kakao_id", "UTF-8") + "=" + URLEncoder.encode(kakao_id, "UTF-8");
                    data += "&" + URLEncoder.encode("res_person", "UTF-8") + "=" + URLEncoder.encode(res_person, "UTF-8");
                    data += "&" + URLEncoder.encode("res_endtime", "UTF-8") + "=" + URLEncoder.encode(res_endtime, "UTF-8");

                    URL url = new URL(INSERTNORMALURL);
                    URLConnection conn = url.openConnection();

                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        InsertData insertData = new InsertData();
        insertData.execute(store_id,res_date,res_time,kakao_id,res_person, res_endtime);
    }

}
