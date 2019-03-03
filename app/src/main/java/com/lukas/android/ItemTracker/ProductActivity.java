package com.lukas.android.ItemTracker;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.CursorLoader;
import android.app.AlertDialog;
import android.app.LoaderManager;

import com.lukas.android.ItemTracker.barcodereader.BarcodeProductActivity;
import com.lukas.android.ItemTracker.data.ItemContract;

public class ProductActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static EditText BarcodeId;
    EditText ProductName;
    EditText ProductDurability;

    private Uri mCurrentProductUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        BarcodeId = findViewById(R.id.barcode_edit);
        ProductName = findViewById(R.id.name_edit);
        ProductDurability = findViewById(R.id.durability_edit);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();
        BarcodeId.setText(intent.getStringExtra("barcode"));
        ProductName.setText(intent.getStringExtra("name"));
        ProductDurability.setText(intent.getStringExtra("durability"));

        /*String barcode = BarcodeId.getText().toString().trim();
        Log.v("HHHHHHHHHHHHHHH", "HHHHHH"+barcode);*/

        if (mCurrentProductUri == null) {
            setTitle(R.string.product_add_title);
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.product_edit_title);
            getLoaderManager().initLoader(0, null, this);
        }
    }

    public void openCamera(View view){
        String name = ProductName.getText().toString().trim();
        String durability = ProductDurability.getText().toString().trim();
        String barcode = BarcodeId.getText().toString().trim();
        Intent openScan = new Intent(ProductActivity.this, BarcodeProductActivity.class);
        openScan.putExtra("name", name);
        openScan.putExtra("durability", durability);
        openScan.putExtra("barcode", barcode);
        openScan.setData(mCurrentProductUri);
        startActivity(openScan);
    }

    private void saveProduct() {
        String idText = BarcodeId.getText().toString().trim();
        long barcode;
        if(idText.isEmpty()){
            barcode = 0;
        }else{
            barcode = Long.parseLong(idText);
        }
        if (barcode < 99999999999L) {
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

        String durabilityText = ProductDurability.getText().toString().trim();
        int durability;
        if(durabilityText.isEmpty()){
            durability = 0;
        }else{
            durability = Integer.parseInt(durabilityText);
        }
        if (durability == 0) {
            Toast.makeText(this, getString(R.string.sanity_durability),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues insertValues = new ContentValues();
        insertValues.put(ItemContract.ItemEntry.COLUMN_NAME, name);
        insertValues.put(ItemContract.ItemEntry.COLUMN_DURABILITY, durability);
        insertValues.put(ItemContract.ItemEntry.COLUMN_BARCODE, barcode);

        if (mCurrentProductUri == null) {
            Uri newUri = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI_PRODUCTS, insertValues);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.duplicate_product_barcode),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentProductUri, insertValues, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.duplicate_product_barcode),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.update_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        NavUtils.navigateUpFromSameTask(ProductActivity.this);
    }

    private void deleteProduct() {
        if (mCurrentProductUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.error),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        NavUtils.navigateUpFromSameTask(ProductActivity.this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_product);
            menuItem.setVisible(false);
        }
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
        else if (id == R.id.action_delete_product) {
            AlertDialog mAlertDialog;
            mAlertDialog = new AlertDialog.Builder(ProductActivity.this).create();
            mAlertDialog.setTitle(getString(R.string.delete_product));
            mAlertDialog.setMessage(getString(R.string.are_you_sure));
            mAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            deleteProduct();
                        }
                    });
            mAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            if (!mAlertDialog.isShowing()) {
                mAlertDialog.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_NAME,
                ItemContract.ItemEntry.COLUMN_DURABILITY,
                ItemContract.ItemEntry.COLUMN_BARCODE
        };

        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }
        //proceed with moving to the first row of the cursor and reading data from it
        if (data.moveToFirst()) {
            int nameColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME);
            ProductName.setText(data.getString(nameColumnIndex));

            int durabilityColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_DURABILITY);
            ProductDurability.setText(data.getInt(durabilityColumnIndex) + "");

            int barcodeColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_BARCODE);
            BarcodeId.setText(data.getLong(barcodeColumnIndex) + "");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ProductName.setText("");
        ProductDurability.setText("");
        BarcodeId.setText("");
    }
}
