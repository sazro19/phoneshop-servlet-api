package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.BaseItem;
import com.es.phoneshop.model.product.cart.Cart;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Order extends Cart implements BaseItem {
    private Long id;
    private String secureId;

    private BigDecimal subtotal;
    private BigDecimal deliveryCost;

    private String firstname;
    private String lastname;
    private String phone;
    private LocalDate deliveryDate;
    private String deliveryAddress;
    private PaymentMethod paymentMethod;

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecureId() {
        return secureId;
    }

    public void setSecureId(String secureId) {
        this.secureId = secureId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id)
                && Objects.equals(subtotal, order.subtotal)
                && Objects.equals(deliveryCost, order.deliveryCost)
                && Objects.equals(firstname, order.firstname)
                && Objects.equals(lastname, order.lastname)
                && Objects.equals(phone, order.phone)
                && Objects.equals(deliveryDate, order.deliveryDate)
                && Objects.equals(deliveryAddress, order.deliveryAddress)
                && paymentMethod == order.paymentMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subtotal, deliveryCost, firstname, lastname,
                            phone, deliveryDate, deliveryAddress, paymentMethod);
    }
}
