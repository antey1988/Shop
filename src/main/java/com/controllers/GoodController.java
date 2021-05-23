package com.controllers;

import com.entities.Good;
import com.services.GoodServiceImp;
import org.h2.engine.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("/items-list")
public class GoodController {
    private GoodServiceImp goodServiceImp;

    @Autowired
    public void setGoodServiceImp(GoodServiceImp goodServiceImp) {
        this.goodServiceImp = goodServiceImp;
    }

    @GetMapping("/items-list")
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
        if (!model.containsAttribute("item")) {
            model.addAttribute("item", new Good());
        }
        return "add-good";
    }

    @PostMapping("/addPost")
    public String newGood(Model model, Good item) {
        Good good = goodServiceImp.save(item);
        model.addAttribute("item", good);
        return "redirect:/add";
    }

    @DeleteMapping("/{id}")
    public String removeGood(@PathVariable("id") Long id) {
        goodServiceImp.delete(id);
        return "redirect:/items-list";
    }
}
