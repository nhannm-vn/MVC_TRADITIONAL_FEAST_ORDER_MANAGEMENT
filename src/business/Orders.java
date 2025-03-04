package business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import model.Customer;
import model.FeastMenu;
import model.Order;
import tools.Acceptable;
import tools.Inputter;

public class Orders {

    // Tạo danh sách lưu các order
    public ArrayList<Order> orderList = new ArrayList<>();

    // Method
    // Chức năng handle cho việc trùng thông tin đơn hàng
    //nghĩa là đơn hàng có thông tin giống y đúc
    public boolean handleData(Order orderItem) {
        /*
            _Thông tin đơn hàng được coi là
            duy nhất dựa trên sự kết hợp đồng thời của
            3 thuộc tính: Mã khách hàng, Mã Set Menu và ngày sự kiện.
            _Khách hàng được đặt nhiều đơn nhưng vào nhiều ngày khác nhau
         */
        // Định dạng của chuỗi ngày
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Chuyển chuỗi thành LocalDate
        LocalDate orderItemDateEvent = LocalDate.parse(orderItem.getEventDate(), formatter);
        for (Order order : orderList) {
            LocalDate itemListEventDate = LocalDate.parse(order.getEventDate(), formatter);
            // TH lớn nhất nếu trùng mã Set Menu thì coi như sai luôn
            if (order.getOrderId().equals(orderItem.getOrderId())) {
                return false;
            } else {
                // TH: một người đặt đơn khác nhau vào cùng một ngày
                if (itemListEventDate.equals(orderItemDateEvent)
                        && orderItem.getCustomerId().equals(order.getCustomerId())) {
                    return false;
                }else if(itemListEventDate.isBefore(orderItemDateEvent)
                        && orderItem.getCustomerId().equals(order.getCustomerId())){
                    return true;
                }
            }
        }
        return true;
    }

    // method display
    public void displaySetMenu(Order orderItem, Customers cusManagement, FeastMenus feastManagement) {
        // Tìm ra 2 instance
        Customer cusObj = cusManagement.searchCustomerByCode(orderItem.getCustomerId());
        FeastMenu feaObj = feastManagement.searchMenuById(orderItem.getMenuId());
        String str1 = String.format(
                "----------------------------------------------------------------\n"
                + "           Customer order information [Order ID: " + orderItem.getOrderId() + "]\n"
                + "----------------------------------------------------------------"
        );
        System.out.println(str1);
        String str2 = String.format(
                "Code           :%s\n"
                + "Customer name  :%s\n"
                + "Phone number   :%s\n"
                + "Email          :%s\n"
                + "----------------------------------------------------------------",
                cusObj.getCustomerCode(), cusObj.getName(), cusObj.getPhone(), cusObj.getEmail()
        );
        System.out.println(str2);
        // Format cho số tiền
        DecimalFormat formatter = new DecimalFormat("#,###,### vnd");
        String formattedFee = formatter.format(feaObj.getPrice());
        // ------------------
        // Chặt ra riêng các món ăn để hiển thị cho đẹp
        StringTokenizer st = new StringTokenizer(feaObj.getIngredients(), "#");
        String appetizer = st.nextToken().trim().substring(1);
        String mainCourse = st.nextToken().trim();
        String desert = st.nextToken().trim();
        String updateDesrt = desert.substring(0, desert.length() - 1);
        // ----------------------------------------------------------
        String str3 = String.format(
                "Code of Set Menu        :%s\n"
                + "Set menu name           :%s\n"
                + "Event date              :%s\n"
                + "Number of tables        :%d\n"
                + "Price                   :%s\n"
                + "Ingredients             :\n"
                + "   %s\n"
                + "   %s\n"
                + "   %s\n"
                + "----------------------------------------------------------------",
                feaObj.getMenuCode(), feaObj.getMenuName(), orderItem.getEventDate(),
                orderItem.getNumberOfTables(), formattedFee, appetizer, mainCourse, updateDesrt
        );
        System.out.println(str3);
        // ---------------------
        // Format cho totalCost
        String formattedTotalCost = formatter.format(orderItem.getTotalCost());
        String str4 = String.format("Total cost     :%s\n"
                + "----------------------------------------------------------------", formattedTotalCost
        );
        System.out.println(str4);
    }

    // hàm kiểm tra ngày tháng có trong tương lai không 
    //vd: 31/02/2025
    public boolean isFutureDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);

            Date inputDate = sdf.parse(dateString); 
            Date today = new Date();

            return inputDate.after(today);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Chức năng đặt tiệc
    public void setMenu(Customers cusManagement, FeastMenus feastManagement) {
        // Nhập customerId
        //Lưu ý mà này phải nằm trong danh sách 
        boolean isCheck = false;
        String customerId = "";
        do {
            isCheck = false;
            customerId = Inputter.getString("Input customerId: ",
                    "Data is invalid !. Re-enter ...",
                    Acceptable.CUS_ID_VALID);
            Customer temp = cusManagement.searchCustomerByCode(customerId);
            // Mã của khách hàng phải có trong danh sách
            if (temp != null) {
                isCheck = true;
            } else {
                System.out.println("Data is invalid !. Re-enter ...");
            }
        } while (!isCheck);

        // Nhập menuId
        String menuId = "";
        do {
            isCheck = false;
            menuId = Inputter.getString("Input menuId: ",
                    "Data is invalid !. Re-enter ...", Acceptable.MENU_ID_VALID);
            FeastMenu temp = feastManagement.searchMenuById(menuId);
            // Mã bàn phải nằm trong list mới được
            if (temp != null) {
                isCheck = true;
            } else {
                System.out.println("Data is invalid !. Re-enter ...");
            }
        } while (!isCheck);

        // Nhập số lượng bàn
        //số lượng bàn phải không được bỏ trống và phải > 0
        int numberOfTables = Integer.parseInt(Inputter.getString("Input number of tables: ",
                "Data is invalid !. Re-enter ...", Acceptable.NUM_OF_TABLE));

        // Nhập ngày diễn ra sự kiện
        //Lưu ý không được diễn ra trong quá khứ
        String eventDate = "";
        do {
            isCheck = false;
            eventDate = Inputter.getString("Input event date: ",
                    "Data is invalid !. Re-enter ...", Acceptable.DATE_EVENT);
            // Định dạng của chuỗi ngày
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Chuyển chuỗi thành LocalDate
            LocalDate inputDate = LocalDate.parse(eventDate, formatter);
            // Lấy ngày hiện tại
            LocalDate currentDate = LocalDate.now();
            // Check xem ngày vừa nhập có nằm trong quá khứ hay không
            if (inputDate.isAfter(currentDate) && isFutureDate(eventDate)) {
                isCheck = true;
            } else {
                System.out.println("Don't provide event date in the past");
            }
        } while (!isCheck);

        // Tính số tiền dựa vào số bàn và tiền của mỗi loại event
        // Tìm giá tiền cụ thể của loại event dựa vào mã của nó
        double priceOfEvent = feastManagement.searchMenuById(menuId).getPrice();
        double totalCost = priceOfEvent * numberOfTables;

        // Nếu vượt qua handle thêm vào ds và hiển thị thông tin ra
        // Tạo object
        Order newOrder = new Order(orderList.size() + 1 + "", customerId, menuId, eventDate, numberOfTables, totalCost);
        if (handleData(newOrder)) {
            // Thêm vào danh sách đồng thời hiển thị ra 
            orderList.add(newOrder);
            // Hiển thị thông tin của nó ra 
            displaySetMenu(newOrder, cusManagement, feastManagement);
        } else {
            System.out.println("Dupplicate data or data invalid !");
            return;
        }
    }

    // -------------------------------------------------------------------------
    // Chức năng tìm order bằng orderId
    public Order searchOrderById(String orderId) {
        for (Order item : orderList) {
            if (item.getOrderId().equals(orderId)) {
                return item;
            }
        }
        return null;
    }

    // Chức năng update đơn tiệc đặt 
    public void updateSetMenu(Customers cusManagement, FeastMenus feastManagement) {
        // Nhập vào orderId của đơn tiệc cần tìm
        String orderId = Inputter.getString("Input orderId you wanna update: ",
                "Data is invalid !. Re-enter ...", Acceptable.ORDER_ID);
        // Dựa vào orderId tìm xem có thằng cần update hay không
        Order orderFind = searchOrderById(orderId);
        if (orderFind == null) {
            // Nếu không có thì sẽ thông báo
            System.out.println("This Order does not exist!");
        } else {
            // Nếu có thì tiến hành nhập các dữ liệu cần update
            boolean isCheck;
            // Yêu cầu người dùng nhập mã thực đơn nếu muốn update
            // Nhập menuId
            String menuId = "";
            do {
                isCheck = false;
                menuId = Inputter.getString("Input menuId: ",
                        "Data is invalid !. Re-enter ...", Acceptable.MENU_ID_VALID);
                FeastMenu temp = feastManagement.searchMenuById(menuId);
                // Mã bàn phải nằm trong list mới được
                if (temp != null) {
                    isCheck = true;
                } else {
                    System.out.println("Data is invalid !. Re-enter ...");
                }
            } while (!isCheck);

            // Yêu cầu người dùng nhập số lượng bàn đặt nếu muốn update
            //số lượng bàn phải không được bỏ trống và phải > 0
            int numberOfTables = Integer.parseInt(Inputter.getString("Input number of tables: ",
                    "Data is invalid !. Re-enter ...", Acceptable.NUM_OF_TABLE));

            // Yêu cầu nhập ngày cần update
            String eventDate = "";
            do {
                isCheck = false;
                eventDate = Inputter.getString("Input event date: ",
                        "Data is invalid !. Re-enter ...", Acceptable.DATE_EVENT);
                // Định dạng của chuỗi ngày
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                // Chuyển chuỗi thành LocalDate
                LocalDate inputDate = LocalDate.parse(eventDate, formatter);
                // Lấy ngày hiện tại
                LocalDate currentDate = LocalDate.now();
                // Check xem ngày vừa nhập có nằm trong quá khứ hay không
                if (inputDate.isAfter(currentDate)) {
                    isCheck = true;
                } else {
                    System.out.println("Don't provide event date in the past");
                }
            } while (!isCheck);

            // Update các thông tin vừa mới cập nhật 
            orderFind.setNumberOfTables(numberOfTables);
            orderFind.setEventDate(eventDate);
            // Nếu mà mã menu thay đổi thì update số tiền luôn
            if (!orderFind.getMenuId().equals(menuId)) {
                // Tính lại số tiền và cập nhật lại totalCost
                // Tìm số tiền của mỗi bàn theo mã menuId mới
                double price = feastManagement.searchMenuById(menuId).getPrice();
                double updateToTalCost = price * numberOfTables;
                // set lại totalCost mới
                orderFind.setTotalCost(updateToTalCost);
            }
            orderFind.setMenuId(menuId);

            // Thông báo thành công
            System.out.println("Update order item successful!");
        }
    }

    // ---------------------------
    public Order dataToObject(String line) {
        // Tao ra StringTokenizer de bam chuoi
        StringTokenizer st = new StringTokenizer(line, ",");
        // Bam cac fields ra 
        String orderId = st.nextToken().trim();
        String customerId = st.nextToken().trim();
        String menuId = st.nextToken().trim();
        String eventDate = st.nextToken().trim();
        int numberOfTables = Integer.parseInt(st.nextToken().trim());
        double totalCost = Double.parseDouble(st.nextToken().trim());
        // Duc ra object
        Order order = new Order(orderId, customerId, menuId, eventDate, numberOfTables, totalCost);
        return order;
    }

    public void readFromFile() {
        // Luu duong dan
        String pathFile = "E:\\Lab211\\lab2\\FeastOrderServices.csv";
        try {
            // Tao obj file
            File f = new File(pathFile);
            if (!f.exists()) {
                System.out.println("Cannot read data from FeastOrderServices.csv. Please check it");
                return;
            }
            // Tao thang doc file
            FileReader fr = new FileReader(f);
            // Tao buffer doc du lieu tu may
            BufferedReader br = new BufferedReader(fr);
            // Doc dong dau tien
            String line = br.readLine();
            // Con doc duoc thi doc
            while (line != null && !line.isEmpty()) {
                // Ep du lieu ra
                Order order = dataToObject(line);
                if (order != null) {
                    orderList.add(order);
                }
                // Doc dong tiep theo
                line = br.readLine();
            }
            // sau khi xong thi dong
            br.close();
        } catch (Exception e) {
            System.out.println("File loi roi: " + e);
        }
    }

    public void saveToFile() {
        // Luu duong dan
        String pathFile = "E:\\Lab211\\lab2\\FeastOrderServices.csv";
        try {
            // Tao obj file
            File f = new File(pathFile);
            // Tao anh ghi file
            FileWriter fw = new FileWriter(f);
            // Tao buffer ghi file
            BufferedWriter writter = new BufferedWriter(fw);
            // Duyet mang ghi file
            for (Order item : orderList) {
                writter.write(item.getOrderId() + "," + item.getCustomerId() + ","
                        + item.getMenuId() + "," + item.getEventDate() + "," + item.getNumberOfTables()
                        + "," + item.getTotalCost());
                // Xuong dong
                writter.newLine();
            }
            // Dong 
            writter.close();
        } catch (Exception e) {
            System.out.println("File loi roi");
        }
    }

}
