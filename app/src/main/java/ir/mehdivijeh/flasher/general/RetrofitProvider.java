package ir.mehdivijeh.flasher.general;

import android.content.Context;

import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {
  public static Retrofit provideRetrofit(HttpUrl httpUrl, Context context){
    return new Retrofit.Builder()
      .baseUrl(httpUrl)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .client(ClientProvider.provideOkHttpClient(context))
      .build();
  }

}
