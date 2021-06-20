package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.lock.SessionLock;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
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
            if (quantity <= 0) {
                throw new IllegalArgumentException("Invalid quantity");
            }
            Product product = productDao.getItem(productId);
            Optional<CartItem> containedCartItem = getCartItemByProductId(cart, productId);
            if (containedCartItem.isPresent()) {
                int oldQuantity = containedCartItem.get().getQuantity();
                if (isStockNotAvailable(product, quantity)) {
                    throw new OutOfStockException(product, quantity, product.getStock() - oldQuantity);
                }
                int newQuantity = oldQuantity + quantity;
                if (isStockNotAvailable(product, newQuantity)) {
                    throw new OutOfStockException(product, newQuantity, product.getStock() - oldQuantity);
                }
                containedCartItem.get().setQuantity(newQuantity);
                updateTotalQuantityAndPrice(cart);
                return;
            }
            if (isStockNotAvailable(product, quantity)) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            CartItem cartItem = new CartItem(product, quantity);
            cart.getItems().add(cartItem);
            updateTotalQuantityAndPrice(cart);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        lock.writeLock().lock();
        try {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Invalid quantity");
            }
            Product product = productDao.getItem(productId);
            if (isStockNotAvailable(product, quantity)) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            Optional<CartItem> containedCartItem = getCartItemByProductId(cart, productId);
            if (containedCartItem.isPresent()) {
                containedCartItem.get().setQuantity(quantity);
                updateTotalQuantityAndPrice(cart);
                return;
            }
            CartItem cartItem = new CartItem(product, quantity);
            cart.getItems().add(cartItem);
            updateTotalQuantityAndPrice(cart);
        } finally {
            updateTotalQuantityAndPrice(cart);
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        lock.writeLock().lock();
        try {
            cart.getItems().removeIf(cartItem -> productId.equals(cartItem.getProduct().getId()));
            updateTotalQuantityAndPrice(cart);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clearCart(Cart cart) {
        lock.writeLock().lock();
        try {
            cart.getItems().clear();
            cart.setTotalQuantity(0);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Optional<CartItem> getCartItemByProductId(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findAny();
    }

    private void updateTotalQuantityAndPrice(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum());
        cart.setTotalPrice(cart.getItems().stream()
                .map(cartItem -> cartItem.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private boolean isStockNotAvailable(Product product, int newQuantity) {
        return product.getStock() < newQuantity;
    }
}
