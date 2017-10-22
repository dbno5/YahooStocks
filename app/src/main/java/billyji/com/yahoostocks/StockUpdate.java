package billyji.com.yahoostocks;

import java.util.Date;

/**
 * Created by bj0716 on 10/21/17.
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StockUpdate implements Serializable {
    private final String stockSymbol;
    private final BigDecimal price;
    private final String name;

    public StockUpdate(String stockSymbol, BigDecimal price, String name) {
        this.stockSymbol = stockSymbol;
        this.price = price;
        this.name = name;
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

    public static StockUpdate create(StockQuote r) {
        if(r.getName() == null || r.getSymbol() == null)
            return new StockUpdate("dummy", new BigDecimal(0), "dummy");

        return new StockUpdate(r.getSymbol(), r.getLastTradePriceOnly(), r.getName());
    }
}
