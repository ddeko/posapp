package mamabe.posappandroid.APIs;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {
    public static String BASE_URL = "http://192.168.43.53/posapp_api/index.php/Api/Posapp/";
    public static String BASE_URL2 = "http://mamabepos.esy.es/posapp_api/index.php/Api/Posapp/10.5.50.105";

    private ServiceGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        //okHttpClient.networkInterceptors().add(new StethoInterceptor());

        Retrofit builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return builder.create(serviceClass);
    }
}