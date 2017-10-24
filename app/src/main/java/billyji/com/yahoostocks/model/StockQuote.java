package billyji.com.yahoostocks.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class StockQuote
{
    private String symbol;

    @SerializedName("Name")
    private String name;

    @SerializedName("LastTradePriceOnly")
    private BigDecimal lastTradePriceOnly;

    @SerializedName("Ask")
    private BigDecimal ask;

    @SerializedName("Bid")
    private BigDecimal bid;

    @SerializedName("Change")
    private BigDecimal change;

    BigDecimal getAsk()
    {
        return ask;
    }

    BigDecimal getBid()
    {
        return bid;
    }

    BigDecimal getChange()
    {
        return change;
    }

    String getSymbol() {
        return symbol;
    }

    String getName() {
        return name;
    }

    BigDecimal getLastTradePriceOnly() {
        return lastTradePriceOnly;
    }
}