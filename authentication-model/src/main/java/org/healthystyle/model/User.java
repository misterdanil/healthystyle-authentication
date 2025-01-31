package org.healthystyle.model;

import java.time.Instant;
import java.time.LocalDate;

import org.healthystyle.model.role.Role;
import org.healthystyle.model.security.RefreshToken;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "_user")
public class User {
	@Id
	@SequenceGenerator(name = "user_id_generator", sequenceName = "user_sequence", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "user_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, columnDefinition = "VARCHAR(50) CONSTRAINT CK_valid_username CHECK (~ '\\w{3,}')")
	private String username;
	@Column(name = "first_name", columnDefinition = "VARCHAR(100) CONSTRAINT CK_valid_first_name CHECK (first_name ~ '^\\p{L}{2}[\\p{L} ,.'-]{0,98}$')")
	private String firstName;
	@Column(name = "last_name", columnDefinition = "VARCHAR(100) CONSTRAINT CK_valid_last_name CHECK (last_name ~ '^\\p{L}{2}[\\p{L} ,.'-]{0,98}$')")
	private String lastName;
	@Column(name = "telephone_number", columnDefinition = "VARCHAR(15) CONSTRAINT CK_valid_telephone_number CHECK (telephone_number ~ '^\\+?[0-9]{5,15}$')")
	private String telephoneNumber;
	@Column(nullable = false, columnDefinition = "VARCHAR(320) CONSTRAINT CK_valid_email CHECK (email ~ '^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$')")
	private String email;
	@Column(nullable = false, columnDefinition = "VARCHAR(100) CONSTRAINT CK_valid_password CHECK (password ~ '^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{8,}$')")
	private String password;
	@Temporal(TemporalType.DATE)
	@Column(nullable = false, columnDefinition = "DATE CONSTRAINT CK_min_age CHECK (date_part('year', age(birthdate)) > 18)")
	private LocalDate birthdate;
	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private RefreshToken refreshToken;
	@Column(name = "image_id")
	private Long imageId;
	@Column(name = "region_id")
	private Long regionId;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "removed_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant removedOn;

	public User(String username, String email, String password, Role role) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public RefreshToken getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(RefreshToken refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

}
