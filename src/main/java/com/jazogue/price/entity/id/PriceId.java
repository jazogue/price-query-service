package com.jazogue.price.entity.id;

import java.io.Serializable;
import java.util.Objects;

public class PriceId implements Serializable {

    private int priceList;
    
    private int productId;

    public PriceId() {}

    public PriceId(int priceList, int productId) {
        this.priceList = priceList;
        this.productId = productId;
    }

    public int getPriceList() {
        return priceList;
    }

    public void setPriceList(int priceList) {
        this.priceList = priceList;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceId priceId = (PriceId) o;
        return priceList == priceId.priceList && productId == priceId.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(priceList, productId);
    }
}
