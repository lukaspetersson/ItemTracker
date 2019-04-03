package com.lukas.android.ItemTracker.barcodereader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.lukas.android.ItemTracker.ProductActivity;
import com.lukas.android.ItemTracker.barcodereader.ui.camera.CameraSource;
import com.lukas.android.ItemTracker.barcodereader.ui.camera.CameraSourcePreview;
import com.lukas.android.ItemTracker.R;
import com.lukas.android.ItemTracker.barcodereader.ui.camera.GraphicOverlay;


import java.io.IOException;


public final class BarcodeProductActivity extends AppCompatActivity implements BarcodeGraphicTracker.BarcodeUpdateListener {
    private static final String TAG = "Barcode-reader";

    // intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private boolean autoFocus;
    private boolean useFlash;

    private ImageView mFocus;
    private ImageView mFlash;

    private String productName;
    private String productDurability;
    private String previousBarcode;
    private Uri mCurrentProductUri;

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_product);
        setTitle(R.string.scan_title);


        //send screenheight to camerasourcepreview
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        BarcodeItemActivity.screenHeight = size.y;

        //find context of the views
        ImageView mSquere = findViewById(R.id.squere_product);
        mPreview = findViewById(R.id.preview_product);
        mGraphicOverlay = findViewById(R.id.graphicOverlay_product);
        mFocus = findViewById(R.id.focus_product);
        mFlash = findViewById(R.id.flash_product);

        //bring views in front of camera
        mFocus.bringToFront();
        mFlash.bringToFront();
        mSquere.bringToFront();

        //defualt options
        autoFocus = true;
        useFlash = false;

        Intent intent = getIntent();
        productDurability = intent.getStringExtra("durability");
        productName = intent.getStringExtra("name");
        previousBarcode = intent.getStringExtra("barcode");
        mCurrentProductUri = intent.getData();

        // Check for the camera permission before accessing the camera, if the permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }

        //gesture detector for zoom
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

    }

    //returns height of view when it has been drawn
    private int getHeightOfView(View contentview) {
        contentview.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return contentview.getMeasuredHeight();
    }

    public void clickFlash(View view) {
        //toggle flash boolean
        useFlash = !useFlash;

        //switch image
        if (useFlash) {
            mFlash.setImageResource(R.drawable.baseline_flash_on_white_36);
            Toast.makeText(this, getString(R.string.flash_on), Toast.LENGTH_SHORT).show();
        } else {
            mFlash.setImageResource(R.drawable.baseline_flash_off_white_36);
            Toast.makeText(this, getString(R.string.flash_off), Toast.LENGTH_SHORT).show();
        }

        //rebuild camera with new options
        createCameraSource();
        startCameraSource();
    }

    public void clickFocus(View view) {
        //toggle autofocus boolean
        autoFocus = !autoFocus;

        //switch image
        if (autoFocus) {
            mFocus.setImageResource(R.drawable.baseline_center_focus_strong_white_36);
            Toast.makeText(this, getString(R.string.auto_focus_on), Toast.LENGTH_SHORT).show();
        } else {
            mFocus.setImageResource(R.drawable.baseline_center_focus_weak_white_36);
            Toast.makeText(this, getString(R.string.auto_focus_off), Toast.LENGTH_SHORT).show();
        }

        //rebuild camera with new options
        createCameraSource();
        startCameraSource();
    }

    //if premision is not granted, ask for it
    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        //ask for permission
        ActivityCompat.requestPermissions(BarcodeProductActivity.this, permissions,
                RC_HANDLE_CAMERA_PERM);

    }

    //creates and starts camera
    //uppressing InlinedApi since there is a check that the minimum version is met before using the constant
    @SuppressLint("InlinedApi")
    private void createCameraSource() {
        Context context = getApplicationContext();

        //create barcode detector, factory used to create a separate tracker instance for each barcode
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, this);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());

        //check if the required native libraries are currently available
        if (!barcodeDetector.isOperational()) {
            //Detector dependencies are not yet available

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        // Creates and starts the camera
        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f);

        mCameraSource = builder
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .setFocusMode(autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null)
                .build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    //callback for the result from requesting permissions
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            createCameraSource();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        //if camera permission is denied
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error)
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    // Starts or restarts the camera source, if it exists
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    //measures how much to zoom
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean b = scaleGestureDetector.onTouchEvent(e);

        boolean c = gestureDetector.onTouchEvent(e);

        return b || c || super.onTouchEvent(e);
    }

    //handles zoom
    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        //Responds to scaling events for a gesture in progress
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mCameraSource.doZoom(detector.getScaleFactor());
            return false;
        }

        //Responds to the beginning of a scaling gesture
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mCameraSource.doZoom(detector.getScaleFactor());
            return true;
        }

        //Responds to the end of a scale gesture
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mCameraSource.doZoom(detector.getScaleFactor());
        }
    }

    @Override
    public void onBackPressed() {
        Intent backToProduct = new Intent(BarcodeProductActivity.this, ProductActivity.class);
        backToProduct.putExtra("barcode", previousBarcode);
        backToProduct.putExtra("name", productName);
        backToProduct.putExtra("durability", productDurability);
        backToProduct.setData(mCurrentProductUri);
        startActivity(backToProduct);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBarcodeDetected(Barcode barcode) {
        Intent backToProduct = new Intent(BarcodeProductActivity.this, ProductActivity.class);
        backToProduct.putExtra("barcode", barcode.displayValue);
        backToProduct.putExtra("name", productName);
        backToProduct.putExtra("durability", productDurability);
        backToProduct.setData(mCurrentProductUri);
        startActivity(backToProduct);
    }

}


