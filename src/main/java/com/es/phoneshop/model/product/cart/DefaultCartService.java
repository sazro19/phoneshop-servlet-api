package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.cart.exception.OutOfStockException;

public class DefaultCartService implements CartService {
    private Cart cart = new Cart();

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
    public Cart getCart() {
        return cart;
    }

    @Override
    public void add(Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        CartItem cartItem = new CartItem(product, quantity);
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
        if (isStockNotAvailable(product, quantity)) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        cart.getItems().add(cartItem);
    }

    private boolean isStockNotAvailable(Product product, int newQuantity) {
        return product.getStock() < newQuantity;
    }
}
