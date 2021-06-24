package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartItem;
import com.es.phoneshop.web.OrderDetailsAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
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
        lock.readLock().lock();
        try {
            return Arrays.stream(PaymentMethod.values())
                    .map(PaymentMethod::getValue)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    @Override
    public void checkFirstname(Order order, String firstname, Map<String, String> errors) {
        setOrderFieldOrErrors(firstname, OrderDetailsAttributes.getFIRSTNAME(), errors, order::setFirstname);
    }

    @Override
    public void checkLastname(Order order, String lastname, Map<String, String> errors) {
        setOrderFieldOrErrors(lastname, OrderDetailsAttributes.getLASTNAME(), errors, order::setLastname);
    }

    @Override
    public void checkDeliveryAddress(Order order, String address, Map<String, String> errors) {
        setOrderFieldOrErrors(address, OrderDetailsAttributes.getDeliveryAddress(), errors, order::setDeliveryAddress);
    }

    @Override
    public void checkDeliveryDate(Order order, String date, Map<String, String> errors) {
        lock.writeLock().lock();
        try {
            if (date == null || date.isEmpty()) {
                errors.put(OrderDetailsAttributes.getDeliveryDate(), "delivery date is required");
            } else {
                LocalDate localDate = LocalDate.parse(date);
                if (localDate.isBefore(LocalDate.now())) {
                    errors.put(OrderDetailsAttributes.getDeliveryDate(), "invalid delivery date");
                } else {
                    order.setDeliveryDate(localDate);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void checkPaymentMethod(Order order, String paymentMethod, Map<String, String> errors) {
        lock.writeLock().lock();
        try {
            if (paymentMethod == null || paymentMethod.isEmpty()) {
                errors.put(OrderDetailsAttributes.getPaymentMethod(), "payment method is required");
            } else {
                order.setPaymentMethod(PaymentMethod.getEnum(paymentMethod));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void checkPhone(Order order, String phone, Map<String, String> errors) {
        lock.writeLock().lock();
        try {
            if (phone == null || phone.isEmpty()) {
                errors.put(OrderDetailsAttributes.getPHONE(), "phone is required");
            } else if (!isPhoneCorrect(phone)) {
                errors.put(OrderDetailsAttributes.getPHONE(), "invalid phone number");
            } else {
                order.setPhone(phone);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Order getOrderBySecureId(String secureId) {
        return orderDao.getItemBySecureOrderId(secureId);
    }

    private boolean isPhoneCorrect(String phone) {
        return (phone.matches("^\\+[\\(\\-]?(\\d[\\(\\)\\-]?){11}\\d$") ||
                phone.matches("^\\(?(\\d[\\-\\(\\)]?){9}\\d$")) &&
                phone.matches("[\\+]?\\d*(\\(\\d{3}\\))?\\d*\\-?\\d*\\-?\\d*\\d$");
    }

    private void setOrderFieldOrErrors(String value, String field, Map<String, String> errors,
                                       Consumer<String> consumer) {
        lock.writeLock().lock();
        try {
            if (value == null || value.isEmpty()) {
                errors.put(field, field + " is required");
            } else {
                consumer.accept(value);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(993);
    }
}
