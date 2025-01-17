package compgc01.model;

import org.json.simple.JSONObject;

/**
 * A class represeting a general user
 * to act as the parent class for employee and customer.
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 07.11.2017
 */
public class User {
    
    private String firstName, lastName, username, password, email, avatar, profile;
    private Long id;
    
    /**
     * Constructor for the class User.
     * @param firstName User first name
     * @param lastName User last name
     * @param username User username
     * @param password User password
     * @param email User email address
     */
    public User(String firstName, String lastName, String username, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
	public User(String firstName, String lastName, String username, String password, String email, String avatar,
			String profile, Long id) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.avatar = avatar;
		this.profile = profile;
		this.id = id;
	}

	/**
     * Returns the user's first name.
     * @return First name
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Sets the user's first name.
     * @param firstName New first name
     */
    public void setFirstName(String firstName) {
        if (!firstName.isEmpty())
            this.firstName = firstName;
    }
    
    /**
     * Returns the user's last name.
     * @return Last name
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Sets the user's last name.
     * @param lastName New last name
     */
    public void setLastName(String lastName) {
        if (!lastName.isEmpty())
            this.lastName = lastName;
    }
    
    /**
     * Returns the user's full name.
     * @return First and last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    /**
     * Returns the user's username.
     * @return Username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the user's username.
     * @param username New username
     */
    public void setUsername(String username) {
        if (!username.isEmpty())
            this.username = username;
    }
    
    /**
     * Returns the user's password.
     * @return Password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the user's password.
     * @param password New password
     */
    public void setPassword(String password) {
        if (!password.isEmpty() && !password.equals(this.password))
            this.password = password;
    }
    
    /**
     * Returns the user's email address.
     * @return Email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the user's email address.
     * @param email New email
     */
    public void setEmail(String email) {
        if (!email.isEmpty())
            this.email = email;
    }
    
    /**
     * Returns the user's type as a String.
     * @return Type
     */
    public String getType() {
        return "user";
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the pathImg
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @param pathImg the pathImg to set
	 */
	public void setAvatar(String pathImg) {
		this.avatar = pathImg;
	}

	/**
	 * @return the profile
	 */
	public String getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
    public String toJSONString() {
        JSONObject json = new JSONObject();
        json.put("email", getEmail());
        json.put("first_name", getFirstName());
        json.put("last_name", getLastName());
        json.put("profile", getProfile());
        return json.toJSONString();
    }
}