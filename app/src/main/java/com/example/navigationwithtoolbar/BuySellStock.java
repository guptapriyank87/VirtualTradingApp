package com.example.navigationwithtoolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationwithtoolbar.productModel.Product;
import com.example.navigationwithtoolbar.productModel.ProductAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static com.example.navigationwithtoolbar.productModel.PortfolioProductAdapter.round;

public class BuySellStock extends AppCompatActivity
{

    public TextView companyName,price,status,companyCode,avaiStocks,worth;
    public int avi;
    String c,p,s,cd;
    AlertDialog placeBuyOrder,placeSellOrder;
    Button btnBuy,btnSell,btnWatch;
    Context ctx;
    Constants constants;

    Toolbar toolbar;
    View logo_back;

    //buy dialog declaration
    View buyView ;
    EditText buyNumber ;
    Button buyButton ;
    Button buyCancleButton;
    TextView debitAmount,BADCompanyCode ;
    Float prc;
    TextView dailogCompanyName,dailogCompanyPrice,dailogCompanyCode;



    ////sell dialog declaration
    View sellView ;
    EditText sellNumber ;
    Button sellButton ;
    Button sellCancleButton;
    TextView sellAmount,availableSellStocks ;
    Double sellprc;
    TextView selldailogCompanyName,selldailogCompanyPrice,selldailogCompanyCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell_stock);
        ctx = this;
        final AvailableStocks availableStocks = new AvailableStocks(this);


        toolbar = findViewById(R.id.buySell_toolbar);
        logo_back = toolbar.getChildAt(1);
        logo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Buy dialog initialisation
        buyView = getLayoutInflater().inflate(R.layout.buy_dialog,null);
        buyNumber  = buyView.findViewById(R.id.buyNumber);
        buyButton = buyView.findViewById(R.id.btnBADBuy);
        BADCompanyCode = buyView.findViewById(R.id.BADcompanyCode);
        buyCancleButton = buyView.findViewById(R.id.btnBADCancle);
        debitAmount = buyView.findViewById(R.id.debitAmount);
        dailogCompanyName = buyView.findViewById(R.id.buyDialogCompanyName);
        dailogCompanyPrice = buyView.findViewById(R.id.buyDialogCurrentPrice);

        //Sell dialog initialisation
        sellView = getLayoutInflater().inflate(R.layout.sell_dialog,null);
        sellNumber = sellView.findViewById(R.id.SADSellNumber);
        sellButton = sellView.findViewById(R.id.btnSADSell);
        sellCancleButton = sellView.findViewById(R.id.btnSADCancle);
        sellAmount = sellView.findViewById(R.id.sellAmount);
        selldailogCompanyName = sellView.findViewById(R.id.SellDialogCompanyName);
        selldailogCompanyPrice = sellView.findViewById(R.id.sellDialogCurrentPrice);
        selldailogCompanyCode = sellView.findViewById(R.id.SADcompanyCode);
        availableSellStocks = sellView.findViewById(R.id.SADAvailableStocks);


        final Intent in = getIntent();
        worth = findViewById(R.id.BSSworth);
        companyName = findViewById(R.id.companyName);
        companyCode = findViewById(R.id.BScompanyCode);
        btnBuy = findViewById(R.id.btnBSS_Buy);
        btnSell = findViewById(R.id.btnBSS_Sell);
        btnWatch = findViewById(R.id.btnBSS_Watch);
        price = findViewById(R.id.currentPrice);
        status = findViewById(R.id.status);
        avaiStocks = findViewById(R.id.BSSavailableStocks);
        placeBuyOrder = new AlertDialog.Builder(this).create();
        placeSellOrder = new AlertDialog.Builder(this).create();
        c = in.getStringExtra("companyName");
        p = in.getStringExtra("price");
        p = p.replace("INR ","");
        cd = in.getStringExtra("code");
        prc = Float.parseFloat(p);
        s = in.getStringExtra("status");
        Log.i("strings","\n"+in.getStringExtra("companyName")
                                +"\n"+in.getStringExtra("price")
                                +"\n"+in.getStringExtra("status"));
        companyName.setText(in.getStringExtra("companyName"));
        companyCode.setText(cd);
        price.setText(in.getStringExtra("price"));

        if (s.equals("Strong Buy")){
            status.setText(s);
            status.setTextColor(getResources().getColor(R.color.design_default_color_background));
            status.setBackgroundResource(R.color.strongBuy);
        }else if (s.equals("Strong Sell")){
            status.setText(s);
            status.setTextColor(getResources().getColor(R.color.design_default_color_background));
            status.setBackgroundResource(R.color.strongSell);
        }else if (s.equals("Buy")){
            status.setText(s);
            status.setTextColor(getResources().getColor(R.color.design_default_color_background));
            status.setBackgroundResource(R.color.buy);
        }else if (s.equals("Sell")){
            status.setText(s);
            status.setTextColor(getResources().getColor(R.color.design_default_color_background));
            status.setBackgroundResource(R.color.sell);
        }else {
            status.setText(s);
            status.setTextColor(getResources().getColor(R.color.design_default_color_background));
            status.setBackgroundResource(R.color.neutral);
        }
        constants = new Constants(this);
        availableStocks.execute("getavi",constants.getEmail(),in.getStringExtra("companyName"));
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogCompanyName.setText(c);
                buyButton.setEnabled(false);
                dailogCompanyPrice.setText("INR "+p);
                BADCompanyCode.setText(cd);
                buyNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length()==0){
                            s = s+"0";
                        }
                        Log.i("Textchange",s+"  "+start+"  "+before+"  "+count);
                        float nbr = Float.parseFloat(s.toString());
                        float amt = nbr*prc;
                        String amount = String.format("%.0f",round(amt,2));
                        debitAmount.setText("INR "+amount);
                        if(nbr>0 && nbr<101){
                            buyButton.setEnabled(true);
                        }else{
                            buyButton.setEnabled(false);
                        }
                        if (nbr>100){
                            buyNumber.setError("you can only buy 100 or less stocks at a time.");
                            buyNumber.setTextColor(getResources().getColor(R.color.strongSell));
                        }else{
                            buyNumber.setTextColor(getResources().getColor(R.color.values));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                buyCancleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        placeBuyOrder.hide();
                    }
                });
                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constants constants = new Constants(ctx);
                        String email = constants.getEmail();
                        BuyStock buyStock = new BuyStock(ctx);
                        buyStock.execute("buy",email,cd,buyNumber.getText().toString());
                        placeBuyOrder.hide();
                    }
                });
                placeBuyOrder.setView(buyView);
                placeBuyOrder.show();
            }
        });
        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellButton.setEnabled(false);
                selldailogCompanyName.setText(c);
                selldailogCompanyPrice.setText("INR " + p);
                sellNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length()==0){
                            s = s+"0";
                        }
                        Log.i("Textchange",s+"  "+start+"  "+before+"  "+count);
                        float nbr = Float.parseFloat(s.toString());
                        float amt = prc*nbr;
                        String amount = String.format("%.0f",round(amt,2));
                        sellAmount.setText("INR "+amount);
                        if(Integer.parseInt(s.toString()) != 0) {
                            if (avi >= Integer.parseInt(s.toString())) {
                                sellNumber.setTextColor(ContextCompat.getColor(ctx, R.color.strongBuy));
                                sellButton.setEnabled(true);
                            } else {
                                sellNumber.setTextColor(ContextCompat.getColor(ctx, R.color.strongSell));
                                sellButton.setEnabled(false);
                            }
                        }else{
                            sellButton.setEnabled(false);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                availableSellStocks.setText(String.valueOf(avi));
                sellCancleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        placeSellOrder.hide();
                    }
                });
                sellButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constants constants = new Constants(ctx);
                        String email = constants.getEmail();
                        SellStock sellStock = new SellStock(ctx);
                        sellStock.execute("sell",email,cd,sellNumber.getText().toString());
                        avi = avi - Integer.parseInt(sellNumber.getText().toString());

                        placeSellOrder.hide();
                    }
                });

                placeSellOrder.setView(sellView);
                placeSellOrder.show();
            }
        });
        btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Watchlist watchlist = new Watchlist(ctx);
                watchlist.execute("add",constants.getEmail(),cd);
            }
        });

    }
    public class BuyStock extends AsyncTask<String, String, String> {
        Context context;
        private Constants constants;
        private String ip;



        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            constants = new Constants(context);
            ip = constants.getIp();
            String buy_url = "http://"+ip+"/pgr/buystock.php";
            if (type.equals("buy")){
                try {
                    String email = strings[1];
                    String companyCode = strings[2];
                    String buyNumber = strings[3];
                    Log.i("status","inside the buy try catch");
                    URL url = new URL(buy_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.i("status","Http url connection established properly");

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    Log.i("status","buffer writer working");

                    String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                            URLEncoder.encode("companyCode","UTF-8")+"="+URLEncoder.encode(companyCode,"UTF-8")+"&"+
                            URLEncoder.encode("buyNumber","UTF-8")+"="+URLEncoder.encode(buyNumber,"UTF-8");
                    Log.i("postData",post_data);

                    bufferedWriter.write(post_data);
                    Log.i("status","bufferedWriter.write(post_data) executed successfully");

                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    //reading response for feedback
                    Log.i("status","now reading feedback");


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result = "";
                    String line  = "";
                    while ((line = bufferedReader.readLine())!=null){
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    System.out.println(result);
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                boolean buySuccessful = s.contains("buysuccessful");
                boolean buyUnsuccessful = s.contains("buyunsuccessful");

                if (buySuccessful) {
                    Log.i("status","insert successful!");
                    Toast.makeText(context, "Your order was successful!", Toast.LENGTH_SHORT).show();
                    constants = new Constants(context);
                    AvailableStocks availableStocks = new AvailableStocks(context);
                    availableStocks.execute("getavi",constants.getEmail(),c);
                } else if (buyUnsuccessful) {
                    Toast.makeText(context, "Your order was unsuccessful!", Toast.LENGTH_SHORT).show();
                } else {
                    //alertDialog.setMessage("Unknown error!");
                    Toast.makeText(context, "Network error! Please try again", Toast.LENGTH_SHORT).show();
                    Log.i("error", s);
                }
            }catch (Exception e){
                Toast.makeText(context, "Unknown error! Please try again", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                Log.i("Exception","e");
                Log.i("error", s);
            }
        }

        public BuyStock(Context ctx) {
            context = ctx;
        }
    }
    public class SellStock extends AsyncTask<String, String, String> {
        Context context;
        private Constants constants;
        private String ip;



        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            constants = new Constants(context);
            ip = constants.getIp();
            String sell_url = "http://"+ip+"/pgr/sellstock.php";
            if (type.equals("sell")){
                try {
                    String email = strings[1];
                    String companyCode = strings[2];
                    String sellNumber = strings[3];
                    Log.i("status","inside the buy try catch");
                    URL url = new URL(sell_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.i("status","Http url connection established properly");

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    Log.i("status","buffer writer working");

                    String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                            URLEncoder.encode("companyCode","UTF-8")+"="+URLEncoder.encode(companyCode,"UTF-8")+"&"+
                            URLEncoder.encode("sellNumber","UTF-8")+"="+URLEncoder.encode(sellNumber,"UTF-8");
                    Log.i("postData",post_data);

                    bufferedWriter.write(post_data);
                    Log.i("status","bufferedWriter.write(post_data) executed successfully");

                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    //reading response for feedback
                    Log.i("status","now reading feedback");


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result = "";
                    String line  = "";
                    while ((line = bufferedReader.readLine())!=null){
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                boolean sellSuccessful = s.contains("sellsuccessful");
                boolean sellUnsuccessful = s.contains("sellunsuccessful");

                if (sellSuccessful) {
                    Log.i("status","insert successful!");
                    Toast.makeText(context, "Your order was successful!", Toast.LENGTH_SHORT).show();
                    constants = new Constants(context);
                    AvailableStocks availableStocks = new AvailableStocks(context);
                    availableStocks.execute("getavi",constants.getEmail(),c);
                }else if (sellUnsuccessful) {
                    Toast.makeText(context, "Your order was unsuccessful!", Toast.LENGTH_SHORT).show();
                }else {
                    //alertDialog.setMessage("Unknown error!");
                    Toast.makeText(context, "Network error! Please try again", Toast.LENGTH_SHORT).show();
                    Log.i("error", s);
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.i("Exception","e");
                Log.i("error", s);
            }
        }

        public SellStock(Context ctx) {
            context = ctx;
        }
    }

    public class AvailableStocks extends AsyncTask<String, String, String> {
        Context context;
        private Constants constants;
        private String ip;
        private String r;
        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            constants = new Constants(context);
            ip = constants.getIp();
            String sell_url = "http://"+ip+"/pgr/getavailablestocks.php";
            if (type.equals("getavi")){
                try {
                    String email = strings[1];
                    String companyName = strings[2];
                    Log.i("status","inside the buy try catch");
                    URL url = new URL(sell_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.i("status","Http url connection established properly");

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    Log.i("status","buffer writer working");

                    String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                            URLEncoder.encode("companyName","UTF-8")+"="+URLEncoder.encode(companyName,"UTF-8");
                            Log.i("postData",post_data);

                    bufferedWriter.write(post_data);
                    Log.i("status","bufferedWriter.write(post_data) executed successfully");

                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    //reading response for feedback
                    Log.i("status","now reading feedback");


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result = "";
                    String line  = "";
                    while ((line = bufferedReader.readLine())!=null){
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                    finish();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            //available stocks will be received here in s.
            if (s != null){

                s = s.replace("INR ","");
                float w = Float.parseFloat(s)*Float.parseFloat(p);
                double wRoundOff = Math.round(w * 100.0) / 100.0;
                worth.setText("INR "+ String.format("%.0f",round(w,2)));
                avaiStocks.setText(s);
                Log.i("avi",s);
                avi = Integer.parseInt(s);
            }else{
                avi = 0;
                Toast.makeText(context, "there was a network error", Toast.LENGTH_SHORT).show();
            }


        }
        public AvailableStocks(Context ctx) {
            context = ctx;
        }
    }
    public class Watchlist extends AsyncTask<String, String, String> {
        Context context;
        private Constants constants;
        private String ip;



        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            constants = new Constants(context);
            ip = constants.getIp();
            String add_url = "http://"+ip+"/pgr/addtowatchlist.php";
            if (type.equals("add")){
                try {
                    String email = strings[1];
                    String companyCode = strings[2];
                    Log.i("status","inside the buy try catch");
                    URL url = new URL(add_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.i("status","Http url connection established properly");

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    Log.i("status","buffer writer working");

                    String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                            URLEncoder.encode("companyCode","UTF-8")+"="+URLEncoder.encode(companyCode,"UTF-8");
                    Log.i("postData",post_data);

                    bufferedWriter.write(post_data);
                    Log.i("status","bufferedWriter.write(post_data) executed successfully");

                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    //reading response for feedback
                    Log.i("status","now reading feedback");


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result = "";
                    String line  = "";
                    while ((line = bufferedReader.readLine())!=null){
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                boolean addSuccessful = s.contains("insertsuccessful");
                boolean addUnsuccessful = s.contains("insertunsuccessful");
                boolean alreadyExist = s.contains("alreadyexist");

                if (addSuccessful) {
                    Log.i("status","insert successful!");
                    Toast.makeText(context, "Successfully added to your watchlist!", Toast.LENGTH_SHORT).show();
                    constants = new Constants(context);
                    AvailableStocks availableStocks = new AvailableStocks(context);
                    availableStocks.execute("getavi",constants.getEmail(),c);
                }else if (addUnsuccessful) {
                    Toast.makeText(context, "There was some problem adding this company to your watchlist", Toast.LENGTH_SHORT).show();
                }else if (alreadyExist)
                {
                    Toast.makeText(context, "this company is already in your watchlist", Toast.LENGTH_SHORT).show();
                }else{
                    //alertDialog.setMessage("Unknown error!");
                    Toast.makeText(context, "Network error! Please try again", Toast.LENGTH_SHORT).show();
                    Log.i("error", s);
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.i("Exception","e");
                Log.i("error",s);
            }
        }

        public Watchlist(Context ctx) {
            context = ctx;
        }
    }
    public float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
