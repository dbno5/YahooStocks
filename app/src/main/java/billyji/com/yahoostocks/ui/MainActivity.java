package billyji.com.yahoostocks.ui;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import billyji.com.yahoostocks.R;
import billyji.com.yahoostocks.adapter.StockDataAdapter;
import billyji.com.yahoostocks.network.YahooApiRunner;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.stock_updates_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.stock_to_add)
    EditText stockToAdd;

    private StockDataAdapter stockDataAdapter;
    private YahooApiRunner yahooApiRunner;
    private final List<String> defaultStocks = new ArrayList<>(Arrays.asList( "UNH", "MA", "AMZN", "ADP", "BBVA" ));

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        stockDataAdapter = new StockDataAdapter();
        recyclerView.setAdapter(stockDataAdapter);

        yahooApiRunner = new YahooApiRunner(this, stockDataAdapter);
        generateDefaultStocks();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        stockToAdd.setOnEditorActionListener(
            (v, actionId, event) ->
            {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard();
                    if(stockToAdd.getText().toString().equals(""))
                        return true;

                    yahooApiRunner.addStock(stockToAdd.getText().toString());
                    stockToAdd.getText().clear();
                    return true;
                }
                return false;
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_options, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
            .getActionView();

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
        {
            public boolean onQueryTextChange(String newText)
            {
                if(newText.length() == 0)
                    searchView.clearFocus();
                
                stockDataAdapter.filterList(newText);
                return true;
            }

            public boolean onQueryTextSubmit(String query)
            {
                searchView.clearFocus();
                return false;
            }
        };

        if (searchView != null && searchManager != null)
        {
            searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
            searchView.setQueryHint(getString(R.string.search_query_hint));
            searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            searchView.setOnQueryTextListener(queryTextListener);
            searchView.clearFocus();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_refresh:
                yahooApiRunner.updateStocks();
                break;
            case android.R.id.home:
                finish();
            default:
                break;
        }

        return true;
    }

    private void generateDefaultStocks()
    {
        yahooApiRunner.addStocks(defaultStocks);
    }

    private void hideKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

            if(imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showSnackbarMessage(String message)
    {
        Snackbar
            .make(recyclerView, message, Snackbar.LENGTH_LONG)
            .show();
    }
}