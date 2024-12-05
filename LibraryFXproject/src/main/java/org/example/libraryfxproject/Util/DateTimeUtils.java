package org.example.libraryfxproject.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lớp tiện ích cho việc xử lý và định dạng ngày giờ.
 */
public class DateTimeUtils {

    //Format cho date
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d-MMM-yy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Constructor
    private DateTimeUtils() {
    }

    /**
     * Định dạng ngày thành chuỗi.
     * @param date Ngày cần định dạng
     * @return Chuỗi ngày đã định dạng
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    /**
     * Định dạng ngày giờ thành chuỗi.
     * @param dateTime Ngày giờ cần định dạng
     * @return Chuỗi ngày giờ đã định dạng
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_TIME_FORMATTER) : "";
    }

    /**
     * Phân tích chuỗi thành đối tượng LocalDate.
     * @param dateString Chuỗi ngày cần phân tích
     * @return Đối tượng LocalDate
     */
    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }

    /**
     * Phân tích chuỗi thành đối tượng LocalDateTime.
     * @param dateTimeString Chuỗi ngày giờ cần phân tích
     * @return Đối tượng LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
    }

    /**
     * Kiểm tra xem ngày có thuộc quá khứ không.
     * @param date Ngày cần kiểm tra
     * @return true nếu ngày là quá khứ, ngược lại false
     */
    public static boolean isDateInPast(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    /**
     * Thêm số ngày vào một ngày.
     * @param date Ngày gốc
     * @param days Số ngày cần thêm
     * @return Ngày mới sau khi thêm
     */
    public static LocalDate addDaysToDate(LocalDate date, long days) {
        return date.plusDays(days);
    }

}
