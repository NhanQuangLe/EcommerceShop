package com.example.ecommerceshop.qui.cart;

public interface IClickProductCartItemListener {
    public void sendParentAdapter(boolean b,ProductCart productCart);
    public void addListSelectedItem(ProductCart productCart);
    public void removeListSelectedItem(ProductCart productCart);

    public void checkAllCheckbox();
    public void sendInfoProduct(ProductCart productCart);
    public void showProductDetail(ProductCart productCart);
}
