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

package org.openmrs.module.patientsummary.web.controller.portlet;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.module.patientsummary.PatientSummaryTemplate;
import org.openmrs.module.patientsummary.util.ConfigurationUtil;
import org.openmrs.util.OpenmrsClassLoader;
import org.openmrs.web.controller.PortletController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the PatientSummaries portlet.
 */
@Controller
@RequestMapping("**/patientSummaries.portlet")
public class PatientSummariesPortletController extends PortletController {
	
	/**
	 * @see PortletController#populateModel(HttpServletRequest, Map)
	 */
	@Override
	protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
		Thread.currentThread().setContextClassLoader(OpenmrsClassLoader.getInstance());
		List<PatientSummaryTemplate> patientSummaries = ConfigurationUtil.getPatientSummaryTemplatesForDashboard();
		model.put("patientSummaries", patientSummaries);
		if (patientSummaries.size() > 0) {
			model.put("defaultSummary", patientSummaries.get(0));
		}
	}
}
