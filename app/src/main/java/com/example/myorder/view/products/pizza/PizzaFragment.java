package com.example.myorder.view.products.pizza;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.myorder.ViewModel.ICartActions;
import com.example.myorder.ViewModel.products.PizzaViewModel;
import com.example.myorder.model.dto.BaseProduct;
import com.example.myorder.model.dto.Pizza;
import com.example.myorder.view.products.base.BaseProductFragment;

import java.util.List;

public class PizzaFragment extends BaseProductFragment {

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(PizzaViewModel.class);
    }

    @Override
    protected void initAdapter() {
        adapter = new ProductAdapter<Pizza>(this, Glide.with(this), getActivity().getSupportFragmentManager()) {
            @Override
            protected void setBottomSheetProduct(ICartActions iCartActions) {
                bottomSheetDialogFragment = new BottomSheetPizza(requestManager, iCartActions);
            }

            @Override
            protected void setProductAndShowBottomSheet(int i) {
                ((BottomSheetPizza) bottomSheetDialogFragment).setPizza(productList.get(i));
                bottomSheetDialogFragment.show(fragmentManager, "Подробнее");
            }

            @Override
            protected BaseProduct getProductByPosition(int position) {
                return productList.get(position);
            }
        };


    }

    @Override
    protected void setLayoutManager() {
        binding.recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    protected void observeProductsList() {
        viewModel.getProductList().observe(getViewLifecycleOwner(), o -> adapter.setProductsList((List) o));
    }
}