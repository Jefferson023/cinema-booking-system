package compgc01.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * A class whose objects represet a booking history item (a single booking).
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 08.12.2017
 */
public class BookingHistoryItem {

    private String status, firstName, lastName, film, date, time, seat, idNumber;
    private String username;
    private Long sessionId;
    public BookingHistoryItem (String status, String username, String firstName, String lastName, String film, String date, String time, String price, String idNumber, Long sessionId) {

        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.film = film;
        this.date = date;
        this.time = time;
        this.seat = price;
        this.idNumber = idNumber;
        this.username = username;
        this.sessionId = sessionId;
    }

    public void setStatus(String status) {
		this.status = status;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFilm(String film) {
		this.film = film;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {

        return status;
    }

    public String getFirstName() {

        return firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public String getFilm() {

        return film;
    }

    public String getDate() {

        return date;
    }

    public String getTime() {

        return time;
    }

    public String getSeat() {

        return seat;
    }

    public String getIdNumber() {

        return idNumber;
    }

    public String getUsername() {

        return username;
    }

	/**
	 * @return the sessionId
	 */
	public Long getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
}