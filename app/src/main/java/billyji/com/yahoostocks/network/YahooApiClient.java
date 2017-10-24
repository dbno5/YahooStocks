package billyji.com.yahoostocks.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class YahooApiClient
{
    private final static String BASE_URL = "https://query.yahooapis.com/v1/public/";

    final private Retrofit retrofit = new Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build();

    YahooApiInterface create() {
        return retrofit.create(YahooApiInterface.class);
    }
}
