package com.ademozalp.workmanagerjava;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class RefreshDatabase extends Worker {
    Context myContext;
    public RefreshDatabase(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.myContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        int value = data.getInt("value", 0);
        refreshDatabase(value);
        return Result.success();
    }

    private void refreshDatabase(int value){
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("com.ademozalp.workmanagerjava", Context.MODE_PRIVATE);
        int myNumber = sharedPreferences.getInt("myNumber", 0);
        myNumber +=  value;
        System.out.println(myNumber);
        sharedPreferences.edit().putInt("myNumber", myNumber).apply();
    }
}
