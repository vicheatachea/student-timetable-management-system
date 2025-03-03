package view.controllers.pages.user;

import controller.UserController;
import dto.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class UserProfileController {
	private UserController userController;
	private Stage stage;

	@FXML
	private ImageView profileImageView;
	@FXML
	private Label nameLabel;
	@FXML
	private Label roleLabel;
	@FXML
	private Label socialNumberLabel;

	public void setUserController(UserController userController) {
		this.userController = userController;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	private void handleLogout() {
		userController.logout();
		stage.close();
	}
	
	@FXML
	public void initialize() {
	}

	@FXML
	public void updateUserInfo() {
		if (userController != null) {
			UserDTO userDTO = userController.getLoggedInUser();
			nameLabel.setText(userDTO.firstName() + " " + userDTO.lastName());
			roleLabel.setText(userDTO.role());
			socialNumberLabel.setText(userDTO.socialNumber());
		}
	}
}