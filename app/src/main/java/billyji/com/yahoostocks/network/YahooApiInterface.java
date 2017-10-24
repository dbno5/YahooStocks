package billyji.com.yahoostocks.network;

import billyji.com.yahoostocks.model.StockResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YahooApiInterface
{
    @GET("yql?format=json")
    Observable<StockResult> yqlQuery(
        @Query("q") String query,
        @Query("env") String env
    );
}
