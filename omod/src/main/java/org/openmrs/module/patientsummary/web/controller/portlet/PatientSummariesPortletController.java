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

import org.openmrs.module.patientsummary.PatientSummaryUtil;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.renderer.SimpleHtmlReportRenderer;
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
	 * @see org.openmrs.web.controller.PortletController#populateModel(javax.servlet.http.HttpServletRequest,
	 *      java.util.Map)
	 */
	@Override
	protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
		List<ReportDesign> reportDesigns = PatientSummaryUtil.getReportDesigns();
		model.put("reportDesigns", reportDesigns);
		boolean getSummaryOnPageLoad = false;
		if (reportDesigns.size() == 1) {
			ReportDesign reportDesign = reportDesigns.get(0);
			if (SimpleHtmlReportRenderer.class.isAssignableFrom(reportDesign.getRendererType())
			        && reportDesign.getReportDefinition().getParameters().isEmpty()) {
				getSummaryOnPageLoad = true;
			}
		}
		model.put("getSummaryOnPageLoad", getSummaryOnPageLoad);
	}
}
