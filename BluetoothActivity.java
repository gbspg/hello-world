package com.example.gabrielgomes.teste;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BluetoothActivity extends Activity {
private ListView listaBluetooth;
private String[] para_raios = {"Para-raios 1", "Para-raios 2", "Para-raios 3",
            "Para-raios 4", "Para-raios 5", "Para-raios 6", "Para-raios 7"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        listaBluetooth = (ListView) findViewById(R.id.listaBluetoothId);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                para_raios
        );
        listaBluetooth.setAdapter(adaptador);
        listaBluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Pareado e Conectado",Toast.LENGTH_LONG).show();
            }
        });

    }
}
