package billyji.com.yahoostocks;

import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.stock_updates_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.stock_to_add)
    EditText stockToAdd;
    @BindView(R.id.generate_defaults)
    Button defaultsButton;

    private LinearLayoutManager layoutManager;
    private SearchView searchView;

    private StockDataAdapter stockDataAdapter;
    private YahooFinanceApiInterface yahooService;
    private boolean hideButton;
    private final static String env = "store://datatables.org/alltableswithkeys";
    private final List<String> defaultStocks = new ArrayList<>(Arrays.asList( "UNH", "MA", "AMZ", "AMZN", "ADP", "BBVA" ));
    private final StringBuilder totalQuery = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewClickListener listener = (view, position) -> {
            Toast.makeText(this, "Position " + position, Toast.LENGTH_SHORT).show();
        };

        stockDataAdapter = new StockDataAdapter(listener);
        recyclerView.setAdapter(stockDataAdapter);

        yahooService = new ApiClient().create();

        stockToAdd.setOnEditorActionListener(
            new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        hideKeyboard();
                        if(stockToAdd.getText().toString().equals(""))
                            return true;

                        addStock(stockToAdd.getText().toString());
                        return true;
                    }
                    return false;
                }
            });
    }

    private boolean checkConnection()
    {
        ConnectivityManager cm =
            (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork;

        if (cm != null)
        {
            activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

            if (!isConnected)
            {
                Snackbar
                    .make(recyclerView, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                    .show();
            }
            hideButton = isConnected;
            return isConnected;
        }
        return false;
    }

    private void hideKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

            if(imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
//        searchView.setOnQueryTextListener(new OnQueryTextListener()
//        {
//            @Override
//            public boolean onQueryTextSubmit(String query)
//            {
//                String requestName = searchView.getQuery().toString().trim();
//                Log.e("tits", requestName);
//                stockDataAdapter.search(requestName);
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query)
//            {
//                return false;
//            }
//        });
    }

    private void addStocks(List<String> stocks)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_options, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
            (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView =
            (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(getComponentName()));

        return true;
    }


    @OnClick(R.id.generate_defaults)
    public void generateDefaults()
    {
        addStocks(defaultStocks);
        if(hideButton)
        {
            defaultsButton.setVisibility(View.GONE);
            stockToAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_refresh:
                updateStocks();
                break;
            case android.R.id.home:
                finish();
            default:
                break;
        }

        return true;
    }

    private void addStock(String stockSymbol)
    {
        String processedStockSymbol = processString(stockSymbol);
        if(!processedStockSymbol.equals("NO"))
            queryYahoo(processedStockSymbol);
    }

    private String processString(String stockSymbol)
    {
        if(checkIfValidString(stockSymbol))
            return "'MA','" + stockSymbol + "')";

        showCouldNotFindStockSymbolMessage();
        return "NO";
    }

    private boolean checkIfValidString(String stockSymbol)
    {
        return stockSymbol.matches("[a-zA-Z.]+");
    }

    private void showCouldNotFindStockSymbolMessage()
    {
        Snackbar
            .make(recyclerView, "Couldn't find this stock symbol", Snackbar.LENGTH_LONG)
            .show();
    }

    private void queryYahoo(String stock)
    {
        if(!checkConnection())
            return;

        String query = "select * from yahoo.finance.quotes where symbol in (" + stock;

        //here need to only call once with totalquery string, need to
        //stop previous call as well
//        Observable.interval(0, 5, TimeUnit.SECONDS)
//            .flatMap(i ->
            yahooService.yqlQuery(query, env)
            .subscribeOn(Schedulers.io())
            .map(r -> r.getQuery().getResults().getQuote())
            .flatMap(Observable::fromIterable)
            .map(StockUpdate::create)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stockUpdate -> {
                if(stockUpdate.getStockSymbol().equals("INVALID"))
                {
                    showCouldNotFindStockSymbolMessage();
                    return;
                }
                Log.e("hey", "hey");
                stockDataAdapter.add(stockUpdate);
            });
    }

    private void updateStocks()
    {
        if(totalQuery.length() == 0)
            return;

        totalQuery.setLength(Math.max(totalQuery.length() - 1, 0));
        totalQuery.append(")");
        queryYahoo(totalQuery.toString());
    }

}