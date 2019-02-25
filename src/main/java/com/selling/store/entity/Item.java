package com.selling.store.entity;

/**
 * Entity for storing item data
 */
public class Item {

    /**
     * Item name
     */
    private String name;

    /**
     * Vendor code
     */
    private String code;

    /**
     * Actual price
     */
    private String price;

    /**
     * Creates new item and inits all its fields with data
     *
     * @param name  name of new item
     * @param code  vendor code of new item
     * @param price price of new item
     */
    public Item(String name, String code, String price) {
        this.name = name;
        this.code = code;
        this.price = price;
    }

    /**
     * Name getter
     */
    public String getName() {
        return name;
    }

    /**
     * Price getter
     */
    public String getPrice() {
        return price;
    }

    /**
     * Vendor code getter
     */
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
