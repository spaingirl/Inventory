package myapp.inventory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import myapp.inventory.data.Product;

public class ProductAdapter extends BaseAdapter {

    List<Product> mProducts = new ArrayList<Product>();
    private LayoutInflater inflater;
    public final static String LOG_ID = "Product Adapter";
    final private MainActivity mParent;

    public ProductAdapter(MainActivity context, List<Product> news) {
        this.mProducts = news;
        inflater = LayoutInflater.from(context);
        mParent = context;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        View element = convertView;
        ViewHolder holder;
        if (element == null) {
            element = inflater.inflate(R.layout.product_item, parent, false);
            holder = new ViewHolder(element);
            element.setTag(holder);
        } else {
            holder = (ViewHolder) element.getTag();
        }

        try {
            final Product aProduct = (Product) getItem(position);
            Context context = element.getContext();
            holder.name.setText(context.getString(R.string.product_name) + " : " + aProduct.getName());
            holder.quantity.setText(context.getString(R.string.product_quantity) + " : " + aProduct.getQuantity().toString() + " " + context.getString(R.string.product_units));
            holder.price.setText(context.getString(R.string.product_price) + " : " + aProduct.getPrice().toString() + context.getString(R.string.product_coin));
            holder.photo.setImageBitmap(aProduct.getImage());


            holder.button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mParent.order(aProduct);
                }
            });

        } catch (Exception e) {
            Log.e(MainActivity.LOG_ID, e.getMessage());
        }
        return element;
    }

    @Override
    public Product getItem(int position) {
        return mProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    public void setProducts(List<Product> data) {
        mProducts.clear();
        mProducts.addAll(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        @BindView(R.id.nameList)
        TextView name;
        @BindView(R.id.quantityList)
        TextView quantity;
        @BindView(R.id.priceList)
        TextView price;
        @BindView(R.id.photoList)
        ImageView photo;
        @BindView(R.id.buyListBtn)
        Button button;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
