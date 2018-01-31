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

import sku.jvj.seatcock.Interactor.MyInfoInteractorImpl;
import sku.jvj.seatcock.Model.StoreReview;

import static sku.jvj.seatcock.Interface.MyInfo.MyInfoInteractor.MYREVIEWDEL_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.MyInfo.MyInfoInteractor.MYREVIEWDEL_NETWORK_SUCCESS;
import static sku.jvj.seatcock.Interface.MyInfo.MyInfoInteractor.MYREVIEW_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.MyInfo.MyInfoInteractor.MYREVIEW_NETWORK_SUCCESS;
import static sku.jvj.seatcock.Interface.MyInfo.MyInfoInteractor.PROFILE_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.MyInfo.MyInfoInteractor.PROFILE_NETWORK_SUCCESS;


/**
 * Created by Android Hong on 2016-09-06.
 */
public class MyReviewData {

    private final MyInfoInteractorImpl.MyInfo handler;

    private JSONArray jsonArray = null;


    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    //php변수를 입력
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_RV_NUM = "rv_num";
    private static final String TAG_STORE_ID = "store_id";
    private static final String TAG_KAKAO_NAME = "kakao_name";
    private static final String TAG_PROFILEPATH = "profilepath";
    private static final String TAG_RV_CON = "rv_con";
    private static final String TAG_AVG_GPA = "avg_gpa";
    private static final String TAG_RV_DAY = "rv_day";
    private static final String TAG_RVPIC_DATA1 = "RVPIC_data1";
    private static final String TAG_RVPIC_DATA2 = "RVPIC_data2";
    private static final String TAG_RVPIC_DATA3 = "RVPIC_data3";

    private static final String URL = "http://14.63.213.157/dongimg/myreview.php";
    private static final String URLDEL = "http://14.63.213.157/dongimg/delete_review.php";
    private static final String PROFILEURL = "http://14.63.213.157/dongimg/update_profile.php";
    private String myJSONString;

    private ArrayList<String> pictureArrayList;
    private ArrayList<StoreReview> storeReviewArrayList;

    /**
     * 생성자
     */
    public MyReviewData(MyInfoInteractorImpl.MyInfo handler) {
        this.handler =handler;
    }

    /**
     * KAKAO ID 로 리뷰 불러오는 메소드
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
                    java.net.URL url = new URL(URL);
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
                }catch (IllegalArgumentException e ){
                    return new String("failOfTimeOut");
                }catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String result) {
                Log.e("result123",result);
                myJSONString = result;
                if(result.trim().equals("failOfTimeOut")){
                    handler.sendEmptyMessage(MYREVIEW_NETWORK_FAILER);
                }else{
                    handler.sendEmptyMessage(MYREVIEW_NETWORK_SUCCESS);
                }
            }
        }

        GetData getData = new GetData();
        getData.execute(kakao_id);
    }


    public ArrayList<StoreReview> setData() {
        storeReviewArrayList = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                StoreReview storeReview = new StoreReview();
                storeReview.setStoreName(c.getString(TAG_NAME));
                storeReview.setStoreAddress(c.getString(TAG_ADDRESS));
                storeReview.setStoreId(c.getString(TAG_STORE_ID));
                storeReview.setReviewNum(Integer.parseInt(c.getString(TAG_RV_NUM)));
                storeReview.setKakaoName(c.getString(TAG_KAKAO_NAME));
                storeReview.setKakaoProfile(c.getString(TAG_PROFILEPATH));
                storeReview.setContent(c.getString(TAG_RV_CON));
                storeReview.setGpa(Float.parseFloat(c.getString(TAG_AVG_GPA)));
                storeReview.setDate(c.getString(TAG_RV_DAY));

                if (c.getString(TAG_RVPIC_DATA1).equals("0")) {
                    pictureArrayList = null;
                } else {
                    pictureArrayList = new ArrayList<>();
                    pictureArrayList.add(checkPicture(c.getString(TAG_RVPIC_DATA1)));
                    if (!c.getString(TAG_RVPIC_DATA2).equals("0")) {
                        pictureArrayList.add(checkPicture(c.getString(TAG_RVPIC_DATA2)));
                        if (!c.getString(TAG_RVPIC_DATA3).equals("0")) {
                            pictureArrayList.add(checkPicture(c.getString(TAG_RVPIC_DATA3)));
                        }
                    }
                    storeReview.setReviewPicture(pictureArrayList);
                }
                storeReviewArrayList.add(storeReview);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return storeReviewArrayList;
    }

    /**
     * 프로필 업데이트
     * @param profilepath
     */
    public  void profileUpdateData(String kakao_id,String profilepath) {
        class UpdateData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String kakao_id = (String) params[0];
                    String profilepath = (String) params[1];

                    String data = URLEncoder.encode("kakao_id", "UTF-8") + "=" + URLEncoder.encode(kakao_id, "UTF-8");
                    data += "&" + URLEncoder.encode("profilepath", "UTF-8") + "=" + URLEncoder.encode(profilepath, "UTF-8");


                    java.net.URL url = new URL(PROFILEURL);
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

                }catch (IllegalArgumentException e ){
                    return new String("failOfTimeOut");
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String result){
                Log.e("result123456",result);
                if(result.trim().equals("success")){
                    handler.sendEmptyMessage(PROFILE_NETWORK_SUCCESS);
                }else{
                    handler.sendEmptyMessage(PROFILE_NETWORK_FAILER);
                }
            }
        }

        UpdateData updateData = new UpdateData();
        updateData.execute(kakao_id,profilepath);
    }

    /**
     * 리뷰 삭제하기
     * @param rv_num
     */
    public void deleteToReview(int rv_num){
        class DeleteData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String rv_num = (String) params[0];

                    String data = URLEncoder.encode("rv_num", "UTF-8") + "=" + URLEncoder.encode(rv_num, "UTF-8");
                    URL url = new URL(URLDEL);

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

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }catch (IllegalArgumentException e ){
                    return new String("failOfTimeOut");
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String result) {
                Log.e("result",result);
                if(result.trim().equals("success")){
                    handler.sendEmptyMessage(MYREVIEWDEL_NETWORK_SUCCESS);
                }else{
                    handler.sendEmptyMessage(MYREVIEWDEL_NETWORK_FAILER);
                }

            }
        }
        DeleteData deleteData = new DeleteData();
        deleteData.execute(Integer.toString(rv_num));
    }

    /**
     * 사진유무 체크
     * @param source
     * @return
     */
    private String checkPicture(String source) {
        String result;
        if (source.equals("0")) {
            result = "0";
        } else {
            result = "http://14.63.213.157/dongimg/reviewimg/" + source;
        }
        return result;
    }
}
