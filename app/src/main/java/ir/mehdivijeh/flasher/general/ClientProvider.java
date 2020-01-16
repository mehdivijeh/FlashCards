package ir.mehdivijeh.flasher.general;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class ClientProvider {

    public static OkHttpClient provideOkHttpClient(Context context) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES);

        okHttpClient.addInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //show logs if app is in Debug mode
        okHttpClient.addNetworkInterceptor(loggingInterceptor);
        okHttpClient.addInterceptor(loggingInterceptor);
   /* if (BuildConfig.DEBUG) {
      okHttpClient.addNetworkInterceptor(loggingInterceptor);
      okHttpClient.addInterceptor(loggingInterceptor);
    }*/

        return okHttpClient.build();

    }

}
