package billyji.com.yahoostocks;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by bj0716 on 10/21/17.
 */

public class StockQuote {
    private String symbol;

    @SerializedName("Name")
    private String name;

    @SerializedName("LastTradePriceOnly")
    private BigDecimal lastTradePriceOnly;

    @SerializedName("DaysLow")
    private BigDecimal daysLow;

    @SerializedName("DaysHigh")
    private BigDecimal daysHigh;

    @SerializedName("Volume")
    private String volume;

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getDaysLow() {
        return daysLow;
    }

    public BigDecimal getDaysHigh() {
        return daysHigh;
    }

    public String getVolume() {
        return volume;
    }

    public BigDecimal getLastTradePriceOnly() {
        return lastTradePriceOnly;
    }
}