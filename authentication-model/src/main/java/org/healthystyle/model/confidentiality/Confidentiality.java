package org.healthystyle.model.confidentiality;

import java.time.Instant;
import java.util.Objects;

import org.healthystyle.model.User;
import org.healthystyle.model.confidentiality.accessability.BirthdatePartAccessability;
import org.healthystyle.model.confidentiality.accessability.BirthdateViewAccessability;
import org.healthystyle.model.confidentiality.accessability.ContactPartAccessability;
import org.healthystyle.model.confidentiality.accessability.ContactViewAccessability;
import org.healthystyle.model.confidentiality.accessability.ModeratorBannedAccessability;
import org.healthystyle.model.confidentiality.accessability.ModeratorWarnedAccessability;
import org.healthystyle.model.confidentiality.accessability.RealNameViewAccessability;
import org.healthystyle.model.confidentiality.accessability.SearchOutputAccessability;
import org.healthystyle.model.confidentiality.accessability.type.ViewAccessabilityType;

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
@Table
public class Confidentiality {
	@Id
	@SequenceGenerator(name = "confidentiality_id_generator", sequenceName = "confidentiality_sequence", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "confidentiality_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	@ManyToOne
	@JoinColumn(name = "real_name_view_accessability_id"/* , nullable = false */)
	private RealNameViewAccessability realNameViewAccessability;
	@ManyToOne
	@JoinColumn(name = "contact_view_accessability_id"/* , nullable = false */)
	private ContactViewAccessability contactViewAccessability;
	@ManyToOne
	@JoinColumn(name = "contact_part_accessability_id"/* , nullable = false */)
	private ContactPartAccessability contactPartAccessability;
	@ManyToOne
	@JoinColumn(name = "birthdate_view_accessability_id"/* , nullable = false */)
	private BirthdateViewAccessability birthdateViewAccessability;
	@ManyToOne
	@JoinColumn(name = "birthdate_part_accessability_id"/* , nullable = false */)
	private BirthdatePartAccessability birthdatePartAccessability;
	@ManyToOne
	@JoinColumn(name = "moderator_banned_accessability_id")
	private ModeratorBannedAccessability moderatorBannedAccessability;
	@ManyToOne
	@JoinColumn(name = "moderator_warned_accessability_id")
	private ModeratorWarnedAccessability moderatorWarnedAccessability;
	@ManyToOne
	@JoinColumn(name = "search_output_accessability_id"/* , nullable = false */)
	private SearchOutputAccessability searchOutputAccessability;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn = Instant.now();
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "removed_on")
	private Instant removedOn;

	public Confidentiality() {
		super();
	}

	public Confidentiality(User user) {
		super();

		Objects.requireNonNull(user, "User must be not null");

		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public RealNameViewAccessability getRealNameViewAccessability() {
		return realNameViewAccessability;
	}

	public void setRealNameViewAccessability(RealNameViewAccessability realNameViewAccessability) {
		this.realNameViewAccessability = realNameViewAccessability;
	}

	public ContactViewAccessability getContactViewAccessability() {
		return contactViewAccessability;
	}

	public void setContactViewAccessability(ContactViewAccessability contactViewAccessability) {
		this.contactViewAccessability = contactViewAccessability;
	}

	public ContactPartAccessability getContactPartAccessability() {
		return contactPartAccessability;
	}

	public void setContactPartAccessability(ContactPartAccessability contactPartAccessability) {
		this.contactPartAccessability = contactPartAccessability;
	}

	public BirthdateViewAccessability getBirthdateViewAccessability() {
		return birthdateViewAccessability;
	}

	public void setBirthdateViewAccessability(BirthdateViewAccessability birthdateViewAccessability) {
		this.birthdateViewAccessability = birthdateViewAccessability;
	}

	public BirthdatePartAccessability getBirthdatePartAccessability() {
		return birthdatePartAccessability;
	}

	public void setBirthdatePartAccessability(BirthdatePartAccessability birthdatePartAccessability) {
		this.birthdatePartAccessability = birthdatePartAccessability;
	}

	public ModeratorBannedAccessability getModeratorBannedAccessability() {
		return moderatorBannedAccessability;
	}

	public void setModeratorBannedAccessability(ModeratorBannedAccessability moderatorBannedAccessability) {
		this.moderatorBannedAccessability = moderatorBannedAccessability;
	}

	public ModeratorWarnedAccessability getModeratorWarnedAccessability() {
		return moderatorWarnedAccessability;
	}

	public void setModeratorWarnedAccessability(ModeratorWarnedAccessability moderatorWarnedAccessability) {
		this.moderatorWarnedAccessability = moderatorWarnedAccessability;
	}

	public SearchOutputAccessability getSearchOutputAccessability() {
		return searchOutputAccessability;
	}

	public void setSearchOutputAccessability(SearchOutputAccessability searchOutputAccessability) {
		this.searchOutputAccessability = searchOutputAccessability;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

	public Instant getRemovedOn() {
		return removedOn;
	}

	public void setRemovedOn(Instant removedOn) {
		this.removedOn = removedOn;
	}

}
