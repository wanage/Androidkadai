package com.app05.app05;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Calendar;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils;
import weka.filters.unsupervised.attribute.Remove;
import weka.core.*;

public class MainActivity extends Activity /*implements SensorEventListener*/{

    private SensorManager manager;
    private Sensor sensor;
    private SensorEventListener sample_listener;
    float gx,gy,gz;


    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    int s,m;

    Instances train;
    J48 classifier;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        setContentView(R.layout.activity_main);

        text1 = (TextView)findViewById(R.id.textView1);
        text2 = (TextView)findViewById(R.id.textView2);
        text3 = (TextView)findViewById(R.id.textView3);
        text4 = (TextView)findViewById(R.id.answerView);

        sample_listener = new SampleSensorEventListener();
        Button sendButton = (Button) findViewById(R.id.button1);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MyView.class);

                startActivity(intent);
            }
        });
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Start:",s +":"+ m);


            }
        });
        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Stop:",s +":"+ m);
            }
        });

        //ArffLoader al = new ArffLoader();
        try {
            ArffLoader al = new ArffLoader();
            al.setFile(new File("/storage/sdcard0/arffdata/Human.arff"));
            train = new Instances(al.getDataSet());
            train.setClassIndex(train.numAttributes()-1);
            classifier = new J48();
            classifier.buildClassifier(train);

        //} catch (IOException e) {
        //    e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

//            @SuppressLint("SdCardPath") ConverterUtils.DataSource source = new ConverterUtils.DataSource("/storage/sdcard0/arffdata/Human.arff");
//            Instances  train= source.getDataSet();

//            Evaluation eval = new Evaluation(train);
//            eval.evaluateModel(classifier, train);

//            System.out.println(eval.toSummaryString());


    }
    @Override
    protected void onResume(){
        super.onResume();
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor  = manager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
        manager.registerListener(sample_listener, sensor, SensorManager.SENSOR_DELAY_NORMAL );
    }
    @Override
    protected void onPause(){
        super.onPause();
        manager.unregisterListener(sample_listener);
    }

    class SampleSensorEventListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent e) {
            if(e.sensor.getType() == Sensor.TYPE_ACCELEROMETER ){

                Log.d("aiueo","mae");

                gx = e.values[0];
                gy = e.values[1];
                gz = e.values[2];
                String str_x = "X軸の加速度:" + gx;
                text1.setText(str_x);
                String str_y = "Y軸の加速度:" + gy;
                text2.setText(str_y);
                String str_z = "Z軸の加速度:" + gz;
                text3.setText(str_z);
//                Kakikomi();
                Log.d("aiueo","ato");

                j48make();

            }
        }


        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

    }

    private void Kakikomi(){
        super.onStop();
        OutputStream out;
        Calendar now = Calendar.getInstance();
        s = now.get(now.SECOND);
        m = now.get(now.MILLISECOND);

        try {
            out = openFileOutput("XYZ.csv", Context.MODE_APPEND);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));

            //追記する
            writer.write(String.valueOf(gx) + "," + String.valueOf(gy) + ","+String.valueOf(gz)+",,,"+s+":"+m+"\n");
            writer.close();
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }


    }

    /**
     * This example class trains a J48 classifier on a dataset and outputs for
     * a second dataset the actual and predicted class label, as well as the
     * class distribution.
     */


    public void j48make() {
        try {
            Attribute x = new Attribute("x", 0);
            Attribute y = new Attribute("y", 1);
            Attribute z = new Attribute("z", 2);

            Instance instance =  new DenseInstance(3);
            instance.setValue(x, gx);
            instance.setValue(y, gy);
            instance.setValue(z, gz);
            instance.setDataset(train);

            double result = classifier.classifyInstance(instance);
//           System.out.println(result);

            if(result == 1){
                text4.setText("静止中");
            }
            else if(result == 0){
                text4.setText("歩行中");
            }
            else {
                text4.setText("走行中");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}