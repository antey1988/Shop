package com.controllers;

import com.entities.*;
import com.repositories.OrderLineRepository;
import com.service.GoodId;
import com.service.GoodForm;
import com.service.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {
    private Order currentOrderDAO;
    private OrderForm currentOrderForm;
    private List<Good> selectedGoods;
    private List<GoodForm> selectedGoodForm;

    private com.dao.OrderService orderService;
    private com.dao.GoodService goodService;
    private OrderLineRepository orderLineRepository;

    @Autowired
    public void setOrderService(com.dao.OrderService orderService) {
        this.orderService = orderService;
    }
    @Autowired
    public void setGoodService(com.dao.GoodService goodService) {
        this.goodService = goodService;
    }
    @Autowired
    public void setOrderLineRepository(OrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
    }

    @GetMapping("/orders")
    public String listOrdersWithFilter(Model model, @RequestParam(value = "id", required = false) Long id) {
        List<Order> orders;
        if (id == null) {
            orders = orderService.findAll();
        } else {
            orders = Collections.singletonList(orderService.findById(id));
        }

        model.addAttribute("orders", orders.stream().map(OrderForm::fromOrder).collect(Collectors.toList()));
        model.addAttribute("id", id);
        return "orders-list";
    }

    @GetMapping("/orders/add")
    public String createGood(Model model) {
        if (currentOrderForm == null) {
            currentOrderDAO = new Order();
            currentOrderForm = OrderForm.fromOrder(currentOrderDAO);
        }

        if (selectedGoodForm != null) {
            currentOrderForm.setTabledGoodForm(new ArrayList<>(selectedGoodForm));
        }

        OrderForm orderForm = currentOrderForm;

        model.addAttribute("order", orderForm);
        return "order-edit";
    }

    @GetMapping("/orders/edit")
    public String showEditGoodsForm(Model model, @RequestParam(value = "id") Long id) {
        if (currentOrderForm == null) {
            currentOrderDAO = orderService.findById(id);
            currentOrderForm = OrderForm.fromOrder(currentOrderDAO);
            List<OrderLine> orderLines = currentOrderDAO.getOrderLines();
            if (orderLines != null && !orderLines.isEmpty()) {
                List<GoodForm> goods = orderLines.stream()
                        .map(line->new GoodForm(line.getGood().getId(), line.getGood().getName(), line.getGood().getPrice(), line.getCount()))
                        .collect(Collectors.toList());

                currentOrderForm.setTabledGoodForm(goods);
            }
        }

        if (selectedGoodForm != null) {
            currentOrderForm.setTabledGoodForm(new ArrayList<>(selectedGoodForm));
        }

        OrderForm orderForm = currentOrderForm;

        model.addAttribute("order", orderForm);
        return "order-edit";
    }

    @PostMapping("/orders/orderlines")
    public String getOrderLines(RedirectAttributes attributes,
                                @RequestParam(value = "id",required = false) Long orderId,
                                @ModelAttribute(value = "goodId") GoodId goodId) {
        selectedGoods = goodService.findAllById(goodId.getCheckedItems());
        selectedGoodForm = selectedGoods.stream().map(good->new GoodForm(good.getId(), good.getName(), good.getPrice(), 0)).collect(Collectors.toList());
        if (orderId != null) {
            attributes.addAttribute("id", orderId);
            return "redirect:/orders/edit";
        } else {
            return "redirect:/orders/add";
        }
    }

    @PostMapping("/orders/save")
    public String modifyGoods(@ModelAttribute(value = "order") OrderForm orderForm) {
        orderForm.toOrder(currentOrderDAO);

        List<GoodForm> goodsForm = orderForm.getTabledGoodForm();
        List<OrderLine> orderLines = currentOrderDAO.getOrderLines();
        if (orderLines == null) {
            orderLines = new ArrayList<>();
            currentOrderDAO.setOrderLines(orderLines);
        }


        if (goodsForm != null && !goodsForm.isEmpty()) {
            for (GoodForm goodForm : goodsForm) {
                boolean isOld = false;
                for (OrderLine line : orderLines) {
                    if (line.getGood().getId() == goodForm.getId()) {
                        line.setCount(goodForm.getCount());
                        isOld = true;
                        break;
                    }
                }
                if (!isOld) {
                    for (Good good : selectedGoods) {
                        if (good.getId() == goodForm.getId()) {
                            OrderLine line = new OrderLine();
                            line.setOrder(currentOrderDAO);
                            line.setGood(good);
                            line.setCount(goodForm.getCount());
                            orderLines.add(line);
                            break;
                        }
                    }
                }
            }
        }

        orderService.save(currentOrderDAO);
        currentOrderDAO = null;
        currentOrderForm = null;
        selectedGoods = null;
        selectedGoodForm = null;
        return "redirect:/orders";
    }

    @DeleteMapping("/orders/delete/{id}")
    public String removeGoods(@PathVariable("id") Long id) {
        orderService.delete(id);
        return "redirect:/orders";
    }
}
