package billyji.com.yahoostocks;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    final private Retrofit retrofit = new Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://query.yahooapis.com/v1/public/")
        .build();

    public YahooFinanceApiInterface create() {
        return retrofit.create(YahooFinanceApiInterface.class);
    }

}
