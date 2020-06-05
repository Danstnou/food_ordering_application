package com.example.myorder.view.orders.courierChoice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myorder.R;
import com.example.myorder.ViewModel.moderator.ModeratorViewModel;
import com.example.myorder.ViewModel.orders.SharedConfirmOrderViewModel;
import com.example.myorder.databinding.RecyclerviewFragmentBinding;
import com.example.myorder.model.dto.Courier;
import com.example.myorder.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CourierChoice extends Fragment {
    private RecyclerviewFragmentBinding binding;
    private ModeratorViewModel viewModel;
    private SharedConfirmOrderViewModel sharedOrderViewModel;
    private CouriersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RecyclerviewFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedOrderViewModel = new ViewModelProvider(getActivity()).get(SharedConfirmOrderViewModel.class);
        viewModel = new ViewModelProvider(getActivity()).get(ModeratorViewModel.class);

        initRecyclerViewAndAdapter();
        listenerCourierList();
    }

    private void initRecyclerViewAndAdapter() {
        adapter = new CouriersAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(Constants.itemDecoration);
    }

    private void listenerCourierList() {
        viewModel.getCouriersList().observe(getViewLifecycleOwner(), courierDtoList ->
                adapter.setCouriersList(courierDtoList));
    }

    /*
     * Адаптер
     */

    class CouriersAdapter extends RecyclerView.Adapter<CouriersAdapter.ViewHolder> {
        private List<Courier> couriersList;

        private void setCouriersList(List<Courier> couriersList) {
            this.couriersList = couriersList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courier_item, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.buttonSelect.setOnClickListener(v -> {
                Courier courier = couriersList.get((Integer) v.getTag());
                sharedOrderViewModel.setCourier(courier); // может и не обновиться, так как другой модератор мог удалить заказ
                Snackbar.make(getView(), "Курьер " + courier.name + " назначен", Snackbar.LENGTH_SHORT).show();
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
            });

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Courier courier = couriersList.get(position);
            holder.textViewName.setText(courier.name);
            holder.buttonSelect.setTag(position);
        }

        @Override
        public int getItemCount() {
            return couriersList == null ? 0 : couriersList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewName;
            Button buttonSelect;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewName = itemView.findViewById(R.id.textView_name);
                buttonSelect = itemView.findViewById(R.id.button_select);
            }
        }
    }
}