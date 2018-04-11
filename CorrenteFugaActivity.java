package com.example.gabrielgomes.teste;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CorrenteFugaActivity extends Activity {
    LineChart mChart;
    private Button bOsc;
    private static final int MESSAGE_READ = 3;
    Handler mHandler;
   // ConnectedThread connectedThread;
    private static String c = null;
    float dataFloat = 0;
    float[] dataVector = new float[36];
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_corrente_fuga);
        bOsc = findViewById(R.id.atualizationId);
        mChart = findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        ArrayList<Entry> yValues = new ArrayList<>();
        for(float i = 0; i < 50; i++ ){
            double angle = (2*3.1416/50)*i;
            double sin = Math.sin(angle);
            yValues.add(new Entry(i,(float) sin));
        }
        for(int i = 0; i < 36; i++){
            dataVector[i] = 0;
        }
        LineDataSet set1 = new LineDataSet(yValues,"Data Set 1");
        set1.setFillAlpha(110);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        mChart.setData(data);

        bOsc.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View view) {
                GlobalState globalState = (GlobalState) getApplication();

                //creating an intent which takes the information from the other activity
                //connectedThread.cancel();
                ConnectedThread connectedThread = new ConnectedThread(globalState.mSocket);
                connectedThread.start();
                connectedThread.write("S");
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        StringBuilder bluetoothData = new StringBuilder();
                        if(msg.what == MESSAGE_READ){
                            String received = (String) msg.obj;
                            bluetoothData.append(received);
                            int end = bluetoothData.indexOf("}");
                     //       dataFloat = Float.parseFloat(bluetoothData);
                    //        ArrayList<Entry> yData = new ArrayList<>();
                   //         yData.add(new Entry(,dataFloat));
                            if(end >0){
                             String fullData1 = bluetoothData.substring(0,end);
                             int size = bluetoothData.length();
                               if(fullData1.charAt(0) == '{'){
                                    String finalData = fullData1.substring(1,end);

                        //            dataFloat = Float.parseFloat(finalData);
                        //            dataVector[i] = dataFloat;
                        //            if(i== 35){i=0;}else{i=i+1;}
/*
                                    if(i == 35){
                                        ArrayList<Entry> yData = new ArrayList<>();
                                        for(int j = 0; j < 36; j++){
                                            yData.add(new Entry(j,dataVector[j]));
                                        }
                                        LineDataSet set2 = new LineDataSet(yData,"Data Set 1");
                                        set2.setFillAlpha(110);
                                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                        dataSets.add(set2);
                                        LineData data2 = new LineData(dataSets);
                                        mChart.setData(data2);
                                        mChart.setData(data2);
                                        mChart.notifyDataSetChanged();
                                        mChart.invalidate();
                                        i = 0;
                                    }else {
                                        i = i + 1;
                                    }
                                    */
                                   Toast.makeText(getApplicationContext(),finalData,Toast.LENGTH_SHORT).show();
                                }
                                bluetoothData.delete(0,end);
                            }

                        }
                    }
                };

                }
        });



    }

    private class ConnectedThread extends Thread {
 //       private final BluetoothSocket mmSocket;
        final GlobalState globalState = (GlobalState) getApplication();
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread (BluetoothSocket socket) {

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = globalState.mSocket.getInputStream();
                tmpOut = globalState.mSocket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    String dataBt = new String(buffer,0,bytes);
                    mHandler.obtainMessage(MESSAGE_READ,bytes,-1,dataBt).sendToTarget();
                    //adding message float

                    // Send the obtained bytes to the UI activity
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String dataSend) {
            byte[] bytes = dataSend.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                globalState.mSocket.close();
            } catch (IOException e) { }
        }
    }
}