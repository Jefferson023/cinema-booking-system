package compgc01.model;

import java.util.List;

public class Sessao {
	private Long id, movieId;
	double price;
	private String movieTitle, description, date, schedule;
	private List<Long> watchersId;
	private List<String> availableSeats, reservedSeats, soldSeats;
	
	public Sessao(Long id, Long movieId, double price, String movieTitle, String description, String date,
			String schedule, List<Long> watchersId, List<String> availableSeats, List<String> reservedSeats,
			List<String> soldSeats) {
		super();
		this.id = id;
		this.movieId = movieId;
		this.price = price;
		this.movieTitle = movieTitle;
		this.description = description;
		this.date = date;
		this.schedule = schedule;
		this.watchersId = watchersId;
		this.availableSeats = availableSeats;
		this.reservedSeats = reservedSeats;
		this.soldSeats = soldSeats;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMovieId() {
		return movieId;
	}
	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getMovieTitle() {
		return movieTitle;
	}
	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public List<Long> getWatchersId() {
		return watchersId;
	}
	public void setWatchersId(List<Long> watchersId) {
		this.watchersId = watchersId;
	}
	public List<String> getAvailableSeats() {
		return availableSeats;
	}
	public void setAvailableSeats(List<String> availableSeats) {
		this.availableSeats = availableSeats;
	}
	public List<String> getReservedSeats() {
		return reservedSeats;
	}
	public void setReservedSeats(List<String> reservedSeats) {
		this.reservedSeats = reservedSeats;
	}
	public List<String> getSoldSeats() {
		return soldSeats;
	}
	public void setSoldSeats(List<String> soldSeats) {
		this.soldSeats = soldSeats;
	}
}
