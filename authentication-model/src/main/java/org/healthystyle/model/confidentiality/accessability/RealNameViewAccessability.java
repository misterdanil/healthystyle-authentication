package org.healthystyle.model.confidentiality.accessability;

import java.time.Instant;
import java.util.Objects;

import org.healthystyle.model.confidentiality.Confidentiality;
import org.healthystyle.model.confidentiality.accessability.type.ViewAccessabilityType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "real_name_accessability")
public class RealNameViewAccessability {
	@Id
	@SequenceGenerator(name = "real_name_accessability_generator", sequenceName = "real_name_accessability_sequence", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "real_name_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private ViewAccessabilityType type;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;
	@Column(name = "removed_on")
	private Instant removedOn;

	public RealNameViewAccessability() {
		super();
	}

	public RealNameViewAccessability(ViewAccessabilityType type) {
		super();

		Objects.requireNonNull(type, "Type of accessability must be pointed");

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
