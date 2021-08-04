package compgc01.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;

/**
 * A class represeting a film.
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 11.12.2017
 */
public class Film {
	private Long id;
    private String title = "Default Title", description = "Default Description", trailer = "Default Trailer",
            startDate = "yyyy-mm-dd", endDate = "yyyy-mm-dd";
    private List<String> schedules = Arrays.asList("hh:mm", "hh:mm", "hh:mm");
    private String banner;

    public Film(Long id, String title, String description, String trailer, String startDate, 
    		String endDate, List<String> times, String banner) {

        if (!title.isEmpty() && !description.isEmpty() && !trailer.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty() && !(times.size() == 0))
            this.title = title;
        this.description = description;
        this.trailer = trailer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.schedules = times;
        this.banner = banner;
        this.id = id;
    }

    public String getTitle() {

        return title;
    }

    void setTitle(String title) {

        if (!title.isEmpty())
            this.title = title;
    }

    public String getDescription() {

        return description;
    }

    void setDescription(String description) {

        if (!description.isEmpty())
            this.description = description;
    }

    public String getTrailer() {

        return trailer;
    }

    void setTrailer(String trailer) {

        if (!trailer.isEmpty())
            this.trailer = trailer;
    }
    
    public String getStartDate() {

        return startDate;
    }

    void setStartDate(String startDate) {

        if (!startDate.isEmpty())
            this.startDate = startDate;
    }

    public String getEndDate() {

        return endDate;
    }

    void setEndDate(String endDate) {

        if (!endDate.isEmpty())
            this.endDate = endDate;
    }

    public List<String> getSchedules() {

        return schedules;
    }

    void setSchedules(List<String> times) {

        if (!(times.size() == 0))
            this.schedules = times;
    }

	/**
	 * @return the banner
	 */
	public String getBanner() {
		return banner;
	}

	/**
	 * @param banner the banner to set
	 */
	public void setBanner(String banner) {
		this.banner = banner;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
}