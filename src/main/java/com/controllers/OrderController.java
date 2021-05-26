package com.controllers;

import com.entities.GoodId;
import com.entities.Order;
import com.entities.OrderLine;
import com.repositories.OrderLineRepository;
import com.services.GoodService;
import com.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    private List<OrderLine> orderLines;
    private Order currentOrder;
    private OrderService orderService;
    private GoodService goodService;
    private OrderLineRepository orderLineRepository;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
    @Autowired
    public void setGoodService(GoodService goodService) {
        this.goodService = goodService;
    }
    @Autowired
    public void setOrderLineRepository(OrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
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
        if (currentOrder == null) {
            currentOrder = new Order();
        }
        Order order = currentOrder;
        if (orderLines != null) {
            orderLines.forEach(orderLine -> orderLine.setOrder(order));
            currentOrder.setOrderLines(new ArrayList<>(orderLines));
        }
        model.addAttribute("order", order);
        return "order-edit";
    }

    @GetMapping("/orders/edit")
    public String showEditGoodsForm(Model model, @RequestParam(value = "id") Long id) {
        if (currentOrder == null) {
            currentOrder = orderService.findById(id);
        }
        Order order = currentOrder;
        if (orderLines != null) {
            orderLines.forEach(orderLine -> orderLine.setOrder(order));
            currentOrder.setOrderLines(new ArrayList<>(orderLines));
        }
        model.addAttribute("order", order);
        return "order-edit";
    }

    @PostMapping("/orders/orderlines")
    public String getOrderLines(RedirectAttributes attributes,
                                @RequestParam(value = "id",required = false) Long orderId,
                                @ModelAttribute(value = "goodId") GoodId goodId) {
        orderLines = new ArrayList<>();
        goodId.getCheckedItems().forEach(id->{
                    OrderLine orderLine = new OrderLine();
                    orderLine.setGood(goodService.findById(id));
                    orderLines.add(orderLine);
                });
        if (orderId != null) {
            attributes.addAttribute("id", orderId);
            return "redirect:/orders/edit";
        } else {
            return "redirect:/orders/add";
        }
    }

    @PostMapping("/orders/save")
    public String modifyGoods(@ModelAttribute(value = "order")Order order) {
        orderService.save(order);
        currentOrder = null;
        orderLines = null;
        return "redirect:/orders";
    }

    @DeleteMapping("/orders/delete/{id}")
    public String removeGoods(@PathVariable("id") Long id) {
        orderService.delete(id);
        return "redirect:/orders";
    }
}
