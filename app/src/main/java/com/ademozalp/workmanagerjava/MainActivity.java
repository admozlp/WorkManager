package com.ademozalp.workmanagerjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;

import android.os.Bundle;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data data = new Data.Builder().putInt("value", 1).build();// veri aktarımı

        Constraints constraints = new Constraints.Builder()// kurallarımız -> nasıl bir durum varsa çalış
               //.setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build();

       /* WorkRequest workRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)//istek yap -> yapılacak sınıf ne kuralları ekle veri aktarılacaksa ekle ve istek yap
                .setConstraints(constraints)
                .setInputData(data)
                .setInitialDelay(40, TimeUnit.SECONDS)
                .addTag("myTag")
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);//isteği yap -> work manager ile.*/


        WorkRequest workRequest = new PeriodicWorkRequest.Builder(RefreshDatabase.class, 15,TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(data)
                .build();

        WorkManager. getInstance(this).enqueue(workRequest);


        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.getId())// work ün durmunu alabiliyoruz ve duruma göre aksiyon alabiliyoruz
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo.getState() == WorkInfo.State.RUNNING)
                            System.out.println("Running");
                        else if(workInfo.getState() == WorkInfo.State.SUCCEEDED)
                            System.out.println("Succeeded");
                        else if(workInfo.getState() == WorkInfo.State.FAILED)
                            System.out.println("Failed");
                    }
                });


        //WorkManager.getInstance(this).cancelAllWork(); // arkada çalışan bütün işleri sona erdir -> kapat

        /*OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class).build();
        OneTimeWorkRequest oneTimeWorkRequest1 = new OneTimeWorkRequest.Builder(RefreshDatabase.class).build();

        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest)
                .then(oneTimeWorkRequest1)
                .then(oneTimeWorkRequest1)
                .enqueue();*/ // Peş peşe work çalıştırmak -> sadece onetime da çalışır
    }
}