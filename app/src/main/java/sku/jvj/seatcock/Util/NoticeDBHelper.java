package sku.jvj.seatcock.Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.Notice;

/**
 * Created by Android Hong on 2016-10-12.
 */

public class NoticeDBHelper extends SQLiteOpenHelper {

    ArrayList<Notice> notoList = new ArrayList<>();
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public NoticeDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 SearchResult 이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        searchText 문자열 컬럼, date 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE tbl_noti" +
                " (idx INTEGER PRIMARY KEY " +
                ", status INTEGER DEFAULT 1);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * SQLite내의 안읽은 공지 List로 뽑아오기
     * @param
     */
    public ArrayList<Notice> getNoticeList() {
        notoList.clear();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_noti where status = 1", null);

        while (cursor.moveToNext()) {
            Notice searchWord =new Notice(cursor.getInt(0),cursor.getInt(1));
            notoList.add(searchWord);
        }
        db.close();
        return notoList;
    }


    /**
     * 안읽은 공지 갯수 확인
     * @return
     */
    public int getNumber(){
        int number = 0;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(status) FROM tbl_noti WHERE status = 1 ", null);

        while (cursor.moveToNext()) {
            number = cursor.getInt(0);
        }
        db.close();

        return number;
    }

    /**
     * status 모두 읽은것으로 변경
     */
    public void setAllStatusUpdate(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE tbl_noti SET status = 0 WHERE status > 0;");
    }


    public void setStatusUpdate(int idx) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE tbl_noti SET status = 0 WHERE idx ='" + idx + "';");
        db.close();
    }

    /**
     * 공지 새로 만들기
     * @param idx
     */
    public void insertnoti( String idx) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO tbl_noti (idx) values('" + idx + "');");
        db.close();
    }

}
