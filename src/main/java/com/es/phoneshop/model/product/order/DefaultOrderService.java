package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartItem;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private OrderDao orderDao;

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private static class Singleton {
        private static final DefaultOrderService INSTANCE = new DefaultOrderService();
    }

    public static DefaultOrderService getInstance() {
        return Singleton.INSTANCE;
    }

    private DefaultOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    public Order getOrder(Cart cart) {
        lock.writeLock().lock();
        try {
            if (cart.getItems().isEmpty()) {
                return null;
            }
            Order order = new Order();
            order.setItems(cart.getItems().stream().map(item -> {
                try {
                    return (CartItem) item.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList()));
            order.setSubtotal(cart.getTotalPrice());
            order.setDeliveryCost(calculateDeliveryCost());
            order.setTotalPrice(order.getSubtotal().add(order.getDeliveryCost()));

            return order;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<String> getStringPaymentMethods() {
        return Arrays.stream(PaymentMethod.values())
                .map(PaymentMethod::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    @Override
    public boolean isPhoneCorrect(String phone) {
        return (phone.matches("^\\+[\\(\\-]?(\\d[\\(\\)\\-]?){11}\\d$") ||
                phone.matches("^\\(?(\\d[\\-\\(\\)]?){9}\\d$")) &&
                phone.matches("[\\+]?\\d*(\\(\\d{3}\\))?\\d*\\-?\\d*\\-?\\d*\\d$");
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(993);
    }
}
