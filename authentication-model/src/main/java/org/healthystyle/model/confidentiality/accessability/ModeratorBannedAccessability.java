package org.healthystyle.model.confidentiality.accessability;

import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "moderator_banned_accessability")
public class ModeratorBannedAccessability {
	@Id
	@SequenceGenerator(name = "moderator_banned_accessability_id_generator", sequenceName = "moderator_banned_accessability_sequence", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "moderator_banned_accessability_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, unique = true)
	private Boolean type;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "removed_on")
	private Instant removedOn;

	public ModeratorBannedAccessability(Boolean type) {
		super();

		Objects.requireNonNull(type, "Type must be not null");

		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public Boolean getType() {
		return type;
	}

	public void setType(Boolean type) {
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
