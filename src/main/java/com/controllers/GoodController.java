package com.controllers;

import com.entities.Good;
import com.entities.GoodId;
import com.entities.Order;
import com.services.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Controller
@Transactional
public class GoodController {
    private GoodService goodService;

    @Autowired
    public void setGoodServiceImp(GoodService goodService) {
        this.goodService = goodService;
    }

    @GetMapping("/items")
    public String showGoodsWithFilter(Model model, @RequestParam(value = "id", required = false) Long id) {
        if (id == null) {
            model.addAttribute("items", goodService.findAll());
        } else {
            model.addAttribute("items", goodService.findById(id));
        }
        model.addAttribute("id", id);
        return "items-list";
    }

    @GetMapping("/items/add")
    public String showCreateGood(Model model) {
        Good goods = new Good();
        model.addAttribute("item", goods);
        return "item-edit";
    }

    @GetMapping("/items/edit")
    public String showEditGood(Model model, @RequestParam("id") Long id) {
        Good good = goodService.findById(id);
        model.addAttribute("item", good);
        return "item-edit";
    }

    @GetMapping("/items/select")
    public String showSelectGood(Model model, @RequestParam(value = "orderid", required = false) Long id ){
//                                 ,@ModelAttribute(value = "order")Order order) {
        model.addAttribute("items", goodService.findAll());
//        model.addAttribute("order", order);
        model.addAttribute("orderid", id);
        model.addAttribute("goodId", new GoodId());
        return "items-select";
    }

    @PostMapping("/items/save")
    public String modifyGoods(@ModelAttribute(value = "item")Good item) {
        goodService.save(item);
        return "redirect:/items";
    }

    @DeleteMapping("/items/delete/{id}")
    public String removeGoods(@PathVariable("id") Long id) {
        goodService.delete(id);
        return "redirect:/items";
    }
}
