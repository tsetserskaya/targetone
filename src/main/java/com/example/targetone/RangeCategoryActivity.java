package com.example.targetone;


import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
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


public class RangeCategoryActivity extends ListActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listRanges = getListView();

        try {
            SQLiteOpenHelper RangesDatabaseHelper = new RangesDatabaseHelper(this);
            db = RangesDatabaseHelper.getReadableDatabase();
            cursor = db.query("RANGES",
                    new String[]{"_id", "RANGE"},
                    null, null, null, null, null);
            CursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"RANGE"},
                    new int[]{android.R.id.text1},
                    0);
            listRanges.setAdapter(listAdapter);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        //Включаем bluetooth. Если он уже включен, то ничего не произойдет
        String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        startActivityForResult(new Intent(enableBT), 0);
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
        Intent intent = new Intent(RangeCategoryActivity.this, ObjectCategoryActivity.class);
        intent.putExtra(ObjectCategoryActivity.EXTRA_RANGENO, (int) id);
        startActivity(intent);
    }
/*
    //connect BT in background ###########################
//Фоновое подключение bluetooth.
    private class ConnectBT extends AsyncTask<Void, Void, Void> {

        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        protected void onPreExecute() {
            progress = ProgressDialog.show(RangeCategoryActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }
        protected Void doInBackground(Void... device) {


            try {
                if (clientSocket == null || !isBtConnected)
                {
                    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice deviceBT = bluetooth.getRemoteDevice(address);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    clientSocket.connect();//start connection
                }
            } catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                Toast toast = Toast.makeText(RangeCategoryActivity.this,
                        "Connection Failed.  Try again.", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
            else
            {
                Toast toast = Toast.makeText(RangeCategoryActivity.this,
                        "Bluetooth is Connected", Toast.LENGTH_SHORT);
                toast.show();
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
    //connect BT in background ##########################
*/
}
