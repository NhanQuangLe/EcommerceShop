package com.example.ecommerceshop.qui.payment;

import com.example.ecommerceshop.nhan.Model.Address;
import com.example.ecommerceshop.qui.cart.ProductCart;

import java.util.ArrayList;
import java.util.List;

public class ItemPayment {
    private String shopId;
    private String shopName;
    private List<ProductCart> listProductCart;
    private long tongTienHang;
    private long tienKhuyenMai;
    private long tongThanhToan;
    private long tienVanChuyen;
    private Voucher voucher;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    private Address address;

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public ItemPayment(String shopId, String shopName, List<ProductCart> listProductCart, long tongTienHang, long tienKhuyenMai, long tongThanhToan, long tienVanChuyen) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.listProductCart = listProductCart;
        this.tongTienHang = tongTienHang;
        this.tienKhuyenMai = tienKhuyenMai;
        this.tongThanhToan = tongThanhToan;
        this.tienVanChuyen = tienVanChuyen;
    }

    public ItemPayment() {

    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<ProductCart> getListProductCart() {
        return listProductCart;
    }

    public void setListProductCart(List<ProductCart> listProductCart) {
        this.listProductCart = listProductCart;
    }
    public long getTienKhuyenMai() {
        return tienKhuyenMai;
    }

    public void setTienKhuyenMai(long tienKhuyenMai) {
        this.tienKhuyenMai = tienKhuyenMai;
    }
    public long getTongThanhToan() {
        return tongThanhToan;
    }

    public void setTongThanhToan(long tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }
    public long getTongTienHang() {
        long tongTienHang = 0;
        for (ProductCart productCart : this.listProductCart) {
            if (productCart.getProductDiscountPrice() == 0)
                tongTienHang += productCart.getProductPrice() * productCart.getProductQuantity();
            else
                tongTienHang += (productCart.getProductPrice() - productCart.getProductDiscountPrice()) * productCart.getProductQuantity();
        }
        return tongTienHang;
    }

    public void setTongTienHang(long tongTienHang) {
        this.tongTienHang = tongTienHang;
    }
    public long getTienVanChuyen() {
        long tien = 0;
        if (address!=null){
            // Hàm tính tiền
        }
        return tien;
    }

    public void setTienVanChuyen(long tienVanChuyen) {
        this.tienVanChuyen = tienVanChuyen;
    }


}
