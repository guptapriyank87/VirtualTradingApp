package com.example.navigationwithtoolbar.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navigationwithtoolbar.R;
import com.example.navigationwithtoolbar.productModel.Product;
import com.example.navigationwithtoolbar.productModel.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    List<Product> productList;
    SwipeRefreshLayout swipeRefreshLayout;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    View v;


    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home,null);
        productList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.homeRecyclerView);
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout = v.findViewById(R.id.refreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetcIt();
            }
        });
        fetcIt();
        //return inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }
    public void fetcIt(){
        DataFetcher dataFetcher = new DataFetcher(getActivity());
        dataFetcher.execute("fetchdata");
    }
    public class DataFetcher extends AsyncTask<String,String,String> {
        Context context;
        String c,p,s;
        public List<Product> productList;
        String ip = "192.168.1.4";
        private Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            String fetchData_url = "http://"+ip+"/pgr/fetchdata.php";
            productList = new ArrayList<>();
            swipeRefreshLayout.setRefreshing(true);
            if (type.equals("fetchdata")){
                try {
                    URL url = new URL(fetchData_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
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
                    swipeRefreshLayout.setRefreshing(false);
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                } catch (IOException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
            swipeRefreshLayout.setRefreshing(false);
            return "error";
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String str) {
            Log.i("jason String",str);
            try {
                JSONArray jsonArr = new JSONArray(str);
                for (int i = 0; i < jsonArr.length(); i++)
                {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    c = jsonObj.getString("companyName");
                    p = jsonObj.getString("price");
                    s = jsonObj.getString("status");
                    productList.add(new Product(c,"INR "+p,s));
                }
                productAdapter =new ProductAdapter(getActivity(),productList);
                recyclerView.setAdapter(productAdapter);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        public DataFetcher(Context ctx) {
            context = ctx;
        }

    }
}