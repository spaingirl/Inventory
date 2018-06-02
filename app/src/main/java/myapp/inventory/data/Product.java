package myapp.inventory.data;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Product {
    private long mId;
    private String mName;
    private Float mPrice;
    private Integer mQuantity;
    private byte[] mPhoto;

    public Product() {
        this.mName = null;
        this.mPrice = null;
        this.mQuantity = null;
        this.mPhoto = null;
    }

    static Product fromCursor(Cursor cursor) {
        Product product = new Product();

        product.mId = cursor.getLong(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry._ID));
        product.mName = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_INVENTORY_NAME));
        product.mPrice = cursor.getFloat(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRICE));
        product.mQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY));
        product.mPhoto = cursor.getBlob(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PHOTO));

        return product;
    }

    public void setName(String name) {
        if (name != null && !("".equals(name))) {
            mName = name;
        }
    }

    public void setQuantity(String quantity) {
        if (quantity != null && !("".equals(quantity))) {
            mQuantity = Integer.parseInt(quantity);
        }
    }

    public void setPrice(String price) {
        if (price != null && !("".equals(price))) {
            mPrice = Float.parseFloat(price);
        }
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public Float getPrice() {
        return mPrice;
    }

    public byte[] getPhoto() {
        return mPhoto;
    }

    public void increase(int increment) {
        if (mQuantity + increment >= 0) {
            mQuantity += increment;
        }
    }

    public void setImage(InputStream fileName) throws FileNotFoundException, IOException {
        if (fileName != null) {
            this.mPhoto = getBlobImage(fileName);
        }
    }

    public Bitmap getImage() throws Exception, FileNotFoundException, IOException {
        if (mPhoto != null) {
            return BitmapFactory.decodeByteArray(this.mPhoto, 0, this.mPhoto.length);
        }
        throw new Exception("No image");
    }

    public byte[] getBlobImage(InputStream file) throws FileNotFoundException, IOException {
        byte[] image = new byte[0];
        if (file != null) {
            image = new byte[file.available()];
            file.read(image);
            file.close();
        }
        return image;
    }

    @Override
    public String toString() {
        return this.mId + " / " +
                this.mName + " / " +
                this.mQuantity + " / " +
                this.mPrice;
    }

    public boolean isComplete() {
        return mName != null && mPrice != null && mQuantity != null && mPhoto != null;
    }
}
