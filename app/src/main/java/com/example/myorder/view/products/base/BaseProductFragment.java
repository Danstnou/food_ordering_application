package com.example.myorder.view.products.base;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.myorder.R;
import com.example.myorder.ViewModel.ICartActions;
import com.example.myorder.ViewModel.products.BaseProductViewModel;
import com.example.myorder.databinding.RecyclerviewFragmentBinding;
import com.example.myorder.model.dto.BaseProduct;
import com.example.myorder.model.entities.ProductCart;
import com.example.myorder.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public abstract class BaseProductFragment extends Fragment implements ICartActions {
    protected BaseProductViewModel viewModel;
    protected RecyclerviewFragmentBinding binding;
    protected ProductAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = RecyclerviewFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViewModel();
        initRecyclerViewAndAdapter();
        observeProductsList();
    }

    abstract protected void setViewModel();

    abstract protected void initAdapter();

    protected void initRecyclerViewAndAdapter() {
        initAdapter();
        setLayoutManager();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(Constants.itemDecoration);
    }

    protected abstract void setLayoutManager();

    protected abstract void observeProductsList();

    /*
     * Реакции UI на действия пользователя с корзиной (вызываются из Адаптера)
     * В заказе пиццы, можно её только добавить
     */

    @Override
    public void increaseProduct(ProductCart productCart) {
        viewModel.increaseProduct(productCart);
    }

    @Override
    public void decreaseProduct(ProductCart productCart) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*
     * Адаптер
     */

    public static abstract class ProductAdapter<T> extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        protected List<T> productList;
        protected BottomSheetDialogFragment bottomSheetDialogFragment;

        protected RequestManager requestManager;
        protected FragmentManager fragmentManager;

        public ProductAdapter(ICartActions iCartActions, RequestManager requestManager, FragmentManager fragmentManager) {
            this.requestManager = requestManager;
            this.fragmentManager = fragmentManager;
            setBottomSheetProduct(iCartActions);
        }

        protected abstract void setBottomSheetProduct(ICartActions iCartActions);

        public void setProductsList(List<T> productList) {
            this.productList = productList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
            final ViewHolder viewHolder = new ProductAdapter.ViewHolder(view);

            viewHolder.buttonDetails.setOnClickListener(v -> {
                if (bottomSheetDialogFragment.isAdded())
                    return;

                buttonDetailsClick((int) v.getTag());
            });
            return viewHolder;
        }

        abstract protected void buttonDetailsClick(int i);

        abstract protected BaseProduct getProductByPosition(int position);

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            BaseProduct product = getProductByPosition(position);

            holder.name.setText(product.name);
            holder.cost.setText("от " + product.cost + "р.");
            holder.ingredients.setText(product.ingredients);
            requestManager.load(product.logo).into(holder.logo);

            holder.buttonDetails.setTag(position); // помечаем, какую номером пиццу мы добавим
        }

        @Override
        public int getItemCount() {
            return productList == null ? 0 : productList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView name,
                    cost,
                    ingredients;
            Button buttonDetails;
            ImageView logo;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.textView_name);
                cost = itemView.findViewById(R.id.textView_cost);
                ingredients = itemView.findViewById(R.id.textView_ingridients);
                buttonDetails = itemView.findViewById(R.id.button_details);
                logo = itemView.findViewById(R.id.imageView_logo);
            }
        }
    }
}