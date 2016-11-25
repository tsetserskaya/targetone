package com.example.targetone;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

import java.sql.Blob;


public class RangesDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ranges"; // Имя базы данных
    private static final int DB_VERSION = 1; // Версия базы данных

    RangesDatabaseHelper (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db, 0, DB_VERSION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE OBJECTS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "OBJECT TEXT, "
                    + "OBJECT_ID TEXT, "
                    + "RANGE_ID INTEGER);");
            //range 100m for test
            insertObject(db, "Фонарик", "5001", 1);
            insertObject(db, "Фонарик", "5002", 1);
            insertObject(db, "Фонарик", "5003", 1);
            insertObject(db, "Фонарик", "5004", 1);
            insertObject(db, "Сигналист", "7001", 1);
            //range 200m for test
            insertObject(db, "Точка", "n81a01", 2);
            insertObject(db, "Точка", "n81a02", 2);
            insertObject(db, "Точка", "n81a03", 2);
            insertObject(db, "Точка", "n81a04", 2);
            insertObject(db, "Точка", "n81a05", 2);
            insertObject(db, "Точка", "n81a06", 2);
            insertObject(db, "Точка", "n81a07", 2);
            insertObject(db, "Точка", "n81a08", 2);
            //300m
            insertObject(db, "Радуга", "A801", 3);
            insertObject(db, "Радуга", "A802", 3);
            insertObject(db, "Радуга", "A803", 3);
            insertObject(db, "Радуга", "A804", 3);
            //others
            //range 1km
            insertObject(db, "Точка", "5001", 10);
            insertObject(db, "Точка", "5002", 10);
            insertObject(db, "Точка", "5003", 10);
            insertObject(db, "Радуга", "A001", 10);
            insertObject(db, "Пэйджер", "0E01", 10);
            insertObject(db, "Колодец", "0A01", 10);
            insertObject(db, "Колодец", "0A02", 10);
            //range 900m
            insertObject(db, "Точка", "5004", 9);
            insertObject(db, "Точка", "5005", 9);
            insertObject(db, "Радуга", "A002", 9);
            //range 800m
            insertObject(db, "Точка", "5006", 8);
            insertObject(db, "Радуга", "A003", 8);
            insertObject(db, "Луноход", "D001", 8);
            //range 700m
            insertObject(db, "Точка", "5006", 7);
            insertObject(db, "Радуга", "A003", 7);
            insertObject(db, "Блиндаж", "B004", 7);
            insertObject(db, "Блиндаж", "B005", 7);
            //range 600m
            insertObject(db, "Фонарик", "D002", 6);
            insertObject(db, "Фонарик", "D003", 6);
            insertObject(db, "Сигналист", "D004", 6);

            db.execSQL("CREATE TABLE RANGES (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "RANGE TEXT);");
            insertRange(db, "Рубеж 100 м");
            insertRange(db, "Рубеж 200 м");
            insertRange(db, "Рубеж 300 м");
            insertRange(db, "Рубеж 400 м");
            insertRange(db, "Рубеж 500 м");
            insertRange(db, "Рубеж 600 м");
            insertRange(db, "Рубеж 700 м");
            insertRange(db, "Рубеж 800 м");
            insertRange(db, "Рубеж 900 м");
            insertRange(db, "Рубеж 1 км");

            db.execSQL("CREATE TABLE FRAMES (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "OBJECT_ID TEXT, "
                    + "ACTION TEXT, "
                    + "FRAME TEXT);");
            //Tx
            insertFrame(db, "5001", "OnTx",  "FF22A4A0F00800000000000000CCAAFF");
            insertFrame(db, "5001", "OffTx", "FF22A4A0F00400000000000000CCAAFF");
            insertFrame(db, "5002", "OnTx",  "FF22A4A1F00800000000000000CCAAFF");
            insertFrame(db, "5002", "OffTx", "FF22A4A1F00400000000000000CCAAFF");
            insertFrame(db, "5003", "OnTx",  "FF22A4A2F00800000000000000CCAAFF");
            insertFrame(db, "5003", "OffTx", "FF22A4A2F00400000000000000CCAAFF");
            insertFrame(db, "5004", "OnTx",  "FF22A4A3F00800000000000000CCAAFF");
            insertFrame(db, "5004", "OffTx", "FF22A4A3F00400000000000000CCAAFF");
            //Rx
            insertFrame(db, "5001", "OnRx",  "FF22A4A0F02000000000000000CCAAFF");
            insertFrame(db, "5001", "OffRx", "FF22A4A0F01000000000000000CCAAFF");
            insertFrame(db, "5002", "OnRx",  "FF22A4A1F02000000000000000CCAAFF");
            insertFrame(db, "5002", "OffRx", "FF22A4A1F01000000000000000CCAAFF");
            insertFrame(db, "5003", "OnRx",  "FF22A4A2F02000000000000000CCAAFF");
            insertFrame(db, "5003", "OffRx", "FF22A4A2F01000000000000000CCAAFF");
            insertFrame(db, "5004", "OnRx",  "FF22A4A3F02000000000000000CCAAFF");
            insertFrame(db, "5004", "OffRx", "FF22A4A3F01000000000000000CCAAFF");
            //Point Light on/off
            insertFrame(db, "n81a01", "PointOff",  "FF228101D00100000000000000CCAAFF");
            insertFrame(db, "n81a01", "PointOn",   "FF228101D00200000000000000CCAAFF");
            insertFrame(db, "n81a02", "PointOff",  "FF228102D00100000000000000CCAAFF");
            insertFrame(db, "n81a02", "PointOn",   "FF228102D00200000000000000CCAAFF");
            insertFrame(db, "n81a03", "PointOff",  "FF228103D00100000000000000CCAAFF");
            insertFrame(db, "n81a03", "PointOn",   "FF228103D00200000000000000CCAAFF");
            insertFrame(db, "n81a04", "PointOff",  "FF228104D00100000000000000CCAAFF");
            insertFrame(db, "n81a04", "PointOn",   "FF228104D00200000000000000CCAAFF");
            insertFrame(db, "n81a05", "PointOff",  "FF228105D00100000000000000CCAAFF");
            insertFrame(db, "n81a05", "PointOn",   "FF228105D00200000000000000CCAAFF");
            insertFrame(db, "n81a06", "PointOff",  "FF228106D00100000000000000CCAAFF");
            insertFrame(db, "n81a06", "PointOn",   "FF228106D00200000000000000CCAAFF");
            insertFrame(db, "n81a07", "PointOff",  "FF228107D00100000000000000CCAAFF");
            insertFrame(db, "n81a07", "PointOn",   "FF228107D00200000000000000CCAAFF");
            insertFrame(db, "n81a08", "PointOff",  "FF228108D00100000000000000CCAAFF");
            insertFrame(db, "n81a08", "PointOn",   "FF228108D00200000000000000CCAAFF");
            //rainbow
            insertFrame(db, "A801", "RedLight", "FF225001A8xy00000000000000CCAAFF");
            insertFrame(db, "A801", "GreenLight", "FF225001A800xy000000000000CCAAFF");
            insertFrame(db, "A801", "BlueLight", "FF225001A80000xy0000000000CCAAFF");
            insertFrame(db, "A801", "WhiteLight", "FF225001A8000000xy00000000CCAAFF");
            insertFrame(db, "A802", "RedLight", "FF225002A8xy00000000000000CCAAFF");
            insertFrame(db, "A802", "GreenLight", "FF225002A800xy000000000000CCAAFF");
            insertFrame(db, "A802", "BlueLight", "FF225002A80000xy0000000000CCAAFF");
            insertFrame(db, "A802", "WhiteLight", "FF225002A8000000xy00000000CCAAFF");
            insertFrame(db, "A803", "RedLight", "FF225003A8xy00000000000000CCAAFF");
            insertFrame(db, "A803", "GreenLight", "FF225003A800xy000000000000CCAAFF");
            insertFrame(db, "A803", "BlueLight", "FF225003A80000xy0000000000CCAAFF");
            insertFrame(db, "A803", "WhiteLight", "FF225003A8000000xy00000000CCAAFF");
            insertFrame(db, "A804", "RedLight", "FF225004A8xy00000000000000CCAAFF");
            insertFrame(db, "A804", "GreenLight", "FF225004A800xy000000000000CCAAFF");
            insertFrame(db, "A804", "BlueLight", "FF225004A80000xy0000000000CCAAFF");
            insertFrame(db, "A804", "WhiteLight", "FF225004A8000000xy00000000CCAAFF");

        }
        if (oldVersion < 2) {

        }
    }

    private static void insertObject(SQLiteDatabase db, String object,
                                    String objectId, int rangeId) {
        ContentValues rangeValues = new ContentValues();
        rangeValues.put("OBJECT", object);
        rangeValues.put("OBJECT_ID", objectId);
        rangeValues.put("RANGE_ID", rangeId);
        db.insert("OBJECTS", null, rangeValues);
    }

    private static void insertRange(SQLiteDatabase db, String range) {
        ContentValues targetFieldValues = new ContentValues();
        targetFieldValues.put("RANGE", range);
        db.insert("RANGES", null, targetFieldValues);
    }

    private static void insertFrame(SQLiteDatabase db, String objectId, String action, String frame) {
        ContentValues frameValues = new ContentValues();
        frameValues.put("OBJECT_ID", objectId);
        frameValues.put("ACTION", action);
        frameValues.put("FRAME", frame);
        db.insert("FRAMES", null, frameValues);
    }


}