package com.example.gabrielgomes.teste;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class devicesList extends ListActivity {
    //This class uses bluetooth adapter as the other.
    private BluetoothAdapter mBluetoothAdapter = null;
    static String macAdress = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creates the array which will store the devices name and stuff.
        ArrayAdapter<String> arrayBluetooth = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);


        //get the bluetooth Adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //takes the returned devices and store their inside the pairedDevices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String btName = device.getName();
                String macBt = device.getAddress();
                arrayBluetooth.add(btName + "\n" + macBt);
            }
        }
        setListAdapter(arrayBluetooth);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String generalInformation = ((TextView) v).getText().toString();
        String adressMac = generalInformation.substring(generalInformation.length() - 17);
        Intent macReturn = new Intent();
        //takes adressMac and put it inside the string macAdress
        macReturn.putExtra(macAdress, adressMac);
        setResult(RESULT_OK, macReturn);
        finish();
    }
}

