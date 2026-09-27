package com.example.bmicalculate;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText editWeight, editHeight;
    private TextView tvBmiValue, tvResult;
    private Button btnCalculate;
    private DecimalFormat formatter = new DecimalFormat("#,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editWeight = findViewById(R.id.editWeight);
        editHeight = findViewById(R.id.editHeight);
        tvBmiValue = findViewById(R.id.tvBmiValue);
        tvResult = findViewById(R.id.tvResult);
        btnCalculate = findViewById(R.id.btnCalculate);

        editWeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        editHeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });
    }

    private void calculateBMI() {
        String weightStr = editWeight.getText().toString();
        String heightStr = editHeight.getText().toString();

        if (weightStr.isEmpty() || heightStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            double heightCm = Double.parseDouble(heightStr);

            if (heightCm <= 0) {
                Toast.makeText(this, getString(R.string.error_height_zero), Toast.LENGTH_SHORT).show();
                return;
            }

            double heightM = heightCm / 100.0;
            double bmi = weight / (heightM * heightM);

            String formattedBmiStr = formatter.format(bmi);
            tvBmiValue.setText(formattedBmiStr);

            double roundedBmi = Double.parseDouble(formattedBmiStr.replace(",", ""));

            if (roundedBmi < 18.5) {
                tvResult.setText(getString(R.string.result_underweight));
            } else if (roundedBmi < 25) {
                tvResult.setText(getString(R.string.result_normal));
            } else if (roundedBmi < 30) {
                tvResult.setText(getString(R.string.result_overweight));
            } else {
                tvResult.setText(getString(R.string.result_obese));
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.error_invalid_number), Toast.LENGTH_SHORT).show();
        }
    }
}

class DecimalDigitsInputFilter implements InputFilter {
    private Pattern mPattern;

    DecimalDigitsInputFilter(int digits, int digitsAfterZero) {
        mPattern = Pattern.compile("[0-9]{0," + (digits - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = mPattern.matcher(dest);
        if (!matcher.matches())
            return "";
        return null;
    }
}