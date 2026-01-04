package Controller;

import Model.MysqlConnector;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtHidePassword;

    @FXML
    private TextField txtShowPassword;

    @FXML
    private ImageView Open_Eye_Icon;

    @FXML
    private ImageView Close_Eye_Icon;

    @FXML
    private Button loginButton;

    private String password = "";

    @FXML
    public void initialize() {
        // Mặc định: ẩn show password, hiển thị hide password
        txtShowPassword.setVisible(false);
        Open_Eye_Icon.setVisible(false);
        Close_Eye_Icon.setVisible(true);

        // Gán Enter key cho login
        Platform.runLater(() -> {
            txtUserName.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    loginButton.fire();
                }
            });
        });
    }

    // ================== Toggle Eye ==================
    @FXML
    public void Close_Eye_ClickOnAction(MouseEvent event) {
        txtShowPassword.setVisible(true);
        txtHidePassword.setVisible(false);
        Close_Eye_Icon.setVisible(false);
        Open_Eye_Icon.setVisible(true);
        txtShowPassword.setText(txtHidePassword.getText());
        txtShowPassword.requestFocus();
        txtShowPassword.positionCaret(txtShowPassword.getText().length());
    }

    @FXML
    public void Open_Eye_ClickOnAction(MouseEvent event) {
        txtShowPassword.setVisible(false);
        txtHidePassword.setVisible(true);
        Open_Eye_Icon.setVisible(false);
        Close_Eye_Icon.setVisible(true);
        txtHidePassword.setText(txtShowPassword.getText());
        txtHidePassword.requestFocus();
        txtHidePassword.positionCaret(txtHidePassword.getText().length());
    }

    // ================== Sync TextFields ==================
    @FXML
    public void HidePasswordOnAction(KeyEvent event) {
        password = txtHidePassword.getText();
        txtShowPassword.setText(password);
    }

    @FXML
    public void ShowPasswordOnAction(KeyEvent event) {
        password = txtShowPassword.getText();
        txtHidePassword.setText(password);
    }

    // ================== LOGIN ==================
    @FXML
    public void CheckLogin(ActionEvent event) {
        String userName = txtUserName.getText();
        password = txtHidePassword.isVisible() ? txtHidePassword.getText() : txtShowPassword.getText();

        if (userName.isEmpty() || password.isEmpty()) {
            ControllerUtil2.showErrorMessage("Vui lòng nhập đầy đủ thông tin để đăng nhập!");
            return;
        }

        try {
            Connection connection = MysqlConnector.getInstance().getConnection();
            if (connection == null) {
                ControllerUtil2.showErrorMessage("Lỗi kết nối với cơ sở dữ liệu.");
                return;
            }

            String sql = "SELECT * FROM user WHERE UserName = ? AND Password = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, userName);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // ===== Load HomeView =====
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/HomeView.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);

                    // Căn giữa, không resize
                    stage.sizeToScene();
                    stage.centerOnScreen();
                    stage.setResizable(false);
                    stage.show();
                } else {
                    ControllerUtil2.showErrorMessage("Sai tên đăng nhập hoặc mật khẩu.");
                }
            }
        } catch (SQLException | java.io.IOException e) {
            e.printStackTrace();
            ControllerUtil2.showErrorMessage("Lỗi kết nối với cơ sở dữ liệu.");
        }
    }
}
