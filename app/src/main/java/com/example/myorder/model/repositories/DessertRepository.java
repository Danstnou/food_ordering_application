package com.example.myorder.model.repositories;

import com.example.myorder.model.dto.Product;
import com.google.firebase.firestore.DocumentSnapshot;

public class DessertRepository extends ProductRepository<Product> {
    private static String collectionPath = "dessert";

    public static DessertRepository instance;

    public DessertRepository(String collectionPath) {
        super(collectionPath);
    }

    public static DessertRepository getInstance() {
        if (instance == null) {
            instance = new DessertRepository(collectionPath);
        }
        return instance;
    }

    @Override
    public Product fromDocumentSnapshotToProduct(DocumentSnapshot documentSnapshot) {
        Product product = documentSnapshot.toObject(Product.class);
        product.idDocument = documentSnapshot.getId();
        product.category = collectionPath;
        return product;
    }
}