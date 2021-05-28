package com.controllers;

import com.forms.GoodId;
import com.forms.OrderForm;
import com.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderController {

    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String listOrdersWithFilter(Model model, @RequestParam(value = "id", required = false) Long id) {
        model.addAttribute("orders", orderService.formListOrders(id));
        model.addAttribute("id", id);
        return "orders-list";
    }

    @GetMapping("/orders/add")
    public String createGood(Model model) {
        model.addAttribute("order", orderService.formOrder(null));
        return "order-edit";
    }

    @GetMapping("/orders/edit")
    public String showEditGoodsForm(Model model, @RequestParam(value = "id") Long id) {
        model.addAttribute("order", orderService.formOrder(id));
        return "order-edit";
    }

    @PostMapping("/orders/orderlines")
    public String getOrderLines(RedirectAttributes attributes,
                                @RequestParam(value = "id",required = false) Long orderId,
                                @ModelAttribute(value = "goodId") GoodId goodId) {
        orderService.fillSelectedGood(goodId);
        if (orderId != null) {
            attributes.addAttribute("id", orderId);
            return "redirect:/orders/edit";
        } else {
            return "redirect:/orders/add";
        }
    }

    @PostMapping("/orders/save")
    public String modifyGoods(@ModelAttribute(value = "order") OrderForm orderForm) {
        orderService.save(orderForm);
        return "redirect:/orders";
    }

    @DeleteMapping("/orders/delete/{id}")
    public String removeGoods(@PathVariable("id") Long id) {
        orderService.delete(id);
        return "redirect:/orders";
    }
}
