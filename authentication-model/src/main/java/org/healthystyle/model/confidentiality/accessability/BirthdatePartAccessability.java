package org.healthystyle.model.confidentiality.accessability;

import java.time.Instant;

import org.healthystyle.model.confidentiality.accessability.type.BirthdatePartAccessabilityType;

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
@Table(name = "birthdate_part_accessability")
public class BirthdatePartAccessability {
	@Id
	@SequenceGenerator(name = "birthdate_part_accessability_id_generator", sequenceName = "birthdate_part_accessability_sequence", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "birthdate_part_accessability_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private BirthdatePartAccessabilityType type;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "removed_on")
	private Instant removedOn;

	public Long getId() {
		return id;
	}

	public BirthdatePartAccessabilityType getType() {
		return type;
	}

	public void setType(BirthdatePartAccessabilityType type) {
		this.type = type;
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
