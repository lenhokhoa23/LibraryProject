package org.example.javaapplicationproject;

public class Menu {
    // Hiển thị menu cho Admin
    public static void showAdminMenu() {
        System.out.println("Chào Admin! Các chức năng khả dụng:");
        System.out.println("[1] Thêm sách");
        System.out.println("[2] Xóa sách");
        System.out.println("[3] Tìm sách");
        System.out.println("[4] Xoá người dùng");
        System.out.println("[5] Tìm người dùng");
        System.out.println("[6] Thoát");
    }

    // Hiển thị menu cho User
    public static void showUserMenu() {
        System.out.println("Chào User! Các chức năng khả dụng:");
        System.out.println("[1] Tìm sách");
        System.out.println("[2] Tìm người dùng");
        System.out.println("[3] Mượn sách");
        System.out.println("[4] Thoát");
    }

    public static void showFindMenu() {
        System.out.println("Bạn muốn tìm sách như thế nào?:");
        System.out.println("[1] Tìm sách theo tên");
        System.out.println("[2] Tìm sách theo thể loại");
        System.out.println("[3] Tìm sách theo tác giả");
        System.out.println("[4] Trở lại :3");
    }
}
