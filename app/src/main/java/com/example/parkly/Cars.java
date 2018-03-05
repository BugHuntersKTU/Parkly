package com.example.parkly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.parkly.DataBase.LicensePlate;
import com.example.parkly.DataBase.LicensePlateDatabase;
import com.example.parkly.DataBase.LicensePlateRepository;
import com.example.parkly.DataBase.LocalUserDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Cars extends AppCompatActivity {

    Button btn_home;
    Button btn_add;
    EditText txt_plate;
    Button btn_confirm;

    private ListView lst_Car;

    //Adapter
    List<LicensePlate> licensePlateList = new ArrayList<>();
    ArrayAdapter<LicensePlate> adapter;

    //Database
    private CompositeDisposable compositeDisposable;
    public LicensePlateRepository licensePlateRepository;

    public void init(){
        btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cars.this, MainActivity.class));
            }
        });
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cars.this, addCar.class));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);

        //Init
        compositeDisposable = new CompositeDisposable();

        //init View


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, licensePlateList);
        lst_Car = findViewById(R.id.lst_Cars);
        registerForContextMenu(lst_Car);
        lst_Car.setAdapter(adapter);

        //Database
        LicensePlateDatabase licensePlateDatabase = LicensePlateDatabase.getInstance(this);
        licensePlateRepository = LicensePlateRepository.getInstance(LocalUserDataSource.getInstance(licensePlateDatabase.licensePlateDao()));

        loadData();

        init();


        //Event
        btn_confirm = findViewById(R.id.btn_confirm);
        txt_plate = findViewById(R.id.txt_plate);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> e) throws Exception {

                        String text = txt_plate.getText().toString();
                        if (text != null) {
                            LicensePlate licensePlate = new LicensePlate();
                            licensePlate.setNumber(text);
                            licensePlateRepository.insertAll(licensePlate);
                            e.onComplete();
                        }
                    }


                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<Object>() {
                                       @Override
                                       public void accept(Object o) throws Exception {
                                           Toast.makeText(Cars.this, "License Plate added !", Toast.LENGTH_SHORT).show();
                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(Throwable throwable) throws Exception {
                                           Toast.makeText(Cars.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                       }
                                   },
                                new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        loadData(); // Refresh data
                                    }
                                }

                        );

            }
        });

    }




    private void loadData()
    {
        Disposable disposable = licensePlateRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<LicensePlate>>() {
                    @Override
                    public void accept(List<LicensePlate> licensePlates) throws Exception {
                        onGetAllLicensePlateSuccess(licensePlates);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(Cars.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllLicensePlateSuccess(List<LicensePlate> licensePlates)
    {
        licensePlateList.clear();
        licensePlateList.addAll(licensePlates);
        adapter.notifyDataSetChanged();
    }

    public void AddLicensePlate(LicensePlate licensePlate)
    {
        licensePlateList.add(licensePlate);
        licensePlateRepository.insertAll(licensePlate);
    }
}
