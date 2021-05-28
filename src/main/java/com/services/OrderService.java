package com.services;

import com.entities.Good;
import com.entities.Order;
import com.entities.OrderLine;
import com.forms.GoodForm;
import com.forms.GoodId;
import com.forms.OrderForm;
import com.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private Order currentOrderDAO;
    private OrderForm currentOrderForm;
    private List<Good> savedGoodsDAO;
    private List<GoodForm> savedGoodsForm;
    private List<Good> selectedGoodsDAO;
    private List<GoodForm> selectedGoodsForm;

    private OrderRepository orderRepository;
    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    private GoodService goodService;
    @Autowired
    public void setGoodService(GoodService goodService) {
        this.goodService = goodService;
    }

//    public Order save(Order order) {
//        return orderRepository.save(order);
//    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        try {
            return orderRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public List<OrderForm> formListOrders(Long id) {
        List<Order> orders;
        if (id == null) {
            orders = findAll();
        } else {
            orders = Collections.singletonList(findById(id));
        }
        return orders.stream().map(OrderForm::fromOrder).collect(Collectors.toList());
    }

    public OrderForm formOrder(Long id) {
        OrderForm orderForm;
        if (currentOrderForm == null) {
            if (id == null) {
                currentOrderDAO = new Order();
            } else {
                currentOrderDAO = findById(id);
            }
            fillSavedGood(currentOrderDAO);
            currentOrderForm = OrderForm.fromOrder(currentOrderDAO);
            currentOrderForm.setTabledGoodForm(savedGoodsForm);
        }
        if (selectedGoodsForm != null) {
            currentOrderForm.getTabledGoodForm().addAll(selectedGoodsForm);
            selectedGoodsForm = null;
        }
        orderForm = currentOrderForm;
        return orderForm;
    }

    public void save(OrderForm orderForm) {
//        selectedGoodsForm = null;
        currentOrderForm = null;
        orderForm.toOrder(currentOrderDAO);
        if (orderForm.getTabledGoodForm() != null) {
            savedGoodsForm = new ArrayList<>(orderForm.getTabledGoodForm());
            if (selectedGoodsDAO != null) {
                savedGoodsDAO.addAll(selectedGoodsDAO);
                selectedGoodsDAO = null;
            }
            currentOrderDAO.setOrderLines(new ArrayList<>());

            for (GoodForm goodForm : savedGoodsForm) {
                for (Good good : savedGoodsDAO) {
                    if (good.getId() == goodForm.getId()) {
                        OrderLine orderLine = new OrderLine();
                        orderLine.setId(goodForm.getOrderLineId());
                        orderLine.setOrder(currentOrderDAO);
                        orderLine.setGood(good);
                        orderLine.setCount(goodForm.getCount());
                        currentOrderDAO.getOrderLines().add(orderLine);
                        break;
                    }
                }
            }
        }

        orderRepository.save(currentOrderDAO);
        currentOrderDAO = null;
        savedGoodsDAO = null;
        savedGoodsForm = null;
    }

    public void fillSelectedGood(GoodId goodId) {
        selectedGoodsDAO = goodService.findAllById(goodId.getCheckedItems());
        selectedGoodsForm = new ArrayList<>();
        goodsDAOToGoodsForm();
    }


    private void fillSavedGood(Order OrderDAO) {
        savedGoodsForm = new ArrayList<>();
        savedGoodsDAO = new ArrayList<>();
        List<OrderLine> orderLines = currentOrderDAO.getOrderLines();
        if (orderLines != null && !orderLines.isEmpty()) {
            orderLinesToGoodsDAOAndGoodsForm(orderLines);
        }
    }

    private void orderLinesToGoodsDAOAndGoodsForm(List<OrderLine> orderLines) {
        orderLines.forEach(
                line->{
                    GoodForm goodForm = new GoodForm();
                    goodForm.setId(line.getGood().getId());
                    goodForm.setName(line.getGood().getName());
                    goodForm.setPrice(line.getGood().getPrice());
                    goodForm.setCount(line.getCount());
                    goodForm.setOrderLineId(line.getId());
                    savedGoodsForm.add(goodForm);
                    savedGoodsDAO.add(line.getGood());
                });
    }

    private void goodsDAOToGoodsForm() {
        selectedGoodsDAO.forEach(goodDAO->{
                    GoodForm goodForm = new GoodForm();
                    goodForm.setId(goodDAO.getId());
                    goodForm.setName(goodDAO.getName());
                    goodForm.setPrice(goodDAO.getPrice());
                    goodForm.setCount(0);
                    selectedGoodsForm.add(goodForm);
                });
    }
}
