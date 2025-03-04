package dispatcher;

import business.Customers;
import business.FeastMenus;
import business.Orders;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Customer;
import model.FeastMenu;
import model.Order;
import tools.Acceptable;
import tools.Inputter;
import tools.Menu;

public class Main {

    public static void main(String[] args) {
        // Tạo ra lần lượt các instance
        // Tạo ra instance quản lí customer
        Customers customerManage = new Customers();
        // Tạo ra instance quản lí feastmenus
        FeastMenus feastManage = new FeastMenus();
        // Tạo ra instance quản lí các order
        Orders orderManage = new Orders();
        // Tạo ra instance quản lí menu
        Menu menu = new Menu("Traditional Feast Order Management Application");

        // Thêm các yêu cầu vào menu
        menu.addNewOption("Register customers.");
        menu.addNewOption("Update customer information.");
        menu.addNewOption("Search for customer information by name.");
        menu.addNewOption("Display feast menus.");
        menu.addNewOption("Place a feast order.");
        menu.addNewOption("Update order information.");
        menu.addNewOption("Save data to file");
        menu.addNewOption("Display Customer or Order lists.");
        menu.addNewOption("Exit program");

        // Đọc file để có dữ liệu trước khi chạy chương trình 
        customerManage.readFromFile();
        feastManage.readFromFile();
        orderManage.readFromFile();

        // Tạo biến để lưu lựa chọn
        int choice;
        while (true) {
            // show menu
            menu.print();
            // Mời nhập lựa chọn
            choice = menu.getChoice();
            // ----------------------
            switch (choice) {
                case 1: {
                    // Kiến trúc sau khi add rồi thì hỏi có muốn add tiếp không
                    boolean isContinute = false;
                    String key;
                    do {
                        isContinute = false;
                        customerManage.addNewCustomer();
                        // Hỏi xem có muốn đặt tiếp không
                        key = Inputter.getString("Do you want continute add new customer [Y/N]: ",
                                "Data-invalid, re-render...", "^\\D{1}$");
                        if (!key.matches("^[Y]y$")) {
                            isContinute = true;
                        }
                    } while (!isContinute);
                    break;
                }
                case 2: {
                    customerManage.updateCustomer();
                    break;
                }
                case 3: {
                    customerManage.searchByName();
                    break;
                }
                case 4: {
                    // Sắp xếp danh sách
                    // Lưu ý clone cần bỏ danh sách vào ()
                    ArrayList<FeastMenu> tempList = feastManage.sortAscendingByPrice(feastManage.feastMenuList);
                    // Hiển thị ra 
                    feastManage.displayFeastMenus(tempList);
                    break;
                }
                case 5: {
                    // Kiến trúc sau khi add rồi thì hỏi có muốn add tiếp không
                    boolean isContinute = false;
                    String key;
                    do {
                        isContinute = false;
                        // Nhận vào 2 anh quản lí để có thể sử dụng 2 mảng của 2 ảnh
                        orderManage.setMenu(customerManage, feastManage);
                        // Hỏi xem có muốn đặt tiếp không
                        key = Inputter.getString("Do you want continute place a feast order [Y/N]: ",
                                "Data-invalid, re-render...", "^\\D{1}$");
                        if (!key.matches("^[Y]y$")) {
                            isContinute = true;
                        }
                    } while (!isContinute);
                    break;
                }
                case 6: {
                    // Kiến trúc sau khi add rồi thì hỏi có muốn update tiếp không
                    boolean isContinute = false;
                    String key;
                    do {
                        isContinute = false;
                        orderManage.updateSetMenu(customerManage, feastManage);
                        // Hỏi xem có muốn đặt tiếp không
                        key = Inputter.getString("Do you want continute update order information [Y/N]",
                                "Data-invalid, re-render...", "^\\D{1}$");
                        if (!key.matches("^[Yy]$")) {
                            isContinute = true;
                        }
                    } while (!isContinute);
                    break;
                }
                case 7: {
                    customerManage.saveToFile();
                    orderManage.saveToFile();
                    // Thông báo
                    System.out.println("Customer data has been successfully saved to Customers.csv");
                    System.out.println("Order data has been successfully saved to FeastOrderServices.csv");
                    break;
                }
                case 8: {
                    String str = String.format(
                            "-------------------------------------\n"
                            + "L1. List Containing Customers Data\n"
                            + "L2. List Containing Orders Data\n"
                            + "-------------------------------------"
                    );
                    System.out.println(str);
                    String keyChoice = Inputter.getString("Please input list you want to show [L1/L2]: ",
                            "Data-invalid, re-input...", "^[Ll][12]$");
                    // ----
                    if (keyChoice.matches("^[Ll]1$")) {
                        if (customerManage.cusList.isEmpty()) {
                            System.out.println("Does not have any customer information");
                        } else {
                            String str1 = String.format(
                                    "----------------------------------------------------------------\n"
                                    + "Code         | Customer Name     | Phone     |   Email\n"
                                    + "----------------------------------------------------------------"
                            );
                            System.out.println(str);
                            for (Customer customer : customerManage.cusList) {
                                System.out.println(customer.toString());
                            }
                            System.out.println("----------------------------------------------------------------");
                        }
                    } else {
                        if (feastManage.feastMenuList.isEmpty()) {
                            System.out.println("No data in the system.");
                        } else {
                            String str2 = String.format(
                                    "-------------------------------------------------------------------------\n"
                                    + "ID | Event date |Customer ID| Set Menu| Price | Tables | Cost\n"
                                    + "-------------------------------------------------------------------------"
                            );
                            System.out.println(str2);
                            for (Order order  : orderManage.orderList) {
                                System.out.println(order.toString());
                            }
                        }
                    }
                    break;
                }
                case 9: {
                    System.out.println("Good bye and see you again!");
                    return;
                }
                default: {
                    System.out.println("This function is not available.");
                    break;
                }
            }
        }

    }
}
