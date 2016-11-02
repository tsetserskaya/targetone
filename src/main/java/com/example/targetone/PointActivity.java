package com.example.targetone;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PointActivity extends Activity implements View.OnClickListener {

    public static final String EXTRA_OBJECTNO1 = "objectNo1";

    private SQLiteDatabase db;
    private Cursor cursor;

    String address = MainActivity.address;
//    String address = "98:D3:31:90:44:F6"; //high speed
    //String address = "6C:23:B9:5B:3F:0F";


    private BluetoothAdapter btAdapter = null;
    //Сокет, с помощью которого мы будем отправлять данные на Arduino
    BluetoothSocket clientSocket = null;
    OutputStream outStream = null;
    private ToggleButton onOffButton;

   byte[] testPointOn, testPointOff;

//    byte[] testPointOff = { (byte)0xFF, 0x22, (byte)0x81,(byte)0x02, (byte)0xD0, 0x01,
//            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
//    byte[] testPointOn = { (byte)0xFF, 0x22, (byte)0x81,(byte)0x02, (byte)0xD0, 0x02,
//            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};

//    byte[] testPointOff1 = { (byte)0xFF, 0x22, (byte)0x81, 0x01, (byte)0xD0, 0x01,
//            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
//    byte[] testPointOn1 = { (byte)0xFF, 0x22, (byte)0x81, 0x01, (byte)0xD0, 0x02,
//            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
//
//    byte[] testPointOff2 = { (byte)0xFF, 0x22, (byte)0x81, 0x02, (byte)0xD0, 0x01,
//            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
//    byte[] testPointOn2 = { (byte)0xFF, 0x22, (byte)0x81, 0x02, (byte)0xD0, 0x02,
//            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
//    byte[] testPointOff3 = { (byte)0xFF, 0x22, (byte)0x81, 0x03, (byte)0xD0, 0x01,
//            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
//    byte[] testPointOn3 = { (byte)0xFF, 0x22, (byte)0x81, 0x03, (byte)0xD0, 0x02,
//            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);


        TestARRAY();
        ConnectBluetooth();
        //addListenerOnButton();
        onOffButton = (ToggleButton) findViewById(R.id.toggle_button_point_light);
        onOffButton.setOnClickListener(this);
    }


     @Override
     public void onClick(View v) {

      btAdapter = BluetoothAdapter.getDefaultAdapter();
         if (btAdapter != null) {
             if (btAdapter.isEnabled()) {
             //Пытаемся послать данные
                 try {
                 //Получаем выходной поток для передачи данных
                 outStream = clientSocket.getOutputStream();

                 if (onOffButton.isChecked()) {
//                                if (objectNo1.equals("n81a01")) outStream.write(testPointOn1);
//                                if (objectNo1.equals("n81a02")) outStream.write(testPointOn2);
//                                if (objectNo1.equals("n81a03")) outStream.write(testPointOn3);
                     outStream.write(testPointOn);
                 } else {

//                                if (objectNo1.equals("n81a01")) outStream.write(testPointOff1);
//                                if (objectNo1.equals("n81a02")) outStream.write(testPointOff2);
//                                if (objectNo1.equals("n81a03")) outStream.write(testPointOff3);
                     outStream.write(testPointOff);
                 }

             } catch (IOException e) {
                 Log.d("BLUETOOTH", e.getMessage());
              }
           }
        }
     }

//    public void addListenerOnButton() {
//        btAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (btAdapter != null) {
//            if (btAdapter.isEnabled()) {
//
//
//
//                onOffButton = (ToggleButton) findViewById(R.id.toggle_button_point_light);
//                onOffButton.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        //Получение рубежа из интента
//                        String objectNo1 = (String) getIntent().getExtras().get(EXTRA_OBJECTNO1);
//
//                        //Пытаемся послать данные
//                        try {
//                            //Получаем выходной поток для передачи данных
//                            outStream = clientSocket.getOutputStream();
//
//                            if (onOffButton.isChecked()) {
//                                if (objectNo1.equals("n81a01")) outStream.write(testPointOn1);
//                                if (objectNo1.equals("n81a02")) outStream.write(testPointOn2);
////                                outStream.write(testPointOn);
//                            }
//                            else {
//
//                                if (objectNo1.equals("n81a01")) outStream.write(testPointOff1);
//                                if (objectNo1.equals("n81a02")) outStream.write(testPointOff2);
//                              // outStream.write(testPointOff);
//                            }
//
//                        } catch (IOException e) {
//                            //Если есть ошибки, выводим их в лог
//                            Log.d("BLUETOOTH", e.getMessage());
//                        }
//                    }
//
//                });
//            }
//        }
//    }

    public void  ConnectBluetooth(){

        // Reset all streams and socket.
        resetConnection();

        //Включаем bluetooth. Если он уже включен, то ничего не произойдет
        String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        startActivityForResult(new Intent(enableBT), 0);
        //Мы хотим использовать тот bluetooth-адаптер, который задается по умолчанию
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        //Пытаемся проделать эти действия
        try{
            //Устройство с данным адресом - наш Bluetooth Bee
            //Адрес опредеяется следующим образом: установите соединение
            //между ПК и модулем (пин: 1234), а затем посмотрите в настройках
            //соединения адрес модуля. Скорее всего он будет аналогичным.
            BluetoothDevice device = bluetooth.getRemoteDevice(address);
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery(); //TESTTTTTTTTTTTTTTTTTTTTTT

            //Инициируем соединение с устройством
            Method m = device.getClass().getMethod(
                    "createRfcommSocket", new Class[] {int.class});

            clientSocket = (BluetoothSocket) m.invoke(device, 1);
            clientSocket.connect();

            //В случае появления любых ошибок, выводим в лог сообщение
        } catch (IOException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (SecurityException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalAccessException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (InvocationTargetException e) {
            Log.d("BLUETOOTH", e.getMessage());
        }

        //Выводим сообщение об успешном подключении
//        Toast.makeText(getApplicationContext(), "BLUETOOTH IS CONNECTED", Toast.LENGTH_LONG).show();

    }

    private void resetConnection() {

        if (outStream != null) {
            try {outStream.close();} catch (Exception e) {}
            outStream = null;
        }

        if (clientSocket != null) {
            try {clientSocket.close();} catch (Exception e) {}
            clientSocket = null;
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        resetConnection();

    }

    public void  TestARRAY() {
        //Получение рубежа из интента
        String objectNo = (String) getIntent().getExtras().get(EXTRA_OBJECTNO1);

        String[] actions = new String[20];
        String[] frames = new String[20];

        for (int c = 0; c < actions.length; ++c) {
            actions[c] = " ";frames[c] = " ";
        }

        try {
            SQLiteOpenHelper RangesDatabaseHelper = new RangesDatabaseHelper(this);
            db = RangesDatabaseHelper.getReadableDatabase();
            cursor = db.query("FRAMES",
                    new String[]{"_id", "OBJECT_ID", "ACTION", "FRAME"},
                    "OBJECT_ID = ?", new String[]{objectNo}, null, null, null);

            int i = 0;

            if (cursor != null) {

                if(cursor.moveToFirst()){
                    do{
                        actions[i] = cursor.getString(cursor.getColumnIndex("ACTION"));
                        frames [i] = cursor.getString(cursor.getColumnIndex("FRAME"));
                        ++i;
                    }while (cursor.moveToNext());
                }

            }
            cursor.close();
            db.close();

        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        String hexString;

        for (int j = 0; j < actions.length; ++j) {
            byte[] out;
            if (actions[j].equals("PointOn")) {
                hexString = frames[j];
                out = TransCoder(hexString);
                testPointOn = out;
            }
            if (actions[j].equals("PointOff")) {
                hexString = frames[j];
                out = TransCoder(hexString);
                testPointOff = out;
            }
        }
    }

    public static byte [] TransCoder(String hexString) {
        String hexVal = "0123456789ABCDEF";
        String hexValL = "0123456789abcdef";
        byte[] out = new byte[hexString.length() / 2];

        int n = hexString.length();

        for (int i = 0; i < n; i += 2) {

            int hn = hexVal.indexOf(hexString.charAt(i));
            if (hn == -1) hn = hexValL.indexOf(hexString.charAt(i));
            int ln = hexVal.indexOf(hexString.charAt(i + 1));
            if (ln == -1) ln = hexValL.indexOf(hexString.charAt(i + 1));

            //high+low
            out[i / 2] = (byte) ((hn << 4) | ln);

        }
        return out;

    }


}
