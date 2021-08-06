package compgc01.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import compgc01.service.AutenticacaoServico;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;
/**
 * The main class for our cinema booking management application, Cine UCL.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 15.12.2017
 * 
 * References:
 * JSON library by https://code.google.com/archive/p/json-simple/,
 * JavaMail library by http://www.oracle.com/technetwork/java/index-138643.html,
 * sendEmail class adapted from https://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/,
 * Encryption class adapted from Johaness Brodwall's example in https://stackoverflow.com/questions/1132567/encrypt-password-in-configuration-files,
 * JavaFX icons made by http://www.jensd.de/wordpress/ for the buttons,
 * film posters and text from http://www.imdb.com/,
 * film trailers from https://www.youtube.com/,
 * film studio logos from Google Images and are copyright to their respective owners,
 * a .png icon showing a .csv file made by https://thenounproject.com/term/csv-file/56841/,
 * UCLlywood sign inspired by the original Hollywood one at http://www.clker.com/cliparts/A/z/5/z/y/H/hollywood-sign-md.png,
 * default film poster by http://comicbook.com/,
 * default user icon by https://www.whatsapp.com/,
 * and all other images adapted from originals at https://www.freepik.com/.
 * Royalty-free music (Palchevel's Canon in D Major performed by Kevin MacLeod)
 * from http://incompetech.com/music/royalty-free/index.html?isrc=USUAN1100301,
 * licensed under Creative Commons: By Attribution 3.0 License http://creativecommons.org/licenses/by/3.0/.
 */
public class Main extends Application {

    static Parent root;
    static Stage primaryStage;
    static Main m = null;
    static User currentUser;
    static Boolean employeeMode = false, christmasSeason = false;
    static Film selectedFilm = null;
    static Image selectedFilmImage = null;
    
    public static Image getSelectedFilmImage() {
		return selectedFilmImage;
	}

	public static void setSelectedFilmImage(Image selectedFilmImage) {
		Main.selectedFilmImage = selectedFilmImage;
	}

	static String selectedDate = "", selectedTime = "";
    static ArrayList<String> selectedSeats;
    
    private static String stars = "";
    private static String comment = "";
    private static String experience = "";
    private static String feedbackFilmTitle = "";

    // arrayLists to be populated with the information from the text files
    static HashSet<Employee> employees = new HashSet<Employee>();
    static HashSet<Customer> customers = new HashSet<Customer>();
    static HashSet<Film> films = new HashSet<Film>();
    static HashSet<BookingHistoryItem> bookings = new HashSet<BookingHistoryItem>();

    static String token;

    static BufferedImage imagemUsuario;
    
    /**
     * The main method. It checks whether the designed files exist. If not, it generates them.
     * Then, the first scene is launched.
     * @param String [] args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        m = new Main();

        Encryption.setKey();

        launch(args);
    }

    public static HashSet<Employee> getEmployeeList() {

        return employees;
    }

    public static HashSet<Customer> getCustomerList() {

        return customers;
    }

    public static HashSet<Film> getFilmList() {

        return films;
    }

    public static HashSet<BookingHistoryItem> getBookingList() {

        return bookings;
    }

    public static void resetEmployeeList() {

        employees.clear();
    }

    public static void resetCustomerList() {

        customers.clear();
    }

    public static void resetFilmList() {

        films.clear();
    }

    public static void resetBookingList() {

        bookings.clear();
    }

    public static Main getMainApplication() {

        return m;
    }
    
    public static String getToken() {
    	return token;
    }
    
    public static void setToken(String novoToken) {
    	token = novoToken;
    }

    public static String getPath() {

        String path = ClassLoader.getSystemClassLoader().getResource(".").getPath();

        // leave this here to run in Eclipse... if proper deployment then
        // remove code to only run from jar file
        if (path.contains("zcinema/bin"))
            path = path.split("zcinema")[0];

        return path;
    }

    public static User getCurrentUser() {

        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {

        Main.currentUser = currentUser;
    }

    public static boolean isEmployee() {

        return !currentUser.getProfile().equals("Cliente");
    }

    public static void setEmployeeMode(boolean employeeMode) {

        Main.employeeMode = employeeMode;
    }

    public static boolean isChristmasSeason() {

        return christmasSeason;
    }

    public static void setChristmasSeason(boolean christmasSeason) {

        Main.christmasSeason = christmasSeason;
    }

    public static Customer getCustomerByUsername(String username) {
        for (Customer c : customers)
            if (c.getUsername().equals(username))
                return c;

        return null;
    }

    public static Film getFilmByTitle(String title) {

        for (Film film : Main.getFilmList()) {
            if (film.getTitle().equals(title))
                return film;
        }

        return null;
    }

    public static void setSelectedFilm(Film selectedFilm) {

        Main.selectedFilm = selectedFilm;
    }

    public static Film getSelectedFilm() {

        return selectedFilm;
    }

    public static void setSelectedDate(String selectedDate) {

        Main.selectedDate = selectedDate;
    }

    public static String getSelectedDate() {

        return selectedDate;
    }

    public static void setSelectedTime(String selectedTime) {

        Main.selectedTime = selectedTime;
    }

    public static String getSelectedTime() {

        return selectedTime;
    }

    public static void setSelectedSeats(ArrayList<String> selectedSeats) {

        Main.selectedSeats = selectedSeats;
    }

    public static ArrayList<String> getSelectedSeats() {

        return selectedSeats;
    }

    public static Parent getRoot() {

        return root;
    }

    public static void setRoot(Parent root) {

        Main.root = root;
    }

    public static Stage getStage() {

        return primaryStage;
    }

    public static void setStage(Stage stage) {

        Main.primaryStage = stage;
    }

    /**
     * The method that kicks off the first scene of our application, the LoginScene.
     * @param Stage primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        try {
            // setting up the login scene
            root = FXMLLoader.load(getClass().getResource("/scenes/LoginScene.fxml"));
            Main.primaryStage = primaryStage;
            primaryStage.setTitle("Cinema Booking Management System");
            primaryStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 700, 400);
            scene.getStylesheets().add(getClass().getResource("/scenes/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImagemUsuario(BufferedImage imagem) {
    	imagemUsuario = imagem;
    }
    
    public static BufferedImage getImagemUsuario() {
    	return imagemUsuario;
    }
    
	public static String getStars() {
		return stars;
	}

	public static void setStars(String stars) {
		Main.stars = stars;
	}

	public static String getExperience() {
		return experience;
	}

	public static void setExperience(String experience) {
		Main.experience = experience;
	}

	public static String getFeedbackFilmTitle() {
		return feedbackFilmTitle;
	}

	public static void setFeedbackFilmTitle(String feedbackFilmTitle) {
		Main.feedbackFilmTitle = feedbackFilmTitle;
	}

	public static String getComment() {
		return comment;
	}

	public static void setComment(String comment) {
		Main.comment = comment;
	}
}