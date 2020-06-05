package com.example.myorder.model.repositories;

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
    protected CollectionReference collection;

    private MutableLiveData<List<T>> productList;

    ExecutorService executorService;

    public ProductRepository(String collectionPath) {
        db = FirebaseFirestore.getInstance(); // БД Cloud Firestore
        collection = db.collection(collectionPath); // приходит путь к коллекции
        executorService = ExecutorServiceInstance.getInstance(); // пул потоков
    }

    /*
     * Подписка на список продуктов
     */

    public void observeProducts() {
        // создаём слушателя
        collection.addSnapshotListener((queryDocumentSnapshots, e) ->
                executorService.execute(() -> {
                    // проверка на отсутствие документов
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<T> productList = new ArrayList<>();

                        // пробуем певерести документ в объект
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            // пробуем певерести документ в объект
                            try {
                                productList.add(fromDocumentSnapshotToProduct(documentSnapshot));
                            } catch (Exception e1) {
                                // если какое-то поле документа(тип) не совпало с полем объекта - пропустим
                            }
                        }

                        java.util.Collections.shuffle(productList); // перемешиваем, для разнообразия вида
                        this.productList.postValue(productList); // добавляем в новом потоке(изменяем состояние для наблюдателя)
                    }
                })
        );
    }

    // переопределяемый метод, для различных типов продуктов
    protected abstract T fromDocumentSnapshotToProduct(DocumentSnapshot documentSnapshot);

    /*
     * Получение списка продуктов
     */

    public MutableLiveData<List<T>> getProductList() {
        if (productList == null) {
            productList = new MutableLiveData<>();
            observeProducts(); // сначала выполняем асинхронную операцию
        }
        return productList; // возвращаем liveData, на которую подпишется view
    }
}