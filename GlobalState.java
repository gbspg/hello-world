package com.example.gabrielgomes.teste;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;

public class GlobalState extends Application {
    BluetoothAdapter mAdapter = null;
    BluetoothSocket mSocket = null;

}
