package com.controllers;

import com.entities.Good;
import com.services.GoodServiceImp;
import org.h2.engine.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/items-list")
public class GoodController {
    private GoodServiceImp goodServiceImp;

    @Autowired
    public void setGoodServiceImp(GoodServiceImp goodServiceImp) {
        this.goodServiceImp = goodServiceImp;
    }

    @GetMapping
    public String showGoodWithFilter(Model model, @RequestParam(value = "id", required = false) Long id) {
        if (id == null) {
            model.addAttribute("items", goodServiceImp.findAll());
        } else {
            model.addAttribute("items", goodServiceImp.findById(id));
        }
        model.addAttribute("item", new Good());
        model.addAttribute("id", id);
        return "items-list";
    }

    @GetMapping("/add")
    public String createGood(Model model) {
        Good goods = new Good();
        model.addAttribute("item", goods);
        return "item-edit";
    }

    @GetMapping("/edit/{id}")
    public String showEditGoodsForm(Model model, @PathVariable("id") Long id) {
        Good good = goodServiceImp.findById(id);
        model.addAttribute("item", good);
        return "item-edit";
    }

    @PostMapping("/edit")
    public String modifyGoods(@ModelAttribute(value = "item")Good item) {
        goodServiceImp.save(item);
        return "redirect:/items-list";
    }

    @DeleteMapping("/{id}")
    public String removeGoods(@PathVariable("id") Long id) {
        goodServiceImp.delete(id);
        return "redirect:/items-list";
    }
}
