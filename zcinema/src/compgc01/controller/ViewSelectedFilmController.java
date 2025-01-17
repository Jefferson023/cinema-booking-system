package compgc01.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import compgc01.model.BookingHistoryItem;
import compgc01.model.Film;
import compgc01.model.Main;
import compgc01.model.SceneCreator;
import compgc01.service.FilmeServico;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * The controller for the View Selected Film Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 14.12.2017
 */
public class ViewSelectedFilmController implements Initializable {

	Film selectedFilm = null;
	File imgFile = null;
	Desktop desktop = Desktop.getDesktop();

	@FXML
	ImageView selectedFilmPoster;
	@FXML
	Text title;
	@FXML
	Text description;
	@FXML
	Text startDate;
	@FXML
	Text endDate;
	@FXML
	Text time;
	@FXML
	Button backButton, bookButton, deleteFilmButton;

	private FilmeServico filmeServico = new FilmeServico();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (Main.isEmployee())
			bookButton.setText("Go to Bookings");

		selectedFilm = Main.getSelectedFilm();
		// System.out.println(Main.getSelectedFilmTitle());
		selectedFilmPoster.setImage(Main.getSelectedFilmImage());
		
		title.setText(selectedFilm.getTitle());
		description.setText(selectedFilm.getDescription());
		startDate.setText(selectedFilm.getStartDate());
		endDate.setText(selectedFilm.getEndDate());

		String displayedTimes = "";
		for (int i = 0; i < selectedFilm.getSchedules().size(); i++) {
			if (!selectedFilm.getSchedules().get(i).equals("hh:mm"))
				displayedTimes += selectedFilm.getSchedules().get(i) + ", ";
		}
		time.setText(displayedTimes.substring(0, displayedTimes.length() - 2));

		if (!Main.isEmployee())
			deleteFilmButton.setVisible(false);

		selectedFilmPoster.setOnMouseClicked((event) -> {

			try {
				for (Film film : Main.getFilmList()) {
					if (film.getTitle().equals(title.getText()))
						desktop.browse(new URI(film.getTrailer()));
				}
			} catch (IOException | URISyntaxException e) {
			}
		});
	}

	/**
	 * Extra feature that allows the user to delete a movie from the list
	 * 
	 * @param ActionEvent event
	 */
	@FXML
	public void deleteFilm(ActionEvent event) throws IOException {

		Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to delete this movie?", ButtonType.NO,
				ButtonType.YES);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {

//			for (BookingHistoryItem booking : Main.getBookingList()) {
//
//				// if there is a booking for the selected film
//				// and if the booking's date is in the future
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//				if (booking.getFilm().equals(Main.getSelectedFilm()) && !booking.getStatus().equals("cancelled")
//						&& LocalDate.parse(booking.getDate(), formatter).compareTo(LocalDate.now()) >= 0) {
//					Alert existingBookingAlert = new Alert(AlertType.WARNING,
//							"You cannot delete a film with future bookings!", ButtonType.OK);
//					existingBookingAlert.showAndWait();
//					if (existingBookingAlert.getResult() == ButtonType.OK) {
//						existingBookingAlert.close();
//						return;
//					}
//				}
//			}

			// if there are no future booking for the selected film
			// the employee can safely delete it
			//Main.modifyJSONFile("filmsJSON.txt", selectedFilm.getTitle(), "", "delete");
			//imgFile.delete();

			//Main.resetFilmList();
			//Main.readJSONFile("filmsJSON.txt");
			try {
				filmeServico.remover(Main.getSelectedFilm(), Main.getToken());
			} catch (Exception e) {
				backToPrevScene(event);
			}
			backToPrevScene(event);
		} else {
			return;
		}
	}

	@FXML
	public void goToBookingScene(ActionEvent event) throws IOException {

		SceneCreator.launchScene("/scenes/ManageBookingsScene.fxml");
	}

	@FXML
	public void backToPrevScene(ActionEvent event) throws IOException {

		SceneCreator.launchScene("/scenes/ViewFilmsScene.fxml");
	}
}