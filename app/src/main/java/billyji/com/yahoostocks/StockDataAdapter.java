package billyji.com.yahoostocks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StockDataAdapter extends RecyclerView.Adapter<StockUpdateViewHolder> {
    private final List<StockUpdate> data = new ArrayList<>();
    private List<StockUpdate> tempData = new ArrayList<>();
    private List<StockUpdate> deletedData = new ArrayList<>();

    @Override
    public StockUpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_update_item, parent, false);
        return new StockUpdateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StockUpdateViewHolder holder, int position) {
        StockUpdate stockUpdate = data.get(position);
        holder.setStockSymbol(stockUpdate.getStockSymbol());
        holder.setPrice(stockUpdate.getPrice());
        holder.setName(stockUpdate.getName());
        holder.setAsk(stockUpdate.getAsk());
        holder.setBid(stockUpdate.getBid());
        holder.setChange(stockUpdate.getChange());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(StockUpdate newStockUpdate) {
        for (StockUpdate stockUpdate : data) {
            if (stockUpdate.getStockSymbol().equals(newStockUpdate.getStockSymbol()))
            {
                if (stockUpdate.getPrice().equals(newStockUpdate.getPrice()))
                {
                    return;
                }
                else
                {
                    data.remove(stockUpdate);
                }
                break;
            }
        }

        data.add(0, newStockUpdate);

        Collections.sort(data, new Comparator<StockUpdate>() {
            @Override
            public int compare(final StockUpdate object1, final StockUpdate object2) {
                return object1.getStockSymbol().compareTo(object2.getStockSymbol());
            }
        });

        notifyDataSetChanged();

    }

    public void filterList(String stockIdentifier)
    {
        data.addAll(deletedData);
        deletedData.clear();
        tempData.addAll(data);

        for(StockUpdate stock : tempData)
        {
            if(stockIdentifier.length() > stock.getStockSymbol().length())
            {
                data.remove(stock);
                deletedData.add(stock);
            }
            else if(!stock.getStockSymbol().substring(0, stockIdentifier.length()).equals(stockIdentifier) &&
                !stock.getName().substring(0, stockIdentifier.length()).equals(stockIdentifier))
            {
                data.remove(stock);
                deletedData.add(stock);
            }
        }

        Collections.sort(data, new Comparator<StockUpdate>() {
            @Override
            public int compare(final StockUpdate object1, final StockUpdate object2) {
                return object1.getStockSymbol().compareTo(object2.getStockSymbol());
            }
        });

        notifyDataSetChanged();
        tempData.clear();
    }
}