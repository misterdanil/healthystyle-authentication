package org.healthystyle.authentication.service.confidentiality.impl;

import org.healthystyle.authentication.repository.confidentiality.ConfidentialityRepository;
import org.healthystyle.authentication.repository.confidentiality.accessability.BirthdatePartAccessabilityRepository;
import org.healthystyle.authentication.repository.confidentiality.accessability.BirthdateViewAccessabilityRepository;
import org.healthystyle.authentication.repository.confidentiality.accessability.ContactPartAccessabilityRepository;
import org.healthystyle.authentication.repository.confidentiality.accessability.ContactViewAccessabilityRepository;
import org.healthystyle.authentication.repository.confidentiality.accessability.ModeratorBannedAccessabilityRepository;
import org.healthystyle.authentication.repository.confidentiality.accessability.ModeratorWarnedAccessabilityRepository;
import org.healthystyle.authentication.repository.confidentiality.accessability.RealNameViewAccessabilityRepository;
import org.healthystyle.authentication.repository.confidentiality.accessability.SearchOutputAccessabilityRepository;
import org.healthystyle.authentication.service.UserService;
import org.healthystyle.authentication.service.confidentiality.ConfidentialityService;
import org.healthystyle.model.User;
import org.healthystyle.model.confidentiality.Confidentiality;
import org.healthystyle.model.confidentiality.accessability.BirthdatePartAccessability;
import org.healthystyle.model.confidentiality.accessability.BirthdateViewAccessability;
import org.healthystyle.model.confidentiality.accessability.ContactPartAccessability;
import org.healthystyle.model.confidentiality.accessability.ContactViewAccessability;
import org.healthystyle.model.confidentiality.accessability.ModeratorBannedAccessability;
import org.healthystyle.model.confidentiality.accessability.ModeratorWarnedAccessability;
import org.healthystyle.model.confidentiality.accessability.RealNameViewAccessability;
import org.healthystyle.model.confidentiality.accessability.SearchOutputAccessability;
import org.healthystyle.model.confidentiality.accessability.type.BirthdatePartAccessabilityType;
import org.healthystyle.model.confidentiality.accessability.type.ContactPartAccessabilityType;
import org.healthystyle.model.confidentiality.accessability.type.ViewAccessabilityType;
import org.healthystyle.model.role.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ConfidentialityServiceImpl implements ConfidentialityService {
	@Autowired
	private ConfidentialityRepository repository;
	@Autowired
	private BirthdatePartAccessabilityRepository birthdatePartAccessabilityRepository;
	@Autowired
	private BirthdateViewAccessabilityRepository birthdateViewAccessabilityRepository;
	@Autowired
	private ContactPartAccessabilityRepository contactPartAccessabilityRepository;
	@Autowired
	private ContactViewAccessabilityRepository contactViewAccessabilityRepository;
	@Autowired
	private ModeratorBannedAccessabilityRepository moderatorBannedAccessabilityRepository;
	@Autowired
	private ModeratorWarnedAccessabilityRepository moderatorWarnedAccessabilityRepository;
	@Autowired
	private RealNameViewAccessabilityRepository realNameViewAccessabilityRepository;
	@Autowired
	private SearchOutputAccessabilityRepository searchOutputAccessabilityRepository;
	@Autowired
	private UserService userService;

	private static final Logger LOG = LoggerFactory.getLogger(ConfidentialityServiceImpl.class);

	@Override
	public Confidentiality save(User user) {
		LOG.debug("Saving a confidentiality for a user '{}'", user.getId());

		Confidentiality confidentiality = new Confidentiality(user);
		initDefaultSettings(confidentiality);
		repository.save(confidentiality);

		LOG.info("Confidentiality for user '{}' was saved successfully", user.getId());

		return confidentiality;
	}

	private void initDefaultSettings(Confidentiality confidentiality) {
		LOG.debug("Init default settings");
		confidentiality.setBirthdatePartAccessability(
				birthdatePartAccessabilityRepository.findByType(BirthdatePartAccessabilityType.FULL));
		confidentiality.setBirthdateViewAccessability(
				birthdateViewAccessabilityRepository.findByType(ViewAccessabilityType.SELECTIVE));
		confidentiality.setContactPartAccessability(
				contactPartAccessabilityRepository.findByType(ContactPartAccessabilityType.NOTHING));
		confidentiality.setContactViewAccessability(
				contactViewAccessabilityRepository.findByType(ViewAccessabilityType.SELECTIVE));
		confidentiality.setRealNameViewAccessability(
				realNameViewAccessabilityRepository.findByType(ViewAccessabilityType.NOBODY));
		confidentiality.setSearchOutputAccessability(searchOutputAccessabilityRepository.findByType(false));
	}

	@Override
	public void update(BirthdatePartAccessabilityType birthdatePartAccessabilityType,
			ViewAccessabilityType birthdateViewAccessabilityType,
			ContactPartAccessabilityType contactPartAccessabilityType,
			ViewAccessabilityType contactViewAccessabilityType, ViewAccessabilityType realNameViewAccessabilityType,
			Boolean searchOutputAccessabilityType, Boolean moderatorBannedViewAccessabilityType,
			Boolean moderatorWarnedViewAccessabilityType) {
		Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

		Confidentiality confidentiality = repository.findByUser(userId);
		LOG.debug("Updating a confidentiality settings for user '{}'. Confidentiality: {}", userId, confidentiality);
		if (birthdatePartAccessabilityType != null
				&& !confidentiality.getBirthdatePartAccessability().getType().equals(birthdatePartAccessabilityType)) {
			BirthdatePartAccessability birthdatePartAccessability = birthdatePartAccessabilityRepository
					.findByType(birthdatePartAccessabilityType);
			confidentiality.setBirthdatePartAccessability(birthdatePartAccessability);
			LOG.debug("Changed birthdate part accessability to '{}' for user '{}'", birthdatePartAccessabilityType,
					userId);

		}
		if (birthdateViewAccessabilityType != null
				&& !confidentiality.getBirthdateViewAccessability().getType().equals(birthdateViewAccessabilityType)) {
			BirthdateViewAccessability birthdateViewAccessability = birthdateViewAccessabilityRepository
					.findByType(birthdateViewAccessabilityType);
			confidentiality.setBirthdateViewAccessability(birthdateViewAccessability);
			LOG.debug("Changed birthdate view accessability to '{}' for user '{}'", birthdateViewAccessability, userId);
		}
		if (contactPartAccessabilityType != null
				&& !confidentiality.getContactPartAccessability().getType().equals(contactPartAccessabilityType)) {
			ContactPartAccessability contactPartAccessability = contactPartAccessabilityRepository
					.findByType(contactPartAccessabilityType);
			confidentiality.setContactPartAccessability(contactPartAccessability);
			LOG.debug("Changed contact part accessability to '{}' for user '{}'", contactPartAccessabilityType, userId);
		}
		if (contactViewAccessabilityType != null
				&& !confidentiality.getContactViewAccessability().getType().equals(contactViewAccessabilityType)) {
			ContactViewAccessability contactViewAccessability = contactViewAccessabilityRepository
					.findByType(contactViewAccessabilityType);
			confidentiality.setContactViewAccessability(contactViewAccessability);
			LOG.debug("Changed contact view accessability to '{}' for user '{}'", contactViewAccessabilityType, userId);
		}
		if (realNameViewAccessabilityType != null
				&& !confidentiality.getRealNameViewAccessability().getType().equals(realNameViewAccessabilityType)) {
			RealNameViewAccessability realNameViewAccessability = realNameViewAccessabilityRepository
					.findByType(realNameViewAccessabilityType);
			confidentiality.setRealNameViewAccessability(realNameViewAccessability);
			LOG.debug("Changed real name view accessability to '{}' for user '{}'", realNameViewAccessabilityType,
					userId);
		}
		if (searchOutputAccessabilityType != null
				&& !confidentiality.getSearchOutputAccessability().getType().equals(searchOutputAccessabilityType)) {
			SearchOutputAccessability searchOutputAccessability = searchOutputAccessabilityRepository
					.findByType(searchOutputAccessabilityType);
			confidentiality.setSearchOutputAccessability(searchOutputAccessability);
			LOG.debug("Changed search output accessability to '{}' for user '{}'", searchOutputAccessabilityType,
					userId);
		}
		if (userService.hasRole(userId, Name.MODERATOR)) {
			if (moderatorBannedViewAccessabilityType != null && !confidentiality.getModeratorBannedAccessability()
					.getType().equals(moderatorBannedViewAccessabilityType)) {
				ModeratorBannedAccessability moderatorBannedAccessability = moderatorBannedAccessabilityRepository
						.findByType(moderatorBannedViewAccessabilityType);
				confidentiality.setModeratorBannedAccessability(moderatorBannedAccessability);
				LOG.debug("Changed moderator banned accessability to '{}' for user '{}'",
						moderatorBannedViewAccessabilityType, userId);
			}
			if (moderatorWarnedViewAccessabilityType != null && !confidentiality.getModeratorWarnedAccessability()
					.getType().equals(moderatorWarnedViewAccessabilityType)) {
				ModeratorWarnedAccessability moderatorWarnedAccessability = moderatorWarnedAccessabilityRepository
						.findByType(moderatorWarnedViewAccessabilityType);
				confidentiality.setModeratorWarnedAccessability(moderatorWarnedAccessability);
				LOG.debug("Changed moderator warned accessability to '{}' for user '{}'",
						moderatorWarnedViewAccessabilityType, userId);
			}
		}

		LOG.info("Confidentiality of user '{}' was changed: {}", userId, confidentiality);
		repository.save(confidentiality);
	}

}
