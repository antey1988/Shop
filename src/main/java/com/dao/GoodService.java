package com.dao;

import java.util.List;
import com.entities.Good;

public interface GoodService {
    //добавление и изменение товара
    Good save(Good good);
    //удаление товара
    void delete(Long id);
    //получение всех товаров
    List<Good> findAll();
    //получение списка товаров по списку id
    List<Good> findAllById(Iterable<Long> IDs);
    //получение товара по id
    Good findById(Long id);
}
