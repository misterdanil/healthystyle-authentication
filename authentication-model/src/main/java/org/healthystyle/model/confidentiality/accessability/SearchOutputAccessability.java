package org.healthystyle.model.confidentiality.accessability;

import java.time.Instant;

import org.healthystyle.model.confidentiality.Confidentiality;

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
@Table(name = "search_output_accessability")
public class SearchOutputAccessability {
	@Id
	@SequenceGenerator(name = "search_output_accessability_id_generator", sequenceName = "search_output_accessability_sequence", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "search_output_accessability_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, unique = true)
	private Boolean type;
	@OneToOne
	@JoinColumn(name = "confidentiality_id", nullable = false, unique = true)
	private Confidentiality confidentiality;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "removed_on")
	private Instant removedOn;

	public SearchOutputAccessability(Boolean type, Confidentiality confidentiality) {
		super();
		this.type = type;
		this.confidentiality = confidentiality;
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
