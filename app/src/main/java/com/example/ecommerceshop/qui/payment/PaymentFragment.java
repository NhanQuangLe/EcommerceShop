//package com.example.ecommerceshop.qui.payment;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.ecommerceshop.R;
//import com.example.ecommerceshop.databinding.FragmentPaymentBinding;
//import com.example.ecommerceshop.qui.cart.ProductCart;
//import com.example.ecommerceshop.qui.cart.ShopProductCart;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class PaymentFragment extends Fragment {
//    public static final String TAG = PaymentFragment.class.getName();
//    private FragmentPaymentBinding mFragmentPaymentBinding;
//    private View mView;
//    private ArrayList<ProductCart> listSelectedCart;
//    private ItemPaymentAdapter itemPaymentAdapter;
//    private List<ItemPayment> mListItemPayment;
//    private ISenData iSenData;
//
//    private interface DataCallBack {
//        void onDataChanged(ItemPaymentAdapter itemPaymentAdapter, List<ItemPayment> mListItemPayment);
//    }
//
//    private DataCallBack dataCallBack;
//
//    public void setDataCallBack(DataCallBack dataCallBack) {
//        this.dataCallBack = dataCallBack;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        mFragmentPaymentBinding = FragmentPaymentBinding.inflate(inflater, container, false);
//        mView = mFragmentPaymentBinding.getRoot();
//
//        init();
//        iListener();
//
//        return mView;
//    }
//
//    private void init() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//        mFragmentPaymentBinding.rcvItemPayment.setLayoutManager(linearLayoutManager);
//        listSelectedCart = getArguments().getParcelableArrayList("listSelectedCart");
//        itemPaymentAdapter = new ItemPaymentAdapter(getContext());
//        itemPaymentAdapter.setiSendData(new ISenData() {
//            @Override
//            public void senDataToAdapter(List<Voucher> vouchers) {
//
//            }
//
//            @Override
//            public void senDataToPaymentFragment(List<ItemPayment> vouchers) {
//                long tienKhuyenMai = 0;
//                for (Voucher voucher : vouchers) {
//                    tienKhuyenMai += voucher.getDiscountPrice();
//                }
//
//                Log.e("Tha", tienKhuyenMai+"");
//                mFragmentPaymentBinding.tv1.setText(tienKhuyenMai+"");
//                Log.e("Tha", mFragmentPaymentBinding.tv1.getText().toString()+"");
//            }
//        });
//        mListItemPayment = new ArrayList<>();
//        itemPaymentAdapter.setData(mListItemPayment);
//        mFragmentPaymentBinding.rcvItemPayment.setAdapter(itemPaymentAdapter);
//        ArrayList<String> listShopId = new ArrayList<>();
//        ArrayList<String> listShopName = new ArrayList<>();
//
//        for (ProductCart productCart : listSelectedCart) {
//            if (!listShopId.contains(productCart.getShopId())) {
//                listShopId.add(productCart.getShopId());
//                listShopName.add(productCart.getShopName());
//            }
//        }
//        for (int i = 0; i < listShopId.size(); i++) {
//            String shopId = listShopId.get(i);
//            ItemPayment itemPayment = new ItemPayment();
//            itemPayment.setShopId(shopId);
//            itemPayment.setListProductCart(new ArrayList<>());
//            itemPayment.setShopName(listShopName.get(i));
//            for (ProductCart productCart : listSelectedCart) {
//                if (productCart.getShopId().equals(shopId)) {
//                    itemPayment.getListProductCart().add(productCart);
//                }
//            }
//            mListItemPayment.add(itemPayment);
//        }
//
//        itemPaymentAdapter.notifyDataSetChanged();
//    }
//
//    private void iListener() {
//        mFragmentPaymentBinding.btnBackward.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().finish();
//            }
//        });
//    }
//
//
//}