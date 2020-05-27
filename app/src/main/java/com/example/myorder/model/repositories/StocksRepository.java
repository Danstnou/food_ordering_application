package com.example.myorder.model.repositories;

import com.example.myorder.model.dto.Stock;
import com.example.myorder.model.repositories.baseProducts.ProductRepository;
import com.google.firebase.firestore.DocumentSnapshot;

public class StocksRepository extends ProductRepository<Stock> {
    private static String collectionPath = "stocks";

    public static StocksRepository instance;

    public StocksRepository(String collectionPath) {
        super(collectionPath);
    }

    public static StocksRepository getInstance() {
        if (instance == null) {
            instance = new StocksRepository(collectionPath);
        }
        return instance;
    }

    @Override
    public Stock fromDocumentSnapshotToProduct(DocumentSnapshot documentSnapshot) {
        return documentSnapshot.toObject(Stock.class);
    }
}