package com.example.electricitybillapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editUnits, editRebate;
    Spinner monthSpinner;
    ListView listView;
    TextView textResult;
    Button btnCalculate, btnDeleteAll;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Optional: Make full screen (hides status bar too if needed)
        // getWindow().setFlags(
        //         WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //         WindowManager.LayoutParams.FLAG_FULLSCREEN
        // );

        setContentView(R.layout.activity_main);

        // Initialize views
        monthSpinner = findViewById(R.id.spinnerMonth);
        editUnits = findViewById(R.id.edit_units);
        editRebate = findViewById(R.id.edit_rebate);
        listView = findViewById(R.id.listView);
        textResult = findViewById(R.id.text_result);
        btnCalculate = findViewById(R.id.buttonCalculate);
        btnDeleteAll = findViewById(R.id.buttonDeleteAll);

        db = new DatabaseHelper(this);

        // Populate month spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.months,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        // Calculate & Save Button
        btnCalculate.setOnClickListener(v -> calculateBill());

        // Delete All History Button
        btnDeleteAll.setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete all saved records?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        db.deleteAllBills();     // Delete records from DB
                        loadHistory();           // Refresh list view
                        Toast.makeText(MainActivity.this, "All records deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // Load history from DB
        loadHistory();

        // Bottom navigation
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true; // Already on home screen
            } else if (id == R.id.nav_about) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });

        // ListView click → open detail page
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Bill bill = (Bill) parent.getItemAtPosition(position);
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("bill", bill);
            startActivity(intent);
        });
    }

    private void calculateBill() {
        String month = monthSpinner.getSelectedItem().toString();
        String unitsStr = editUnits.getText().toString();
        String rebateStr = editRebate.getText().toString();

        if (unitsStr.isEmpty() || rebateStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int units = Integer.parseInt(unitsStr);

        double rebate;
        try {
            rebate = Double.parseDouble(rebateStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid rebate value", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rebate < 0 || rebate > 5) {
            Toast.makeText(this, "Rebate must be between 0% and 5%", Toast.LENGTH_SHORT).show();
            return;
        }

        rebate /= 100;
        double total = calculateCharges(units);
        double finalCost = total - (total * rebate);

        Bill bill = new Bill(month, units, total, rebate, finalCost);
        db.insertBill(bill);

        String result = String.format("Month: %s\nUnits: %d\nTotal: RM %.2f\nRebate: %.1f%%\nFinal Cost: RM %.2f",
                month, units, total, rebate * 100, finalCost);
        textResult.setText(result);

        loadHistory();
    }

    private double calculateCharges(int units) {
        double total = 0;
        if (units <= 200) {
            total = units * 0.218;
        } else if (units <= 300) {
            total = 200 * 0.218 + (units - 200) * 0.334;
        } else if (units <= 600) {
            total = 200 * 0.218 + 100 * 0.334 + (units - 300) * 0.516;
        } else {
            total = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (units - 600) * 0.546;
        }
        return total;
    }

    private void loadHistory() {
        List<Bill> bills = db.getAllBills();
        ArrayAdapter<Bill> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bills);
        listView.setAdapter(adapter);
    }
}
