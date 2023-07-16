package com.example.ecommerceshop.qui.payment;

import android.content.Context;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ecommerceshop.nhan.Model.Address;
import com.example.ecommerceshop.qui.cart.ProductCart;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class ItemPayment {
    public static long TienVanChuyen = 0;
    private String shopId;
    private String shopName;

    public String getShopProvince() {
        return shopProvince;
    }

    public void setShopProvince(String shopProvince) {
        this.shopProvince = shopProvince;
    }

    private String shopProvince;
    private List<ProductCart> listProductCart;
    private long tongTienHang;
    private long tienKhuyenMai;
    private long tongThanhToan;
    private long tienVanChuyen;
    private Voucher voucher;
    public long priceUnitDistance;

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;


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
        return getTongTienHang() - getTienKhuyenMai() + getTienVanChuyen();
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

    public long getTienVanChuyen() {
        long tien = 0;
        if (address!=null){
            List<android.location.Address> addresses = null;
            if (shopProvince != null) {
                Geocoder geocoder = new Geocoder(mContext);
                try {
                    addresses = geocoder.getFromLocationName(shopProvince, 1);
                    if (addresses != null) {
                        android.location.Address addressTemp = addresses.get(0);
                        LatLng shop = new LatLng(addressTemp.getLatitude(), addressTemp.getLongitude());
                        LatLng user = new LatLng(address.getLatitude(),address.getLongitude());
                        double distance = SphericalUtil.computeDistanceBetween(shop, user);
                        tien = (long) (distance/1000 * priceUnitDistance);

                    }
                } catch (Exception e) {
                    tien = 30000;
                }
            }
        }
        return tien;
    }




}
