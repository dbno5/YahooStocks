package billyji.com.yahoostocks;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bj0716 on 10/21/17.
 */


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class StockUpdateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private static final NumberFormat PRICE_FORMAT = new DecimalFormat("#0.00");

    @BindView(R.id.stock_item_symbol)
    TextView stockSymbol;
    @BindView(R.id.stock_item_name)
    TextView name;
    @BindView(R.id.stock_item_price)
    TextView price;

    private RecyclerViewClickListener clickListener;

    public StockUpdateViewHolder(View v, RecyclerViewClickListener listener) {
        super(v);
        ButterKnife.bind(this, v);
        v.setOnClickListener(this);
        clickListener = listener;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol.setText(stockSymbol);
    }

    public void setPrice(BigDecimal price) {
        this.price.setText(PRICE_FORMAT.format(price.floatValue()));
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    @Override
    public void onClick(View view) {
        clickListener.onClick(view, getAdapterPosition());
    }
}