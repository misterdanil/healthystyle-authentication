package org.healthystyle.model.settings.confidentiality.accessability;

import java.time.Instant;
import java.util.Objects;

import org.healthystyle.model.settings.confidentiality.accessability.type.ContactPartAccessabilityType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "contact_part_accessability")
public class ContactPartAccessability {
	@Id
	@SequenceGenerator(name = "contact_part_accessability_id_generator", sequenceName = "contact_part_accessability_sequence", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "contact_part_accessability_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private ContactPartAccessabilityType type;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "removed_on")
	private Instant removedOn;

	public ContactPartAccessability(ContactPartAccessabilityType type) {
		super();

		Objects.requireNonNull(type, "Type must be not null");

		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public ContactPartAccessabilityType getType() {
		return type;
	}

	public void setType(ContactPartAccessabilityType type) {
		this.type = type;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Instant createdOn) {
		this.createdOn = createdOn;
	}

	public Instant getRemovedOn() {
		return removedOn;
	}

	public void setRemovedOn(Instant removedOn) {
		this.removedOn = removedOn;
	}

}
