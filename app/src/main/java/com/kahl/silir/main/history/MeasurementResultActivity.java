package com.kahl.silir.main.history;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.kahl.silir.R;
import com.kahl.silir.entity.DataGenerator;
import com.kahl.silir.entity.RiemmanIntegrator;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import java.util.ArrayList;
import java.util.List;

public class MeasurementResultActivity extends AppCompatActivity {
    private final int MENU_UPLOAD_ID = 1204;
    private final int MENU_DELETE_ID = 1998;

    private LineChart flowVolumeLoop;
    private Activity activity = this;

    private double[] flowVolumeData = new double[6000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_result);

//        flowVolumeLoop = (LineChart) findViewById(R.id.flow_volume_chart);
//        new AsyncCaller().execute();
    }

    private class AsyncCaller extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(activity);
        private LineData data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Calculating...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            double[] flowRateData = new DataGenerator().generate();
            flowVolumeData[0] = 0;
            List<Entry> entries = new ArrayList<>();
            for (int i = 0; i < 6000; i++) {
                entries.add(new Entry(i, (float) flowRateData[i]));
            }
            LineDataSet dataSet = new LineDataSet(entries, "Flow rate");
            dataSet.setColor(getResources().getColor(R.color.colorPrimary));
            data = new LineData(dataSet);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            flowVolumeLoop.setData(data);
            flowVolumeLoop.invalidate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem uploadItem = menu.add(0, MENU_UPLOAD_ID, 0, "Upload to cloud");
        uploadItem.setIcon(MaterialDrawableBuilder.with(this).setColor(Color.WHITE)
                .setIcon(MaterialDrawableBuilder.IconValue.CLOUD_UPLOAD).build());
        uploadItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        MenuItem deleteItem = menu.add(0, MENU_DELETE_ID, 1, "Delete");
        deleteItem.setIcon(MaterialDrawableBuilder.with(this).setColor(Color.WHITE)
                .setIcon(MaterialDrawableBuilder.IconValue.DELETE).build());
        deleteItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }
}
