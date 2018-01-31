package sku.jvj.seatcock.Server;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.ReviewWriteInteractorImpl;
import sku.jvj.seatcock.Util.Util;

import static sku.jvj.seatcock.Interface.Store.Review.ReviewWriteInteractor.REVIEW_INSERT_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Store.Review.ReviewWriteInteractor.REVIEW_INSERT_NETWORK_SUCCESS;
import static sku.jvj.seatcock.Interface.Store.Review.ReviewWriteInteractor.REVIEW_INSERT_PICTURE_SUCCESS;

/**
 * Created by Android Hong on 2016-10-25.
 */

public class StoreReiviewWriteData {


    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 15000;

    private ReviewWriteInteractorImpl.ReviewWrite handler;

    public StoreReiviewWriteData(ReviewWriteInteractorImpl.ReviewWrite handler){
        this.handler = handler;
    }

    /**
     * 점포ID 와 kakao_id 로 리뷰 저장하는 메소드
     */
    // 현재 회원정보를 안받아옴
    public void insertToReview(String store_id, String rv_con, String rv_rating1, String rv_rating2, String rv_rating3, String kakao_id, final ArrayList<String> pictureArrayList) {

        class InsertData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected String doInBackground(String... params) {
                try {
                    String store_id = (String) params[0];
                    String rv_con = (String) params[1];
                    String rv_rating1 = (String) params[2];
                    String rv_rating2 = (String) params[3];
                    String rv_rating3 = (String) params[4];
                    String kakao_id = (String) params[5];

                    String link = "http://14.63.213.157/dongimg/insert_review_v2.php";

                    String data = URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");
                    data += "&" + URLEncoder.encode("rv_con", "UTF-8") + "=" + URLEncoder.encode(rv_con, "UTF-8");
                    data += "&" + URLEncoder.encode("rv_avg1", "UTF-8") + "=" + URLEncoder.encode(rv_rating1, "UTF-8");
                    data += "&" + URLEncoder.encode("rv_avg2", "UTF-8") + "=" + URLEncoder.encode(rv_rating2, "UTF-8");
                    data += "&" + URLEncoder.encode("rv_avg3", "UTF-8") + "=" + URLEncoder.encode(rv_rating3, "UTF-8");
                    data += "&" + URLEncoder.encode("kakao_id", "UTF-8") + "=" + URLEncoder.encode(kakao_id, "UTF-8");

                    switch (pictureArrayList.size()) {
                        case 0:
                            data += "&" + URLEncoder.encode("rvpic_data1", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
                            data += "&" + URLEncoder.encode("rvpic_data2", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
                            data += "&" + URLEncoder.encode("rvpic_data3", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
                            break;
                        case 1:
                            data += "&" + URLEncoder.encode("rvpic_data1", "UTF-8") + "=" + URLEncoder.encode(Util.getFileName(pictureArrayList.get(0)), "UTF-8");
                            data += "&" + URLEncoder.encode("rvpic_data2", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
                            data += "&" + URLEncoder.encode("rvpic_data3", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
                            break;
                        case 2:
                            data += "&" + URLEncoder.encode("rvpic_data1", "UTF-8") + "=" + URLEncoder.encode(Util.getFileName(pictureArrayList.get(0)), "UTF-8");
                            data += "&" + URLEncoder.encode("rvpic_data2", "UTF-8") + "=" + URLEncoder.encode(Util.getFileName(pictureArrayList.get(1)), "UTF-8");
                            data += "&" + URLEncoder.encode("rvpic_data3", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
                            break;
                        case 3:
                           data += "&" + URLEncoder.encode("rvpic_data1", "UTF-8") + "=" + URLEncoder.encode(Util.getFileName(pictureArrayList.get(0)), "UTF-8");
                            data += "&" + URLEncoder.encode("rvpic_data2", "UTF-8") + "=" + URLEncoder.encode(Util.getFileName(pictureArrayList.get(1)), "UTF-8");
                            data += "&" + URLEncoder.encode("rvpic_data3", "UTF-8") + "=" + URLEncoder.encode(Util.getFileName(pictureArrayList.get(2)), "UTF-8");
                            break;

                    }

                    URL url = new URL(link);
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
                if(result.trim().equals("success")){
                    handler.sendEmptyMessage(REVIEW_INSERT_NETWORK_SUCCESS);
                }else{
                    handler.sendEmptyMessage(REVIEW_INSERT_NETWORK_FAILER);
                }
            }
        }

        InsertData insertData = new InsertData();
        insertData.execute(store_id, rv_con, rv_rating1, rv_rating2, rv_rating3, kakao_id);
    }

    /**
     * 파일 실제 데이터 업로드
     *
     * @param filePath
     */
    public void uploadImage(String filePath,final int size,final int count) {
        class InsertData extends AsyncTask<String, Void, String> {
            private int serverResponseCode = 0;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                String upLoadServerUri = "http://14.63.213.157/dongimg/UploadToServer.php";//서버컴퓨터의 ip주소

                String fileName = (String) params[0];
                String sourceFileUri = (String) params[0];

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);

                try {
                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(upLoadServerUri); // 서버 URL

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + fileName + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);
                    Log.i("업로드 완료", "업로드 완료");
                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (IllegalArgumentException e) {
                    return new String("failOfTimeOut");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return Integer.toString(serverResponseCode);
            }

            @Override
            protected void onPostExecute(String result) {
                if(count == size) {
                    handler.sendEmptyMessage(REVIEW_INSERT_PICTURE_SUCCESS);
                }
            }
        }
        InsertData insertData = new InsertData();
        insertData.execute(filePath);
    }


}
