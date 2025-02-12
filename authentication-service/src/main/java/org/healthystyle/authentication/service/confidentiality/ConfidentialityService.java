package org.healthystyle.authentication.service.confidentiality;

import org.healthystyle.model.User;
import org.healthystyle.model.confidentiality.Confidentiality;
import org.healthystyle.model.confidentiality.accessability.type.BirthdatePartAccessabilityType;
import org.healthystyle.model.confidentiality.accessability.type.ContactPartAccessabilityType;
import org.healthystyle.model.confidentiality.accessability.type.ViewAccessabilityType;

public interface ConfidentialityService {
	Confidentiality save(User user);

	void update(BirthdatePartAccessabilityType birthdatePartAccessabilityType,
			ViewAccessabilityType birthdateViewAccessabilityType,
			ContactPartAccessabilityType contactPartAccessabilityType,
			ViewAccessabilityType contactViewAccessabilityType, ViewAccessabilityType realNameViewAccessabilityType,
			Boolean searchOutputAccessabilityType, Boolean moderatorBannedViewAccessabilityType,
			Boolean moderatorWarnedViewAccessabilityType);
}
