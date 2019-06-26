package com.example.navigationwithtoolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class BuySellStock extends AppCompatActivity {

    TextView companyName,price,status;
    String c,p,s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell_stock);
        Intent in = getIntent();
        companyName = findViewById(R.id.companyName);
        price = findViewById(R.id.currentPrice);
        status = findViewById(R.id.status);
        s = in.getStringExtra("status");
        Log.i("strings","\n"+in.getStringExtra("companyName")
                                +"\n"+in.getStringExtra("price")
                                +"\n"+in.getStringExtra("status"));
        companyName.setText(in.getStringExtra("companyName"));
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

    }

}
