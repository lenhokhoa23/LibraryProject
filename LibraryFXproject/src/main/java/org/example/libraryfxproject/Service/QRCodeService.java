package org.example.libraryfxproject.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.example.libraryfxproject.Model.Book;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class QRCodeService {
    private static QRCodeService qrCodeService;

    private QRCodeService() {
    }

    public static synchronized QRCodeService getInstance() {
        if (qrCodeService == null) {
            qrCodeService = new QRCodeService();
        }
        return qrCodeService;
    }

    /**
     * Tạo mã QR cho một cuốn sách.
     * @param book  Cuốn sách cần tạo mã QR
     * @param width Chiều rộng của mã QR
     * @param height Chiều cao của mã QR
     * @return Mã QR dưới dạng hình ảnh
     * @throws IOException Lỗi khi đọc/ghi tệp
     * @throws WriterException Lỗi khi tạo mã QR
     */
    public Image generateQRCode(Book book, int width, int height) throws IOException, WriterException {
        String qrContent = String.format("Book: %s\nAuthor: %s\nISBN: %s\nPrice: %s",
                book.getTitle(),
                book.getAuthor(),
                book.getISBN(),
                book.getPrice());

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();

        return new Image(new ByteArrayInputStream(qrCodeBytes));
    }

    /**
     * Lưu mã QR vào tệp.
     * @param book      Cuốn sách cần tạo mã QR
     * @param filePath Đường dẫn tệp để lưu mã QR
     * @throws WriterException Lỗi khi tạo mã QR
     * @throws IOException Lỗi khi ghi tệp
     */
    public void saveQRCodeToFile(Book book, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String qrContent = String.format("Book: %s\nAuthor: %s\nISBN: %s\nPrice: %s",
                book.getTitle(),
                book.getAuthor(),
                book.getISBN(),
                book.getPrice());
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
        java.nio.file.Path path = java.nio.file.Paths.get(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
