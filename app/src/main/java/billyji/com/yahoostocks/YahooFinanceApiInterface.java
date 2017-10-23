package billyji.com.yahoostocks;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YahooFinanceApiInterface {
    @GET("yql?format=json")
    Observable<StockResult> yqlQuery(
        @Query("q") String query,
        @Query("env") String env
    );
}
