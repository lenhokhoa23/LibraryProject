package org.example.javaapplicationproject;

public class Menu {
    // Hiển thị menu cho Admin
    public static void showAdminMenu() {
        System.out.println("Chào Admin! Các chức năng khả dụng:");
        System.out.println("[1] Thêm sách");
        System.out.println("[2] Xóa sách");
        System.out.println("[3] Tìm sách");
        System.out.println("[4] Xoá người dùng");
        System.out.println("[5] Thoát");
    }

    // Hiển thị menu cho User
    public static void showUserMenu() {
        System.out.println("Chào User! Các chức năng khả dụng:");
        System.out.println("[1] Tìm sách");
        System.out.println("[2] Thoát");
    }
}
