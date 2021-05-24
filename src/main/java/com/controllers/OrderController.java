package com.controllers;

import com.entities.Order;
import com.entities.OrderLine;
import com.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String listOrdersWithFilter(Model model, @RequestParam(value = "id", required = false) Long id) {
        if (id == null) {
            model.addAttribute("orders", orderService.findAll());
        } else {
            model.addAttribute("orders", orderService.findById(id));
        }
        model.addAttribute("id", id);
        return "orders-list";
    }

    @GetMapping("/orders/add")
    public String createGood(Model model) {
        Order order = new Order();
        OrderLine line = new OrderLine();
        model.addAttribute("order", order);
        model.addAttribute("line", line);
        return "order-edit";
    }

    @GetMapping("/orders/edit")
    public String showEditGoodsForm(Model model, @RequestParam("id") Long id) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "order-edit";
    }

    @PostMapping("/orders/save")
    public String modifyGoods(@ModelAttribute(value = "order")Order order) {
        orderService.save(order);
        return "redirect:/orders";
    }

    @DeleteMapping("/orders/delete/{id}")
    public String removeGoods(@PathVariable("id") Long id) {
        orderService.delete(id);
        return "redirect:/orders-list";
    }
}
