package kr.kjy.janban;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "test.db";
    static final int DATABASE_VERSION = 1; // 버전 번호를 여기에 추가

    // DBHelper 생성자
    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Data Table 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Data (Weight STRING, UID STRING)");

    }

    // Data Table Upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Data");
        onCreate(db);
    }

    // Data Table 데이터 입력
    public void insert(int Weight, int UID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO Data (Weight, UID) VALUES (" + Weight + ", " + UID + ")");
        db.close();
    }

    // Data Table 데이터 수정
    public void Update(int Weight, int UID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE Data SET UID = " + UID  + " WHERE UID = " + UID);
        db.close();
    }

    // Data Table 데이터 삭제
    public void Delete(String UID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM Data WHERE UID = " + UID);
        db.close();
    }

    // Data Table 조회
    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM Data", null);
        while (cursor.moveToNext()) {
            result += " 무게 : " + cursor.getString(0)
                    + ", UID : "
                    + cursor.getInt(1)
                    + "\n";
        }

        return result;


    }
}
