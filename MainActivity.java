package com.example.gabrielgomes.teste;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
   //Bluetooth Stuff
    private static final int activateBT = 1;
    private static final int connectBT = 2;
    private static String MAC = null;
    BluetoothDevice mDevice = null;
    //

    static String stuff = null;
    boolean connection = true;

    UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private ImageView botaoBluetooth;
    private ImageView botaoContador;
    private ImageView botaoCorrenteFuga;
    private ImageView botaoDownload;
    //get Global State atributes

    private AlertDialog.Builder alerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bundle bundle = new Bundle();
        bundle.putString("nomeCliente", "UUID");
        Intent intent = new Intent(this, CorrenteFugaActivity.class);
        intent.putExtras(bundle);
        //saving the Id of those imageViews to access it later
        botaoBluetooth = findViewById(R.id.bluetoothId);
        botaoContador = findViewById(R.id.contadorId);
        botaoCorrenteFuga = findViewById(R.id.correnteFugaId);
        botaoDownload = findViewById(R.id.downloadId);

        //the setOnClickListener allow us to implement the calling of the activity by the imageView
        botaoCorrenteFuga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent teste = new Intent(MainActivity.this, CorrenteFugaActivity.class);

                teste.putExtra("teste","teste");
                    startActivity(teste);
            }
        });
        botaoContador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ContadorActivity.class));


            }
        });
        botaoBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalState globalState = (GlobalState) getApplication();
                globalState.mAdapter = BluetoothAdapter.getDefaultAdapter();
                if(globalState.mAdapter == null){
                    Intent teste = new Intent();
                    //takes adressMac and put it inside the string macAdress
                    teste.putExtra("teste","teste");
                    Toast.makeText(MainActivity.this,"O dispositivo não suporta bluet" +
                            "ooth",Toast.LENGTH_LONG).show();

                } else if(!globalState.mAdapter.isEnabled()){
                    Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBT,activateBT);
                } else if(globalState.mAdapter.isEnabled() == true){
                    Intent openList = new Intent(MainActivity.this,devicesList.class);
                    startActivityForResult(openList, connectBT);
                }


            }
        });
        botaoDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating the alert dialog
                alerta = new AlertDialog.Builder(MainActivity.this);

                //setting up the alert dialog.
                alerta.setTitle("ALERTA");

                //configuring the message
                alerta.setMessage("Você deseja apagar da memória do aplicativo " +
                        "os itens baixados?");

                //does not permit the closing of the dialog clicking outside the box.
                alerta.setCancelable(false);

                alerta.setIcon(android.R.drawable.ic_delete);



                //buttons
                alerta.setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this,"Os dados NÃO foram " +
                                        "apagados da memória",Toast.LENGTH_LONG).show();
                            }
                        });
                alerta.setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this,"Dados apagados da " +
                                        "memória",Toast.LENGTH_LONG).show();
                            }
                        });
                alerta.create();
                alerta.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case activateBT:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(),"O bluetooth foi ativado",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"O bluetooth não foi ativado",Toast.LENGTH_LONG).show();
                    //finish();
                }
                break;
            case connectBT:
                if(resultCode == Activity.RESULT_OK){
                    //intent para passar os dados entre a main activity e a corrente de fuga
                   MAC = data.getExtras().getString(devicesList.macAdress);
                    GlobalState globalState = (GlobalState) getApplication();
                    mDevice = globalState.mAdapter.getRemoteDevice(MAC);
                    try{
                        globalState.mSocket = mDevice.createRfcommSocketToServiceRecord(mUUID);//canal para comunicação;
                        globalState.mSocket.connect(); //tentativa de conecção
                        connection = true;
                        Toast.makeText(getApplicationContext(),"Conectado com Sucesso", Toast.LENGTH_LONG).show();


                    }catch(IOException erro){
                        connection = false;
                        Toast.makeText(getApplicationContext(),"Ocorreu um erro", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Falha ao conectar",Toast.LENGTH_LONG).show();
                }
        }
    }
}
