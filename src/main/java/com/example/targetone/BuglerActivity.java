package com.example.targetone;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BuglerActivity extends Activity {

    String address = MainActivity.address;
    //String address = "6C:23:B9:5B:3F:0F";

    private BluetoothAdapter btAdapter = null;
    //Сокет, с помощью которого мы будем отправлять данные на Arduino
    BluetoothSocket clientSocket = null;
    OutputStream  outStream = null;

    private Spinner signalsSpinner, soundsSpinner;
    private ToggleButton onOffButton;

    byte[] testRxOff = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xA0, (byte)0xF0, 0x10,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
    byte[] testRxOn = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xA0, (byte)0xF0, 0x20,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};

    byte[] sigAtten = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xB0, (byte)0xF0, 0x00,
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
    byte[] sigFire = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xB0, (byte)0xF0, 0x00,
            0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
    byte[] sigCncel = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xB0, (byte)0xF0, 0x00,
            0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};

    byte[] wavTest = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xB0, (byte)0xF0, 0x00,
            0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
    byte[] wavAlarm = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xB0, (byte)0xF0, 0x00,
            0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
    byte[] wavKap = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xB0, (byte)0xF0, 0x00,
            0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
    byte[] wavPop = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xB0, (byte)0xF0, 0x00,
            0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
    byte[] wavRing = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xB0, (byte)0xF0, 0x00,
            0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
    byte[] wavTruum = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xB0, (byte)0xF0, 0x00,
            0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
    byte[] wavPist = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xB0, (byte)0xF0, 0x00,
            0x07, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};
    byte[] wavAuto = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xB0, (byte)0xF0, 0x00,
            0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};

    byte[] sReset = { (byte)0xFF, 0x22, (byte)0xA4,(byte)0xB0, (byte)0xF0, 0x11,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0xCC,(byte)0xAA, (byte)0xFF};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bugler);

        ConnectBluetooth();
        addListenerOnButton();
        addListenerOnSignalsSpinnerItemSelection();
        addListenerOnSignalsSoundsItemSelection();

    }

    public void addListenerOnButton() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null) {
            if (btAdapter.isEnabled()) {
                onOffButton = (ToggleButton) findViewById(R.id.toggleButtonBugler);
                onOffButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        //Пытаемся послать данные
                        try {
                            //Получаем выходной поток для передачи данных
                            outStream = clientSocket.getOutputStream();

                            if (onOffButton.isChecked()) outStream.write(testRxOn);
                            else outStream.write(testRxOff);

                        } catch (IOException e) {
                            Log.d("BLUETOOTH", e.getMessage());
                        }
                    }

                });
            }
        }
    }

    public void addListenerOnSignalsSpinnerItemSelection() {
        signalsSpinner = (Spinner) findViewById(R.id.spinnerSignalBugler);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> signalsAdapter = ArrayAdapter
                .createFromResource(this, R.array.signals,
                        android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        signalsAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        signalsSpinner.setAdapter(signalsAdapter);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null) {
            if (btAdapter.isEnabled()) {
                signalsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        //Пытаемся послать данные
                        try {
                            //Получаем выходной поток для передачи данных
                            outStream = clientSocket.getOutputStream();
                            switch (position) {
                                case 0:
                                    outStream.write(sReset);
                                    break;
                                case 1:
                                    outStream.write(sigAtten);
                                    break;
                                case 2:
                                    outStream.write(sigFire);
                                    break;
                                case 3:
                                    outStream.write(sigCncel);
                                    break;

                            }

                        } catch (IOException e) {
                            //Если есть ошибки, выводим их в лог
                            Log.d("BLUETOOTH", e.getMessage());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });
            }
        }
    }

    public void addListenerOnSignalsSoundsItemSelection(){
        soundsSpinner = (Spinner) findViewById(R.id.spinnerSoundBugler);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> soundsAdapter = ArrayAdapter
                .createFromResource(this, R.array.sounds,
                        android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        soundsAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        soundsSpinner.setAdapter(soundsAdapter);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null) {
            if (btAdapter.isEnabled()) {
                soundsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        //Пытаемся послать данные
                        try {
                            //Получаем выходной поток для передачи данных
                            OutputStream outStream = clientSocket.getOutputStream();

                            switch (position) {
                                case 0:
                                    outStream.write(sReset);
                                    break;
                                case 1:
                                    outStream.write(wavTest);
                                    break;
                                case 2:
                                    outStream.write(wavAlarm);
                                    break;
                                case 3:
                                    outStream.write(wavKap);
                                    break;
                                case 4:
                                    outStream.write(wavPop);
                                    break;
                                case 5:
                                    outStream.write(wavRing);
                                    break;
                                case 6:
                                    outStream.write(wavTruum);
                                    break;
                                case 7:
                                    outStream.write(wavPist);
                                    break;
                                case 8:
                                    outStream.write(wavAuto);
                                    break;
                            }

                        } catch (IOException e) {
                            //Если есть ошибки, выводим их в лог
                            Log.d("BLUETOOTH", e.getMessage());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });
            }
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

    @Override
    public void onStart(){
        super.onStart();


    }

    @Override
    public void onRestart(){
        super.onRestart();


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
}
