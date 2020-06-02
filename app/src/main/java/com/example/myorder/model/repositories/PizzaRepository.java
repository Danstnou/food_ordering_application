package com.example.myorder.model.repositories;

import com.example.myorder.model.dto.Pizza;
import com.google.firebase.firestore.DocumentSnapshot;

public class PizzaRepository extends ProductRepository<Pizza> {
    private static String collectionPath = "pizza"; // путь к коллекции пиццы в БД

    public static PizzaRepository instance;

    public PizzaRepository(String collectionPath) {
        super(collectionPath);
    }

    public static PizzaRepository getInstance() {
        if (instance == null) {
            instance = new PizzaRepository(collectionPath); // репозиторий будет одиночкой
        }
        return instance;
    }

    @Override
    public Pizza fromDocumentSnapshotToProduct(DocumentSnapshot documentSnapshot) {
        Pizza pizza = documentSnapshot.toObject(Pizza.class); // переводим в наш тип (Пицца)
        pizza.idDocument = documentSnapshot.getId(); // ID документа из БД
        pizza.cost = pizza.cost25; // начальная стоимость у каждого продукта. у пиццы - минимальная
        pizza.category = collectionPath; // категория продукта для истории
        return pizza;
    }
}