package com.services;

import com.entities.Good;
import com.repositories.GoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GoodService {

    private GoodRepository goodRepository;

    @Autowired
    public void setGoodRepository(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }

    public Good save(Good good) {
        return goodRepository.save(good);
    }

    public void delete(Long id) {
        goodRepository.deleteById(id);
    }

    public List<Good> findAll() {
        return goodRepository.findAll();
    }

    public List<Good> findAllById(Iterable<Long> IDs) {
        return goodRepository.findAllById(IDs);
    }

    public Good findById(Long id) {
        try {
            return goodRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
