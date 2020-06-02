package com.example.myorder.model.repositories;

import com.example.myorder.model.dto.Product;
import com.google.firebase.firestore.DocumentSnapshot;

public class RollsRepository extends ProductRepository<Product> {
    private static String collectionPath = "rolls";

    public static RollsRepository instance;

    public RollsRepository(String collectionPath) {
        super(collectionPath);
    }

    public static RollsRepository getInstance() {
        if (instance == null) {
            instance = new RollsRepository(collectionPath);
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