package com.services;

import com.entities.Good;
import com.repositories.GoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GoodServiceImp implements GoodService {
    @Autowired
    private GoodRepository goodRepository;

    public void setGoodRepository(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }

    @Override
    public Good save(Good good) {
        return goodRepository.save(good);
    }

    @Override
    public void delete(Long id) {
        goodRepository.deleteById(id);
    }

    @Override
    public List<Good> findAll() {
        return goodRepository.findAll();
    }

    @Override
    public Good findById(Long id) {
        try {
            return goodRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
