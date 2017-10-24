package billyji.com.yahoostocks.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

import billyji.com.yahoostocks.R;
import billyji.com.yahoostocks.adapter.StockDataAdapter;
import billyji.com.yahoostocks.model.StockUpdate;
import billyji.com.yahoostocks.ui.MainActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class YahooApiRunner
{
    private final StringBuilder totalQuery = new StringBuilder();
    private final static String env = "store://datatables.org/alltableswithkeys";
    private final YahooApiInterface yahooService;
    private final Context context;
    private final StockDataAdapter stockDataAdapter;

    public YahooApiRunner(Context context, StockDataAdapter stockDataAdapter)
    {
        yahooService = new YahooApiClient().create();
        this.stockDataAdapter = stockDataAdapter;
        this.context = context;
    }

    public void addStocks(List<String> stocks)
    {
        StringBuilder stocksToAdd = new StringBuilder();
        for(String stock : stocks)
        {
            stocksToAdd.append("'");
            stocksToAdd.append(stock);
            stocksToAdd.append("',");
        }
        totalQuery.append(stocksToAdd.toString());
        stocksToAdd.setLength(Math.max(stocksToAdd.length() - 1, 0));
        stocksToAdd.append(")");

        queryYahoo(stocksToAdd.toString());
    }

    public void addStock(String stockSymbol)
    {
        String processedStockSymbol = processString(stockSymbol);
        if(!processedStockSymbol.equals(StockUpdate.INVALID_STOCK))
            queryYahoo(processedStockSymbol);
    }

    public void updateStocks()
    {
        if(totalQuery.length() == 0)
            return;

        totalQuery.setLength(Math.max(totalQuery.length() - 1, 0));
        totalQuery.append(")");
        queryYahoo(totalQuery.toString());
    }

    private String processString(String stockSymbol)
    {
        if(checkIfValidString(stockSymbol))
            return "'MA','" + stockSymbol + "')";

        ((MainActivity)context).showSnackbarMessage(context.getString(R.string.no_such_stock_symbol));
        return StockUpdate.INVALID_STOCK;
    }

    private boolean checkIfValidString(String stockSymbol)
    {
        return stockSymbol.matches("[a-zA-Z.]+");
    }

    private void queryYahoo(String stock)
    {
        if(!checkConnection())
            return;

        String query = "select * from yahoo.finance.quotes where symbol in (" + stock;

        yahooService.yqlQuery(query, env)
            .subscribeOn(Schedulers.io())
            .map(r -> r.getQuery().getResults().getQuote())
            .flatMap(Observable::fromIterable)
            .map(StockUpdate::create)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stockUpdate -> {
                if(stockUpdate.getStockSymbol().equals(StockUpdate.INVALID_STOCK))
                {
                    ((MainActivity)context).showSnackbarMessage(context.getString(R.string.no_such_stock_symbol));
                    return;
                }
                stockDataAdapter.add(stockUpdate);
            });
    }

    private boolean checkConnection()
    {
        ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork;

        if (cm != null)
        {
            activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

            if (!isConnected)
            {
                ((MainActivity)context).showSnackbarMessage(context.getString(R.string.no_network_connection));
            }
            return isConnected;
        }
        return false;
    }
}
