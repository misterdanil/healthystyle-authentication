package org.healthystyle.model.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.healthystyle.model.role.opportunity.Opportunity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table
public class Role {
	@Id
	@SequenceGenerator(name = "role_id_generator", sequenceName = "role_sequence", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "role_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private Name name;
	@ManyToOne
	@JoinColumn(name = "parent_role_id")
	private Role parent;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parent", fetch = FetchType.LAZY)
	private List<Role> childRoles;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_opportunity", joinColumns = @JoinColumn(name = "role_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "opportunity_id", nullable = false))
	private List<Opportunity> opportunities;

	public Role() {
		super();
	}

	public Role(Name name, Opportunity... opportunities) {
		super();
		Objects.requireNonNull(name, "Name must be not null");

		Objects.requireNonNull(opportunities, "Opportunities must be not null");
		if (opportunities.length == 0)
			throw new NoSuchElementException("Role cannot be created without opportunities");

		this.name = name;
		this.opportunities = new ArrayList<>(Arrays.asList(opportunities));
	}

	public Role(Name name, Role parent, Opportunity... opportunities) {
		this(name, opportunities);
		this.parent = parent;
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

	public Role getParent() {
		return parent;
	}

	public void setParent(Role parent) {
		this.parent = parent;
	}

	public List<Role> getChildRoles() {
		return childRoles;
	}

	public void addChildRole(Role childRole) {
		getChildRoles().add(childRole);
	}

	public List<Opportunity> getOpportunities() {
		return opportunities;
	}

	public void addOpportunity(Opportunity opportunity) {
		getOpportunities().add(opportunity);
	}

}
