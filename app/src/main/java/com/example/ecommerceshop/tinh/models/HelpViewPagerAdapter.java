package com.example.ecommerceshop.tinh.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ecommerceshop.tinh.Fragment.FrequentlyAskedQuestions;
import com.example.ecommerceshop.tinh.Fragment.PrivacyPolicy;
import com.example.ecommerceshop.tinh.Fragment.PromotionsAndOffers;
import com.example.ecommerceshop.tinh.Fragment.ReturnAndRefundPolicy;
import com.example.ecommerceshop.tinh.Fragment.ShippingAndPaymentPolicy;
import com.example.ecommerceshop.tinh.Fragment.TermsOfService;

public class HelpViewPagerAdapter extends FragmentStatePagerAdapter {

    public HelpViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new TermsOfService();
            case 1:
                return new PrivacyPolicy();
            case 2:
                return new FrequentlyAskedQuestions();
            case 3:
                return new ShippingAndPaymentPolicy();
            case 4:
                return new ReturnAndRefundPolicy();
            default:
                return new PromotionsAndOffers();
        }
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch(position)
        {
            case 0:
                title = "Điều khoản dịch vụ";
                break;
            case 1:
                title = "Chính sách bảo mật";
                break;
            case 2:
                title = "Câu hỏi thường gặp";
                break;
            case 3:
                title = "Chính sách vận chuyển và Thanh toán";
                break;
            case 4:
                title = "Chính sách trả và Hoàn tiền";
                break;
            case 5:
                title = "Khuyến mãi và Ưu đãi";
                break;
        }
        return title;
    }
}
