package com.controllers;

import com.entities.Good;
import com.entities.Order;
import com.entities.OrderLine;
import com.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class OrderController {
    private List<OrderLine> orderLines = new ArrayList<>();
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
        orderLines.forEach(orderLine -> orderLine.setOrder(order));
        order.setOrderLines(orderLines);
        model.addAttribute("order", order);
        return "order-edit";
    }

    @GetMapping("/orders/edit")
    public String showEditGoodsForm(Model model, @RequestParam(value = "id") Long id) {
        Order order = orderService.findById(id);
        orderLines.forEach(orderLine -> orderLine.setOrder(order));
        order.setOrderLines(orderLines);
        model.addAttribute("order", order);
        return "order-edit";
    }

    @PostMapping("/orders/orderlines")
    public String getOrderLines(@RequestParam(value = "id",required = false) Long id,
                                @ModelAttribute(value = "items") Map<Good, Boolean> items) {
        orderLines.clear();
        items.entrySet().stream().filter(Map.Entry::getValue)
                .forEach(entry->{
                    OrderLine orderLine = new OrderLine();
                    orderLine.setGood(entry.getKey());
                    orderLines.add(orderLine);
                });
        if (id != null) {
            return "redirect:/orders/edit";
        } else {
            return "redirect:/orders/add";
        }
    }

    @PostMapping("/orders/save")
    public String modifyGoods(@ModelAttribute(value = "order")Order order) {
        orderService.save(order);
        return "redirect:/orders";
    }

    @DeleteMapping("/orders/delete/{id}")
    public String removeGoods(@PathVariable("id") Long id) {
        orderService.delete(id);
        return "redirect:/orders";
    }
}
