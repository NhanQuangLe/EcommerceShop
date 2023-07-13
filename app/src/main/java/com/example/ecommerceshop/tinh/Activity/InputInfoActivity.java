package com.example.ecommerceshop.tinh.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ecommerceshop.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InputInfoActivity extends AppCompatActivity {

    private RoundedImageView avatar;
    private FrameLayout layoutImage;
    private TextView textAddImage, textError, textErrorGender, textErrorBirthdate, textErrorImage;
    private EditText edittextName, edittextBirthdate;
    private ImageView buttonShowDatePicker;
    private CheckBox cbNam, cbNu;
    private Button buttonStart;
    private ProgressBar progressBar;
    private String encodedImage;
    final Calendar myCalendar= Calendar.getInstance();
    
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);
        InitUI();
        buttonStart.setOnClickListener(v -> {
            Boolean checkName = IsValidName();
            Boolean checkImage = IsValidImage();
            Boolean checkDate = IsValidDate();
            Boolean checkCheckBox = IsValidGender();
            if (checkName && checkDate && checkCheckBox && checkImage)
            {
                Started();
            }
        });

        cbNam.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbNam.isChecked())
            {
                cbNam.setChecked(true);
                cbNu.setChecked(false);
                textErrorGender.setVisibility(View.INVISIBLE);
            }
            else
            {
                cbNam.setChecked(false);
            }
        });

        cbNu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbNu.isChecked())
            {
                cbNu.setChecked(true);
                cbNam.setChecked(false);
                textErrorGender.setVisibility(View.INVISIBLE);
            }
            else
            {
                cbNu.setChecked(false);
            }
        });

        edittextName.setOnClickListener(v -> {
            if (!edittextName.getText().toString().trim().isEmpty())
            {
                edittextName.setBackgroundResource(R.drawable.background_input);
                textError.setVisibility(View.INVISIBLE);
            }
            else
            {
                edittextName.setBackgroundResource(R.drawable.background_input_error);
                textError.setText("Vui lòng nhập họ tên để bắt đầu!");
                textError.setTextColor(Color.parseColor("#E10000"));
                textError.setVisibility(View.VISIBLE);
            }
        });
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();
        };
        buttonShowDatePicker.setOnClickListener(v -> new DatePickerDialog(InputInfoActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        layoutImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private String encodeImage(Bitmap bitmap)
    {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK)
                {
                    if(result.getData() != null)
                    {
                        Uri imageUri = result.getData().getData();
                        try
                        {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            avatar.setImageBitmap(bitmap);
                            textAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        }
                        catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void updateLabel() {
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        edittextBirthdate.setText(dateFormat.format(myCalendar.getTime()));
        if (edittextBirthdate.getText().toString().trim().isEmpty())
        {
            edittextBirthdate.setBackgroundResource(R.drawable.background_input_error);
            textErrorBirthdate.setText("Vui lòng chọn ngày tháng năm sinh để bắt đầu!");
            textErrorBirthdate.setTextColor(Color.parseColor("#E10000"));
            textErrorBirthdate.setVisibility(View.VISIBLE);
        }
        else
        {
            SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
            Date birthdate = new Date();
            try {
                birthdate = date_format.parse(edittextBirthdate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date now = new Date();

            if (birthdate.compareTo(now) >= 0)
            {
                edittextBirthdate.setBackgroundResource(R.drawable.background_input_error);
                textErrorBirthdate.setText("Ngày sinh không hợp lệ! Vui lòng chọn ngày sinh trước ngày hiện tại!");
                textErrorBirthdate.setTextColor(Color.parseColor("#E10000"));
                textErrorBirthdate.setVisibility(View.VISIBLE);
            }
            else {
                edittextBirthdate.setBackgroundResource(R.drawable.background_input);
                textErrorBirthdate.setVisibility(View.INVISIBLE);
            }
        }
    }

    private Boolean IsValidGender() {

        if (!cbNam.isChecked() &&!cbNu.isChecked())
        {
            textErrorGender.setText("Vui lòng chọn giới tính để bắt đầu!");
            textErrorGender.setTextColor(Color.parseColor("#E10000"));
            textErrorGender.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            textErrorGender.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Boolean IsValidDate() {
        if (edittextBirthdate.getText().toString().trim().isEmpty())
        {
            edittextBirthdate.setBackgroundResource(R.drawable.background_input_error);
            textErrorBirthdate.setText("Vui lòng chọn ngày sinh để bắt đầu!");
            textErrorBirthdate.setTextColor(Color.parseColor("#E10000"));
            textErrorBirthdate.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date birthdate = new Date();
            try {
                birthdate = dateFormat.parse(edittextBirthdate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date now = new Date();

            if (birthdate.compareTo(now) >= 0)
            {
                edittextBirthdate.setBackgroundResource(R.drawable.background_input_error);
                textErrorBirthdate.setText("Ngày sinh không hợp lệ! Vui lòng chọn ngày sinh trước ngày hiện tại!");
                textErrorBirthdate.setTextColor(Color.parseColor("#E10000"));
                textErrorBirthdate.setVisibility(View.VISIBLE);
                return false;
            }
            else {
                edittextBirthdate.setBackgroundResource(R.drawable.background_input);
                textErrorBirthdate.setVisibility(View.INVISIBLE);
                return true;
            }
        }
    }
    private Boolean IsValidImage() {
        if (encodedImage == null)
        {
            textErrorImage.setText("Vui lòng chọn ảnh đại diện!");
            textErrorImage.setTextColor(Color.parseColor("#E10000"));
            textErrorImage.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            textErrorImage.setVisibility(View.INVISIBLE);
            return true;
        }
    }
    private Boolean IsValidName() {
        if (edittextName.getText().toString().trim().isEmpty())
        {
            edittextName.setBackgroundResource(R.drawable.background_input_error);
            textError.setText("Vui lòng nhập họ tên để bắt đầu");
            textError.setTextColor(Color.parseColor("#E10000"));
            textError.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            edittextName.setBackgroundResource(R.drawable.background_input);
            textError.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private void Started() {
        loading(true);

        // thực hiện lưu thông tin

        startActivity(new Intent(InputInfoActivity.this, LoginActivity.class));
        loading(false);
    }


    private void InitUI() {
        avatar = findViewById(R.id.imageProfile);
        textAddImage = findViewById(R.id.textAddImage);
        edittextBirthdate = findViewById(R.id.editTextBirthDate);
        edittextName = findViewById(R.id.editTextName);
        cbNam = findViewById(R.id.checkbox_nam);
        cbNu = findViewById(R.id.checkbox_nu);
        buttonStart = findViewById(R.id.buttonStart);
        progressBar = findViewById(R.id.progressBar);
        textError = findViewById(R.id.textErrorName);
        buttonShowDatePicker = findViewById(R.id.buttonShowDatePicker);
        textErrorGender = findViewById(R.id.textErrorGender);
        textErrorBirthdate = findViewById(R.id.textErrorBirthdate);
        layoutImage = findViewById(R.id.layoutImage);
        textErrorImage = findViewById(R.id.textErrorImage);
    }


    private void loading(Boolean isLoading)
    {
        if(isLoading)
        {
            buttonStart.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            buttonStart.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}