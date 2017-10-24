package billyji.com.yahoostocks;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockUpdate implements Serializable {
    private final String stockSymbol;
    private final BigDecimal price;
    private final String name;
    private final BigDecimal ask;
    private final BigDecimal bid;
    private final BigDecimal change;

    public final static String INVALID_STOCK = "INVALID";

    public StockUpdate(String stockSymbol, BigDecimal price, String name, BigDecimal ask, BigDecimal bid, BigDecimal change) {
        this.stockSymbol = stockSymbol;
        this.price = price;
        this.name = name;
        this.ask = ask == null ? new BigDecimal(-1) : ask;
        this.bid = bid == null ? new BigDecimal(-1) : bid;
        this.change = change == null ? new BigDecimal(-1) : change;
    }

    public StockUpdate(String nullUpdate)
    {
        this.stockSymbol = nullUpdate;
        this.price = null;
        this.name = null;
        this.ask = null;
        this.bid = null;
        this.change = null;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

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

    public static StockUpdate create(StockQuote r) {
        if(r.getName() == null || r.getSymbol() == null)
            return new StockUpdate(INVALID_STOCK);

        return new StockUpdate(r.getSymbol(), r.getLastTradePriceOnly(), r.getName(), r.getAsk(), r.getBid(), r.getChange());
    }
}
