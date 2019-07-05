package com.example.navigationwithtoolbar.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.navigationwithtoolbar.BuySellStock;
import com.example.navigationwithtoolbar.Constants;
import com.example.navigationwithtoolbar.R;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View v;
    private Constants constants;
    private String ip;
    private ImageView error;
    private Toolbar toolbar;
    private MenuItem mSpinnerItem1 = null;
    String r="";

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home,null);
        constants = new Constants(getActivity());
        toolbar = v.findViewById(R.id.toolbar);
        ip = constants.getIp();
        error = v.findViewById(R.id.networkerror);
        productList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.homeRecyclerView);
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout = v.findViewById(R.id.refreshLayout);


        //-----------------------toolbar stuff------------------------

        toolbar.setTitle("Nifty500");

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.share){
                    Toast.makeText(getContext(), "pop", Toast.LENGTH_SHORT).show();
                }else if(id == R.id.search){
                    Toast.makeText(getContext(), "pop", Toast.LENGTH_SHORT).show();
                }else if(id == R.id.about){
                    Toast.makeText(getContext(), "pop", Toast.LENGTH_SHORT).show();
                }else if(id == R.id.exit){
                    Toast.makeText(getContext(), "pop", Toast.LENGTH_SHORT).show();
                }else if(id == R.id.filter){
                    Spinner s = (Spinner) item.getActionView();
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.filter,android.R.layout.simple_spinner_dropdown_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s.setAdapter(adapter);


                    s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String text = parent.getItemAtPosition(position).toString();
                            fetcIt(text);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
                return false;
            }
        });
        //-----------X---------------toolbar----------------X------------------------------





        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetcIt(r);
            }
        });
        fetcIt("All");

        //return inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }
//-------------------------------Toolbar operation-----------------------------------------
    /*
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.share){
            Toast.makeText(getActivity(), "share selected", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.search){
            Toast.makeText(getActivity(), "search selected", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.about) {
            Toast.makeText(getActivity(), "about selected", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    */
//------------------------------------------------------------------------------------------
    public void fetcIt(String s){
        DataFetcher dataFetcher = new DataFetcher(getActivity());
        dataFetcher.execute("fetchdata",s);
    }




    public class DataFetcher extends AsyncTask<String,String,String> {
        Context context;
        String c,p,s,cd;
        public List<Product> productList;
        String filter;
        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            filter = strings[1];
            System.out.println("filter is:"+filter);
            String fetchData_url = "http://"+ip+"/pgr/fetchdata.php";
            productList = new ArrayList<>();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            if (type.equals("fetchdata")){
                try {
                    URL url = new URL(fetchData_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.i("status","Http url connection established properly");
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    Log.i("status","buffer writer working");
                    String post_data = URLEncoder.encode("filter","UTF-8")+"="+URLEncoder.encode(filter,"UTF-8");
                    Log.i("postData",post_data);
                    bufferedWriter.write(post_data);
                    Log.i("status","bufferedWriter.write(post_data) executed successfully");
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
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
                    cd = jsonObj.getString("code");
                    productList.add(new Product(c,"INR "+p,s,cd));
                }
                System.out.println(productList);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProductAdapter.OnCardListner onCardListner = new ProductAdapter.OnCardListner() {
                            @Override
                            public void onCardClick(int position) {
                                Log.i("Card Selected",productList.get(position).getCompanyName());
                                Intent in = new Intent(getActivity(),BuySellStock.class);
                                in.putExtra("companyName",productList.get(position).getCompanyName());
                                in.putExtra("price",productList.get(position).getPrice());
                                in.putExtra("status",productList.get(position).getStatus());
                                in.putExtra("code",productList.get(position).getCode());
                                startActivity(in);
                            }
                        };
                        productAdapter =new ProductAdapter(getActivity(),productList,onCardListner);
                        recyclerView.setAdapter(productAdapter);
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        public DataFetcher(Context ctx) {
            context = ctx;
        }

    }
}
