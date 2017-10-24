package billyji.com.yahoostocks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StockDataAdapter extends RecyclerView.Adapter<StockUpdateViewHolder> {
    private final List<StockUpdate> data = new ArrayList<>();
    private final RecyclerViewClickListener clickListener;

    StockDataAdapter(RecyclerViewClickListener listener) {
        clickListener = listener;
    }

    @Override
    public StockUpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_update_item, parent, false);
        return new StockUpdateViewHolder(v, clickListener);
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

    public void search(String stockIdentifier)
    {

    }
}