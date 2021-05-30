package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class PriceHistory {
    private Calendar calendar;

    private BigDecimal price;

    public PriceHistory() {
        
    }

    public PriceHistory(BigDecimal price) {
        this.calendar = new GregorianCalendar();
        this.price = price;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceHistory that = (PriceHistory) o;
        return calendar.equals(that.calendar) && price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendar, price);
    }
}
