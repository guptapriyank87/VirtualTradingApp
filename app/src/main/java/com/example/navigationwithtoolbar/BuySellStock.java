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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BuySellStock extends AppCompatActivity {

    TextView companyName,price,status,companyCode;
    String c,p,s,cd;
    AlertDialog placeBuyOrder,placeSellOrder;
    Button btnBuy,btnSell,btnWatch;
    Context ctx;

    //buy dialog declaration
    View buyView ;
    EditText buyNumber ;
    Button buyButton ;
    Button buyCancleButton;
    TextView debitAmount,BADCompanyCode ;
    Double prc;
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


        Intent in = getIntent();
        companyName = findViewById(R.id.companyName);
        companyCode = findViewById(R.id.BScompanyCode);
        btnBuy = findViewById(R.id.btnBSS_Buy);
        btnSell = findViewById(R.id.btnBSS_Sell);
        btnWatch = findViewById(R.id.btnBSS_Watch);
        price = findViewById(R.id.currentPrice);
        status = findViewById(R.id.status);
        placeBuyOrder = new AlertDialog.Builder(this).create();
        placeSellOrder = new AlertDialog.Builder(this).create();
        c = in.getStringExtra("companyName");
        p = in.getStringExtra("price");
        p = p.replace("INR ","");
        cd = in.getStringExtra("code");
        prc = Double.parseDouble(p);
        s = in.getStringExtra("status");
        Log.i("strings","\n"+in.getStringExtra("companyName")
                                +"\n"+in.getStringExtra("price")
                                +"\n"+in.getStringExtra("status"));
        companyName.setText(in.getStringExtra("companyName"));
        companyCode.setText(cd);
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
                        Double nbr = Double.parseDouble(s.toString());
                        Double amt = nbr*prc;
                        String amount = amt.toString();
                        debitAmount.setText("INR "+amount);
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
                        Double nbr = Double.parseDouble(s.toString());
                        Double amt = prc*nbr;
                        String amount = amt.toString();
                        sellAmount.setText("INR "+amount);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                sellCancleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        placeSellOrder.hide();
                    }
                });

                placeSellOrder.setView(sellView);
                placeSellOrder.show();
            }
        });

    }
    public static class BuyStock extends AsyncTask<String, String, String> {
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
                boolean buySuccessful = s.contains("buysuccessful");
                boolean buyUnsuccessful = s.contains("buyunsuccessful");
                boolean sellSuccessful = s.contains("sellunsuccessful");
                boolean sellUnsuccessful = s.contains("sellunsuccessful");

                if (buySuccessful) {
                    Log.i("status","insert successful!");
                    Toast.makeText(context, "Your order was successful!", Toast.LENGTH_SHORT).show();
                } else if (buyUnsuccessful) {
                    Toast.makeText(context, "Your order was unsuccessful!", Toast.LENGTH_SHORT).show();
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

        public BuyStock(Context ctx) {
            context = ctx;
        }
    }

}
