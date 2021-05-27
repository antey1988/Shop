package com.controllers;

import com.entities.Good;
import com.service.GoodId;
import com.dao.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Controller
@Transactional
public class GoodController {
    private static Good good1, good2;

    private boolean added = false;
    static {
        good1 = new Good();
        good1.setName("Amediateka");
        good1.setPrice(new BigDecimal("2.00"));
        good2 = new Good();
        good2.setName("Voltage Protector");
        good2.setPrice(new BigDecimal("4.00"));
    }
    private void addItems() {
        if (!added) {
            goodService.save(good1);
            goodService.save(good2);
            added = true;
        }
    }

    private GoodService goodService;

    @Autowired
    public void setGoodServiceImp(GoodService goodService) {
        this.goodService = goodService;
    }



    @GetMapping("/items")
    public String showGoodsWithFilter(Model model, @RequestParam(value = "id", required = false) Long id) {
        addItems();
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
        addItems();
        model.addAttribute("items", goodService.findAll());
//        model.addAttribute("order", order);
        model.addAttribute("orderid", id);
        model.addAttribute("goodId", new GoodId());
//        model.addAttribute("goodForOrder", new GoodForOrder());
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
