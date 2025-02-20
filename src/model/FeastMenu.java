package model;

public class FeastMenu {
    // Properties
    private String menuCode;
    private String menuName;
    private double price;
    private String ingredients;
    
    // Constructor
    // non-fields
    public FeastMenu() {
    }
    
    // full-fields

    public FeastMenu(String menuCode, String menuName, double price, String ingredients) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.price = price;
        this.ingredients = ingredients;
    }
    
    // getter - setter

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    
    // toString
    public String toString(){
        String str = String.format("%-12s| %-12s| %8.0f| %-12s",
                menuCode, menuName, price, ingredients);
        return str;
    }
    
}
