package com.zjh.myapplication6;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    DecimalFormat df = new DecimalFormat("0.00");

    TextView tvBalance;
    EditText etDate;
    EditText etAmount;
    EditText etCategory;
    Button btnAdd;
    Button btnSub;
    EditText fromDate;
    EditText toDate;
    EditText fromAmt;
    EditText toAmt;
    Button btnSearch;
    Button btnClear;
    TableLayout history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        tvBalance = (TextView) findViewById(R.id.tvBalance);
        etDate = (EditText) findViewById(R.id.etDate);
        etAmount = (EditText) findViewById(R.id.amount);
        etCategory = (EditText) findViewById(R.id.category);
        btnAdd = (Button) findViewById(R.id.add);
        btnSub = (Button) findViewById(R.id.minus);
        fromDate = (EditText) findViewById(R.id.fromDate);
        toDate = (EditText) findViewById(R.id.toDate);
        fromAmt = (EditText) findViewById(R.id.fromAmt);
        toAmt = (EditText) findViewById(R.id.toAmt);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnClear = (Button) findViewById(R.id.btnClear);
        history = (TableLayout) findViewById(R.id.tbHistory);

        DisplayAllData();
        AddTransactions();
        Filter();
        ClearFilter();

    }
    public void AddTransactions() {

        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double amt = Double.parseDouble(etAmount.getText().toString());

                        TransactionModel trans = new TransactionModel();

                        trans.mDate = etDate.getText().toString();
                        trans.mAmount = amt;
                        trans.mCategory = etCategory.getText().toString();


                        boolean isInserted = myDb.insertTransaction(trans);

                        if (isInserted) {
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Date not Inserted", Toast.LENGTH_LONG).show();
                        }
                        DisplayAllData();
                        ClearET();
                    }
                }
        );

        btnSub.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double amt = Double.parseDouble(etAmount.getText().toString());

                        if (amt > 0) {
                            amt *= -1;
                        }

                        TransactionModel trans = new TransactionModel();

                        trans.mDate = etDate.getText().toString();
                        trans.mAmount = amt;
                        trans.mCategory = etCategory.getText().toString();

                        boolean isInserted = myDb.insertTransaction(trans);

                        if (isInserted) {
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Date not Inserted", Toast.LENGTH_LONG).show();
                        }
                        DisplayAllData();
                        ClearET();
                    }
                }
        );
    }

    public void Filter() {

        btnSearch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fDate = fromDate.getText().toString();
                        String tDate = toDate.getText().toString();
                        String fAmt = fromAmt.getText().toString();
                        String tAmt = toAmt.getText().toString();

                        Cursor cursor = myDb.filter(fDate, tDate, fAmt, tAmt);
                        DisplayData(cursor, true);
                    }
                }
        );

    }
    public void ClearFilter() {

        btnClear.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClearF();
                        DisplayAllData();
                    }
                }
        );

    }

    public void ClearF() {

        MainActivity.this.fromDate.setText("");
        MainActivity.this.toDate.setText("");
        MainActivity.this.fromAmt.setText("");
        MainActivity.this.toAmt.setText("");
    }
    public void ClearET(){

        MainActivity.this.etDate.setText("");
        MainActivity.this.etAmount.setText("");
        MainActivity.this.etCategory.setText("");
    }

    public void DisplayAllData() {
        Cursor cursor = myDb.getAllData();
        DisplayData(cursor, false);
    }

    public void DisplayData(Cursor cursor, boolean isFiltered) {

        if(cursor == null) {
            ClearTable();
            return;
        }

        ClearTable();

        double bal = 0.0;

        while(cursor.moveToNext()) {
            TableRow tr = new TableRow(this);
            TableRow.LayoutParams columnLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            columnLayout.weight = 1;

            TextView date = new TextView(this);
            date.setLayoutParams(columnLayout);
            date.setText(cursor.getString(1));
            tr.addView(date);

            TextView amount = new TextView(this);
            amount.setLayoutParams(columnLayout);
            amount.setText(cursor.getString(2));
            tr.addView(amount);

            TextView category = new TextView(this);
            category.setLayoutParams(columnLayout);
            category.setText(cursor.getString(3));
            tr.addView(category);

            history.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            double temp = Double.parseDouble(cursor.getString(2));
            bal += temp;
        }
        if (!isFiltered) {
            String curBalance = "Current Balance: $" + df.format(bal).toString();
            MainActivity.this.tvBalance.setText(curBalance);
        }
    }

    public void ClearTable() {
        int childCount = history.getChildCount();
        if (childCount > 1) {
            history.removeViews(1, childCount -1);
        }
    }

}
