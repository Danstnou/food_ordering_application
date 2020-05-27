package com.example.myorder.view.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.myorder.R;
import com.example.myorder.ViewModel.ICartActions;
import com.example.myorder.ViewModel.cart.CartViewModel;
import com.example.myorder.databinding.CartFragmentBinding;
import com.example.myorder.model.entities.ProductCart;
import com.example.myorder.utils.Constants;

import java.util.List;

public class CartFragment extends Fragment implements ICartActions {
    private CartFragmentBinding binding;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    private MenuItem totalCost;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CartFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cartViewModel = new ViewModelProvider(getActivity()).get(CartViewModel.class);
        initRecyclerViewAndAdapter();
        observeProducts();

        setHasOptionsMenu(true);
        clickListenerOrder();
    }

    private void initRecyclerViewAndAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(this, Glide.with(this));
        binding.recyclerView.setAdapter(cartAdapter);
        binding.recyclerView.addItemDecoration(Constants.itemDecoration);
    }

    private void observeProducts() {
        cartViewModel.getAll().observe(getViewLifecycleOwner(), productCartList ->
                cartAdapter.refreshProductsCart(productCartList));
    }

    private void clickListenerOrder() {
        binding.buttonOrder.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_nav_basket_to_nav_order);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cart_menu, menu);
        totalCost = menu.findItem(R.id.cart_total_cost);
        observeTotalCost(); // т.к. сумма показывается в баре, подписываться нужно когда он готов
    }

    private void observeTotalCost() {
        cartViewModel.getTotalCost().observe(getViewLifecycleOwner(), totalCost ->
                this.totalCost.setTitle("Итого: " + (totalCost == null ? 0 : totalCost) + "р."));
    }

    /*
     * Реализация интерфейса: ICartActions
     * Эти функции вызываются из адаптера
     * когда пользователь нажимает на соотв.
     * кнопки на экране.
     */

    @Override
    public void increaseProduct(ProductCart productCart) {
        cartViewModel.increaseProduct(productCart);
    }

    @Override
    public void decreaseProduct(ProductCart productCart) {
        cartViewModel.decreaseProduct(productCart);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*
     * Адаптер
     */

    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
        private List<ProductCart> listProductsCart;
        private ICartActions iCartActions;
        private RequestManager requestManager;

        public CartAdapter(ICartActions iCartActions, RequestManager requestManager) {
            this.iCartActions = iCartActions; // реакция на действия с корзиной
            this.requestManager = requestManager;
        }

        public void refreshProductsCart(List<ProductCart> listProductsCart) {
            this.listProductsCart = listProductsCart;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
            final ViewHolder productCartViewHolder = new ViewHolder(view);

            productCartViewHolder.buttonAdd.setOnClickListener(v -> {
                int number = (int) v.getTag(); // номер продукта в списке (в нём сохранён id)
                iCartActions.increaseProduct(listProductsCart.get(number));
            });

            productCartViewHolder.buttonRemove.setOnClickListener(v -> {
                int number = (int) v.getTag();
                iCartActions.decreaseProduct(listProductsCart.get(number));
            });

            return productCartViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ProductCart product = listProductsCart.get(position);

            requestManager.load(product.logo).into(holder.logo);
            holder.name.setText(product.name + ", " + product.size);
            holder.cost.setText(product.cost_one + "р.");
            holder.totalCost.setText(product.total_cost + "р.");
            holder.count.setText(product.count + "шт.");
            holder.buttonAdd.setTag(position);
            holder.buttonRemove.setTag(position);
        }

        @Override
        public int getItemCount() {
            return listProductsCart == null ? 0 : listProductsCart.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView logo;
            TextView name;
            TextView cost;
            TextView totalCost;
            TextView count;
            Button buttonAdd;
            Button buttonRemove;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                logo = itemView.findViewById(R.id.logo);
                name = itemView.findViewById(R.id.textView_name);
                cost = itemView.findViewById(R.id.textView_cost);
                totalCost = itemView.findViewById(R.id.textView_total_cost);
                count = itemView.findViewById(R.id.textView_count);
                buttonAdd = itemView.findViewById(R.id.button_add);
                buttonRemove = itemView.findViewById(R.id.button_remove);
            }
        }
    }
}