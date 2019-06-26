package com.example.navigationwithtoolbar.productModel;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationwithtoolbar.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private Context mCtx;
    private List<Product> productList;
    private OnCardListner onCardListner;


    public ProductAdapter(Context mCtx, List<Product> productList,OnCardListner onCardListner) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.onCardListner = onCardListner;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
       // View view = inflater.inflate(R.layout.list_layout,null);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_layout, viewGroup, false);
        ProductViewHolder holder = new ProductViewHolder(view,onCardListner);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        Product product = productList.get(i);
        productViewHolder.tCompanyName.setText(product.getCompanyName());
        productViewHolder.tPrice.setText(product.getPrice());
        productViewHolder.tStatus.setText(product.getStatus());
        if (product.getStatus().equals("Strong Buy")){
            productViewHolder.tStatus.setBackgroundResource(R.color.strongBuy);
        }else if (product.getStatus().equals("Buy")){
            productViewHolder.tStatus.setBackgroundResource(R.color.buy);
        }else if (product.getStatus().equals("Strong Sell")){
            productViewHolder.tStatus.setBackgroundResource(R.color.strongSell);
        }else if (product.getStatus().equals("Sell")){
            productViewHolder.tStatus.setBackgroundResource(R.color.sell);
        }else {
            productViewHolder.tStatus.setBackgroundResource(R.color.neutral);
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tCompanyName,tPrice,tStatus;
        OnCardListner onCardListner;
        public ProductViewHolder(@NonNull View itemView,OnCardListner onCardListner) {
            super(itemView);
            tCompanyName = itemView.findViewById(R.id.companyName);
            tPrice = itemView.findViewById(R.id.price);
            tStatus = itemView.findViewById(R.id.status);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT));
            itemView.setOnClickListener(this);
            this.onCardListner = onCardListner;
        }

        @Override
        public void onClick(View v) {
            onCardListner.onCardClick(getAdapterPosition());
        }
    }
    public interface OnCardListner{
            void onCardClick(int position);
    }
}
