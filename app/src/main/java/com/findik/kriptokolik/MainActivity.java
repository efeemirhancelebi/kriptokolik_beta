package com.findik.kriptokolik;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalProfit;
    private PortfolioDBHelper portfolioDBHelper;
    private ListView coinListView;
    private ArrayAdapter<String> adapter;

    private ArrayList<String> coinDisplayList;

    private ArrayList<String> coinNames;
    private HashMap<String, Double> priceMap;
    private DBHelper dbHelper;
    private Button coinEkleme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // database silip programı baştan başlatmk için
        // this.deleteDatabase("CoinDatabase.db");
        // deleteAllDatabases();

        totalProfit   = findViewById(R.id.totalProfit);
        coinListView  = findViewById(R.id.coinListView);
        coinEkleme    = findViewById(R.id.coin_ekle);

        portfolioDBHelper = new PortfolioDBHelper(this);
        dbHelper          = new DBHelper(this);

        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder().permitAll().build()
        );

        coinDisplayList = new ArrayList<>();
        coinNames       = new ArrayList<>();
        priceMap        = new HashMap<>();

        fetchCoinDataSync();

        adapter = new ArrayAdapter<String>(this, R.layout.row_coin, coinDisplayList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.row_coin, parent, false);
                }
                String rowText = getItem(position);

                TextView tvShortName = convertView.findViewById(R.id.tvShortName);
                TextView tvTotal     = convertView.findViewById(R.id.tvTotal);
                TextView tvAmount    = convertView.findViewById(R.id.tvAmount);
                TextView tvPrice     = convertView.findViewById(R.id.tvPrice);

                if(rowText != null){
                    String[] tokens = rowText.split("\\|");
                    if(tokens.length == 4){
                        tvShortName.setText(tokens[0]);
                        tvTotal.setText(tokens[1]);
                        tvAmount.setText(tokens[2]);
                        tvPrice.setText(tokens[3]);
                    } else {
                        tvShortName.setText(rowText);
                    }
                }
                return convertView;
            }
        };
        coinListView.setAdapter(adapter);


        coinEkleme.setOnClickListener(v->{
            Intent i = new Intent(MainActivity.this, coin_arama_ekrani.class);
            i.putStringArrayListExtra("veri", coinNames);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        double totalVal = loadCoinsFromDatabase();
        portfolioDBHelper.insertPortfolioValue(totalVal);
        plotPortfolioGraph();
        adapter.notifyDataSetChanged();
    }

    private void fetchCoinDataSync(){
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url("https://trade-api.coinlist.co/v1/symbols")
                .build();
        Call call = client.newCall(req);

        try(Response response = call.execute()){
            if(!response.isSuccessful()){
                Log.e("API_ERROR","Response code: "+response.code());
                return;
            }
            String respBody = response.body().string();
            JSONObject root = new JSONObject(respBody);
            JSONArray arr   = root.getJSONArray("symbols");
            for(int i=0; i<arr.length(); i++){
                JSONObject coinObj = arr.getJSONObject(i);
                String baseC = coinObj.optString("base_currency","").toUpperCase().trim();

                double price = coinObj.optDouble("last_price",0.0);
                if(price==0.0){
                    price = coinObj.optDouble("mark_price",0.0);
                }
                if(!baseC.isEmpty()){
                    priceMap.put(baseC, price);
                    if(!coinNames.contains(baseC)){
                        coinNames.add(baseC);
                    }
                }
            }

        } catch(IOException | JSONException e){
            e.printStackTrace();
        }
    }

    private double loadCoinsFromDatabase(){
        coinDisplayList.clear();
        double totalPortfolio=0.0;

        Cursor cursor = dbHelper.getAllCoins();
        if(cursor!=null && cursor.moveToFirst()){
            do {
                String dbCoin = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_COIN_NAME));
                if(dbCoin==null) dbCoin="UNKNOWN";
                dbCoin = dbCoin.toUpperCase().trim();

                double miktar = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_AMOUNT));

                double price=0.0;
                Double p= priceMap.get(dbCoin);
                if(p!=null){
                    price=p;
                }

                BigDecimal bdPrice  = BigDecimal.valueOf(price);
                BigDecimal bdMiktar = BigDecimal.valueOf(miktar);
                BigDecimal bdTotal  = bdPrice.multiply(bdMiktar);

                DecimalFormat df = new DecimalFormat("#,##0.####");
                String sPrice  = df.format(bdPrice.doubleValue());
                String sMiktar = df.format(bdMiktar.doubleValue());
                String sTotal  = df.format(bdTotal.doubleValue());

                String rowText = String.format("%s|%s|%s|%s",
                        dbCoin, sTotal, sMiktar, sPrice);
                coinDisplayList.add(rowText);

                totalPortfolio += bdTotal.doubleValue();

            } while(cursor.moveToNext());
            cursor.close();
        }

        DecimalFormat dfOverall = new DecimalFormat("#,##0.##");
        totalProfit.setText("Total Portfolio: " + dfOverall.format(totalPortfolio));
        return totalPortfolio;
    }
    private void plotPortfolioGraph(){
        GraphView graph = findViewById(R.id.coin_graphics);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        // read from second DB
        Cursor c = portfolioDBHelper.getAllPortfolioValues();
        if(c!=null && c.moveToFirst()){
            while(!c.isAfterLast()){
                long ts = c.getLong(c.getColumnIndexOrThrow(PortfolioDBHelper.COLUMN_TIMESTAMP));
                double val = c.getDouble(c.getColumnIndexOrThrow(PortfolioDBHelper.COLUMN_VALUE));

                series.appendData(
                        new DataPoint(new Date(ts), val),
                        true,
                        10000
                );
                c.moveToNext();
            }
            c.close();
        }
        graph.removeAllSeries();
        graph.addSeries(series);


        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));


        if(series.getHighestValueX() > series.getLowestValueX()){
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(series.getLowestValueX());
            graph.getViewport().setMaxX(series.getHighestValueX());
        }

        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
    }


    public void deleteAllDatabases() {
        String[] dbList = this.databaseList();
        for (String dbFileName : dbList) {
            boolean deleted = this.deleteDatabase(dbFileName);
            if (deleted) {
                Log.d("DB_DELETE", "Deleted DB file: " + dbFileName);
            } else {
                Log.d("DB_DELETE", "Could not delete DB file: " + dbFileName);
            }
        }
    }
}
