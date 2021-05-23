package com.services;

import java.util.List;
import com.entities.Good;

public interface GoodService {
    //добавление и изменение товара
    Good save(Good good);
    //удаление товара
    void delete(Long id);
    //получение всех товаров
    List<Good> findAll();
    //получение товара по id
    Good findById(Long id);
}
