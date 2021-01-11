package com.mtdengineering.weatherappdemo.dagger;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mtdengineering.weatherappdemo.repositories.interfaces.IWeatherRepository;
import com.mtdengineering.weatherappdemo.viewmodels.MainViewModel;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mtdengineering.weatherappdemo.utils.Constant.API_BASE_URL;

@Module
class ContextModule
{
    Context context;

    public ContextModule(Context context)
    {
        this.context = context;
    }

    @Provides
    public Context provideContext()
    {
        return context;
    }
}

@Module
class NetworkModule
{
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());

    @Singleton
    @Provides
    public Retrofit provideRetrofit()
    {
        return builder.build();
    }

    @Singleton
    @Provides
    public IWeatherRepository provideWeatherRetrofitService()
    {
        return builder
                .build()
                .create(IWeatherRepository.class);
    }
}

@Module
abstract class ViewModelFactoryModule
{
    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory modelProviderFactory);
}

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindsMainViewModel(MainViewModel viewModel);
}