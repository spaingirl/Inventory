package myapp.inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import myapp.inventory.data.InventoryHelper;
import myapp.inventory.data.Product;

public class DetailActivity extends Activity {

    Product product = null;
    private InventoryHelper mDbHelper;
    public final static String PRODUCT_ID = "id";
    public final static String SUPPLIER_EMAIL = "supplier@test.com";

    @BindView(R.id.nameDetail)
    TextView name;
    @BindView(R.id.quantityDetail)
    TextView quantity;
    @BindView(R.id.priceDetail)
    TextView price;
    @BindView(R.id.photoDetail)
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        long id = intent.getLongExtra(PRODUCT_ID, 0);
        mDbHelper = new InventoryHelper(this);
        product = mDbHelper.getProduct(id);

        updateProduct();
    }

    public void updateProduct() {
        try {
            name.setText(product.getName());
            quantity.setText(product.getQuantity().toString());
            price.setText(product.getPrice().toString());
            photo.setImageBitmap(product.getImage());
        } catch (Exception e) {
            Log.e(MainActivity.LOG_ID, e.getMessage());
        }
    }

    public void onDecrement(View v) {
        try {
            Integer increment = getIncrement();

            if ((product.getQuantity() - increment) >= 0) {
                Log.e(MainActivity.LOG_ID, "Decrement " + increment);
                mDbHelper.incrementQuantity(product, -increment);
                product.increase(-increment);
                updateProduct();
            }
        } catch (Exception e) {
            Log.e(MainActivity.LOG_ID, e.getMessage());
        }
    }

    public void onIncrement(View v) {
        try {
            int increment = getIncrement();
            Log.e(MainActivity.LOG_ID, "Increment " + increment);
            mDbHelper.incrementQuantity(product, increment);
            product.increase(increment);
            updateProduct();
        } catch (Exception e) {
            Log.e(MainActivity.LOG_ID, e.getMessage());
        }
    }

    private int getIncrement() {
        EditText ed = (EditText) findViewById(R.id.quantityIncrement);
        return Integer.parseInt(ed.getText().toString());
    }


    public void onDelete(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.delete_dialog_title));
        dialog.setMessage(getString(R.string.delete_dialog_text));
        dialog.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete();
            }
        });
        dialog.setNegativeButton(getString(R.string.cancel), null);
        dialog.show();
    }

    public void delete() {
        try {
            mDbHelper.deleteProduct(product);
            super.onBackPressed();
            Toast.makeText(this, getString(R.string.product_deleted), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(MainActivity.LOG_ID, e.getMessage());
        }
    }

    public void onOrder(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", SUPPLIER_EMAIL, null));

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.send_mail_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.send_mail_subject) + " " + product.toString());

        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_mail_chooser)));
    }
}
