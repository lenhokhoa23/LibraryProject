package org.example.libraryfxproject.Util;

import java.util.regex.Pattern;

/**
 * Lớp tiện ích để xác thực các trường như ISBN, tiêu đề, chủ đề, danh mục, URL,
 */
public class ValidationUtils {
    private static final String ISBN_PATTERN = "^(\\d{10}|\\d{9}X|\\d{13})$";
    private static final String TITLE_PATTERN = "^[\\p{L}\\p{M}0-9 ]+$";
    private static final String SUBJECT_PATTERN = "^[A-Za-z ]+$";
    private static final String CATEGORY_PATTERN = "^[A-Za-z ]+$";
    private static final String URL_PATTERN = "^(http:\\/\\/|https:\\/\\/)([\\w\\-]+\\.)+[\\w\\-]+(\\/[^\\s]*)?$";
    private static final String AUTHOR_PATTERN = "^[\\p{L}\\p{M}. ]+$";
    private static final String USERNAME_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*_\\S]{8,20}$";
    private static final String PHONE_PATTERN = "^0\\d{9,}$";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@gmail\\.com$";

    /**
     * Xác thực tên người dùng dựa trên mẫu đã được định nghĩa trước.
     * @param username tên người dùng cần xác thực
     * @return true nếu tên người dùng hợp lệ, false nếu không hợp lệ
     */
    public static boolean isValidUsername(String username) {
        return Pattern.matches(USERNAME_PATTERN, username);
    }

    /**
     * Xác thực số điện thoại dựa trên mẫu đã được định nghĩa trước.
     * @param phoneNumber số điện thoại cần xác thực
     * @return true nếu số điện thoại hợp lệ, false nếu không hợp lệ
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_PATTERN, phoneNumber);
    }

    /**
     * Xác thực địa chỉ email dựa trên mẫu đã được định nghĩa trước.
     * @param email địa chỉ email cần xác thực
     * @return true nếu email hợp lệ, false nếu không hợp lệ
     */
    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    /**
     * Xác thực tên tác giả dựa trên mẫu đã được định nghĩa trước.
     * @param author tên tác giả cần xác thực
     * @return true nếu tên tác giả hợp lệ, false nếu không hợp lệ
     */
    public static boolean isValidAuthor(String author) {
        return Pattern.matches(AUTHOR_PATTERN, author);
    }

    /**
     * Xác thực URL dựa trên mẫu đã được định nghĩa trước.
     * @param url URL cần xác thực
     * @return true nếu URL hợp lệ, false nếu không hợp lệ
     */
    public static boolean isValidURL(String url) {
        return Pattern.matches(URL_PATTERN, url);
    }

    /**
     * Xác thực tên danh mục dựa trên mẫu đã được định nghĩa trước.
     * @param category tên danh mục cần xác thực
     * @return true nếu tên danh mục hợp lệ, false nếu không hợp lệ
     */
    public static boolean isValidCategory(String category) {
        return Pattern.matches(CATEGORY_PATTERN, category);
    }

    /**
     * Xác thực chủ đề dựa trên mẫu đã được định nghĩa trước.
     * @param subject chủ đề cần xác thực
     * @return true nếu chủ đề hợp lệ, false nếu không hợp lệ
     */
    public static boolean isValidSubject(String subject) {
        return Pattern.matches(SUBJECT_PATTERN, subject);
    }

    /**
     * Xác thực tiêu đề dựa trên mẫu đã được định nghĩa trước.
     * @param title tiêu đề cần xác thực
     * @return true nếu tiêu đề hợp lệ, false nếu không hợp lệ
     */
    public static boolean isValidTitle(String title) {
        return Pattern.matches(TITLE_PATTERN, title);
    }

    /**
     * Xác thực mã ISBN dựa trên mẫu đã được định nghĩa trước.
     * @param ISBN mã ISBN cần xác thực
     * @return true nếu mã ISBN hợp lệ, false nếu không hợp lệ
     */
    public static boolean isValidISBN(String ISBN) {
        return Pattern.matches(ISBN_PATTERN, ISBN);
    }

}
