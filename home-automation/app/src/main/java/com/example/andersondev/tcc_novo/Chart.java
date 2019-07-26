package com.example.andersondev.tcc_novo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.lang.reflect.Array;
import java.util.ArrayList;

public class Chart extends AppCompatActivity{

    private static final String TAG = "Chart";
    Cursor aguaValor;
    Cursor aguaLitros;
    Cursor energiaValor;
    private LineChart nChart;
    BancoDados db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        Intent intent = getIntent();
        String retorno = intent.getStringExtra(Intent.EXTRA_TEXT);
        db = new BancoDados(getApplicationContext());
        Log.d("MENSAGEM", retorno);
        int month = Integer.parseInt(retorno) + 1;
        nChart = (LineChart) findViewById(R.id.chart);

        //nChart.setOnChartGestureListener(Chart.this);
        //nChart.setOnChartValueSelectedListener(Chart.this);
        ArrayList<Entry> yAguaValor = new ArrayList<>();
        ArrayList<Entry> yAguaLitro = new ArrayList<>();
        ArrayList<Entry> yEnergia   = new ArrayList<>();

        aguaValor = db.getConsumo(month, 1);
        aguaLitros = db.getConsumo(month, 3);
        energiaValor = db.getConsumo(month, 2);

        Log.d("MENSAGEM3", String.valueOf(aguaValor.getCount()));
        if(aguaValor.moveToFirst()) {
            while (aguaValor.moveToNext()) {
                yAguaValor.add(new Entry(Integer.parseInt(aguaValor.getString(1)), Float.valueOf(aguaValor.getString(0))));
            }
        }

        if(aguaLitros.moveToFirst()){
            while(aguaLitros.moveToNext()){
                yAguaLitro.add(new Entry(Integer.parseInt(aguaLitros.getString(1)), Float.valueOf(aguaLitros.getString(0))));
            }
        }

        if(energiaValor.moveToFirst()){
            while(energiaValor.moveToNext()){
                yEnergia.add(new Entry(Integer.parseInt(energiaValor.getString(1)), Float.valueOf(energiaValor.getString(0))));
            }
        }

        nChart.setDragEnabled(true);
        nChart.setScaleEnabled(false);
        nChart.setPinchZoom(true);
        //yAguaValor.add(new Entry(29, 50));
        ArrayList<Entry> yValues = new ArrayList<>();
        yAguaValor.add(new Entry(1, 0.0002f));
        yAguaValor.add(new Entry(2, 0.000002f));
        yAguaValor.add(new Entry(3, 0.2f));
        yAguaValor.add(new Entry(4, 0.02f));
        yAguaValor.add(new Entry(5, 0.03f));
        yAguaValor.add(new Entry(6, 0.05f));
        yAguaValor.add(new Entry(7, 5f));

        yAguaLitro.add(new Entry(1, 0.005f));
        yAguaLitro.add(new Entry(2, 0.003002f));
        yAguaLitro.add(new Entry(3, 0.5f));
        yAguaLitro.add(new Entry(4, 0.0002f));
        yAguaLitro.add(new Entry(5, 0.035f));
        yAguaLitro.add(new Entry(6, 0.15f));
        yAguaLitro.add(new Entry(7, 4f));

        yEnergia.add(new Entry(1, 0.3002f));
        yEnergia.add(new Entry(2, 0.020002f));
        yEnergia.add(new Entry(3, 0.6f));
        yEnergia.add(new Entry(4, 0.002f));
        yEnergia.add(new Entry(5, 0.003f));
        yEnergia.add(new Entry(6, 0.05f));
        yEnergia.add(new Entry(7, 3f));



        LineDataSet set1 = new LineDataSet( yAguaValor, "Valor água");
        LineDataSet set2 = new LineDataSet( yAguaLitro, "Litros água");
        LineDataSet set3 = new LineDataSet( yEnergia, "Valor energia");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);

        set1.setFillAlpha(110);
        set1.setLineWidth(5);

        set2.setFillAlpha(110);
        set2.setLineWidth(5);
        set2.setColor(Color.RED);

        set3.setFillAlpha(110);
        set3.setLineWidth(5);
        set3.setColor(R.color.colorAccent);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        LineData data = new LineData(dataSets);

        nChart.setData(data);
        nChart.invalidate();
    }
}
