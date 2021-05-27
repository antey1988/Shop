package com.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CLIENT", nullable = false)
    private String client;

    @Column(name = "DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderLine> orderLines;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
//        this.orderLines.addAll(orderLines);
        this.orderLines = orderLines;
    }

    public void addOrderLine(OrderLine orderLine) {
        orderLine.setOrder(this);
        this.orderLines.add(orderLine);
    }

}
