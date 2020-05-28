package com.example.myorder.model.repositories.baseProducts;

import androidx.lifecycle.MutableLiveData;

import com.example.myorder.utils.ExecutorServiceInstance;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public abstract class ProductRepository<T> {
    private FirebaseFirestore db;
    private CollectionReference collection;

    private MutableLiveData<List<T>> productList;

    ExecutorService executorService;

    public ProductRepository(String collectionPath) {
        db = FirebaseFirestore.getInstance();
        collection = db.collection(collectionPath);
        executorService = ExecutorServiceInstance.getInstance();
    }

    /*
     * Подписка на список продуктов
     */

    public void observeProducts() {
        executorService.execute(() -> collection.addSnapshotListener((queryDocumentSnapshots, e) -> {

            if (!queryDocumentSnapshots.isEmpty()) {
                List<T> productList = new ArrayList<>();

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments())
                    try {
                        productList.add(fromDocumentSnapshotToProduct(documentSnapshot));
                    } catch (Exception e1) {
                    }

                java.util.Collections.shuffle(productList);
                this.productList.postValue(productList);
            }
        }));
    }

    protected abstract T fromDocumentSnapshotToProduct(DocumentSnapshot documentSnapshot);

    /*
     * Получение списка продуктов
     */

    public MutableLiveData<List<T>> getProductList() {
        if (productList == null) {
            productList = new MutableLiveData<>();
            observeProducts();
        }
        return productList;
    }
}