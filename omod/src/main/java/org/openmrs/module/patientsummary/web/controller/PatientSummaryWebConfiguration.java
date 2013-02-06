/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.patientsummary.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientsummary.PatientSummaryTemplate;
import org.openmrs.module.patientsummary.api.PatientSummaryService;

public class PatientSummaryWebConfiguration {
	
	private static final String GP_PATIENT_DASHBOARD_SUMMARIES = "patientsummary.patientDashboardSummaries";
	
	public static boolean enableOnPatientDashboard(PatientSummaryTemplate template) {
		String uuids = Context.getAdministrationService().getGlobalProperty(GP_PATIENT_DASHBOARD_SUMMARIES, "");
		return uuids.contains(template.getUuid());
	}
	
	public static void saveEnableTemplateOnPatientDashboard(PatientSummaryTemplate template, boolean enableOnPatientDashboard) {
		PatientSummaryService service = Context.getService(PatientSummaryService.class);
		String templateUuid = template.getUuid();
		String uuids = Context.getAdministrationService().getGlobalProperty(GP_PATIENT_DASHBOARD_SUMMARIES, "");
		
		uuids = StringUtils.deleteWhitespace(uuids);
		String[] uuidArray = uuids.split(",");
		List<String> uuidList = new ArrayList<String>();
		for (int index = 0; index < uuidArray.length; index++) {
			String uuid = uuidArray[index];
			if (StringUtils.isBlank(uuid)) {
				continue;
			}
			
			//Add only if uuid references an existing template.
			if (service.getPatientSummaryTemplateByUuid(uuid) != null) {
				uuidList.add(uuid);
			}
		}
		
		if (enableOnPatientDashboard) {
			//Add this template uuid if not already part of the global property value.
			if (!uuidList.contains(templateUuid)) {
				uuidList.add(templateUuid);
			}
		} else {
			//Remove this template uuid if already part of the global property value.
			if (uuidList.contains(templateUuid)) {
				uuidList.remove(templateUuid);
			}
		}
		
		uuids = StringUtils.join(uuidList, ",");
		Context.getAdministrationService().setGlobalProperty(GP_PATIENT_DASHBOARD_SUMMARIES, uuids);
	}
}