package com.example.parkly.DataBase;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Giedrius on 2018-03-05.
 */

public class LicensePlateRepository implements LicensePlateDao{

    private LicensePlateDao mLicensePlateDao;

    private static LicensePlateRepository mInstance;

    public LicensePlateRepository(LicensePlateDao mLicensePlateDao)
    {
        this.mLicensePlateDao = mLicensePlateDao;
    }

    public static LicensePlateRepository getInstance(LicensePlateDao mLicensePlateDao)
    {
        if (mInstance == null)
        {
            mInstance = new LicensePlateRepository(mLicensePlateDao);
        }
        return mInstance;
    }

    @Override
    public Flowable<List<LicensePlate>> getAll() {
        return mLicensePlateDao.getAll();
    }

    @Override
    public Flowable<LicensePlate> loadAllByIds(int[] licensePlateIds) {
        return mLicensePlateDao.loadAllByIds(licensePlateIds);
    }

    @Override
    public void insertAll(LicensePlate... numbers) {
        mLicensePlateDao.insertAll(numbers);
    }

    @Override
    public void delete(LicensePlate number) {
        mLicensePlateDao.delete(number);
    }

    @Override
    public void clear() {
        mLicensePlateDao.clear();
    }
}
