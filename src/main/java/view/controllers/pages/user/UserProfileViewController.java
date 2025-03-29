package view.controllers.pages.user;

import controller.BaseController;
import controller.UserController;
import dto.UserDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import view.controllers.ControllerAware;
import view.controllers.SidebarControllerAware;
import view.controllers.components.SidebarViewController;
import java.util.ResourceBundle;


public class UserProfileViewController implements ControllerAware, SidebarControllerAware {
	private UserController userController;
	private SidebarViewController sidebarViewController;
	private ResourceBundle viewText;
	private BaseController baseController;

	@FXML
	private ImageView profileImageView;
	@FXML
	private Label nameLabel;
	@FXML
	private Label roleLabel;
	@FXML
	private Label socialNumberLabel;
	@FXML
	private Label myAccountLabel;
	@FXML
	private Button logoutButton;

	@Override
	public void setBaseController(BaseController baseController) {
		this.baseController = baseController;
		this.userController = baseController.getUserController();
		this.viewText = baseController.getLocaleController().getUIBundle();
	}

	public void setSidebarViewController(SidebarViewController sidebarViewController) {
		this.sidebarViewController = sidebarViewController;
	}


	@FXML
	private void handleLogout() {
		userController.logout();
		sidebarViewController.updateUserButtons();
		Stage stage = (Stage) nameLabel.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void initialize() {
		Platform.runLater(() -> {
			myAccountLabel.setText(viewText.getString("userprofile.myAccount"));
			logoutButton.setText(viewText.getString("userprofile.logout"));
		});
	}


	@FXML
	public void updateUserInfo() {
		if (userController == null || !userController.isUserLoggedIn()) {
			showAlert(viewText.getString("error.accountError"), viewText.getString("error.notLoggedIn"));
			return;
		}

		UserDTO userDTO = userController.getLoggedInUser();

		if (userDTO == null) {
			showAlert(viewText.getString("error.accountError"), viewText.getString("error.accountInformationNotFound"));
			userController.logout();
			return;
		}

		nameLabel.setText(userDTO.firstName() + " " + userDTO.lastName());
		roleLabel.setText(userDTO.role());
		socialNumberLabel.setText(userDTO.socialNumber());


	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}