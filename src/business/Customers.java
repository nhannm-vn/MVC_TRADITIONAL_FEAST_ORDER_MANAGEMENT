package business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import model.Customer;
import tools.Acceptable;
import tools.Inputter;

public class Customers {

    // Tạo danh sách để quản lí các customer 
    public ArrayList<Customer> cusList = new ArrayList<>();

    // Method tìm customer trong danh sách dựa vào customerCode
    public Customer searchCustomerByCode(String customerCode) {
        Customer cus;
        for (Customer item : cusList) {
            if (item.getCustomerCode().equals(customerCode)) {
                return item;
            }
        }
        return null;
    }

    // addCustomer(customer: Customer) 
    // Method thêm customer
    public void addNewCustomer() {
        // Nhập các thông tin và check valid
        // Check valid customerCode
        boolean isCheck = false;
        String customerCode = "";
        do {
            isCheck = false;
            customerCode = Inputter.getString("Input customerCode: ", 
                    "Data is invalid !. Re-enter ...",
                    Acceptable.CUS_ID_VALID);
            Customer temp = searchCustomerByCode(customerCode);
            if(temp == null) isCheck = true;
            else System.out.println("Data is invalid !. Re-enter ...");
        } while (!isCheck);
        
        // Check valid name
        String name = Inputter.getString("Input name: ", 
                                "Data is invalid !. Re-enter ..." ,
                                Acceptable.NAME_VALID);

        // Check valid phone 
        String phone = Inputter.getString("Input phone number: ", 
                                "Data is invalid !. Re-enter ..." ,
                                Acceptable.PHONE_VALID);
        
        // Check valid email
        String email = Inputter.getString("Input email: ", 
                                "Data is invalid !. Re-enter ..." ,
                                Acceptable.EMAIL_VALID);
        // Tạo ra object
        Customer customer = new Customer(customerCode, name, phone, email);
        // Thêm vào danh sách
        if(customer != null){
            cusList.add(customer);
            System.out.println("Adding customer successful!");
        } 
    }
    
    // updateCustomer
    public void updateCustomer(){
        // Nhập các thông tin và check valid
        // Check valid customerCode
        // Nếu có customer thì mới cho update. Không có thì thông báo
        
        boolean isCheck = false;
        String customerCode = "";
        customerCode = Inputter.getString("Input customerCode: ", 
                    "Data is invalid !. Re-enter ...",
                    Acceptable.CUS_ID_VALID);
        // Tìm nó trong danh sách
        Customer updateCustomer = searchCustomerByCode(customerCode);
        if(updateCustomer == null){
            System.out.println("This customer does not exist");
        }else{
            // Yêu cầu nhập thêm các field khác
            // Check valid name
            String newName = Inputter.getString("Input name: ", 
                                "Data is invalid !. Re-enter ..." ,
                                Acceptable.NAME_VALID);
            
            // Check valid phone 
            String newPhone = Inputter.getString("Input phone number: ", 
                                "Data is invalid !. Re-enter ..." ,
                                Acceptable.PHONE_VALID);
            
            // Check valid email
            String newEmail = Inputter.getString("Input phone email: ", 
                                "Data is invalid !. Re-enter ..." ,
                                Acceptable.EMAIL_VALID);
            // Update lại cho 
            updateCustomer.setName(newName);
            updateCustomer.setPhone(newPhone);
            updateCustomer.setEmail(newEmail);
            // Update
            cusList.set(cusList.indexOf(updateCustomer), updateCustomer);
            // Thông báo
            System.out.println("Update customer successful!");
        }
    }
    
    // searchByName
    public void searchByName(){
        // Tạo mảng để lưu 
        ArrayList<Customer> searchList = new ArrayList<>();
        // Nhập vào name hoặc a part of name
        String keyName = Inputter.getString("Input name or a part of name: ", 
                "Data is invalid !. Re-enter ...", "^\\D+$");
        // Duyệt vào thêm vào mảng tạm
        for (Customer item : cusList) {
            if(item.getName().contains(keyName) || item.getName().contains(keyName.toLowerCase())){
                searchList.add(item);
            }
        }
        // Nếu mảng trống thì sẽ thông báo luôn
        if(searchList.isEmpty()){
            System.out.println("No one matches the search criteria!");
        }else{
            // Sắp xếp dựa vào tên trước rồi mới in ra màn hình
            Comparator orderByLastName = new Comparator<Customer>() {
                @Override
                public int compare(Customer o1, Customer o2) {
                   // Tim ra 2 last name truoc
                    String lastName1 = o1.getName().substring(o1.getName().lastIndexOf(" ") + 1);
                    String lastName2 = o2.getName().substring(o2.getName().lastIndexOf(" ") + 1);
                    return lastName1.compareTo(lastName2);
                }
            };
            // Sap xep
            Collections.sort(searchList, orderByLastName);
            // Show thông tin
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("Code            |   Customer Name       |     Phone         |     Email");
            System.out.println("----------------------------------------------------------------------------");
            for (Customer item : searchList) {
                System.out.println(item.toString());
            }
            System.out.println("----------------------------------------------------------------------------");
        }
    }
    
    // dataToObject
    public Customer dataToObject(String line){
        // Tạo ra StringTokenizer để băm chuỗi
        StringTokenizer st = new StringTokenizer(line, ",");
        String customerCode = st.nextToken().trim();
        String name = st.nextToken().trim();
        String phone = st.nextToken().trim();
        String email = st.nextToken().trim();
        // Đúc ra obj
        Customer cus = new Customer(customerCode, name, phone, email);
        return cus;
    }
    
    // readFromFile
    public void readFromFile(){
        // Lưu đường dẫn
        String pathFile = "E:\\Lab211\\lab2\\Customers.csv";
        // Tạo obj file
        File f = new File(pathFile);
        try {
            if(!f.exists()){
                System.out.println("Cannot read data from Customers.csv. Please check it");
                return;
            }
            // Tạo anh đọc dữ liệu
            FileReader fr = new FileReader(f);
            // Tạo buffer đọc dữ liệu từ file
            BufferedReader br = new BufferedReader(fr);
            // Đọc dòng đầu tiên 
            String line = br.readLine();
            // Kiểm tra nếu còn đọc được thì còn làm
            while(line != null && !line.isEmpty()){
                Customer customer = dataToObject(line);
                if(customer != null){
                    // Thêm vào danh sách
                    cusList.add(customer);
                }
                // Đọc dòng tiếp theo
                line = br.readLine();
            }
            // Đóng sau khi đã đọc xong
            br.close();
        } catch (Exception e) {
            System.out.println("File loi roi: " + e);
        }
    }
    
    // method ghi file
    public void saveToFile(){
        // Lưu đường dẫn
        String pathFile = "E:\\Lab211\\lab2\\Customers.csv";
        // Tạo obj file
        File f = new File(pathFile);
        try {
            // Kiểm xem đã có obj file chưa
            if(!f.exists()){
                System.out.println("Cannot write data to Customers.csv. Please check it");
                return;
            }
            // Tạo FileWriter để ghi
            FileWriter fw = new FileWriter(f);
            // Tạo buffer để ghi nội dung vào file
            BufferedWriter writter = new BufferedWriter(fw);
            // Duyệt mảng để thêm vào file
            for (Customer item : cusList) {
                writter.write(item.getCustomerCode() + "," + item.getName() + ","
                + item.getPhone() + "," + item.getEmail());
                // Xuống dòng
                writter.newLine();
            }
            // Đóng
            writter.close();
        } catch (Exception e) {
            System.out.println("File loi roi: " +e);
        }
    }
    
}
