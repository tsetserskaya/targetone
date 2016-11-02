package com.example.targetone;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

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

public class LightActivity extends Activity implements View.OnClickListener{

    private SQLiteDatabase db;
    private Cursor cursor;


    public static final String EXTRA_OBJECTNO = "objectNo";

    String address = MainActivity.address;
//    String address = "98:D3:31:90:44:F6"; //high speed
    //String address = "6C:23:B9:5B:3F:0F";


    //Экземпляры классов наших кнопок
    ToggleButton redButton;
    ToggleButton greenButton;

    BluetoothAdapter btAdapter = null;

    BluetoothSocket clientSocket = null;
    OutputStream  outStream = null;
    InputStream inStream = null;

    byte[] TxOn;
    byte[] TxOff;
    byte[] RxOn;
    byte[] RxOff;

    //Эта функция запускается автоматически при запуске приложения
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        TestARRAY();
        ConnectBluetooth();


        //"Соединям" вид кнопки в окне приложения с реализацией
        redButton = (ToggleButton) findViewById(R.id.toggle_button_Rx);
        greenButton = (ToggleButton) findViewById(R.id.toggle_button_Tx);

        //Добавлем "слушатель нажатий" к кнопке
        redButton.setOnClickListener(this);
        greenButton.setOnClickListener(this);

    }


    //Как раз эта функция и будет вызываться

    @Override
    public void onClick(View v) {

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null) {
            if (btAdapter.isEnabled()) {

                //Пытаемся послать данные
                try {
                    //Получаем выходной поток для передачи данных
                    outStream = clientSocket.getOutputStream();

                    //В зависимости от того, какая кнопка была нажата,
                    //изменяем данные для посылки
                    if (v == redButton) {

                        if (redButton.isChecked()) {
                            outStream.write(RxOn);
                         }
                        else {
                            outStream.write(RxOff);
                        }

                    } else if (v == greenButton) {
                        if (greenButton.isChecked()) {
                                outStream.write(TxOn);
                        }
                        else {
//                          if (objectNo.equals("5001")) outStream.write(testTxOffA0);
                            outStream.write(TxOff);
                        }
                    }


                } catch (IOException e) {
                    //Если есть ошибки, выводим их в лог
                    Log.d("BLUETOOTH", e.getMessage());
                }
            }
        }
    }


    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        resetConnection();
    }

    @Override
    public void onResume(){
        super.onResume();
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
            //Устройство с данным адресом - наш Bluetooth
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

    public void  TestARRAY() {
        //Получение рубежа из интента
        String objectNo = (String) getIntent().getExtras().get(EXTRA_OBJECTNO);

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
            if (actions[j].equals("OnTx")) {
                hexString = frames[j];
                out = TransCoder(hexString);
                TxOn = out;
            }
            if (actions[j].equals("OffTx")) {
                hexString = frames[j];
                out = TransCoder(hexString);
                TxOff = out;
            }
            if (actions[j].equals("OnRx")) {
                hexString = frames[j];
                out = TransCoder(hexString);
                RxOn = out;
            }
            if (actions[j].equals("OffRx")) {
                hexString = frames[j];
                out = TransCoder(hexString);
                RxOff = out;
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

