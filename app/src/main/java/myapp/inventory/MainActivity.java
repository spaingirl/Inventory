package myapp.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import myapp.inventory.data.InventoryHelper;
import myapp.inventory.data.Product;

public class MainActivity extends AppCompatActivity {

    public final static String LOG_ID = "Inventory: ";
    public final static String PRODUCT_ID = "id";
    private InventoryHelper mDbHelper;
    private ListView list;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new InventoryHelper(this);
        list = (ListView) findViewById(R.id.list);
        adapter = new ProductAdapter(this, new ArrayList<Product>());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long l) {
                Log.i(MainActivity.LOG_ID, "Go to detail");
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                Product product = (Product) adapter.getItemAtPosition(position);
                intent.putExtra(PRODUCT_ID, product.getId());
                startActivity(intent);
            }
        });

        list.setEmptyView(findViewById(R.id.empty_list_item));

        loadProductList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProductList();
    }

    private void loadProductList() {
        adapter.setProducts(mDbHelper.getAllProducts());
    }

    public void onAddProduct(View view) {
        Intent intent = new Intent(this, FormActivity.class);
        startActivity(intent);
    }

    public void order(Product product) {
        if (product.getQuantity() > 0) {
            try {
                mDbHelper.incrementQuantity(product, -1);
            } catch (IOException e) {
                Toast.makeText(this, getString(R.string.error_buying_product), Toast.LENGTH_LONG).show();
                Log.e(MainActivity.LOG_ID, e.getMessage());
            } finally {
                loadProductList();
            }
        }
    }
}
