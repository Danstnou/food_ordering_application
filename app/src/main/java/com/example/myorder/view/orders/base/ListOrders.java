package com.example.myorder.view.orders.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myorder.R;
import com.example.myorder.databinding.RecyclerviewFragmentBinding;
import com.example.myorder.model.dto.Order;
import com.example.myorder.utils.Constants;

import java.util.List;

public abstract class ListOrders extends Fragment {
    protected RecyclerviewFragmentBinding binding;
    protected OrdersAdapter adapter;
    protected NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RecyclerviewFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        initRecyclerViewAndAdapter();
        observeOrderList();
    }

    protected void initRecyclerViewAndAdapter() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setupAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(Constants.itemDecoration);
    }

    protected abstract void observeOrderList();

    protected abstract void setupAdapter();

    /*
     * Адаптер
     */

    public abstract static class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
        private List<Order> orderList;

        public void setOrderList(List<Order> orderList) {
            this.orderList = orderList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Order order = orderList.get(position);

            holder.textViewAddress.setText(order.user.getAddress());
            holder.textViewCost.setText(order.total_cost + "р.");
            setOptionalTextView(holder.textViewOptional, order);
            clickListenerButtonDetails(holder.buttonDetails, order);
        }

        protected abstract void clickListenerButtonDetails(Button button, Order order);

        protected abstract void setOptionalTextView(TextView textViewOptional, Order order);

        @Override
        public int getItemCount() {
            return orderList == null ? 0 : orderList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewAddress;
            public TextView textViewOptional;
            private TextView textViewCost;
            public Button buttonDetails;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewAddress = itemView.findViewById(R.id.textView_address);
                textViewOptional = itemView.findViewById(R.id.textView_optional);
                textViewCost = itemView.findViewById(R.id.textView_cost);
                buttonDetails = itemView.findViewById(R.id.button_details);
            }
        }
    }
}