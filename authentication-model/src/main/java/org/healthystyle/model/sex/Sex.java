package org.healthystyle.model.sex;

import java.time.Instant;

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
@Table
public class Sex {
	@Id
	@SequenceGenerator(name = "sex_generator", sequenceName = "sex_sequence", initialValue = 1, allocationSize = 2)
	@GeneratedValue(generator = "sex_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private Type type;
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "removed_on")
	private Instant removedOn;

	public Long getId() {
		return id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
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
