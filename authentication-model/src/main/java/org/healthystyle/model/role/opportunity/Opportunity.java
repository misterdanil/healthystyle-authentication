package org.healthystyle.model.role.opportunity;

import java.time.Instant;
import java.util.Date;

import org.healthystyle.model.role.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Opportunity {
	@Id
	@SequenceGenerator(name = "opportunity_id_generator", sequenceName = "opportunity_sequence", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "opportunity_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private Name name;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn = Instant.now();

	public Opportunity() {
		super();
	}

	public Opportunity(Name name) {
		super();
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}
}
