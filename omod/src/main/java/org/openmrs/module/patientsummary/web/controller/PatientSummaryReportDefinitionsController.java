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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.patientsummary.PatientSummaryReportDefinition;
import org.openmrs.module.patientsummary.api.PatientSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for the PatientSummaryReportDefinition list page
 */
@Controller
public class PatientSummaryReportDefinitionsController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	@Qualifier("patientsummary.PatientSummaryService")
	PatientSummaryService service;
	
	/**
	 * Retrieves all patient summary reports
	 */
	@RequestMapping("/module/patientsummary/patientSummaryReportDefinitions")
	public ModelMap listPatientSummaryReportDefinitions(ModelMap model,
	                                                   @RequestParam(required = false, value = "includeRetired") Boolean includeRetired) {
		
		// Get list of existing reports
		boolean includeRet = (includeRetired == Boolean.TRUE);
		List<PatientSummaryReportDefinition> reportDefinitions = service.getAllPatientSummaryReportDefinitions(includeRet);
		
		model.addAttribute("reportDefinitions", reportDefinitions);
		
		return model;
	}
	
	@RequestMapping("/module/patientsummary/purgePatientSummaryReportDefinition")
	public String purgePatientSummaryReportDefinition(String uuid) {
		PatientSummaryReportDefinition reportDefinition = service.getPatientSummaryReportDefinitionByUuid(uuid);
		service.purgeReportDefinition(reportDefinition);
		
		return "redirect:patientSummaryReportDefinitions.list";
	}
}
