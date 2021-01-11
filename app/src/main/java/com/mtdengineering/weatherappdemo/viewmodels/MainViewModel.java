package com.mtdengineering.weatherappdemo.viewmodels;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mtdengineering.weatherappdemo.models.WeatherInfo;
import com.mtdengineering.weatherappdemo.repositories.interfaces.IWeatherRepository;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

public class MainViewModel  extends ViewModel
{
    IWeatherRepository iWeatherRepository;

    @Inject
    public MainViewModel(IWeatherRepository iWeatherRepository)
    {
        this.iWeatherRepository = iWeatherRepository;
    }

    private MutableLiveData<WeatherInfo> weatherInfoLiveData = new MutableLiveData<>();

    public LiveData<WeatherInfo> getWeatherInfo()
    {
        return weatherInfoLiveData;
    }

    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<String> getError()
    {
        return errorLiveData;
    }

    @SuppressLint("CheckResult")
    public void getWeatherInfo(String latitude, String longitude, String api_key)
    {
        iWeatherRepository.getWeatherInfo(latitude, longitude, api_key)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        (w) -> weatherInfoLiveData.postValue(w),
                        (t) -> errorLiveData.postValue(t.getMessage()),
                        () -> { }); // TODO
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();
    }
}
