package billyji.com.yahoostocks;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class StockQuote {
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

    public BigDecimal getAsk()
    {
        return ask;
    }

    public BigDecimal getBid()
    {
        return bid;
    }

    public BigDecimal getChange()
    {
        return change;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getLastTradePriceOnly() {
        return lastTradePriceOnly;
    }
}