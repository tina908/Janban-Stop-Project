package kr.kjy.janban;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class sqldb extends SQLiteOpenHelper {


    // dbHelper 클래스를 사용하여 SQLite 데이터베이스 생성 또는 열기
    dbHelper dbHelper;
    SQLiteDatabase db;

    public sqldb(Context context) {
        super(context, "test.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터 테이블 생성
        db.execSQL("CREATE TABLE Data (Weight STRING, UID STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스 스키마 업그레이드 로직을 여기에 추가
        // 예를 들어, 테이블을 삭제하고 다시 생성할 수 있습니다.
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS Data");
            onCreate(db);
        }
    }

    public String getLatestData() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { "Weight", "UID" }; // 선택할 열
        String orderBy = "RowId DESC"; // 최신 데이터가 맨 위에 오도록 정렬
        Cursor cursor = db.query("Data", projection, null, null, null, null, orderBy, "1"); // 1개의 데이터만 가져옵니다.

        if (cursor.moveToFirst()) {
            int weightIndex = cursor.getColumnIndex("Weight");
            int uidIndex = cursor.getColumnIndex("UID");

            if (weightIndex >= 0 && uidIndex >= 0) {
                String weight = cursor.getString(weightIndex);
                String uid = cursor.getString(uidIndex);
                return weight + ", " + uid;
            }
        }

// 데이터를 찾지 못했거나 열이 없을 경우
        return null;
    }


    // 데이터 입력
    public long insertData(String columnName, String dataValue) {
        ContentValues values = new ContentValues();
        values.put(columnName, dataValue);
        long newRowId = db.insert("Data", null, values);
        return newRowId;
    }

    // 데이터 검색
    public String searchData(String columnName, String desiredValue) {
        String[] projection = { columnName };
        String selection = columnName + " = ?";
        String[] selectionArgs = { desiredValue };
        Cursor cursor = db.query("Data", projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            // 데이터를 찾았습니다.
            @SuppressLint("Range") String dataValue = cursor.getString(cursor.getColumnIndex(columnName));
            return dataValue;
        } else {
            // 데이터를 찾지 못했습니다.
            return null;
        }


    }

    // 데이터베이스 연결 종료
    public void close() {
        db.close();
    }


}
