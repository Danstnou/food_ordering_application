package com.example.myorder.view.products.rolls;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.myorder.ViewModel.ICartActions;
import com.example.myorder.ViewModel.products.RollsViewModel;
import com.example.myorder.model.dto.BaseProduct;
import com.example.myorder.model.dto.Product;
import com.example.myorder.view.products.base.BaseProductFragment;
import com.example.myorder.view.products.base.BottomSheetProduct;

import java.util.List;

public class RollsFragment extends BaseProductFragment {

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(RollsViewModel.class);
    }

    @Override
    protected void initAdapter() {
        adapter = new ProductAdapter<Product>(this, Glide.with(this), getActivity().getSupportFragmentManager()) {
            @Override
            protected void setBottomSheetProduct(ICartActions iCartActions) {
                bottomSheetDialogFragment = new BottomSheetProduct(requestManager, iCartActions);
            }

            @Override
            protected void setProductAndShowBottomSheet(int i) {
                ((BottomSheetProduct) bottomSheetDialogFragment).setProduct(productList.get(i));
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