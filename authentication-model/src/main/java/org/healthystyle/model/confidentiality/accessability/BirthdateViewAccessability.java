package org.healthystyle.model.confidentiality.accessability;

import java.time.Instant;
import java.util.Objects;

import org.healthystyle.model.confidentiality.accessability.type.ViewAccessabilityType;

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
@Table(name = "birtdate_view_accessability")
public class BirthdateViewAccessability {
	@Id
	@SequenceGenerator(name = "birtdate_view_accessability_id_generator", sequenceName = "birtdate_view_accessability_sequence", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "birtdate_view_accessability_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private ViewAccessabilityType type;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "removed_on")
	private Instant removedOn;

	public BirthdateViewAccessability(ViewAccessabilityType type) {
		super();

		Objects.requireNonNull(type, "Type must be not null");

		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public ViewAccessabilityType getType() {
		return type;
	}

	public void setType(ViewAccessabilityType type) {
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
