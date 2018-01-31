package sku.jvj.seatcock.Server;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sku.jvj.seatcock.Interactor.MyReservationInteractorImpl;
import sku.jvj.seatcock.Model.StoreReservation;

import static sku.jvj.seatcock.Interface.MyReservation.MyReservationInteractor.CHANGE_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.MyReservation.MyReservationInteractor.CHANGE_NETWORK_SUCCESS;
import static sku.jvj.seatcock.Interface.MyReservation.MyReservationInteractor.RESERVATION_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.MyReservation.MyReservationInteractor.RESERVATION_NETWORK_SUCCESS;

/**
 * Created by Android Hong on 2016-09-06.
 */
public class MyReservationData {

    private MyReservationInteractorImpl.MyReservationHandler handler;

    public JSONArray jsonArray;

    private ArrayList<StoreReservation> myBeforeReservationArrayList;
    private ArrayList<StoreReservation> myNowReservationArrayList;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    //php변수를 입력
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_RES_DATE = "res_date";
    private static final String TAG_RES_TIME = "res_time";
    private static final String TAG_END_TIME = "end_time";
    private static final String TAG_PHONE_NUM = "phone_num";
    private static final String TAG_RES_PERSON = "res_person";
    private static final String TAG_SEAT_NUM = "seat_num";
    private static final String TAG_COMPLETE = "complete";
    private static final String TAG_STORE_ID = "store_id";
    private static final String TAG_ZNCHK = "znchk";

    private static final String GETURL = "http://14.63.213.157/dongimg/myreservation_v1.0.php";
    private static final String UPDATEURL = "http://14.63.213.157/dongimg/update_res.php";
    private String myJSONString;

    /**
     * 생성자
     */
    public MyReservationData(MyReservationInteractorImpl.MyReservationHandler handler) {
        this.handler = handler;
    }

    /**
     * http://14.63.213.157/dongimg/myreservation.php 에서
     * kakao_id로 데이터 값을 불러와 JSON 파싱하기
     */
    public void getData(String kakao_id) {
        class GetData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String kakao_id = (String) params[0];

                    String data = URLEncoder.encode("kakao_id", "UTF-8") + "=" + URLEncoder.encode(kakao_id, "UTF-8");

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
                } catch (IOException e) {
                    return new String("Exception : " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSONString = result;
                if (result.trim().equals("failOfTimeOut")) {
                    handler.sendEmptyMessage(RESERVATION_NETWORK_FAILER);
                } else {
                    handler.sendEmptyMessage(RESERVATION_NETWORK_SUCCESS);
                }
            }
        }

        GetData getData = new GetData();
        getData.execute(kakao_id);

    }

    /**
     * getData()에서 파싱한 데이터를
     * complete에 따라  ArrayList에 데이터를 저장하기
     */
    public ArrayList<StoreReservation> setData(String reservationType) {

        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            myBeforeReservationArrayList = new ArrayList<>();
            myNowReservationArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                StoreReservation storeReservation = new StoreReservation();
                storeReservation.setDate(c.getString(TAG_RES_DATE));
                storeReservation.setTime(c.getString(TAG_RES_TIME) + " ~ " + c.getString(TAG_END_TIME));
                storeReservation.setStoreName(c.getString(TAG_NAME));
                storeReservation.setStoreAddress(c.getString(TAG_ADDRESS));
                storeReservation.setSeatNum(c.getString(TAG_SEAT_NUM));
                storeReservation.setStoreId(c.getString(TAG_STORE_ID));
                storeReservation.setPerson(Integer.parseInt(c.getString(TAG_RES_PERSON)));
                storeReservation.setStorePhoneNumber(c.getString(TAG_PHONE_NUM));
                storeReservation.setComplete(c.getString(TAG_COMPLETE));
                storeReservation.setZnCheck(c.getString(TAG_ZNCHK));

                if (storeReservation.isComplete()) {
                    //complete 0이 현재 예약중
                    myNowReservationArrayList.add(storeReservation);
                } else {
                    //complete 1이 지난 예약중
                    myBeforeReservationArrayList.add(storeReservation);
                }

                Collections.sort(myNowReservationArrayList, new Comparator<StoreReservation>() {
                    @Override
                    public int compare(StoreReservation lhs, StoreReservation rhs) {
                        return (lhs.getDate() + lhs.getTime()).compareToIgnoreCase(rhs.getDate() + rhs.getTime());
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            e.getMessage();
        }

        if(reservationType.equals("beforeReservation")){
            return myBeforeReservationArrayList;
        }else{
            return myNowReservationArrayList;
        }
    }

    /**
     * 예약 Complete 바꾸기
     * @param kakao_id
     * @param date
     * @param time
     * @param store_id
     * @param znCheck
     */
    public void changeReservationData(String kakao_id, String date, String time, String store_id, String znCheck) {
        class UpdateData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String kakao_id = (String) params[0];
                    String date = (String) params[1];
                    String time = (String) params[2];
                    String store_id = (String) params[3];
                    String znCheck = (String) params[4];

                    String data = URLEncoder.encode("kakao_id", "UTF-8") + "=" + URLEncoder.encode(kakao_id, "UTF-8");
                    data += "&" + URLEncoder.encode("res_date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                    data += "&" + URLEncoder.encode("res_time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
                    data += "&" + URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");
                    data += "&" + URLEncoder.encode("znchk", "UTF-8") + "=" + URLEncoder.encode(znCheck, "UTF-8");

                    URL url = new URL(UPDATEURL);
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
                if (result.trim().equals("success")) {
                    handler.sendEmptyMessage(CHANGE_NETWORK_SUCCESS);
                } else {
                    handler.sendEmptyMessage(CHANGE_NETWORK_FAILER);
                }
            }
        }

        UpdateData updateData = new UpdateData();
        updateData.execute(kakao_id, date, time, store_id, znCheck);
    }

}
