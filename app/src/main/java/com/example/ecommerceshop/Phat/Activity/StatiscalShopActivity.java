package com.example.ecommerceshop.Phat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.Phat.Adapter.AdapterTopProducts;
import com.example.ecommerceshop.Phat.Adapter.SpinnerAdapter;
import com.example.ecommerceshop.Phat.Model.OrderItem;
import com.example.ecommerceshop.Phat.Model.OrderShop;
import com.example.ecommerceshop.Phat.Model.RevenueYear;
import com.example.ecommerceshop.Phat.Model.SpinnerItem;
import com.example.ecommerceshop.Phat.Model.TopProduct;
import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.toast.CustomToast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatiscalShopActivity extends AppCompatActivity {

    TextView totalQuantity,completedQuantity,cancelledQuantity,monthRevenue,YearRevenue;

    RecyclerView top5Bestseller;
    FirebaseAuth firebaseAuth;
    ImageView btnback;
    LineChart lineChart;
    Spinner spinnerOrdMonth,spinnerOrdYear;
    PieChart pieChart;
    FrameLayout overlay;
    List<SpinnerItem> itemsMonth, itemsYear;
    int selectedMonth, selectedYear;
    boolean firstLoad= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_statiscal_shop);
        initUi();
        loadData();

        spinnerOrdMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedMonth = position;
                loadData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        spinnerOrdYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedYear = Integer.parseInt(((SpinnerItem)(adapterView.getItemAtPosition(position))).getItemName().toString());
                loadData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
    }

    private void loadDetailDataByDate(int month, int year, ArrayList<OrderShop> orderShops) {
        ArrayList<TopProduct> topProducts = new ArrayList<>();
        topProducts.clear();
        int completed=0, cancelled=0;
        long laptopRe=0, smartphoneRe=0, pkRe=0, discount = 0 ;
        float t1=0, t2=0,t3=0,t4=0,t5=0,t6=0,t7=0,t8=0,t9=0,t10=0,t11=0,t12=0;
        for(OrderShop orderShop : orderShops){
            if(month==0){
                if(Integer.parseInt(getYearFromDate(orderShop.getOrderedDate())) == year){
                    if(orderShop.getOrderStatus().equals("Completed")) {
                        completed++;
                        for(OrderItem orderItem: orderShop.getItems()){
                            if(orderItem.getpCategory().equals("Smartphone")) smartphoneRe+= orderItem.getpPrice()*orderItem.getpQuantity();
                            if(orderItem.getpCategory().equals("Laptop")) laptopRe+= orderItem.getpPrice()*orderItem.getpQuantity();
                            if(orderItem.getpCategory().equals("Accessory")) pkRe+= orderItem.getpPrice()*orderItem.getpQuantity();
                            topProducts.add(new TopProduct(orderItem.getPid(),orderItem.getpName(), orderItem.getpAvatar(),orderItem.getpPrice(), orderItem.getpQuantity()));
                        }
                        discount+= orderShop.getDiscountPrice()- orderShop.getShipPrice();
                    }
                    if(orderShop.equals("Cancelled")) cancelled++;
                }
            }
            else {
                if(Integer.parseInt(getYearFromDate(orderShop.getOrderedDate())) == year && Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate()))==month){
                    if(orderShop.getOrderStatus().equals("Completed")) {
                        completed++;
                        for(OrderItem orderItem: orderShop.getItems()){
                            if(orderItem.getpCategory().equals("Smartphone")) smartphoneRe+= orderItem.getpPrice()*orderItem.getpQuantity();
                            if(orderItem.getpCategory().equals("Laptop")) laptopRe+= orderItem.getpPrice()*orderItem.getpQuantity();
                            if(orderItem.getpCategory().equals("Accessory")) pkRe+= orderItem.getpPrice()*orderItem.getpQuantity();
                            topProducts.add(new TopProduct(orderItem.getPid(),orderItem.getpName(), orderItem.getpAvatar(),orderItem.getpPrice(), orderItem.getpQuantity()));
                        }
                        discount+= orderShop.getDiscountPrice() - orderShop.getShipPrice();
                    }
                    if(orderShop.equals("Cancelled")) cancelled++;
                }
            }
            if(orderShop.getOrderStatus().equals("Completed") && Integer.parseInt(getYearFromDate(orderShop.getOrderedDate())) == year) {
                if(Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate())) ==1) t1+=orderShop.getTotalPrice();
                if(Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate())) ==2) t2+=orderShop.getTotalPrice();
                if(Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate())) ==3) t3+=orderShop.getTotalPrice();
                if(Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate())) ==4) t4+=orderShop.getTotalPrice();
                if(Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate())) ==5) t5+=orderShop.getTotalPrice();
                if(Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate())) ==6) t6+=orderShop.getTotalPrice();
                if(Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate())) ==7) t7+=orderShop.getTotalPrice();
                if(Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate())) ==8) t8+=orderShop.getTotalPrice();
                if(Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate())) ==9) t9+=orderShop.getTotalPrice();
                if(Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate())) ==10) t10+=orderShop.getTotalPrice();
                if(Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate())) ==11) t11+=orderShop.getTotalPrice();
                if(Integer.parseInt(getMonthFromDate(orderShop.getOrderedDate())) ==12) t12+=orderShop.getTotalPrice();
            }
        }
        Map<String , Integer> uniqueArr = new HashMap<>();
        uniqueArr.clear();
        for(TopProduct topProduct : topProducts){
            uniqueArr.put(topProduct.getId(), uniqueArr.getOrDefault(topProduct.getId(), 0)+ topProduct.getQuan());
        }
        ArrayList<TopProduct> ListTopProduct=new ArrayList<>();
        ListTopProduct.clear();
        for (Map.Entry<String, Integer> entry : uniqueArr.entrySet()){
            for(TopProduct topProduct : topProducts){
                if(entry.getKey().equals(topProduct.getId())){
                    ListTopProduct.add(new TopProduct(entry.getKey(), topProduct.getName(), topProduct.getAvatar(),topProduct.getPrice(), entry.getValue()));
                    break;
                }
            }
        }
        Collections.sort(ListTopProduct, new Comparator<TopProduct>() {
            @Override
            public int compare(TopProduct t1, TopProduct t2) {
                return Integer.compare(t2.getQuan(), t1.getQuan());
            }
        });
        ArrayList<TopProduct> top5 =  new ArrayList<>();
        top5.clear();
        if(ListTopProduct.size()> 5){
            for(int i =0; i<5; i++){
                top5.add(ListTopProduct.get(i));
            }
        }
        else top5= new ArrayList<>(ListTopProduct);

        AdapterTopProducts adapterTopProducts = new AdapterTopProducts(getApplicationContext(), top5);
        top5Bestseller.setAdapter(adapterTopProducts);
        long total =0;
        total = (long) (t1+t2+t3+t4+t5+t6+t7+t8+t9+t10+t11+t12);
        t1/= 1_000_000;
        t2/= 1_000_000;
        t3/= 1_000_000;
        t4/= 1_000_000;
        t5/= 1_000_000;
        t6/= 1_000_000;
        t7/= 1_000_000;
        t8/= 1_000_000;
        t9/= 1_000_000;
        t10/= 1_000_000;
        t11/= 1_000_000;
        t12/= 1_000_000;

        loadDTNam(new RevenueYear(t1,t2,t3,t5,t4,t6,t7,t8,t9,t10,t11,t12), total);
        LoadOrderQuantity(completed, cancelled);
        loadPieChart(laptopRe, smartphoneRe, pkRe, discount);
    }

    private void loadPieChart(long lap, long smartphone, long pk, long discount) {
        float lapPer=0, smartphonePer=0, pkPer=0;
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        long total = lap+smartphone+pk-discount;
        if(total !=0){
            overlay.setVisibility(View.GONE);
            lapPer= (float)(lap/(lap+smartphone+pk)*100);
            smartphonePer = (float)(smartphone/(lap+smartphone+pk)*100);
            pkPer = (float)(pk/(lap+smartphone+pk)*100);
        }
        else {
            overlay.setVisibility(View.VISIBLE);
        }

        monthRevenue.setText(Constants.convertToVND(total));

        List<PieEntry> entries = new ArrayList<>();
        if(lap!=0)  entries.add(new PieEntry(Float.parseFloat(decimalFormat.format(lapPer)), "Laptop"));
        if(smartphone!=0)  entries.add(new PieEntry(Float.parseFloat(decimalFormat.format(smartphonePer)), "Smartphone"));
        if(pk!=0)  entries.add(new PieEntry(Float.parseFloat(decimalFormat.format(pkPer)), "Phụ kiện"));
        PieDataSet dataSet = new PieDataSet(entries, "Tỷ lệ doanh thu");
        dataSet.setColors(getColor(R.color.primary_yellow), getColor(R.color.green1), Color.RED);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setDrawValues(true);
        dataSet.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Chuyển đổi giá trị float của tháng sang định dạng chuỗi

                return value+"% ~ "+ Constants.convertToVND((long)(value*total)/100) ;
            }
        });
        PieData pieData = new PieData(dataSet);
        Legend legend = pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pieChart.setData(pieData);
        Description description = new Description();
        description.setText("Biểu đồ tỷ lệ doanh thu");
        pieChart.setDescription(description);
        pieChart.getDescription().setTextAlign( Paint.Align.CENTER);

        pieChart.setExtraRightOffset(40);
        pieChart.setEntryLabelTextSize(10f);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setDrawHoleEnabled(false);
        pieChart.invalidate();
    }
    private void loadDTNam(RevenueYear revenueYear, long total) {
        YearRevenue.setText(Constants.convertToVND(total));
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, revenueYear.getT1()));
        entries.add(new Entry(2, revenueYear.getT2()));
        entries.add(new Entry(3, revenueYear.getT3()));
        entries.add(new Entry(4, revenueYear.getT4()));
        entries.add(new Entry(5, revenueYear.getT5()));
        entries.add(new Entry(6, revenueYear.getT6()));
        entries.add(new Entry(7, revenueYear.getT7()));
        entries.add(new Entry(8, revenueYear.getT8()));
        entries.add(new Entry(9, revenueYear.getT9()));
        entries.add(new Entry(10, revenueYear.getT10()));
        entries.add(new Entry(11, revenueYear.getT11()));
        entries.add(new Entry(12, revenueYear.getT12()));
        LineDataSet dataSet = new LineDataSet(entries, "Doanh thu");
        dataSet.setColor(getColor(R.color.green1));
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(9f);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setText("Biểu đồ doanh thu theo năm");
        lineChart.getXAxis().setLabelCount(entries.size(), true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Chuyển đổi giá trị float của tháng sang định dạng chuỗi
                int month = (int) value;
                return "t " + month;
            }
        });
        lineChart.getAxisLeft().setValueFormatter(new ValueFormatter(){
            @Override
            public String getFormattedValue(float value) {
                // Chuyển đổi giá trị float sang định dạng chuỗi và thêm "triệu đ" vào cuối
                return value  + " triệu đ";
            }
        });
        lineChart.getAxisRight().setEnabled(false);
        lineChart.invalidate();
    }

    private void loadData() {
        ArrayList<OrderShop> orderShops = new ArrayList<>();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderShops.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String uid = ""+ds.getRef().getKey();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(uid).child("Customer").child("Orders").orderByChild("shopId")
                            .equalTo(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        ArrayList<String> years= new ArrayList<>();
                                        for (DataSnapshot ds: snapshot.getChildren()){
                                            OrderShop orderShop = ds.getValue(OrderShop.class);
                                            years.add(getYearFromDate(orderShop.getOrderedDate()));
                                            orderShops.add(orderShop);
                                        }
                                        if(firstLoad){
                                            firstLoad=false;
                                            Map<String, Integer> uniqueStrings = filterAndCountDuplicates(years);
                                            itemsYear=new ArrayList<>();
                                            itemsYear.clear();
                                            for (Map.Entry<String, Integer> entry : uniqueStrings.entrySet()){
                                                itemsYear.add(new SpinnerItem(entry.getKey()));
                                            }
                                            Collections.sort(itemsYear, new Comparator<SpinnerItem>() {
                                                @Override
                                                public int compare(SpinnerItem o1, SpinnerItem o2) {
                                                    return Integer.compare(Integer.parseInt(o2.getItemName()), Integer.parseInt(o1.getItemName()));
                                                }
                                            });
                                            SpinnerAdapter adapter2 = new SpinnerAdapter(getApplicationContext(), itemsYear);
                                            spinnerOrdYear.setAdapter(adapter2);
                                            spinnerOrdYear.setSelection(0);
                                        }

                                        loadDetailDataByDate(selectedMonth, selectedYear, orderShops);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    CustomToast.makeText(getApplicationContext(),error.getMessage()+"",CustomToast.SHORT,CustomToast.ERROR).show();

                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                CustomToast.makeText(getApplicationContext(),error.getMessage()+"",CustomToast.SHORT,CustomToast.ERROR).show();

            }
        });
    }

    private void LoadOrderQuantity(int completed, int cancelled) {
        completedQuantity.setText(String.valueOf(completed));
        cancelledQuantity.setText(String.valueOf(cancelled));
        totalQuantity.setText(String.valueOf(completed+cancelled));
    }

    public static String getMonthFromDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM");
        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Map<String, Integer> filterAndCountDuplicates(ArrayList<String> strings) {
        Map<String, Integer> uniqueStrings = new HashMap<>();
        for (String str : strings) {
            uniqueStrings.put(str, uniqueStrings.getOrDefault(str, 0) + 1);
        }
        return uniqueStrings;
    }
    public static String getYearFromDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy");
        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void initUi() {
        totalQuantity=findViewById(R.id.totalQuantity);
        btnback=findViewById(R.id.btnback);
        completedQuantity=findViewById(R.id.completedQuantity);
        cancelledQuantity=findViewById(R.id.cancelledQuantity);
        firebaseAuth=FirebaseAuth.getInstance();
        lineChart=findViewById(R.id.lineChart);
        spinnerOrdMonth=findViewById(R.id.spinnerOrdMonth);
        pieChart=findViewById(R.id.pieChart);
        monthRevenue=findViewById(R.id.monthRevenue);
        YearRevenue=findViewById(R.id.YearRevenue);
        top5Bestseller=findViewById(R.id.top5Bestseller);
        spinnerOrdYear=findViewById(R.id.spinnerOrdYear);
        overlay=findViewById(R.id.overlay);
        itemsMonth = new ArrayList<>();
        itemsMonth.add(new SpinnerItem("All"));
        itemsMonth.add(new SpinnerItem("January"));
        itemsMonth.add(new SpinnerItem("February"));
        itemsMonth.add(new SpinnerItem("March"));
        itemsMonth.add(new SpinnerItem("April"));
        itemsMonth.add(new SpinnerItem("May"));
        itemsMonth.add(new SpinnerItem("June"));
        itemsMonth.add(new SpinnerItem("July"));
        itemsMonth.add(new SpinnerItem("August"));
        itemsMonth.add(new SpinnerItem("September"));
        itemsMonth.add(new SpinnerItem("October"));
        itemsMonth.add(new SpinnerItem("November"));
        itemsMonth.add(new SpinnerItem("December"));

        SpinnerAdapter adapter1 = new SpinnerAdapter(getApplicationContext(), itemsMonth);
        spinnerOrdMonth.setAdapter(adapter1);
        spinnerOrdMonth.setSelection(0);
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);

    }
}