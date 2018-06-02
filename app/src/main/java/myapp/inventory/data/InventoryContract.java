package myapp.inventory.data;

import android.provider.BaseColumns;

public class InventoryContract {
    private InventoryContract() {
    }

    public static final class InventoryEntry implements BaseColumns {

        /**
         * Name of database table
         */
        public final static String TABLE_NAME = "Inventory";

        /**
         * Unique ID number for the products (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Product's name
         * Type: TEXT
         */
        public final static String COLUMN_INVENTORY_NAME = "name";

        /**
         * Product's price
         * Type: NUMERIC
         */
        public final static String COLUMN_INVENTORY_PRICE = "price";

        /**
         * Product's mQuantity in stock
         * Type: INTEGER
         */
        public final static String COLUMN_INVENTORY_QUANTITY = "quantity";

        /**
         * Product's mPhoto
         * Type: BLOB
         */
        public final static String COLUMN_INVENTORY_PHOTO = "photo";


    }
}
