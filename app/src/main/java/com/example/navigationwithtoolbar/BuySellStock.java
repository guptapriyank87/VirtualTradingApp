package com.example.navigationwithtoolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BuySellStock extends AppCompatActivity {

    TextView companyName,price,status;
    String c,p,s;
    AlertDialog placeOrder;
    Button btnBuy,btnSell,btnWatch;
    View buyView ;
    EditText buyNumber ;
    Button buyButton ;
    Button cancleButton;
    TextView debitAmount ;
    Double prc;
    TextView dailogCompanyName,dailogCompanyPrice,dailogCompanyCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell_stock);


        buyView = getLayoutInflater().inflate(R.layout.buy_dialog,null);
        buyNumber  = buyView.findViewById(R.id.buyNumber);
        buyButton = buyView.findViewById(R.id.btnBADBuy);
        cancleButton = buyView.findViewById(R.id.btnBADCancle);
        debitAmount = buyView.findViewById(R.id.debitAmount);
        dailogCompanyName = buyView.findViewById(R.id.buyDialogCompanyName);
        dailogCompanyPrice = buyView.findViewById(R.id.buyDialogCurrentPrice);
        dailogCompanyCode = buyView.findViewById(R.id.debitAmount);


        Intent in = getIntent();
        companyName = findViewById(R.id.companyName);
        btnBuy = findViewById(R.id.btnBSS_Buy);
        btnSell = findViewById(R.id.btnBSS_Sell);
        btnWatch = findViewById(R.id.btnBSS_Watch);
        price = findViewById(R.id.currentPrice);
        status = findViewById(R.id.status);
        placeOrder = new AlertDialog.Builder(this).create();
        c = in.getStringExtra("companyName");
        p = in.getStringExtra("price");
        p = p.replace("INR ","");
        prc = Double.parseDouble(p);
        s = in.getStringExtra("status");
        Log.i("strings","\n"+in.getStringExtra("companyName")
                                +"\n"+in.getStringExtra("price")
                                +"\n"+in.getStringExtra("status"));
        companyName.setText(in.getStringExtra("companyName"));
        price.setText("INR "+in.getStringExtra("price"));

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
        //TODO:apply available stock information here
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogCompanyName.setText(c);
                dailogCompanyPrice.setText("INR "+p);
                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
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
                        Double nbr = Double.parseDouble(s.toString());
                        Double amt = nbr*prc;
                        String amount = amt.toString();
                        debitAmount.setText("INR "+amount);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                placeOrder.setView(buyView);
                placeOrder.show();
            }
        });


    }
    public static class BackgroundWorker extends AsyncTask<String, String, String> {
        Context context;
        AlertDialog alertDialog;
        private Constants constants;
        private String ip;



        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            constants = new Constants(context);
            ip = constants.getIp();
            String buy_url = "http://"+ip+"/pgr/buystock.php";
            String sell_url = "http://"+ip+"/pgr/sellstock.php";
            if (type.equals("buy")){
                try {
                    String email = strings[1];
                    String companyName = strings[2];
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
                            URLEncoder.encode("companyName","UTF-8")+"="+URLEncoder.encode(companyName,"UTF-8")+"&"+
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

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (type.equals("sell")){
                try {
                    String email = strings[1];
                    String companyName = strings[2];
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
                            URLEncoder.encode("companyName","UTF-8")+"="+URLEncoder.encode(companyName,"UTF-8")+"&"+
                            URLEncoder.encode("buyNumber","UTF-8")+"="+URLEncoder.encode(sellNumber,"UTF-8");
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
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Status");
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                boolean buySuccessful = s.contains("successful");
                boolean buyUnsuccessful = s.contains("unsuccessful");
                boolean sellSuccessful = s.contains("unsuccessful");
                boolean sellUnsuccessful = s.contains("unsuccessful");

                if (buySuccessful) {
                    alertDialog.setMessage("Buy Successful");
                    alertDialog.show();
                } else if (buyUnsuccessful) {
                    alertDialog.setMessage("Buy Unsuccessful");
                    alertDialog.show();
                } else if (sellSuccessful) {
                    alertDialog.setMessage("Sell Successful");
                    alertDialog.show();
                }else if (sellUnsuccessful) {
                    alertDialog.setMessage("Sell Unsuccessful");
                    alertDialog.show();
                }else {
                    alertDialog.setMessage("Unknown error!");
                    Log.i("error", s);
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.i("Exception","e");
                alertDialog.setMessage("Network Error!");
                alertDialog.show();
                Log.i("error", s);
            }
        }

        public BackgroundWorker(Context ctx) {
            context = ctx;
        }
    }

}
