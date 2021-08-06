package compgc01.controller;

import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import compgc01.model.BookingHistoryItem;
import compgc01.model.Customer;
import compgc01.model.Encryption;
import compgc01.model.Film;
import compgc01.model.Main;
import compgc01.model.SceneCreator;
import compgc01.model.Sessao;
import compgc01.model.User;
import compgc01.service.FilmeServico;
import compgc01.service.ReservasServico;
import compgc01.service.UserServico;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The controller for the Bookings Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 12.12.2017
 */
public class ManageBookingsController implements Initializable {

    int bookedSeatsCount;

    @FXML
    static Stage stage;
    @FXML
    GridPane gridSeats;
    @FXML
    Button backButton, giveFeedback;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox<String> filmDropDownList, timeDropDownList, customerDropDownList;
    @FXML
    Label bookedSeatsLabel, availableSeatsLabel, totalSeatsLabel;
    @FXML
    Text customer;
    @FXML
    MaterialIconView A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6, C1, C2, C3, C4, C5, C6;

    FilmeServico filmeServico = new FilmeServico();
    
    UserServico userServico = new UserServico();
    
    ReservasServico reservasServico = new ReservasServico();
    
    List<Film> filmes = new ArrayList<Film>();
    List<User> usuarios = new ArrayList<User>();
    
    private Sessao sessao;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	System.out.print(Main.getToken());
    	try {
			filmes = filmeServico.getAll(Main.getToken());
			if (Main.isEmployee()) {
				usuarios = userServico.getAll(Main.getToken());
			}
			else {
				usuarios.add(Main.getCurrentUser());
			}
		} catch (IOException | org.json.simple.parser.ParseException e1) {
			System.out.print(e1.getMessage());
		}
 
        if(!Main.isEmployee()) {
            customerDropDownList.setVisible(false); 
            customer.setVisible(false);
            giveFeedback.setVisible(true);
        }
        

        // setting the date to the current one in the default time-zone of the system
        datePicker.setValue(LocalDate.now());
        try {
            populateFilmDropDownList(new ActionEvent());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        populateTimeDropDownList(new ActionEvent());
        populateUserDropDownList(new ActionEvent());

        // setting the total number of seats to a value of 18
        totalSeatsLabel.setText("Total seats: 18");
        bookedSeatsCount = 0;
        Main.setSelectedSeats(new ArrayList<String>());

        // getting the most recent version of the bookings file
        Main.resetBookingList();
        Main.resetEmployeeList();
        Main.resetCustomerList();
        Main.resetFilmList();

        if (!Main.isEmployee()) {
            bookedSeatsLabel.setVisible(false);
            availableSeatsLabel.setVisible(false);
            totalSeatsLabel.setVisible(false);
        }

        // action that is fired whenever the time is changed
        timeDropDownList.setOnAction((event) -> {

            try {
                //datePicker.getValue().equals(null);

                Main.getSelectedSeats().clear();

                Main.setSelectedTime(timeDropDownList.getValue());

                this.sessao = reservasServico.getSessao(Main.getToken(), 
                		datePicker.getValue().toString(), Main.getSelectedFilm().getTitle(), 
                		Main.getSelectedTime());
                // resetting the number of booked seats for every date, film, and time
                bookedSeatsCount = 0;

                // resetting all seats to black every time the user selects a new screening time
                for (int i = 0; i < 18; i++) {
                    gridSeats.getChildren().get(i)
                    .setStyle("-fx-fill:black; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
                }

                List<String> ocupados = Stream.concat(sessao.getSoldSeats().stream(), 
                		sessao.getReservedSeats().stream())
                        .collect(Collectors.toList());

                ocupados.forEach(seat -> {
                    for (int i = 0; i < 18; i++) {
                        if (gridSeats.getChildren().get(i).getId().equals(seat)) {
                            gridSeats.getChildren().get(i).setStyle(
                                    "-fx-fill:#c9b3b3; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
                            // incrementing the count of the booked seats
                            bookedSeatsCount++;
                        }
                    }
                });


                // setting the number of the booked seats and empty seats every time there is an action
                // and the specific screening (film, date, and time) changes
                bookedSeatsLabel.setText("Booked seats: " + bookedSeatsCount);
                availableSeatsLabel.setText("Available seats: " + (18 - bookedSeatsCount));

            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }

        });
    }

    @FXML
    private void selectSeat(MouseEvent e) {

        // firing a pop up message if user clicks on already booked seat
        if (((Node) e.getSource()).getStyle()
                .equals("-fx-fill:#c9b3b3; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;")) {
            Alert alert = new Alert(AlertType.WARNING,
                    "The seat " + ((Node) e.getSource()).getId() + " is already booked!", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        } else {
            // turning seat back to black if it is red - unselecting it
            if (((Node) e.getSource()).getStyle()
                    .equals("-fx-fill:red; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;")) {
                ((Node) e.getSource())
                .setStyle("-fx-fill:black; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
                Main.getSelectedSeats().remove(((Node) e.getSource()).getId());
            }
            // turning seat red if it is black - selecting it
            else {
                ((Node) e.getSource())
                .setStyle("-fx-fill:red; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
                Main.getSelectedSeats().add(((Node) e.getSource()).getId());
            }
        }
    }

    /**
     * Method that gets called when a customer clicks on the book seat button to make a booking.
     * @param e Mouse event representing a click on the book seat button
     * @throws IOException Exception to be thrown if there is a problem with storing the booking in the JSON text files
     * @throws GeneralSecurityException Exception to be thrown if encryption fails
     */
    @FXML
    private void bookSeat(MouseEvent e) throws IOException, GeneralSecurityException {

        if (Main.getSelectedSeats().size() == 0){
            throwAlert();
            return;
        }
        try {
            datePicker.getValue().equals(null);
            filmDropDownList.getValue().equals(null);
            timeDropDownList.getValue().equals(null);
            if (Main.isEmployee())
                customerDropDownList.getValue().equals(null);
        } catch(NullPointerException ex){    		
            throwAlert();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to proceed with the booking?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.NO) {
            alert.close();
            return;
        }
        else {

            Main.getSelectedSeats().forEach(seat -> {
            	Long usuarioId = 0L;
            	if (!Main.isEmployee()) {
            		usuarioId = Main.getCurrentUser().getId();
            	}else {
            		usuarioId = usuarios.stream().filter(u -> u.getFullName()
            				.equals(customerDropDownList.getValue()))
            				.findAny().get().getId();
            	}
            	try {
					reservasServico.cadastrar(usuarioId, this.sessao.getId(), 
							this.sessao.getPrice(), "Inteira", seat, this.sessao.getDate());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            });

            Main.resetBookingList();

            if (!Main.isEmployee()){
                SceneCreator.launchScene("/scenes/BookingSummaryScene.fxml");
                Main.getStage().centerOnScreen();
            } else {
                Alert alert1 = new Alert(AlertType.INFORMATION, "You have completed the booking for " + customerDropDownList.getValue() + "!", ButtonType.OK);
                alert1.showAndWait();
                if(alert1.getResult() == ButtonType.OK) {
                    alert1.close();
                    SceneCreator.launchScene("/scenes/ManageBookingsScene.fxml");
                }
            }
        }
    }

    static Stage getStage() {

        return stage;
    }

    @FXML
    private void showBookingHistoryOnClick(ActionEvent event) throws IOException {

        SceneCreator.launchScene("/scenes/BookingHistoryScene.fxml");
    }
    
    @FXML
    private void giveFeedback(ActionEvent event) throws IOException {

        SceneCreator.launchScene("/scenes/FeedbackScene.fxml");
    }

    @FXML
    private void backToPrevScene(ActionEvent event) throws IOException {

        SceneCreator.launchScene("/scenes/UserScene.fxml");
    }

    /**
     * Method that gets called when a date is selected from the date picker.
     * @param event Action event representing a selection in the date picker
     * @throws ParseException Exception to be thrown if the parsing of film start and end dates to LacalDate objects fails
     */
    @FXML
    private void populateFilmDropDownList(ActionEvent event) throws ParseException {

        //Main.resetFilmList();
        //Main.readJSONFile("filmsJSON.txt");
        try {
            Main.setSelectedDate(datePicker.getValue().toString());

            ObservableList<String> filmTitles = FXCollections.observableArrayList();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            filmes.forEach(filme -> {
            	LocalDate dataFim = LocalDate.parse(filme.getEndDate(), formatter);
            	LocalDate dataInicio = LocalDate.parse(filme.getStartDate(), formatter);
            	boolean condicao1 = dataFim.isAfter(datePicker.getValue());
            	boolean condicao2 = dataFim.isEqual(datePicker.getValue());
            	boolean condicao3 = dataInicio.isBefore(datePicker.getValue());
            	boolean condicao4 = dataInicio.isEqual(datePicker.getValue());
            	
            	if ((condicao3 || condicao4) && (condicao1 || condicao4))
            	filmTitles.add(filme.getTitle());
            });
            
//            for (Film film : Main.getFilmList()) {
//                if ((LocalDate.parse(film.getStartDate(), formatter).isBefore(datePicker.getValue()) ||
//                        LocalDate.parse(film.getStartDate(), formatter).equals(datePicker.getValue()))
//                        && (LocalDate.parse(film.getEndDate(), formatter).isAfter(datePicker.getValue()) ||
//                                LocalDate.parse(film.getEndDate(), formatter).equals(datePicker.getValue())))
//                    filmTitles.add(film.getTitle());
//            }

            filmDropDownList.setItems(filmTitles);
        }
        catch(NullPointerException e) {
            e.getMessage();
        }
    }

    /**
     * Method that gets called when a film is selected from the drop-down list.
     * @param event Action event representing a selection in the films' drop-down list
     */
    @FXML
    private void populateTimeDropDownList(ActionEvent event) {

        try {
        	filmes.forEach(filme -> {
        		if (filme.getTitle() == filmDropDownList.getValue()) {
        			Main.setSelectedFilm(filme);
        		}
        	});
            Film selectedFilm = Main.getSelectedFilm();
            
            ObservableList<String> timesList = FXCollections.observableArrayList(selectedFilm.getSchedules());

            timeDropDownList.setItems(timesList);
        }
        catch (NullPointerException ex) {
            return;
        }
    }

    @FXML
    private void populateUserDropDownList(ActionEvent event) {

        try {
            ObservableList<String> customersList = 
            		FXCollections.observableArrayList();
            
            usuarios.forEach(usuario -> {
            	customersList.add(usuario.getFullName());
            });
            
            customerDropDownList.setItems(customersList);
        } catch(NullPointerException e) {
            return;
        }
    }

    @FXML
    private void throwAlert() {

        Alert alert = new Alert(AlertType.INFORMATION, "Please make sure all fields are selected!", ButtonType.OK);
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK){
            alert.close();
        }
    }
}