package view.controllers.pages.user;

import controller.BaseController;
import controller.UserController;
import dto.UserDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.controllers.ControllerAware;
import view.controllers.SidebarControllerAware;
import view.controllers.components.SidebarViewController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;


public class RegistrationViewController implements ControllerAware, SidebarControllerAware {
	private static final String ERROR_TITLE = "error.title";
	private static final String WARNING_TITLE = "warning.title";

	private BaseController baseController;
	private UserController userController;
	private SidebarViewController sidebarViewController;
	private ResourceBundle viewText;

	@FXML
	private VBox registrationVBox;
	@FXML
	private Label registrationLabel;
	@FXML
	private HBox dobRoleHBox;
	@FXML
	private HBox buttonsHBox;
	@FXML
	private Button backToLoginButton;
	@FXML
	private Button registerButton;
	@FXML
	private Label firstNameLabel;
	@FXML
	private Label lastNameLabel;
	@FXML
	private Label socialNumberLabel;
	@FXML
	private Label usernameLabel;
	@FXML
	private Label registerPasswordLabel;
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField socialNumberField;
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField registerPasswordField;
	@FXML
	private DatePicker dobPicker;
	@FXML
	private ComboBox<String> roleComboBox;

	@Override
	public void setBaseController(BaseController baseController) {
		this.baseController = baseController;
		this.userController = baseController.getUserController();
		this.viewText = baseController.getLocaleController().getUIBundle();
	}

	@Override
	public void setSidebarViewController(SidebarViewController sidebarViewController) {
		this.sidebarViewController = sidebarViewController;
	}

	@FXML
	public void initialize() {
		Platform.runLater(() -> {
			registrationLabel.setText(viewText.getString("register.title"));
			registerButton.setText(viewText.getString("register.register"));
			backToLoginButton.setText(viewText.getString("register.backToLogin"));
			firstNameLabel.setText(viewText.getString("register.firstName"));
			lastNameLabel.setText(viewText.getString("register.lastName"));
			socialNumberLabel.setText(viewText.getString("register.socialNumber"));
			dobPicker.setPromptText(viewText.getString("register.dateOfBirth"));
			registerPasswordLabel.setText(viewText.getString("register.password"));
			usernameLabel.setText(viewText.getString("register.username"));
			roleComboBox.setPromptText(viewText.getString("register.selectRole"));
			roleComboBox.setItems(FXCollections.observableArrayList(
					viewText.getString("register.student"),
					viewText.getString("register.teacher")));
		});

		// Setup the DatePicker to disable today's and future dates.
		dobPicker.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				if (!date.isBefore(LocalDate.now())) {
					setDisable(true);
					setStyle("-fx-background-color: #FFE0E9;");
				}
			}
		});

		socialNumberField.addEventFilter(KeyEvent.KEY_TYPED, this::handleSocialNumber);
	}

	@FXML
	private void handleRegister() {
		try {
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String socialNumber = socialNumberField.getText();
			String username = usernameField.getText();
			String password = registerPasswordField.getText();
			String role = roleComboBox.getValue();
			LocalDateTime dateOfBirth = null;

			if (dobPicker.getValue() != null) {
				dateOfBirth = dobPicker.getValue().atTime(0, 0);
			}

			if (firstName.isEmpty() || lastName.isEmpty() || socialNumber.isEmpty() || username.isEmpty() ||
			    password.isEmpty() || dobPicker.getValue() == null || role == null) {
				showAlert(viewText.getString(ERROR_TITLE), viewText.getString("error.fillAllFields"));
				return;
			}

			if (!firstName.matches("^\\w+(\\s\\w+)*$") || !lastName.matches("^\\w+(\\s\\w+)*$")) {
				showAlert(viewText.getString(WARNING_TITLE), viewText.getString("warning.fullName"));
				return;
			}

			if (socialNumber.length() != 11) {
				showAlert(viewText.getString(WARNING_TITLE), viewText.getString("warning.socialNumber"));
				return;
			}

			if (userController.isUsernameTaken(username)) {
				showAlert(viewText.getString(ERROR_TITLE), viewText.getString("error.username"));
				return;
			}

			if (userController.isSocialNumberTaken(socialNumber)) {
				showAlert(viewText.getString(ERROR_TITLE), viewText.getString("error.socialNumber"));
				return;
			}

			if (password.length() < 8) {
				showAlert(viewText.getString(WARNING_TITLE), viewText.getString("warning.password"));
				return;
			}

			UserDTO userDTO = new UserDTO(username, password, firstName, lastName, dateOfBirth,
			                              socialNumber.toUpperCase(), role.toUpperCase());

			if (userController.registerUser(userDTO)) {
				userController.authenticateUser(username, password);
				sidebarViewController.updateUserButtons();
				Stage stage = (Stage) firstNameField.getScene().getWindow();
				stage.close();
			} else {
				showAlert(viewText.getString(ERROR_TITLE), viewText.getString("error.invalidUserData"));
			}
		} catch (Exception e) {
			showAlert(viewText.getString(ERROR_TITLE), viewText.getString("error.unexpectedError") + e.getMessage());
		}
	}

	@FXML
	private void goToLoginPage() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layouts/pages/user/login-page.fxml"));
			Parent parent = fxmlLoader.load();

			LoginViewController loginViewController = fxmlLoader.getController();
			loginViewController.setBaseController(baseController);
			loginViewController.setSidebarViewController(sidebarViewController);

			Stage stage = (Stage) firstNameField.getScene().getWindow();
			stage.setTitle(viewText.getString("login.title"));
			Scene scene = firstNameField.getScene();
			scene.setRoot(parent);
		} catch (IOException e) {
			showAlert(viewText.getString(ERROR_TITLE), viewText.getString("error.unexpectedError") + e.getMessage());
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void handleSocialNumber(KeyEvent keyEvent) {
		String character = keyEvent.getCharacter();
		String text = socialNumberField.getText();

		if (!character.matches("\\d|\\w") || text.length() >= 11) {
			keyEvent.consume();
			return;
		}

		int caretPosition = socialNumberField.getCaretPosition();
		socialNumberField.setText(text + character);
		socialNumberField.positionCaret(caretPosition + 1);
		keyEvent.consume();
	}
}