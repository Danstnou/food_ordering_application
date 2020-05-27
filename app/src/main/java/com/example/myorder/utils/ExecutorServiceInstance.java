package com.example.myorder.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceInstance {
    private static int NUMBER_OF_THREADS = 4;
    private static ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ExecutorService getInstance() {
        return executorService;
    }
}