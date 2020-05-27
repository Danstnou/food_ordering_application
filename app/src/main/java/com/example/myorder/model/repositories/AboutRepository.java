package com.example.myorder.model.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.myorder.model.dto.About;
import com.example.myorder.utils.ExecutorServiceInstance;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutorService;

public class AboutRepository {
    private FirebaseFirestore db;
    private CollectionReference collectionAbout;
    private String pathAbout = "about";
    private MutableLiveData<About> about;

    private ExecutorService executorService;

    private AboutRepository() {
        db = FirebaseFirestore.getInstance();
        executorService = ExecutorServiceInstance.getInstance();
        collectionAbout = db.collection(pathAbout);

        about = new MutableLiveData<>();
    }

    public static AboutRepository instance;

    public static AboutRepository getInstance() {
        if (instance == null) {
            instance = new AboutRepository();
        }
        return instance;
    }

    public void loadAbout() {
        executorService.execute(() -> collectionAbout
                .document("about")
                .get()
                .addOnCompleteListener(task -> {
                    try {
                        if (!task.isSuccessful())
                            return;

                        this.about.postValue(task.getResult().toObject(About.class));
                    } catch (Exception e) {
                    }
                }));
    }

    public MutableLiveData<About> getAbout() {
        loadAbout();
        return about;
    }
}
