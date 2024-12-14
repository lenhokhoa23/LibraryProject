# Library Management System

Một hệ thống quản lý thư viện với các chức năng cơ bản như mượn, trả sách, quản lý người dùng, tìm kiếm sách, và nhiều tính năng hữu ích khác.

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
- Maven  để quản lý các phụ thuộc

## **Cài đặt và chạy dự án**
### **1. Cài đặt phần mềm**
- Cài đặt [JDK 8+](https://adoptopenjdk.net/) (Java Development Kit).
- Cài đặt [Maven](https://maven.apache.org/)
- Cài đặt IDE hỗ trợ Java [IntelliJ IDEA](https://www.jetbrains.com/idea/)
### **2. Cài đặt dự án**
Clone repository về máy tính của bạn:
git clone https://github.com/lenhokhoa23/LibraryProject.git
### **3. Phân chia công việc**
| **Tên**                 | **Lê Nho Khoa**                     | **Nguyễn Anh Khang**             | **Khổng Quốc Anh**             | **Chung**                      |
|-------------------------|-------------------------------------|----------------------------------|--------------------------------|--------------------------------|
| **Công việc**           | - Thiết kế giao diện UserView       | - Hiển thị thông tin tổng quan   | - Hiển thị danh sách sách      | - Thiết kế giao diện đăng nhập |
|                         | - Biểu đồ, thống kê                 | - Hiển thị bảng hoạt động gần đây| - Thêm, xóa sách               | - Viết README chuyên nghiệp   |
|                         | - Lịch sử hoạt động                 | - Dịch vụ mượn/trả sách           | - Xóa người dùng               |                                |
|                         | - Dịch vụ mượn/trả sách             | - Kiểm tra điều kiện mượn/trả sách| - Filter sách                  |                                |
|                         | - Tìm kiếm sách                     |                                 | - Hồ sơ người dùng             |                                |
|                         | - Hiển thị danh sách người dùng     |                                 | - JUnit Test                   |                                |
|                         | - Cộng đồng sách                    |                                 | - Exception Handling           |                                |
|                         | - Tích hợp GoogleBooks API, QRCode  |                                 | - Kiểm tra điều kiện mượn/trả  |                                |
|                         | - Thiết kế Design Pattern           |                                 |                                |                                |
