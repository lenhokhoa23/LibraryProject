package org.example.javaapplicationproject;

public class Menu {
    // Hiển thị menu cho Admin
    public static void showAdminMenu() {
        System.out.println("Chào Admin! Các chức năng khả dụng:");
        System.out.println("[1] Thêm sách");
        System.out.println("[2] Xóa sách");
        System.out.println("[3] Sửa sách");
        System.out.println("[4] Tìm sách");
        System.out.println("[5] Xoá người dùng");
        System.out.println("[6] Tìm người dùng");
        System.out.println("[7] Xem giá sách người dùng");
        System.out.println("[8] Xem trạng thái sách trong kho");
        System.out.println("[9] Thoát");
    }

    // Hiển thị menu cho User
    public static void showUserMenu() {
        System.out.println("Chào User! Các chức năng khả dụng:");
        System.out.println("[1] Tìm sách");
        System.out.println("[2] Tìm người dùng");
        System.out.println("[3] Mượn sách");
        System.out.println("[4] Hủy mượn sách");
        System.out.println("[5] Hiển thị giá sách cá nhân");
        System.out.println("[6] Thoát");
    }

    public static void showFindMenu() {
        System.out.println("Bạn muốn tìm sách như thế nào?:");
        System.out.println("[1] Tìm sách theo tên");
        System.out.println("[2] Tìm sách theo thể loại");
        System.out.println("[3] Tìm sách theo tác giả");
        System.out.println("[4] Tìm sách nhưng chỉ nhớ 1 phần tên sách");
        System.out.println("[5] Trở lại :3");
    }


    public static void showBorrowingPeriod() {
        System.out.println("Chọn thời gian mượn sách:");
        System.out.println("1. 1 tuần");
        System.out.println("2. 2 tuần");
        System.out.println("3. 1 tháng");
    }
    public static void showLoginMenu () {
        System.out.println("Let's choose your option!!!");
        System.out.println("[0] Login");
        System.out.println("[1] Register");
        System.out.println("[2] Exit");
    }
}
