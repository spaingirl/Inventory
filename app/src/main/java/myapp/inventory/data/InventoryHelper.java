package myapp.inventory.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class InventoryHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "inventory.db";

    public final static String LOG_ID = "Inventory Helper";
    public static SQLiteDatabase mDB = null;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + InventoryContract.InventoryEntry.TABLE_NAME + " (" +
                    InventoryContract.InventoryEntry._ID + " INTEGER PRIMARY KEY ," +
                    InventoryContract.InventoryEntry.COLUMN_INVENTORY_NAME + " TEXT NOT NULL," +
                    InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRICE + " NUMBER NOT NULL," +
                    InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY + " INTEGER NOT NULL," +
                    InventoryContract.InventoryEntry.COLUMN_INVENTORY_PHOTO + " BLOB NOT NULL" +
                    ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + InventoryContract.InventoryEntry.TABLE_NAME;


    public InventoryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        this.mDB = db;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<Product> getAllProducts() {
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_NAME,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRICE,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_PHOTO,
        };

        Cursor cursor = this.getWritableDatabase().query(
                InventoryContract.InventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<Product> list = new ArrayList<Product>();

        try {
            while (cursor.moveToNext()) {
                Product product = Product.fromCursor(cursor);
                list.add(product);
                Log.i(LOG_ID, product.toString());
            }
        } finally {
            cursor.close();
        }

        return list;
    }

    public Product getProduct(long id) {
        String selectQuery = "SELECT  * FROM " + InventoryContract.InventoryEntry.TABLE_NAME + " WHERE "
                + InventoryContract.InventoryEntry._ID + " = " + id;
        Cursor cursor = this.getWritableDatabase().rawQuery(selectQuery, null);
        Product product = null;
        try {
            if (cursor.moveToNext()) {
                product = Product.fromCursor(cursor);
            }
        } finally {
            cursor.close();
        }
        return product;
    }


    public void deleteProduct(Product product) {
        String selectQuery = "DELETE FROM " + InventoryContract.InventoryEntry.TABLE_NAME + " WHERE "
                + InventoryContract.InventoryEntry._ID + "=?";
        Log.i(LOG_ID, "Deleting ... " + product);
        this.getWritableDatabase().execSQL(selectQuery, new String[]{""+product.getId()});
    }


    public void saveProduct(Product product) throws IOException, FileNotFoundException {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_NAME, product.getName());
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PHOTO, product.getPhoto());
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRICE, product.getPrice());
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY, product.getQuantity());
        this.getWritableDatabase().insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
    }

    public void incrementQuantity(Product product, int increment) throws IOException, FileNotFoundException {
        if (product.getQuantity() + increment >= 0) {
            Log.i(LOG_ID, "Update ... " + product.toString());
            ContentValues values = new ContentValues();
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_NAME, product.getName());
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PHOTO, product.getPhoto());
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRICE, product.getPrice());
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY, product.getQuantity() + increment);
            String where = new String(InventoryContract.InventoryEntry._ID + "=" + product.getId());
            this.getWritableDatabase().update(
                    InventoryContract.InventoryEntry.TABLE_NAME,
                    values,
                    where,
                    null);

        }
    }
}
