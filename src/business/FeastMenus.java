package business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import model.FeastMenu;

public class FeastMenus {
    // Tạo cái mảng để lưu các feastMenu
    public ArrayList<FeastMenu> feastMenuList = new ArrayList<>();
    
    // Method-----
    // searchMenuById
    public FeastMenu searchMenuById(String menuId){
        // Tim kiem
        for (FeastMenu item : feastMenuList) {
            if(item.getMenuCode().equals(menuId)) return item;
        }
        return null;
    }
    
    // dataToObject
    public FeastMenu dataToObject(String line){
        // Tạo ra StringTokenizer để băm chuỗi
        StringTokenizer st = new StringTokenizer(line, ",");
        String menuCode = st.nextToken().trim();
        String menuName = st.nextToken().trim();
        double price = Double.parseDouble(st.nextToken().trim());
        String ingredients = st.nextToken().trim();
        // Đúc ra obj
        FeastMenu fm = new FeastMenu(menuCode, menuName, price, ingredients);
        return fm;
    }
    
    // readFromFile
    public void readFromFile(){
        // Lưu đường dẫn
        String pathFile = "E:\\Lab211\\lab2\\FeastMenu.csv";
        // Tạo obj file
        File f = new File(pathFile);
        try {
            if(!f.exists()){
                System.out.println("Cannot read data from feastMenu.csv. Please check it");
                return;
            }
            // Tạo anh đọc dữ liệu
            FileReader fr = new FileReader(f);
            // Tạo buffer đọc dữ liệu từ file
            BufferedReader br = new BufferedReader(fr);
            // Đọc dòng đầu tiên 
            String line = br.readLine();
            // Bỏ dòng title đầu
            line = br.readLine();
            // Kiểm tra nếu còn đọc được thì còn làm
            while(line != null && !line.isEmpty()){
                FeastMenu feastMenu = dataToObject(line);
                if(feastMenu != null){
                    // Thêm vào danh sách
                    feastMenuList.add(feastMenu);
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
    
    // sort tăng dần dựa vào price
    public ArrayList<FeastMenu> sortAscendingByPrice(ArrayList<FeastMenu> menuList ) {
        // clone
        ArrayList<FeastMenu> sortList = new ArrayList<>(menuList);
        // Sắp xếp danh sách dựa vào price
        // Tạo anh sắp xếp
        Comparator orderByPrice = new Comparator<FeastMenu>() {
            @Override
            public int compare(FeastMenu t1, FeastMenu t2) {
                return t1.getPrice() > t2.getPrice() ? 1 : -1;
            }
        };
        // Sap xep
        Collections.sort(sortList, orderByPrice);
        return sortList;
    }
    
    // display
    public void displayFeastMenus(ArrayList<FeastMenu> menuList) {
         ArrayList<FeastMenu> tmp = new ArrayList<>(menuList);
        String str = String.format(
                  "  |------------------------------------------------------------------|\n"
                + "              List of Set Menus for ordering party:\n"
                + "  |------------------------------------------------------------------|\n");

        System.out.println(str);
        
        for (FeastMenu fm : tmp) {
            // Chặt ra riêng các món ăn để hiển thị cho đẹp
            StringTokenizer st = new StringTokenizer(fm.getIngredients(), "#");
            String appetizer = st.nextToken().trim().substring(1);
            String mainCourse = st.nextToken().trim();
            String desert = st.nextToken().trim();
            String updateDesrt = desert.substring(0, desert.length() - 1);
            // ----------------------------------------------------------
            DecimalFormat formatter = new DecimalFormat("#,###,### vnd");
            String formattedFee = formatter.format(fm.getPrice());
            String str2 = String.format(
                      "Code: %s\n"
                    + "Name: %s\n"
                    + "Price: %s\n"
                    + "Ingredients:  \n   "
                    + "%s\n"
                    + "   %s\n"
                    + "   %s\n"
                    + "------------------------------------------------------",
                    fm.getMenuCode(), fm.getMenuName(), formattedFee, appetizer, mainCourse, updateDesrt);
            System.out.println(str2);
        }
    }
    
}
