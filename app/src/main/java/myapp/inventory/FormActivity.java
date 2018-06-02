package myapp.inventory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

import myapp.inventory.data.InventoryHelper;
import myapp.inventory.data.Product;

public class FormActivity extends Activity {

    public final static int PICK_IMAGE = 1;
    private Product product;
    public final static String LOG_ID = "Form Activity";
    private InventoryHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_layout);
        product = new Product();
        mDbHelper = new InventoryHelper(this);
    }

    public void onSelectPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Log.i(LOG_ID, requestCode + " data:null");
                Toast.makeText(this, getString(R.string.error_loading_image), Toast.LENGTH_LONG).show();
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                product.setImage(inputStream);
                ImageView image = (ImageView) findViewById(R.id.formPhoto);
                Bitmap bitmap = product.getImage();
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.i(LOG_ID, e.getMessage());
                Toast.makeText(this, getString(R.string.error_loading_image), 4).show();
            }
        }
    }

    public void onAddProduct(View view) {

        EditText nameET = (EditText) findViewById(R.id.formName);
        EditText priceET = (EditText) findViewById(R.id.formPrice);
        EditText quantityET = (EditText) findViewById(R.id.formQuantity);

        product.setName(nameET.getText().toString());
        product.setPrice(priceET.getText().toString());
        product.setQuantity(quantityET.getText().toString());

        try {
            if (product.isComplete()) {
                mDbHelper.saveProduct(product);
                super.onBackPressed();
            } else {
                Toast.makeText(this, getString(R.string.error_form_not_complete), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(LOG_ID, e.getMessage());
            Toast.makeText(this, getString(R.string.error_saving_product), Toast.LENGTH_LONG).show();
        }
    }
}