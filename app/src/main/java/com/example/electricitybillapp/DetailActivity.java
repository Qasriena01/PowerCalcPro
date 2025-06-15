package com.example.electricitybillapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bill bill = (Bill) getIntent().getSerializableExtra("bill");

        ((TextView)findViewById(R.id.textMonth)).setText("Month: " + bill.getMonth());
        ((TextView)findViewById(R.id.textUnits)).setText("Units: " + bill.getUnit());
        ((TextView)findViewById(R.id.textTotal)).setText("Total: RM " + bill.getTotalCharges());
        ((TextView)findViewById(R.id.textRebate)).setText("Rebate: " + (bill.getRebate()*100) + "%");
        ((TextView)findViewById(R.id.textFinal)).setText("Final Cost: RM " + bill.getFinalCost());
    }
}

