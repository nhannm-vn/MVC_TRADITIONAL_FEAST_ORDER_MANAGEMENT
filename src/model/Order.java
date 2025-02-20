package model;

public class Order {
    // propeties
    private String orderId;
    private String customerId;
    private String menuId;
    private String eventDate;
    private int numberOfTables;
    private double totalCost;
    
    // constructor
    // non - fields

    public Order() {
    }
    
    // full - fields

    public Order(String orderId, String customerId, String menuId, String eventDate, int numberOfTables, double totalCost) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.menuId = menuId;
        this.eventDate = eventDate;
        this.numberOfTables = numberOfTables;
        this.totalCost = totalCost;
    }
    
    
    // getter and setter

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public int getNumberOfTables() {
        return numberOfTables;
    }

    public void setNumberOfTables(int numberOfTables) {
        this.numberOfTables = numberOfTables;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    // toString
    public String toString(){
        String str = String.format("%-2s| %-14s | %-10s | %-12s| %10.0f | %4d | %10.0f", 
                orderId, eventDate, customerId, menuId, totalCost / numberOfTables, numberOfTables, totalCost);
        return str;
    }
}
