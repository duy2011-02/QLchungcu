package Controller;

import Model.MysqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Logincontrollerduphong {

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtHidePassword;

    @FXML
    private TextField txtShowPassword;

    @FXML
    private ImageView Open_Eye_Icon;

    @FXML
    private HBox showPassword;

    @FXML
    private HBox hidePassword;

    @FXML
    private ImageView backGround;

    @FXML
    private Button loginButton;

    private String password = "";

    // ================== INITIALIZE ==================
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            showPassword.setVisible(false);
            hidePassword.setVisible(true);

            // Đăng ký Enter để login
            txtUserName.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    loginButton.fire();
                }
            });
        });
    }

    // ================== UI EVENTS ==================
    @FXML
    public void LostFocus(MouseEvent event) {
        backGround.requestFocus();
    }

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

    @FXML
    public void Close_Eye_ClickOnAction(MouseEvent event) {
        showPassword.setVisible(true);
        hidePassword.setVisible(false);
        txtShowPassword.requestFocus();
    }

    @FXML
    public void Open_Eye_ClickOnAction(MouseEvent event) {
        showPassword.setVisible(false);
        hidePassword.setVisible(true);
        txtHidePassword.requestFocus();
    }

    // ================== LOGIN ==================
    @FXML
    public void CheckLogin(ActionEvent event) {
        String userName = txtUserName.getText();
        password = hidePassword.isVisible() ? txtHidePassword.getText() : txtShowPassword.getText();

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
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userName);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // ===== LOAD HOME VIEW =====
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/HomeView.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);

                    // Căn giữa màn hình
                    stage.sizeToScene();
                    stage.centerOnScreen();
                    stage.setResizable(false); // Nếu muốn không cho resize
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
