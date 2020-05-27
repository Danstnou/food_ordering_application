package com.example.myorder.model.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myorder.model.dto.Courier;
import com.example.myorder.utils.ExecutorServiceInstance;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class CouriersRepository {
    private FirebaseFirestore db;
    private CollectionReference collection;

    private String collectionPath = "couriers";
    private ExecutorService executorService;

    private CouriersRepository() {
        db = FirebaseFirestore.getInstance();
        collection = db.collection(collectionPath);
        executorService = ExecutorServiceInstance.getInstance();
    }

    public static CouriersRepository instance;

    public static CouriersRepository getInstance() {
        if (instance == null) {
            instance = new CouriersRepository();
        }
        return instance;
    }

    /*
     * Получение списка курьеров
     */

    public LiveData<List<Courier>> getCouriersList() {
        MutableLiveData<List<Courier>> mutableLiveData = new MutableLiveData<>();

        executorService.execute(() -> collection.get().addOnCompleteListener(task -> {
            List<Courier> couriersList = null;
            if (task.isSuccessful()) {
                couriersList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult())
                    couriersList.add(document.toObject(Courier.class));
            }
            mutableLiveData.postValue(couriersList);
        }));

        return mutableLiveData;
    }
}