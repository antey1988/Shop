package com.service;

import java.math.BigDecimal;
import java.util.List;

public class GoodForm {
    private long id;
    private String name;
    private BigDecimal price;
    private int count;

    public GoodForm() {
    }

    public GoodForm(long id, String name, BigDecimal price, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }
}
