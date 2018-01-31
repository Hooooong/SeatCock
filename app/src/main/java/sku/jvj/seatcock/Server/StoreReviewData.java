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

import sku.jvj.seatcock.Interactor.ReviewInteractorImpl;
import sku.jvj.seatcock.Model.StoreReview;

import static sku.jvj.seatcock.Interface.Store.Review.ReviewInteractor.REVIEW_DELETE_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Store.Review.ReviewInteractor.REVIEW_DELETE_NETWORK_SUCCESS;
import static sku.jvj.seatcock.Interface.Store.Review.ReviewInteractor.REVIEW_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Store.Review.ReviewInteractor.REVIEW_NETWORK_SUCCESS;

/**
 * Created by Android Hong on 2016-08-31.
 */
public class StoreReviewData {

    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 15000;
    private static final int REVIEW_DETAIL_NETWORK_SUCCESS = 3;
    private static final int REVIEW_DETAIL_NETWORK_FAILER = 4;

    //php변수를 입력

    private static final String TAG_RESULTS = "result";
    private static final String TAG_RV_NUM = "rv_num";
    private static final String TAG_STORE_ID = "store_id";
    private static final String TAG_KAKAO_ID = "kakao_id";
    private static final String TAG_KAKAO_NAME = "kakao_name";
    private static final String TAG_PROFILEPATH = "profilepath";
    private static final String TAG_RV_CON = "rv_con";
    private static final String TAG_AVG_GPA = "avg_gpa";
    private static final String TAG_RV_DAY = "rv_day";
    private static final String TAG_RVPIC_DATA1 = "RVPIC_data1";
    private static final String TAG_RVPIC_DATA2 = "RVPIC_data2";
    private static final String TAG_RVPIC_DATA3 = "RVPIC_data3";

    private static final String URLIN = "http://14.63.213.157/dongimg/store_rv_v3.0.php";
    private static final String URLDEL = "http://14.63.213.157/dongimg/delete_review.php";

    private ReviewInteractorImpl.StoreReviewHandler handler;
    private JSONArray jsonArray = null;
    private String myJSONString;
    private ArrayList<StoreReview> storeReviewArrayList;
    private ArrayList<String> pictureArrayList;
    private StoreReview detailReview;

    /**
     * 생성자
     */
    public StoreReviewData(ReviewInteractorImpl.StoreReviewHandler handler) {
        this.handler = handler;
    }

    /**
     * 점포ID로 전제 리뷰 불러오는 메소드
     */
    public void getTotalData(String store_id) {
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
                    URL url = new URL(URLIN);
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
                Log.e("myJSONString",result);
                myJSONString = result;
                if (result.equals("failOfTimeOut")) {
                    handler.sendEmptyMessage(REVIEW_NETWORK_FAILER);
                } else {
                    handler.sendEmptyMessage(REVIEW_NETWORK_SUCCESS);
                }
            }
        }
        GetData getData = new GetData();
        getData.execute(store_id);
    }

    /**
     * 값 저장하고
     */
    public ArrayList<StoreReview> setTotalData() {
        storeReviewArrayList = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                StoreReview storeReview = new StoreReview();
                storeReview.setReviewNum(Integer.parseInt(c.getString(TAG_RV_NUM)));
                storeReview.setStoreId(c.getString(TAG_STORE_ID));
                storeReview.setKakaoId(c.getString(TAG_KAKAO_ID));
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
                }
                storeReview.setReviewPicture(pictureArrayList);
                storeReviewArrayList.add(storeReview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return storeReviewArrayList;
    }

    /**
     * review 번호로 상세리뷰 불러오는 메소드
     *
     * @param rv_num
     */
    public void getDetailData(String rv_num) {

    }

    public StoreReview setDetailData() {
        return detailReview;
    }

    /**
     * 리뷰 삭제하기
     *
     * @param rv_num
     */
    public void deleteToReview(int rv_num) {
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
                } catch (IllegalArgumentException e) {
                    return new String("failOfTimeOut");
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String result) {
                Log.e("result", result);
                if (result.trim().equals("success")) {
                    handler.sendEmptyMessage(REVIEW_DELETE_NETWORK_SUCCESS);
                } else {
                    handler.sendEmptyMessage(REVIEW_DELETE_NETWORK_FAILER);
                }
            }
        }

        DeleteData deleteData = new DeleteData();
        deleteData.execute(Integer.toString(rv_num));
    }


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
