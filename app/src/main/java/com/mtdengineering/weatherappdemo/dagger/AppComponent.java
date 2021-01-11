package com.mtdengineering.weatherappdemo.dagger;

import com.mtdengineering.weatherappdemo.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { NetworkModule.class,
                       ViewModelFactoryModule.class,
                       MainViewModelModule.class })
public interface AppComponent
{
    void injectActivity(MainActivity activity);
}
