package Controller;

import View.MainView;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Lớp tiện ích hỗ trợ các Controller khác
 */
public class ControllerUtil {

    /**
     * Đổi sang một scene mới dựa trên file FXML.
     * @param fxmlFile tên file FXML (ví dụ: "HomeView.fxml")
     * @param title tiêu đề cửa sổ
     */
    public static void ChangeScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(MainView.class.getResource("/View/" + fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage mainStage = MainView.getMainStage();
            mainStage.setScene(scene);
            mainStage.setTitle(title);
            mainStage.centerOnScreen();   // căn giữa cửa sổ
            mainStage.setResizable(true); // cho phép co giãn
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Không thể tải giao diện: " + fxmlFile);
        }
    }

    /**
     * Hiển thị thông báo thành công
     */
    public static void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị thông báo lỗi
     */
    public static void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị hộp thoại xác nhận
     * @return true nếu người dùng chọn OK
     */
    public static boolean showConfirmationDialog(String title, String header) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Kiểm tra chuỗi rỗng hoặc null
     */
    public static boolean isEmptyOrNull(String value) {
        return value == null || value.trim().isEmpty();
    }
}