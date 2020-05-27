package com.example.myorder.ViewModel.about;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myorder.model.dto.About;
import com.example.myorder.model.repositories.AboutRepository;

public class AboutViewModel extends ViewModel {
    AboutRepository aboutRepository;

    public AboutViewModel() {
        aboutRepository = AboutRepository.getInstance();
    }

    public LiveData<About> getAbout() {
        return aboutRepository.getAbout();
    }
}