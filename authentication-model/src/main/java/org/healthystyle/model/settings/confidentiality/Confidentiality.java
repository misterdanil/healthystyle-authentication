package org.healthystyle.model.settings.confidentiality;

import java.time.Instant;

import org.healthystyle.model.settings.Settings;
import org.healthystyle.model.settings.confidentiality.accessability.BirthdatePartAccessability;
import org.healthystyle.model.settings.confidentiality.accessability.BirthdateViewAccessability;
import org.healthystyle.model.settings.confidentiality.accessability.ContactPartAccessability;
import org.healthystyle.model.settings.confidentiality.accessability.ContactViewAccessability;
import org.healthystyle.model.settings.confidentiality.accessability.ModeratorBannedAccessability;
import org.healthystyle.model.settings.confidentiality.accessability.ModeratorWarnedAccessability;
import org.healthystyle.model.settings.confidentiality.accessability.RealNameViewAccessability;
import org.healthystyle.model.settings.confidentiality.accessability.SearchOutputAccessability;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	@JoinColumn(name = "settings_id", nullable = false)
	private Settings settings;
	@OneToOne(mappedBy = "confidentiality", cascade = CascadeType.ALL, orphanRemoval = true)
	private RealNameViewAccessability realNameViewAccessability;
	@OneToOne(mappedBy = "confidentiality", cascade = CascadeType.ALL, orphanRemoval = true)
	private ContactViewAccessability contactViewAccessability;
	@OneToOne(mappedBy = "confidentiality", cascade = CascadeType.ALL, orphanRemoval = true)
	private ContactPartAccessability contactPartAccessability;
	@OneToOne(mappedBy = "confidentiality", cascade = CascadeType.ALL, orphanRemoval = true)
	private BirthdateViewAccessability birthdateViewAccessability;
	@OneToOne(mappedBy = "confidentiality", cascade = CascadeType.ALL, orphanRemoval = true)
	private BirthdatePartAccessability birthdatePartAccessability;
	@OneToOne(mappedBy = "confidentiality", cascade = CascadeType.ALL, orphanRemoval = true)
	private ModeratorBannedAccessability moderatorBannedAccessability;
	@OneToOne(mappedBy = "confidentiality", cascade = CascadeType.ALL, orphanRemoval = true)
	private ModeratorWarnedAccessability moderatorWarnedAccessability;
	@OneToOne(mappedBy = "confidentiality", cascade = CascadeType.ALL, orphanRemoval = true)
	private SearchOutputAccessability searchOutputAccessability;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "removed_on")
	private Instant removedOn;

	public Confidentiality(Settings settings) {
		super();
		this.settings = settings;
	}

	public Long getId() {
		return id;
	}

	public Settings getSettings() {
		return settings;
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
