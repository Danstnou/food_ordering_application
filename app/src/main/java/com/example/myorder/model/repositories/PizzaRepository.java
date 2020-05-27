package com.example.myorder.model.repositories;

import com.example.myorder.model.dto.Pizza;
import com.example.myorder.model.repositories.baseProducts.ProductRepository;
import com.google.firebase.firestore.DocumentSnapshot;

public class PizzaRepository extends ProductRepository<Pizza> {
    private static String collectionPath = "pizza";

    public static PizzaRepository instance;

    public PizzaRepository(String collectionPath) {
        super(collectionPath);
    }

    public static PizzaRepository getInstance() {
        if (instance == null) {
            instance = new PizzaRepository(collectionPath);
        }
        return instance;
    }

    @Override
    public Pizza fromDocumentSnapshotToProduct(DocumentSnapshot documentSnapshot) {
        Pizza pizza = documentSnapshot.toObject(Pizza.class);
        pizza.idDocument = documentSnapshot.getId();
        pizza.cost = pizza.cost25;
        pizza.category = collectionPath;
        return pizza;
    }
}