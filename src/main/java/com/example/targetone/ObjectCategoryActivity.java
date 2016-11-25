package com.example.targetone;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.app.ProgressDialog;

public class ObjectCategoryActivity extends ListActivity {

    public static final String EXTRA_RANGENO = "rangeNo";

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Получение рубежа из интента
        int rangeNo = (Integer)getIntent().getExtras().get(EXTRA_RANGENO);
        ListView listObjects = getListView();

        try {
            SQLiteOpenHelper RangesDatabaseHelper = new RangesDatabaseHelper(this);
            db = RangesDatabaseHelper.getReadableDatabase();
            cursor = db.query("OBJECTS",
                    new String[] {"_id", "OBJECT", "OBJECT_ID","RANGE_ID"},
                    "RANGE_ID = ?",
                    new String[] {Integer.toString(rangeNo)},
                    null, null,null);
            CursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.two_line_list_item,
                    cursor,
                    new String[]{"OBJECT", "OBJECT_ID"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0);
            listObjects.setAdapter(listAdapter);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    public void onListItemClick(ListView listView,
                                View itemView,
                                int position,
                                long id) {

        String objectId = cursor.getString(2);
        int start = 0;
        int end = 2;
        char[] buf = new char[end - start];
        objectId.getChars(start, end, buf, 0);
        String sCompare = new String(buf);
          String sPoint = "n8";
          String sLight = "50";
          String sBugler = "70";
          String sRainbow = "A8";

        if (sCompare.equals(sLight))
        {
            Intent intent = new Intent(ObjectCategoryActivity.this, LightActivity.class);
            intent.putExtra(LightActivity.EXTRA_OBJECTNO, objectId);
            startActivity(intent);
        }
        if (sCompare.equals(sBugler))
        {
            Intent intent = new Intent(ObjectCategoryActivity.this, BuglerActivity.class);
            startActivity(intent);
        }
        if (sCompare.equals(sPoint))
        {
            Intent intent = new Intent(ObjectCategoryActivity.this, PointActivity.class);
            intent.putExtra(PointActivity.EXTRA_OBJECTNO1, objectId);
            startActivity(intent);
        }
        if (sCompare.equals(sRainbow))
        {
            Intent intent = new Intent(ObjectCategoryActivity.this, RainbowActivity.class);
            intent.putExtra(RainbowActivity.EXTRA_OBJECTNO2, objectId);
            startActivity(intent);
        }
    }

//    if (position == 0) {
//        //int objectId = 5001;
//        //int objectId = cursor.getInt(2);
//        String objectId = cursor.getString(2);
//        Intent intent = new Intent(ObjectCategoryActivity.this, LightActivity.class);
//        intent.putExtra(LightActivity.EXTRA_OBJECTNO, objectId);
//        startActivity(intent);
//    }
//    if (position == 1) {
//        //int objectId = 5002;
//        String objectId = cursor.getString(2);
//        //int objectId = cursor.getInt(2);
//        Intent intent = new Intent(ObjectCategoryActivity.this, LightActivity.class);
//        intent.putExtra(LightActivity.EXTRA_OBJECTNO, objectId);
//        startActivity(intent);
//    }
//    if (position == 2) {
//        //int objectId = 5003;
//       // int objectId = cursor.getInt(2);
//        String objectId = cursor.getString(2);
//        Intent intent = new Intent(ObjectCategoryActivity.this, LightActivity.class);
//        intent.putExtra(LightActivity.EXTRA_OBJECTNO, objectId);
//        startActivity(intent);
//    }
//
//    if (position == 3) {
//        //int objectId = 5004;
//       // int objectId = cursor.getInt(2);
//        String objectId = cursor.getString(2);
//        Intent intent = new Intent(ObjectCategoryActivity.this, LightActivity.class);
//        intent.putExtra(LightActivity.EXTRA_OBJECTNO, objectId);
//        startActivity(intent);
//    }
//
//    if (position == 4) {
//        Intent intent = new Intent(ObjectCategoryActivity.this, BuglerActivity.class);
//        startActivity(intent);
//    }
      //  }



}
