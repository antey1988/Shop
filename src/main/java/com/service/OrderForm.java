package com.service;

import com.entities.Order;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class OrderForm {
    private Long id;
    private String client;
    private Date date;
    private String address;
    private List<GoodForm> tabledGoodForm;

    public void toOrder(Order order) {
        order.setId(id);
        order.setClient(client);
        order.setDate(date);
        order.setAddress(address);
    }

    public static OrderForm fromOrder(Order order) {
        OrderForm orderForm = new OrderForm();
        orderForm.id = order.getId();
        orderForm.client= order.getClient();
        orderForm.date = order.getDate();
        orderForm.address = order.getAddress();
        return orderForm;
    }

    public OrderForm() {
    }

    public Long getId() {
        return id;
    }

    public String getClient() {
        return client;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<GoodForm> getTabledGoodForm() {
        return tabledGoodForm;
    }

    public void setTabledGoodForm(List<GoodForm> tabledGoodForm) {
        this.tabledGoodForm = tabledGoodForm;
    }
}
