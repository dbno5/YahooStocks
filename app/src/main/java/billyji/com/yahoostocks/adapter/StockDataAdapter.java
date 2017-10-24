package billyji.com.yahoostocks.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import billyji.com.yahoostocks.R;
import billyji.com.yahoostocks.model.StockUpdate;

public class StockDataAdapter extends RecyclerView.Adapter<StockUpdateViewHolder>
{
    private final List<StockUpdate> data = new ArrayList<>();
    private final List<StockUpdate> tempData = new ArrayList<>();
    private final List<StockUpdate> deletedData = new ArrayList<>();

    @Override
    public StockUpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_update_item, parent, false);
        return new StockUpdateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StockUpdateViewHolder holder, int position)
    {
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

    public void add(StockUpdate newStockUpdate)
    {
        for (StockUpdate stockUpdate : data)
        {
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
        sortList();
        notifyDataSetChanged();
    }

    private void sortList()
    {
        Collections.sort(data, (object1, object2) -> object1.getStockSymbol().compareTo(object2.getStockSymbol()));
    }

    public void filterList(String stockIdentifier)
    {
        data.addAll(deletedData);
        deletedData.clear();
        tempData.addAll(data);

        for(StockUpdate stock : tempData)
        {
            if(stockIdentifier.length() > stock.getStockSymbol().length() &&
                !stock.getName().substring(0, stockIdentifier.length()).toUpperCase().equals(stockIdentifier.toUpperCase()))
            {
                data.remove(stock);
                deletedData.add(stock);
            }

            else if(stockIdentifier.length() > stock.getStockSymbol().length() &&
                stock.getName().substring(0, stockIdentifier.length()).toUpperCase().equals(stockIdentifier.toUpperCase()))
            {
                //empty statement, just checking for length so string length isn't exceeded in next statement
            }

            else if(!stock.getStockSymbol().substring(0, stockIdentifier.length()).equals(stockIdentifier.toUpperCase()) &&
                !stock.getName().substring(0, stockIdentifier.length()).toUpperCase().equals(stockIdentifier.toUpperCase()))
            {
                data.remove(stock);
                deletedData.add(stock);
            }
        }

        sortList();

        notifyDataSetChanged();
        tempData.clear();
    }
}