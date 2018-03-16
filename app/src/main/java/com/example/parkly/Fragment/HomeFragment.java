package com.example.parkly.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parkly.Activity.addCarActivity;
import com.example.parkly.DataBase.Tables.LicensePlate;
import com.example.parkly.DataBase.LicensePlateDatabase;
import com.example.parkly.DataBase.LicensePlateRepository;
import com.example.parkly.DataBase.LocalUserDataSource;
import com.example.parkly.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by donvel on 2018-03-12.
 */

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkCarRegistration();
    }

    //Color - pasirinkta spalva, chosenHour - pasirinktos valandos[gali buti ir 0], chosenMinute - pasirinktos minutes[gali buti ir 0]
    private void estimatedPriceAndTime(String Color, int chosenHour, int chosenMinute)
    {
        //minutine kaine
        double price;
        //galutine
        double total;
        //numatoma parkavimosi pabaiga
        Calendar currentTime = Calendar.getInstance();
        //nustatomas esamas laikas
        currentTime.setTime(new Date());
        currentTime.add(Calendar.HOUR_OF_DAY, chosenHour);
        currentTime.add(Calendar.MINUTE, chosenMinute);
        currentTime.getTime();

        switch(Color)
        {
            case "Oranzine":
            {
                price = 2 / 60;
                total = ((chosenHour*60) + chosenMinute) * price;
            }
            case "Geltona":
            {
                price = 2 / 60;
                total = ((chosenHour*60) + chosenMinute) * price;
            }
            case "Melyna":
            {
                price = 0.6 / 60;
                total = ((chosenHour*60) + chosenMinute) * price;
            }
            case "Raudona":
            {
                price = 1.2 / 60;
                total = ((chosenHour*60) + chosenMinute) * price;
            }
            case "Zalia":
            {
                price = 0.3 / 60;
                total = ((chosenHour*60) + chosenMinute) * price;
            }

        }
    }
    //Database
    public LicensePlateRepository licensePlateRepository;

    private void checkCarRegistration() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        LicensePlateDatabase licensePlateDatabase = LicensePlateDatabase.getInstance(getActivity());
        licensePlateRepository = LicensePlateRepository.getInstance(LocalUserDataSource.getInstance(licensePlateDatabase.licensePlateDao()));
        final HomeFragment homeFragment = (HomeFragment) getFragmentManager().findFragmentByTag("HOME_FRAGMENT");

        final Disposable disposable = licensePlateRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<LicensePlate>>() {
                    @Override
                    public void accept(List<LicensePlate> licensePlates) throws Exception {
                        if (licensePlates.isEmpty() && homeFragment != null && homeFragment.isVisible())
                            startActivity(new Intent(getActivity(), addCarActivity.class));
                    }
                });
        compositeDisposable.add(disposable);
    }
}
