package sku.jvj.seatcock.Server;

import android.os.AsyncTask;

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

import sku.jvj.seatcock.Interactor.SeatInteractorImpl;
import sku.jvj.seatcock.Model.StoreSeat;

import static sku.jvj.seatcock.Interface.Seat.SeatInteractor.SEAT_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Seat.SeatInteractor.SEAT_NETWORK_SUCCESS;

/**
 * Created by Android Hong on 2016-09-04.
 */
public class SeatData{

    private SeatInteractorImpl.SeatHandler handler;
    private ArrayList<StoreSeat> storeSeatArrayList = new ArrayList<>();
    private JSONArray jsonArray = null;

    //php변수를 입력
    private static final String TAG_RESULTS = "result";

    private static final String TAG_SEAT_NUM = "seat_num";
    private static final String TAG_SEAT_X = "seat_x";
    private static final String TAG_SEAT_Y = "seat_y";
    private static final String TAG_SEAT_NOR_CHK = "seat_nor_chk";
    private static final String TAG_SEAT_ZONE_CHK = "seat_zone_chk";
    private static final String TAG_SEAT_STATUS = "seat_status";        // 0 이 미사용, 1이 사용

    private static final String TAG_WT = "wt";
    private static final String TAG_WAITING_STATUS = "wtstatus";        // 0이 비활성화, 1이 활성화

    private static final String URL = "http://14.63.213.157/dongimg/seat_count_v1.php";
    private String myJSONString;
    private boolean waitingCheck;

    /**
     * 생성자
     */
    public SeatData(SeatInteractorImpl.SeatHandler handler) {
        this.handler = handler;
    }

    /**
     * Store_id 매개변수로
     * 데이터 가지고와서 저장하기
     * @param store_id
     */
    public void getData(String store_id) {
        class GetData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String store_id = (String) params[0];
                    String data = URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");

                    URL url = new URL(URL);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line ;

                    // Read Server Response
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
                myJSONString = result;
                if (result.equals("failOfTimeOut")) {
                    handler.sendEmptyMessage(SEAT_NETWORK_FAILER);
                }else if(result.trim().equals("failure")){
                    handler.sendEmptyMessage(SEAT_NETWORK_FAILER);
                } else {
                    handler.sendEmptyMessage(SEAT_NETWORK_SUCCESS);
                }
            }

        }

        GetData getData  = new GetData();
        getData.execute(store_id);
    }

    /**
     * jSON 파싱한것 저장하기
     */
    public ArrayList<StoreSeat> setData() {
        storeSeatArrayList.clear();
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                StoreSeat storeSeat = new StoreSeat();
                storeSeat.setX(Integer.parseInt(c.getString(TAG_SEAT_X)));
                storeSeat.setY(Integer.parseInt(c.getString(TAG_SEAT_Y)));
                storeSeat.setNum(Integer.parseInt(c.getString(TAG_SEAT_NUM)));
                storeSeat.setSeatNormalCheck(c.getString(TAG_SEAT_NOR_CHK));
                storeSeat.setSeatZoneCheck(c.getString(TAG_SEAT_ZONE_CHK));
                storeSeat.setStatus(c.getString(TAG_SEAT_STATUS));

                storeSeatArrayList.add(storeSeat);
            }

            jsonArray = jsonObj.getJSONArray(TAG_WT);
            JSONObject c = jsonArray.getJSONObject(0);
            waitingCheck = watingCheck(c.getString(TAG_WAITING_STATUS));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return storeSeatArrayList;
    }

    public boolean watingCheck(String wating) {
        boolean result;
        if(wating.equals("0")){
            result = false;
        }else{
            result = true;
        }
        return result;

    }

    public boolean setWatingCheck() {
        return waitingCheck;
    }
}
