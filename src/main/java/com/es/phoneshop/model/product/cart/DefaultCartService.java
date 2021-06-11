package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.lock.SessionLock;

import javax.servlet.http.HttpSession;
import java.util.Optional;
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
        Object sessionLock = SessionLock.getSessionLock(session, null);
        synchronized (sessionLock) {
            Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                session.setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        lock.writeLock().lock();
        try {
            Product product = productDao.getProduct(productId);
            if (isStockNotAvailable(product, quantity)) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            Optional<CartItem> containedCartItem = cart.getItems().stream()
                    .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                    .findFirst();
            if (containedCartItem.isPresent()) {
                int oldQuantity = containedCartItem.get().getQuantity();
                int newQuantity = oldQuantity + quantity;
                if (isStockNotAvailable(product, newQuantity)) {
                    throw new OutOfStockException(product, newQuantity, product.getStock() - oldQuantity);
                }
                containedCartItem.get().setQuantity(newQuantity);
                return;
            }
            CartItem cartItem = new CartItem(product, quantity);
            cart.getItems().add(cartItem);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean isStockNotAvailable(Product product, int newQuantity) {
        return product.getStock() < newQuantity;
    }
}
