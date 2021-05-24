package com.controllers;

import com.entities.Good;
import com.services.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("/items-list")
public class GoodController {
    private GoodService goodService;

    @Autowired
    public void setGoodServiceImp(GoodService goodService) {
        this.goodService = goodService;
    }

    @GetMapping("/items")
    public String showGoodWithFilter(Model model, @RequestParam(value = "id", required = false) Long id) {
        if (id == null) {
            model.addAttribute("items", goodService.findAll());
        } else {
            model.addAttribute("items", goodService.findById(id));
        }
//        model.addAttribute("item", new Good());
        model.addAttribute("id", id);
        return "items-list";
    }

    @GetMapping("/items/add")
    public String createGood(Model model) {
        Good goods = new Good();
        model.addAttribute("item", goods);
        return "item-edit";
    }

//    @GetMapping("/items/edit/{id}")
    @GetMapping("/items/edit")
    public String showEditGoodsForm(Model model, @RequestParam("id") Long id) {
        Good good = goodService.findById(id);
        model.addAttribute("item", good);
        return "item-edit";
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
