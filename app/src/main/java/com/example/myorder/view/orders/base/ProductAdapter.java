package com.example.myorder.view.orders.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.myorder.R;
import com.example.myorder.model.entities.ProductCart;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<ProductCart> listProducts;
    private RequestManager requestManager;

    public ProductAdapter(List<ProductCart> listProducts, RequestManager requestManager) {
        this.listProducts = listProducts;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductCart productCart = listProducts.get(position);

        requestManager.load(productCart.logo).into(holder.logo);
        holder.name.setText(productCart.name);
        holder.cost.setText(productCart.cost_one + "р.");
        holder.totalCost.setText(productCart.total_cost + "р.");
        holder.count.setText(productCart.count + "шт");
    }

    @Override
    public int getItemCount() {
        return listProducts == null ? 0 : listProducts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView name;
        TextView cost;
        TextView totalCost;
        TextView count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            logo = itemView.findViewById(R.id.logo);
            name = itemView.findViewById(R.id.textView_name);
            cost = itemView.findViewById(R.id.textView_cost);
            totalCost = itemView.findViewById(R.id.textView_total_cost);
            count = itemView.findViewById(R.id.textView_count);
        }
    }
}