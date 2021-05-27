package com.dao;

import com.entities.Order;

import java.util.List;

public interface OrderService {
    //добавление и изменение заказа
    Order save(Order order);
    //удаление заказа
    void delete(Long id);
    //получение всех заказов
    List<Order> findAll();
    //получение заказа по id
    Order findById(Long id);
}
