package com.laacompany.infolet.database_transaction;

public class TransactionDBSchema {

    public static final class TransactionTable {
        public static final String NAME = "transactions";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String CATEGORY = "category";
            public static final String CATEGORYINDEX = "category_index";
            public static final String DATE = "date";
            public static final String TYPE = "type";
            public static final String PRICE = "price";
            public static final String NOTE = "note";
        }
    }

}
