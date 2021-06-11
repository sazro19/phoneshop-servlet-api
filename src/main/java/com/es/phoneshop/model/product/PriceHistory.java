package com.es.phoneshop.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class PriceHistory implements Serializable {
    private LocalDate localDate;

    private BigDecimal price;

    public PriceHistory() {
        
    }

    public PriceHistory(BigDecimal price) {
        this.localDate = LocalDate.now();
        this.price = price;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
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
        return localDate.equals(that.localDate) && price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDate, price);
    }
}
