package model;


public class Customer {
    // properties
    private String customerCode;
    private String name;
    private String phone;
    private String email;
    
    // constructor
    // non-field
    public Customer() {
    }
    
    // full-field

    public Customer(String customerCode, String name, String phone, String email) {
        this.customerCode = customerCode;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    
    // getter - setter

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    // method tìm " " từ dưới lên theo tên
    public int position (String name){
        for (int i = name.length() - 1; i >= 0; i--) {
            if(name.charAt(i) == ' '){
                return i;
            }
        }
        return -1;
    }
    
    // method in thong tin
    public String toString(){
            // find pos
            int pos = position(name);
            String fname = name.substring(pos, name.length());
            String lname = name.substring(0, pos);
            String str = String.format("%-8s| %-7s, %-15s | %-14s | %-8s",
             customerCode, fname, lname,  phone, email);
            return str;
    }
    
}
