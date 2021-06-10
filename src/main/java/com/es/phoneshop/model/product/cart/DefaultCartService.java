package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.cart.exception.OutOfStockException;

import javax.servlet.http.HttpSession;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private ProductDao productDao;

    private static class Singleton {
        private static final DefaultCartService INSTANCE = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return Singleton.INSTANCE;
    }

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public Cart getCart(HttpSession session) {
        lock.readLock().lock();
        try {
            Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                session.setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        lock.writeLock().lock();
        try {
            Product product = productDao.getProduct(productId);
            CartItem cartItem = new CartItem(product, quantity);
            if (isStockNotAvailable(product, quantity)) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            if (cart.getItems().contains(cartItem)) {
                int index = cart.getItems().indexOf(cartItem);
                int oldQuantity = cart.getItems().get(index).getQuantity();
                int newQuantity = oldQuantity + quantity;
                if (isStockNotAvailable(product, newQuantity)) {
                    throw new OutOfStockException(product, newQuantity, product.getStock() - oldQuantity);
                }
                cart.getItems().get(index).setQuantity(newQuantity);
                return;
            }
            cart.getItems().add(cartItem);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean isStockNotAvailable(Product product, int newQuantity) {
        return product.getStock() < newQuantity;
    }
}
