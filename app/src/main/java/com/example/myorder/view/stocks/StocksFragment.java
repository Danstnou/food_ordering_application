package com.example.myorder.view.stocks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.myorder.R;
import com.example.myorder.ViewModel.stocks.StocksViewModel;
import com.example.myorder.databinding.RecyclerviewFragmentBinding;
import com.example.myorder.model.dto.Stock;
import com.example.myorder.utils.Constants;

import java.util.List;

public class StocksFragment extends Fragment {
    protected StocksViewModel viewModel;
    protected RecyclerviewFragmentBinding binding;
    protected StockAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = RecyclerviewFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(StocksViewModel.class);
        initRecyclerViewAndAdapter();
        observeProductsList();
    }

    protected void initRecyclerViewAndAdapter() {
        adapter = new StockAdapter(Glide.with(this), getActivity().getSupportFragmentManager());
        binding.recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(Constants.itemDecoration);
    }

    protected void observeProductsList() {
        viewModel.getProductList().observe(getViewLifecycleOwner(), stocks -> adapter.setStockList(stocks));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*
     * Адаптер
     */

    public static class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
        private List<Stock> stockList;
        private BottomSheetStocks bottomSheetStocks;

        private RequestManager requestManager;
        private FragmentManager fragmentManager;

        public StockAdapter(RequestManager requestManager, FragmentManager fragmentManager) {
            this.requestManager = requestManager;
            this.fragmentManager = fragmentManager;
            bottomSheetStocks = new BottomSheetStocks(requestManager);
        }

        public void setStockList(List<Stock> stockList) {
            this.stockList = stockList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.button.setOnClickListener(v -> {
                if (bottomSheetStocks.isAdded())
                    return;

                int i = (int) v.getTag();
                bottomSheetStocks.setStock(stockList.get(i));
                bottomSheetStocks.show(fragmentManager, "Подробнее");
            });

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Stock stock = stockList.get(position);
            holder.name.setText(stock.name);
            requestManager.load(stock.logo).into(holder.logo);
            holder.button.setTag(position); // помечаем, какую номером пиццу мы добавим
        }

        @Override
        public int getItemCount() {
            return stockList == null ? 0 : stockList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            ImageView logo;
            Button button;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.textView_name);
                logo = itemView.findViewById(R.id.imageView_logo);
                button = itemView.findViewById(R.id.button_details);
            }
        }
    }
}