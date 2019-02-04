package com.lukas.android.ItemTracker;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lukas.android.ItemTracker.barcodereader.BarcodeItemActivity;
import com.lukas.android.ItemTracker.barcodereader.BarcodeProductActivity;

public class ProductActivity extends AppCompatActivity {

    EditText BarcodeId;
    EditText ProductName;
    EditText ProductDurability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        setTitle(R.string.product_title);

        BarcodeId = findViewById(R.id.barcode_edit);
        ProductName = findViewById(R.id.name_edit);
        ProductDurability = findViewById(R.id.durability_edit);
    }

    public void openCamera(View view){
        Intent openScan = new Intent(ProductActivity.this, BarcodeProductActivity.class);
        startActivity(openScan);
    }

    private void saveProduct() {

        String idText = BarcodeId.getText().toString().trim();
        long id;
        if(idText.isEmpty()){
            id = 0;
        }else{
            id = Long.parseLong(BarcodeId.getText().toString().trim());
        }
        if (id < 99999999999L) {
                Toast.makeText(this, getString(R.string.sanity_id),
                        Toast.LENGTH_SHORT).show();
            return;
        }

        String name = ProductName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, getString(R.string.sanity_name),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int durability = Integer.parseInt(ProductDurability.getText().toString().trim());
        if (durability == 0) {
            Toast.makeText(this, getString(R.string.sanity_durability),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Log.v("ProductActivity", "HHHHHHHHHHHHHH"+id+name+durability);

        NavUtils.navigateUpFromSameTask(ProductActivity.this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return true;
    }

    //if menu option is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_confirm_product) {
            saveProduct();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
