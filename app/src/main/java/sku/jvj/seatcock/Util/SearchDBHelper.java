package sku.jvj.seatcock.Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.SearchWord;

/**
 * Created by Android Hong on 2016-08-29.
 */
public class SearchDBHelper extends SQLiteOpenHelper {

    ArrayList<SearchWord> searchWordArrayList = new ArrayList<>();

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public SearchDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 SearchResult 이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        searchText 문자열 컬럼, date 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE SearchResult(_id INTEGER PRIMARY KEY AUTOINCREMENT, searchText TEXT, date DATE);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 검색한 단어 DB에 삽입하는 메소드
     * @param searchText
     */
    public void insert(String searchText) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO SearchResult VALUES(null, '" + searchText + "', strftime('%Y-%m-%d %H:%M:%S','now'));");
        db.close();
    }

    /**
     * 검색한 단어 DB에 업데이트하는 메소드
     * @param searchText
     */
    public void update(String searchText) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE SearchResult SET date = strftime('%Y-%m-%d %H:%M:%S','now') WHERE searchText='" + searchText + "';");
        db.close();
    }

    /**
     * X버튼 누르면 DB에 삭제하는 메소드
     * @param searchText
     */
    public void delete(String searchText) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM SearchResult WHERE searchText='" + searchText + "';");
        db.close();
    }

    public ArrayList<SearchWord> getResult() {
        searchWordArrayList.clear();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id ,searchText, strftime('%Y.%m.%d', date) FROM SearchResult ORDER BY date desc", null);
        while (cursor.moveToNext()) {
            SearchWord searchWord =new SearchWord(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
            searchWordArrayList.add(searchWord);
        }
        db.close();
        return searchWordArrayList;
    }

    /**
     * 검색한 단어가 이미 있는지 체크하는 메소드
     * @param searchText
     * @return
     */
    public boolean searchKeyword(String searchText){
        boolean check = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SearchResult WHERE searchText = '"+searchText+"'", null);
        while(cursor.moveToNext()){
            check = true;
        }
        db.close();
        return check;
    }

}
