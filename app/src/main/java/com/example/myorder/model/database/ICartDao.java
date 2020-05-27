package com.example.myorder.model.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myorder.model.entities.ProductCart;

import java.util.List;

@Dao
public interface ICartDao {
    @Query("select * from products")
    LiveData<List<ProductCart>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ProductCart product);

    @Update
    void update(ProductCart product);

    @Delete
    void delete(ProductCart product);

    @Query("select * from products where idProduct = :idProduct and size = :size")
    LiveData<ProductCart> findProduct(String idProduct, String size);

    @Query("select sum(total_cost) from products")
    LiveData<Integer> getTotalCostCart();
}