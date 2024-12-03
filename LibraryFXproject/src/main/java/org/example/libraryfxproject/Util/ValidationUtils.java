package org.example.libraryfxproject.Util;

import java.util.regex.Pattern;

public class ValidationUtils {
    private static final String ISBN_PATTERN = "^(\\d{10}|\\d{9}X|\\d{13})$";
    private static final String TITLE_PATTERN = "^[A-Za-z0-9 ]+$";
    private static final String SUBJECT_PATTERN = "^[A-Za-z ]+$";
    private static final String CATEGORY_PATTERN = "^[A-Za-z ]+$";
    private static final String URL_PATTERN = "^(http:\\/\\/|https:\\/\\/)([\\w\\-]+\\.)+[\\w\\-]+(\\/[^\\s]*)?$";
    private static final String AUTHOR_PATTERN = "^[A-Za-z. ]+$";
    private static final String USERNAME_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*_\\S]{8,20}$";
    private static final String PHONE_PATTERN = "^0\\d{9,}$";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@gmail\\.com$";

    public static boolean isValidUsername(String username) {
        return Pattern.matches(USERNAME_PATTERN, username);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_PATTERN, phoneNumber);
    }

    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    public static boolean isValidAuthor(String author) {
        return Pattern.matches(AUTHOR_PATTERN, author);
    }

    public static boolean isValidURL(String url) {
        return Pattern.matches(URL_PATTERN, url);
    }

    public static boolean isValidCategory(String category) {
        return Pattern.matches(CATEGORY_PATTERN, category);
    }

    public static boolean isValidSubject(String subject) {
        return Pattern.matches(SUBJECT_PATTERN, subject);
    }

    public static boolean isValidTitle(String title) {
        return Pattern.matches(TITLE_PATTERN, title);
    }

    public static boolean isValidISBN(String ISBN) {
        return Pattern.matches(ISBN_PATTERN, ISBN);
    }

}
