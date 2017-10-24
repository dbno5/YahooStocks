package billyji.com.yahoostocks;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    @BindView(R.id.stock_item_ask)
    TextView ask;
    @BindView(R.id.stock_item_bid)
    TextView bid;
    @BindView(R.id.stock_item_change)
    TextView change;
    @BindView(R.id.additional_details)
    RelativeLayout additionalDetailView;

    private int originalHeight = 0;
    private boolean isViewExpanded = false;
    private final StringBuilder sb = new StringBuilder();

    public StockUpdateViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
        v.setOnClickListener(this);
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

    public void setAsk(BigDecimal ask)
    {
        if(ask.intValue() == -1)
            this.ask.setText("N/A");
        else
        {
            sb.append("Ask: ");
            sb.append(PRICE_FORMAT.format(ask.floatValue()));
            this.ask.setText(sb.toString());
            sb.setLength(0);
        }
    }

    public void setBid(BigDecimal bid)
    {
        if(bid.intValue() == -1)
            this.bid.setText("N/A");
        else
        {
            sb.append("Bid: ");
            sb.append(PRICE_FORMAT.format(bid.floatValue()));
            this.bid.setText(sb.toString());
            sb.setLength(0);
        }}

    public void setChange(BigDecimal change)
    {
        setPriceColor(change);
        if(change.intValue() == -1)
            this.change.setText("N/A");
        else
        {
            sb.append("Change: ");
            sb.append(PRICE_FORMAT.format(change.floatValue()));
            this.change.setText(sb.toString());
            sb.setLength(0);
        }
    }

    public void setPriceColor(BigDecimal change)
    {
        if (change.floatValue() < 0 && change.intValue() != -1)
            this.price.setTextColor(Color.parseColor("#FF0000"));
    }

    @Override
    public void onClick(final View view) {
        // If the originalHeight is 0 then find the height of the View being used
        // This would be the height of the cardview
        if (originalHeight == 0) {
            originalHeight = view.getHeight();
        }

        // Declare a ValueAnimator object
        ValueAnimator valueAnimator;
        if (!isViewExpanded) {
            additionalDetailView.setVisibility(View.VISIBLE);
            additionalDetailView.setEnabled(true);
            isViewExpanded = true;
            valueAnimator = ValueAnimator.ofInt(originalHeight, (int)(originalHeight * 1.8)); // These values in this method can be changed to expand however much you like
        } else {
            isViewExpanded = false;
            valueAnimator = ValueAnimator.ofInt((int)(originalHeight * 1.8), originalHeight);

            Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

            a.setDuration(200);
            // Set a listener to the animation and configure onAnimationEnd
            a.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    additionalDetailView.setVisibility(View.INVISIBLE);
                    additionalDetailView.setEnabled(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            // Set the animation on the custom view
            view.startAnimation(a);
        }
        valueAnimator.setDuration(200);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                view.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                view.requestLayout();
            }
        });
        valueAnimator.start();

    }
}