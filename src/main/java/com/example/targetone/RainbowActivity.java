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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RainbowActivity extends Activity {

    public static final String EXTRA_OBJECTNO2 = "objectNo2";

    public int seekR = 0, seekG = 0, seekB = 0, seekW = 0;
    SeekBar redSeekBar, greenSeekBar, blueSeekBar, whiteSeekBar;
    public static String hexFormatR = "00", hexFormatG = "00", hexFormatB = "00", hexFormatW = "00";
    public static String hexStringR, hexStringG, hexStringB, hexStringW;
    TextView TextViewR, TextViewG, TextViewB, TextViewW;


    public SQLiteDatabase db;
    public Cursor cursor;

    String address = MainActivity.address;

    public BluetoothAdapter btAdapter = null;
    //Сокет, с помощью которого мы будем отправлять данные на Arduino
    BluetoothSocket clientSocket = null;
    OutputStream outStream = null;

    byte[] RedLight, GreenLight, BlueLight, WhiteLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainbow);


        ConnectBluetooth();

        redSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_R);
        greenSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_G);
        blueSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_B);
        whiteSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_W);
        updateLight();
        redSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        greenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        blueSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        whiteSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        TextViewR = (TextView)findViewById(R.id.textView9);
        TextViewG = (TextView)findViewById(R.id.textView8);
        TextViewB = (TextView)findViewById(R.id.textView7);
        TextViewW = (TextView)findViewById(R.id.textView6);


    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener() {

                View v;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    // TODO Auto-generated method stub
                    v = seekBar;
                    updateLight();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                    btAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (btAdapter != null) {
                        if (btAdapter.isEnabled()) {
                            //Пытаемся послать данные
                            try {
                                //Получаем выходной поток для передачи данных
                                outStream = clientSocket.getOutputStream();
                                if(v == redSeekBar) {
                                    outStream.write(RedLight);
                                    //TextViewR.setText(hexStringR);
                                }
                                if(v == greenSeekBar) {
                                    outStream.write(GreenLight);
                                    //TextViewG.setText(hexStringG);
                                }
                                if(v == blueSeekBar){
                                    outStream.write(BlueLight);
                                    //TextViewB.setText(hexStringB);
                                }
                                if(v == whiteSeekBar){
                                    outStream.write(WhiteLight);
                                    //TextViewW.setText(hexStringW);
                                }
                            } catch (IOException e) {
                                Log.d("BLUETOOTH", e.getMessage());
                            }
                        }
                    }

                }
            };
    private void updateLight() {
        seekR = redSeekBar.getProgress();
        seekG = greenSeekBar.getProgress();
        seekB = blueSeekBar.getProgress();
        seekW = whiteSeekBar.getProgress();
        hexFormatR = Integer.toString(seekR, 16);
        if (hexFormatR.length()==1){
            hexFormatR = "0" + hexFormatR;
        }
        hexFormatG = Integer.toString(seekG, 16);
        if (hexFormatG.length()==1){
            hexFormatG = "0" + hexFormatG;
        }
        hexFormatB = Integer.toString(seekB, 16);
        if (hexFormatB.length()==1){
            hexFormatB = "0" + hexFormatB;
        }
        hexFormatW = Integer.toString(seekW, 16);
        if (hexFormatW.length()==1){
            hexFormatW = "0" + hexFormatW;
        }
        TestARRAY();
    }

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
    public void onStart(){
        super.onStart();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        resetConnection();
    }

    public void  TestARRAY() {
        //Получение рубежа из интента
        String objectNo = (String) getIntent().getExtras().get(EXTRA_OBJECTNO2);

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



        for (int j = 0; j < actions.length; ++j) {
            byte[] out;
            if (actions[j].equals("RedLight")) {
                hexStringR = frames[j];
                hexStringR = replaceCharAt(hexStringR, 10, hexFormatR.charAt(0));
                hexStringR = replaceCharAt(hexStringR, 11, hexFormatR.charAt(1));
                out = TransCoder(hexStringR);
                RedLight = out;
            }
            if (actions[j].equals("GreenLight")) {
                hexStringG = frames[j];
                hexStringG = replaceCharAt(hexStringG, 12, hexFormatG.charAt(0));
                hexStringG = replaceCharAt(hexStringG, 13, hexFormatG.charAt(1));
                out = TransCoder(hexStringG);
                GreenLight = out;
            }
            if (actions[j].equals("BlueLight")) {
                hexStringB = frames[j];
                hexStringB = replaceCharAt(hexStringB, 14, hexFormatB.charAt(0));
                hexStringB = replaceCharAt(hexStringB, 15, hexFormatB.charAt(1));
                out = TransCoder(hexStringB);
                BlueLight = out;
            }
            if (actions[j].equals("WhiteLight")) {
                hexStringW = frames[j];
                hexStringW = replaceCharAt(hexStringW, 16, hexFormatW.charAt(0));
                hexStringW = replaceCharAt(hexStringW, 17, hexFormatW.charAt(1));
                out = TransCoder(hexStringW);
                WhiteLight = out;
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

    public static String replaceCharAt(String s, int pos, char c) {

        return s.substring(0,pos) + c + s.substring(pos+1);

    }

}
