package com.example.budgetmanagementapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private String selectedCategory = "Others"; // Default category
    private LinearLayout barChartContainer;
    private EditText amountEditText;
    private Map<String, Double> categoryAmounts = new HashMap<>();
    private double totalSpent = 0.0;
    private Button clearTransactionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        categorySpinner = findViewById(R.id.categorySpinner);
        barChartContainer = findViewById(R.id.barChartContainer);
        amountEditText = findViewById(R.id.amountEditText);
        clearTransactionsButton = findViewById(R.id.clearTransactionsButton);

        // Set up category selection listener
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set up Add Transaction button
        Button addTransactionButton = findViewById(R.id.addTransactionButton);
        addTransactionButton.setOnClickListener(this::addTransactionClicked);

        // Show Clear Transactions button when there are transactions
        clearTransactionsButton.setVisibility(View.GONE); // Initially hidden
        clearTransactionsButton.setOnClickListener(v -> clearTransactions());
    }

    public void addTransactionClicked(View view) {
        String amountText = amountEditText.getText().toString();

        if (!amountText.isEmpty()) {
            double amount = Double.parseDouble(amountText);
            addTransaction(amount);
            amountEditText.setText("");  // Clear input after adding transaction
        } else {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
        }
    }

    private void addTransaction(double amount) {
        // Update the amount for the selected category
        categoryAmounts.put(selectedCategory, categoryAmounts.getOrDefault(selectedCategory, 0.0) + amount);

        // Update the total spent amount
        totalSpent += amount;

        // Update the total balance on the screen
        TextView totalBalanceTextView = findViewById(R.id.totalBalanceTextView);
        totalBalanceTextView.setText("Total Spent: ₹" + totalSpent);

        // Create or update the bar dynamically
        updateBar(selectedCategory, amount);

        // Show Clear Transactions button when there's spending
        clearTransactionsButton.setVisibility(View.VISIBLE);
    }

    private void updateBar(String category, double amount) {
        // Find or create the existing bar for the category
        LinearLayout barContainer = findBarForCategory(category);

        // If the bar does not exist, create a new one
        if (barContainer == null) {
            barContainer = createNewBar(category);
        }

        // Update the bar height with the new amount, scaling dynamically
        TextView bar = (TextView) barContainer.getChildAt(0); // We assume only one child TextView (the bar) per category
        double newAmount = categoryAmounts.get(category);

        // Calculate the height of the bar based on the amount
        int barHeight = (int) (newAmount * 5); // Adjust the multiplier to scale the bar
        barHeight = Math.min(barHeight, 600); // Max height to 600px to avoid overflow

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bar.getLayoutParams();
        layoutParams.height = barHeight; // Set new height for the bar
        bar.setLayoutParams(layoutParams);

        // Update the text on the bar (category + amount)
        bar.setText(category + "\n₹" + newAmount);
    }

    private LinearLayout findBarForCategory(String category) {
        for (int i = 0; i < barChartContainer.getChildCount(); i++) {
            LinearLayout barContainer = (LinearLayout) barChartContainer.getChildAt(i);
            TextView bar = (TextView) barContainer.getChildAt(0);
            if (bar.getText().toString().contains(category)) {
                return barContainer;
            }
        }
        return null;
    }

    private LinearLayout createNewBar(String category) {
        // Create a new LinearLayout to hold the bar
        LinearLayout barContainer = new LinearLayout(this);
        barContainer.setOrientation(LinearLayout.VERTICAL);
        barContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));

        // Create a TextView to represent the bar
        TextView bar = new TextView(this);
        bar.setText(category + "\n₹0");
        bar.setBackgroundColor(getBarColorForCategory(category));
        bar.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        bar.setPadding(10, 10, 10, 10);
        barContainer.addView(bar);

        // Add the new bar container to the layout
        barChartContainer.addView(barContainer);
        return barContainer;
    }

    private int getBarColorForCategory(String category) {
        switch (category) {
            case "Food":
                return Color.parseColor("#FFEB3B");
            case "Entertainment":
                return Color.parseColor("#4CAF50");
            case "Bills":
                return Color.parseColor("#2196F3");
            case "Travel":
                return Color.parseColor("#9C27B0");
            default:
                return Color.parseColor("#FF5722"); // Default color for "Others"
        }
    }

    private void clearTransactions() {
        // Clear all transaction data
        categoryAmounts.clear();
        totalSpent = 0.0;

        // Reset the total balance display
        TextView totalBalanceTextView = findViewById(R.id.totalBalanceTextView);
        totalBalanceTextView.setText("Total Spent: ₹0");

        // Clear the bar chart
        barChartContainer.removeAllViews();

        // Hide the Clear Transactions button
        clearTransactionsButton.setVisibility(View.GONE);
    }
}
