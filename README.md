# Library Management System
## **Giới thiệu chung**
Bài tập lớn thiết kế app quản lý thư viện.  
Môn học OOP - INT2204 18, Nhóm 13

- **EER Diagram:** 
![image](https://github.com/user-attachments/assets/5d881e41-8d90-4339-9643-524eeff011aa)

> **Sơ đồ thiết kế:** [Link Google Drive](https://drive.google.com/file/d/1rGBA92pjuLWtgmDpb62q98R7Uro14WyK/view?usp=sharing)

---

## **Mục lục**
1. [Giới thiệu chung](#giới-thiệu-chung)
2. [Các thành viên tham gia](#các-thành-viên-tham-gia)
3. [Mục tiêu dự án](#mục-tiêu-dự-án)
4. [Các tính năng chính](#các-tính-năng-chính)
5. [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
6. [Cài đặt và chạy dự án](#cài-đặt-và-chạy-dự-án)
   - [1. Cài đặt phần mềm](#1-cài-đặt-phần-mềm)
   - [2. Cài đặt dự án](#2-cài-đặt-dự-án)
7. [Phân chia công việc](#phân-chia-công-việc)

## **Các thành viên tham gia**
- **Lê Nho Khoa** - Nhóm trưởng
- **Nguyễn Anh Khang** - Thành viên
- **Khổng Quốc Anh** - Thành viên

## **Mục tiêu dự án**
Dự án này nhằm tạo ra một hệ thống quản lý thư viện giúp người dùng dễ dàng mượn và trả sách, quản lý thông tin người dùng và sách, hỗ trợ tìm kiếm và các chức năng tiện ích khác.


## **Các tính năng chính**
- **Hiển thị thông tin tổng quan:** Biểu đồ thống kê, lịch sử hoạt động, danh sách sách và người dùng.
- **Quản lý sách:** Thêm, sửa, xóa sách, tìm kiếm sách theo tiêu chí.
- **Quản lý người dùng:** Thêm, tìm kiếm người dùng, xóa người dùng, xuất danh sách người dùng.
- **Mượn và trả sách:** Kiểm tra điều kiện mượn sách, xử lý các giao dịch mượn/trả.
- **Thông báo cá nhân:** Cảnh báo các vấn đề liên quan đến mượn/trả sách.
- **Cộng đồng sách:** Tính năng trao đổi, thảo luận sách giữa các thành viên.
- **Tích hợp Google Books API:** Tìm kiếm và lấy thông tin sách từ Google Books.


## **Yêu cầu hệ thống**
- Java 8+ 
- Maven để quản lý các phụ thuộc


## **Cài đặt và chạy dự án**

### **1. Cài đặt phần mềm**
- Cài đặt [JDK 8+](https://adoptopenjdk.net/) (Java Development Kit).
- Cài đặt [Maven](https://maven.apache.org/)
- Cài đặt IDE hỗ trợ Java [IntelliJ IDEA](https://www.jetbrains.com/idea/)

### **2. Cài đặt dự án**
Clone repository về máy: git clone https://github.com/lenhokhoa23/LibraryProject.git

## **Phân chia công việc**

|                         | **Công việc**                                                                                                                   |
|-------------------------|-------------------------------------------------------------------------------------------------------------------------------|
| **Lê Nho Khoa**         | - Hiển thị danh sách người dùng<br>- Thêm, tìm kiếm người dùng<br>- Xuất danh sách người dùng<br>- Tìm kiếm sách<br>- Cộng đồng sách<br>- Thiết kế giao diện UserView, MainMenuView<br>- Thiết kế Design Pattern<br>- Tích hợp GoogleBookAPI, QRCode, comment sách |
| **Nguyễn Anh Khang**    | - Hiển thị thông tin tổng quan, biểu đồ<br>- Hiển thị bảng hoạt động gần đây, lịch sử hoạt động <br>- Dịch vụ mượn, trả sách ở UserView, MainMenuView<br>- Chỉnh sửa sách, người dùng<br>- Giá sách cá nhân<br>- Notification cá nhân<br>- Thiết kế giao diện đăng ký<br>- Kiểm tra điều kiện khi mượn, trả sách |
| **Khổng Quốc Anh**      | - Hiển thị danh sách sách ở UserView, MainMenuView<br>- Thêm, xóa sách<br>- Xóa người dùng<br>- Filter sách<br>- Hồ sơ người dùng<br>- JUnit Test<br>- Exception<br>- Kiểm tra điều kiện khi mượn, trả sách |
| **Chung**               | - Thiết kế giao diện đăng nhập<br>- Package DAO<br> |


