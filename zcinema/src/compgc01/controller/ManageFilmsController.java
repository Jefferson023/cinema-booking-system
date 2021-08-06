package compgc01.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;

import compgc01.model.Film;
import compgc01.model.Main;
import compgc01.model.SceneCreator;
import compgc01.service.FilmeServico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * The controller for the Films Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 12.12.2017
 */
public class ManageFilmsController {

	private File selectedImage;

	@FXML
	Button backButton;
	@FXML
	Text newFilmTitle, newFilmDescription, newFilmStartDate, newFilmEndDate, newFilmTime1, newFilmTime2, newFilmTime3;
	@FXML
	TextArea filmDescription;
	@FXML
	DatePicker filmStartDate, filmEndDate;
	@FXML
	TextField filmTitle, filmTrailer;
	@FXML
	ComboBox<String> filmTime1, filmTime2, filmTime3;
	@FXML
	ImageView uploadedFilmPoster;

	FilmeServico filmeServico = new FilmeServico();
	
	@FXML
	void initialize() throws IOException {
		
		filmTrailer.setPromptText("Enter trailer link here...");

		ObservableList<String> obsList = FXCollections.observableArrayList("13:00", "14:00", "15:00", "16:00", "17:00",
				"18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00", "01:00", "02:00", "03:00");
		filmTime1.setItems(obsList);
		filmTime2.setItems(obsList);
		filmTime3.setItems(obsList);
		filmTime1.setValue("21:00");
		newFilmTime1.setText("21:00");
		filmStartDate.setValue(LocalDate.now());
		newFilmStartDate.setText(LocalDate.now().toString());
	}

	@FXML
	public void launchViewFilms(ActionEvent event) throws IOException {

		SceneCreator.launchScene("/scenes/ViewFilmsScene.fxml");
	}

	@FXML
	public void backToPrevScene(ActionEvent event) throws IOException {

		SceneCreator.launchScene("/scenes/UserScene.fxml");
	}

	/**
	 * Method that gets called every time the user enters a date or select a time when adding a new movie 
	 * @param e Action Event, being the action of selecting an item from the ComboBox or DatePicker
	 */
	@FXML
	public void updateDateAndTime(ActionEvent e) {

		try {
			switch (((Node) e.getSource()).getId()) {
			case "filmStartDate":
				newFilmStartDate.setText(filmStartDate.getValue().toString());
				break;
			case "filmEndDate":
				newFilmEndDate.setText(filmEndDate.getValue().toString());
				break;
			case "filmTime1":
				newFilmTime1.setText(filmTime1.getValue().toString());
				break;
			case "filmTime2":
				newFilmTime2.setText(filmTime2.getValue().toString());
				break;
			case "filmTime3":
				newFilmTime3.setText(filmTime3.getValue().toString());
				break;
			}
		} catch (NullPointerException ex) {
			ex.getMessage();
		}
	}

	/**
	 * Method that gets called every time the user types in any TextField when adding a movie 
	 * @param e KeyEvent, being the action of typing a key in the TextField
	 */
	@FXML
	public void updateFilmText(KeyEvent e) {

		switch (((Node) e.getSource()).getId()) {
		case "filmTitle":
			if (filmTitle.getText().length() > 20) {
				filmTitle.setEditable(false);
			}
			break;
		case "filmDescription":
			if (filmDescription.getText().length() > 220) {
				filmDescription.setEditable(false);
			}
			break;
		}

		if (e.getCode().equals(KeyCode.BACK_SPACE)) {
			filmTitle.setEditable(true);
			filmDescription.setEditable(true);
		}

		switch (((Node) e.getSource()).getId()) {
		case "filmTitle":
			newFilmTitle.setText(filmTitle.getText());
			break;
		case "filmDescription":
			newFilmDescription.setText(filmDescription.getText());
			break;
		}
	}

	/**
	 * Method that gets called when the user clicks on the Button  
	 * @param e KeyEvent, being the action of typing a key in the TextField
	 */
	@FXML
	public void uploadImageClick(ActionEvent event) throws IOException {

		try {
			FileChooser fc = new FileChooser();
			selectedImage = fc.showOpenDialog(null);
			// checking that input file is not null and handling the exception
			if (selectedImage == null)
				return;
			else if (ImageIO.read(selectedImage) == null) {
				Alert alert = new Alert(AlertType.WARNING, "Please upload an image in JPG or PNG format!",
						ButtonType.OK);
				alert.showAndWait();
				if (alert.getResult() == ButtonType.OK) {
					return;
				}
			} else {
				Image img = SwingFXUtils.toFXImage(ImageIO.read(selectedImage), null);
				uploadedFilmPoster.setImage(img);
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * This method writes the film information into a designed JSON file and, saves the film poster
	 * in a designed folder, and restore the main fields back to their default values
	 * @param ActionEvent event
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@FXML
	public void storeFilmInfo(ActionEvent event) throws ParseException {

		try {
			validateFilmInput();

			Alert alert = new Alert(AlertType.INFORMATION, "The film " + filmTitle.getText() + " has been added!",
					ButtonType.OK);
			
			alert.showAndWait();
			
			List<String> schedules = new ArrayList<String>();
			
			if (!newFilmTime1.getText().equals("hh:mm")) {
				schedules.add(newFilmTime1.getText());
			}
			if (!newFilmTime2.getText().equals("hh:mm")) {
				schedules.add(newFilmTime2.getText());
			}
			if (!newFilmTime3.getText().equals("hh:mm")) {
				schedules.add(newFilmTime3.getText());
			}
			
			filmeServico.cadastrar(filmTitle.getText(), filmDescription.getText(), filmTrailer.getText(),
					newFilmStartDate.getText(), newFilmEndDate.getText(), schedules, 
					Files.readAllBytes(selectedImage.toPath()), Main.getToken());
			
			if (alert.getResult() == ButtonType.OK) {
				//Main.resetFilmList();
				filmDescription.setText("");
				filmTitle.setText("");
				filmStartDate.setPromptText("yyyy-mm-dd");
				filmEndDate.setPromptText("yyyy-mm-dd");
				filmTime1.setPromptText("hh:mm");
				filmTime2.setPromptText("hh:mm");
				filmTime3.setPromptText("hh:mm");
				alert.close();
			}
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(AlertType.WARNING, "File Not Found!", ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
			}
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.WARNING, "Error: " + e.getMessage(), ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
			}
		} catch (InvalidFilmInputException e) {
			Alert alert = new Alert(AlertType.WARNING, e.getMessage(), ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
			}
		}
	}

	/**
	 * Method that validates all fields before allowing the employee to add a new movie
	 * @throws the method throws an exception when one of the fields is empty, and when the 
	 * initial and final screening dates and times are not set properly or overlap with those of
	 * already stored movies
	 * @throws the second handled exception regards the Parser. Indeed, the initial and final screening
	 * dates of movies already stored in the JSON files are converted from String to LocalDate.
	 */
	@SuppressWarnings("unlikely-arg-type")
	void validateFilmInput() throws InvalidFilmInputException, ParseException {

		try {
			if (filmTitle.getText().equals("") || filmDescription.getText().equals("")
					|| filmTrailer.getText().equals("") || filmStartDate.getValue().equals("yyyy-mm-dd")
					|| filmEndDate.getValue().equals("yyyy-mm-dd"))
				throw new InvalidFilmInputException("Please complete all fields!");
			else if (selectedImage == null)
				throw new InvalidFilmInputException("Please add the film poster!");
			else if (filmStartDate.getValue().compareTo(LocalDate.now()) < 0)
				throw new InvalidFilmInputException("Start date cannot be before today!");
			else if (filmStartDate.getValue().compareTo(filmEndDate.getValue()) == 0)
				throw new InvalidFilmInputException("Screenings cannot start and end on the same day!");
			else if (filmStartDate.getValue().compareTo(filmEndDate.getValue()) > 0)
				throw new InvalidFilmInputException("End date cannot be before start date!");
		} catch (NullPointerException e) {
			throw new InvalidFilmInputException("Please complete all fields!");
		}
	}
}

class InvalidFilmInputException extends Exception {

	private static final long serialVersionUID = 1L;

	InvalidFilmInputException(String s) {
		super(s);
	}
}