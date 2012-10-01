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
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlwidgets.web.html.Option;
import org.openmrs.module.patientsummary.PatientSummaryReportDefinition;
import org.openmrs.module.patientsummary.api.PatientSummaryService;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.service.ReportService;
import org.openmrs.module.reporting.web.datasets.PatientDataSetEditor;
import org.openmrs.module.reporting.web.util.ParameterUtil;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller for the Patient Summary Report Definition Editor page, it typically delagates to the
 * the respective methods in {@link PatientDataSetEditor} but the return url is ignored so that we
 * send the user back to the patient summary report editor or listing pages accordingly
 */
@Controller
public class PatientSummaryReportDefinitionEditorController {
	
	protected static Log log = LogFactory.getLog(PatientSummaryReportDefinitionEditorController.class);
	
	public final static String FORM_URL_PREFIX = "/module/patientsummary/";
	
	public final static String PATIENT_SUMMARY_REPORT_DEF_LISTING = FORM_URL_PREFIX + "patientSummaryReportDefinitions";
	
	public final static String REDIRECT = "redirect:patientSummaryReportDefinitionEditor.form?uuid=";
	
	@RequestMapping(FORM_URL_PREFIX + "patientSummaryReportDefinitionEditor")
	public String getPatientSummaryReportDefinition(HttpSession session, ModelMap model,
	                                                @RequestParam(required = false, value = "uuid") String uuid) {
		
		PatientSummaryReportDefinition rd = null;
		List<ReportDesign> designs = null;
		
		if (StringUtils.isNotBlank(uuid)) {
			rd = Context.getService(PatientSummaryService.class).getPatientSummaryReportDefinitionByUuid(uuid);
			if (rd == null) {
				log.error("No patient summary report definiton was found with a uuid matching " + uuid);
				session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "patientSummary.error.patientDefinitonNotFound");
				
				return PATIENT_SUMMARY_REPORT_DEF_LISTING;
			}
			designs = Context.getService(ReportService.class).getReportDesigns(rd, null, false);
		} else {
			rd = new PatientSummaryReportDefinition();
		}
		
		if (designs == null)
			designs = new ArrayList<ReportDesign>();
		
		model.addAttribute("report", rd);
		model.addAttribute("designs", designs);
		model.addAttribute("dsd", getDataSetDefinition(rd, session));
		if (session.getAttribute(PatientDataSetEditor.IS_UNSAVED_ATTR) != null) {
			model.addAttribute("unsaved", session.getAttribute(PatientDataSetEditor.IS_UNSAVED_ATTR));
		}
		
		return FORM_URL_PREFIX + "patientSummaryReportDefinitionEditor";
	}
	
	public PatientDataSetDefinition getDataSetDefinition(PatientSummaryReportDefinition rd, HttpSession session) {
		
		PatientDataSetDefinition requestedDatasetDef = rd.getPatientDataSetDefinition();
		PatientDataSetDefinition dsd = getFromSession(session);
		
		//This is a different dataset definition we need to replace the one in the session
		if (dsd != null && !OpenmrsUtil.nullSafeEquals(dsd.getUuid(), requestedDatasetDef.getUuid())) {
			removeFromSession(session);
			dsd = null;
		}
		
		if (dsd == null) {
			dsd = requestedDatasetDef;
			putInSession(session, dsd, dsd.getUuid() == null);
		}
		
		if (dsd.getUuid() == null && StringUtils.isBlank(dsd.getName()))
			dsd.setName(Context.getMessageSourceService().getMessage("patientsummary.untitled"));
		
		return dsd;
	}
	
	@ModelAttribute("parameterTypes")
	public List<Option> getAvailableParameterTypes() {
		return ParameterUtil.getSupportedTypes();
	}
	
	@ModelAttribute("parameterCollectionTypes")
	public List<Option> getAvailableParameterCollectionTypes() {
		return ParameterUtil.getSupportedCollectionTypes();
	}
	
	/**
	 * @see org.openmrs.module.reporting.web.datasets.PatientDataSetEditor#changeNameAndDescription(java.lang.String,
	 *      java.lang.String, javax.servlet.http.HttpSession)
	 */
	@RequestMapping(value = FORM_URL_PREFIX + "patientDataSetEditor-nameDescription", method = RequestMethod.POST)
	public String changeNameAndDescription(@RequestParam("name") String name, @RequestParam("reportUuid") String reportUuid,
	                                       @RequestParam("description") String description, HttpSession session) {
		
		//Ignore the return url from the super class because it points to the patientDatasetEditor in reporting module
		getPatientDataSetEditorContoller().changeNameAndDescription(name, description, session);
		return REDIRECT + reportUuid;
	}
	
	/**
	 * @see org.openmrs.module.reporting.web.datasets.PatientDataSetEditor#addParameter(java.lang.Class,
	 *      java.lang.Class, java.lang.String, java.lang.String, java.lang.String,
	 *      javax.servlet.http.HttpSession)
	 */
	@RequestMapping(value = FORM_URL_PREFIX + "patientDataSetEditor-addParam", method = RequestMethod.POST)
	public String addParameter(@RequestParam("reportUuid") String reportUuid,
	                           @RequestParam(value = "collectionType", required = false) Class<? extends Collection<?>> collectionType,
	                           @RequestParam(value = "parameterType", required = false) Class<?> parameterType,
	                           @RequestParam(value = "name", required = false) String name,
	                           @RequestParam(value = "label", required = false) String label,
	                           @RequestParam(value = "widgetConfiguration", required = false) String widgetConfiguration,
	                           HttpSession session) {
		
		getPatientDataSetEditorContoller().addParameter(collectionType, parameterType, name, label, widgetConfiguration,
		    session);
		return REDIRECT + reportUuid;
	}
	
	/**
	 * @see org.openmrs.module.reporting.web.datasets.PatientDataSetEditor#removeParameter(java.lang.String,
	 *      javax.servlet.http.HttpSession)
	 */
	@RequestMapping(value = FORM_URL_PREFIX + "patientDataSetEditor-removeParam", method = RequestMethod.POST)
	public String removeParameter(@RequestParam("name") String name, @RequestParam("reportUuid") String reportUuid,
	                              HttpSession session) {
		getPatientDataSetEditorContoller().removeParameter(name, session);
		return REDIRECT + reportUuid;
	}
	
	/**
	 * @see org.openmrs.module.reporting.web.datasets.PatientDataSetEditor#addColumn(java.lang.String,
	 *      java.lang.String, javax.servlet.http.HttpSession)
	 */
	@RequestMapping(value = FORM_URL_PREFIX + "patientDataSetEditor-addColumn", method = RequestMethod.POST)
	public String addColumn(@RequestParam("reportUuid") String reportUuid,
	                        @RequestParam(value = "label", required = false) String label,
	                        @RequestParam(value = "columnDefinition", required = false) String columnDefinition,
	                        HttpSession session) {
		getPatientDataSetEditorContoller().addColumn(label, columnDefinition, session);
		return REDIRECT + reportUuid;
	}
	
	/**
	 * @see org.openmrs.module.reporting.web.datasets.PatientDataSetEditor#removeColumn(java.lang.String,
	 *      javax.servlet.http.HttpSession)
	 */
	@RequestMapping(value = FORM_URL_PREFIX + "patientDataSetEditor-removeColumn", method = RequestMethod.POST)
	public String removeColumn(@RequestParam("name") String name, @RequestParam("reportUuid") String reportUuid,
	                           HttpSession session) {
		getPatientDataSetEditorContoller().removeColumn(name, session);
		return REDIRECT + reportUuid;
	}
	
	/**
	 * @see org.openmrs.module.reporting.web.datasets.PatientDataSetEditor#addFilter(java.lang.String,
	 *      javax.servlet.http.HttpSession)
	 */
	@RequestMapping(value = FORM_URL_PREFIX + "patientDataSetEditor-addFilter", method = RequestMethod.POST)
	public String addFilter(@RequestParam(value = "filterDefinition", required = false) String filterDefinition,
	                        @RequestParam("reportUuid") String reportUuid, HttpSession session) {
		
		getPatientDataSetEditorContoller().addFilter(filterDefinition, session);
		return REDIRECT + reportUuid;
	}
	
	/**
	 * @see org.openmrs.module.reporting.web.datasets.PatientDataSetEditor#removeFilter(int,
	 *      javax.servlet.http.HttpSession)
	 */
	@RequestMapping(value = FORM_URL_PREFIX + "patientDataSetEditor-removeFilter", method = RequestMethod.POST)
	public String removeFilter(@RequestParam("filterIndex") int index, @RequestParam("reportUuid") String reportUuid,
	                           HttpSession session) {
		getPatientDataSetEditorContoller().removeFilter(index, session);
		return REDIRECT + reportUuid;
	}
	
	/**
	 * @see org.openmrs.module.reporting.web.datasets.PatientDataSetEditor#sortColumns(org.springframework.web.context.request.WebRequest,
	 *      javax.servlet.http.HttpSession)
	 */
	@RequestMapping(value = FORM_URL_PREFIX + "patientDataSetEditor-sortColumns", method = RequestMethod.POST)
	public String sortColumns(WebRequest request, @RequestParam("reportUuid") String reportUuid, HttpSession session) {
		getPatientDataSetEditorContoller().sortColumns(request, session);
		return REDIRECT + reportUuid;
	}
	
	/**
	 * @see org.openmrs.module.reporting.web.datasets.PatientDataSetEditor#save(javax.servlet.http.HttpSession)
	 */
	@RequestMapping(value = FORM_URL_PREFIX + "patientDataSetEditor-save", method = RequestMethod.POST)
	public String save(HttpSession session, @RequestParam("reportUuid") String reportUuid) {
		PatientDataSetDefinition pdsd = getFromSession(session);
		//The first time the user edits a patient summary report definition, the
		//associated patient dataset has no uuid because the metadata portlet
		//doesn't actually associated to it an existing patient dataset
		boolean needToUpdateSummary = false;
		if (StringUtils.isBlank(pdsd.getUuid())) {
			pdsd.setUuid(UUID.randomUUID().toString());
			needToUpdateSummary = true;
		}
		
		PatientSummaryReportDefinition rd = Context.getService(PatientSummaryService.class)
		        .getPatientSummaryReportDefinitionByUuid(reportUuid);
		if (rd == null) {
			log.error("No patient summary report definiton was found with a uuid matching " + reportUuid);
			session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "patientSummary.error.patientDefinitonNotFound");
			if (getFromSession(session) != null)
				removeFromSession(session);
			
			return PATIENT_SUMMARY_REPORT_DEF_LISTING;
		}
		
		getPatientDataSetEditorContoller().save(session);
		
		//This should update the uuid on the summary report definition to reference
		//the patient dataset we have created above
		if (needToUpdateSummary) {
			rd.getPatientDataSetDefinition().setUuid(pdsd.getUuid());
			Context.getService(ReportDefinitionService.class).saveDefinition(rd);
		}
		
		return REDIRECT + reportUuid;
	}
	
	/**
	 * @see org.openmrs.module.reporting.web.datasets.PatientDataSetEditor#discard(javax.servlet.http.HttpSession)
	 */
	@RequestMapping(value = FORM_URL_PREFIX + "patientDataSetEditor-discard", method = RequestMethod.POST)
	public String discard(HttpSession session) {
		getPatientDataSetEditorContoller().discard(session);
		return "redirect:patientSummaryReportDefinitions.form";
	}
	
	private PatientDataSetEditor getPatientDataSetEditorContoller() {
		return new PatientDataSetEditor();
	}
	
	/** A COUPLE OF UTILITY METHOD COPIED FROM THE {@link PatientDataSetEditor} **/
	
	private void putInSession(HttpSession session, PatientDataSetDefinition dsd, boolean isUnsaved) {
		session.setAttribute(PatientDataSetEditor.DSD_ATTR, dsd);
		session.setAttribute(PatientDataSetEditor.IS_UNSAVED_ATTR, isUnsaved);
	}
	
	private void removeFromSession(HttpSession session) {
		session.removeAttribute(PatientDataSetEditor.DSD_ATTR);
		session.removeAttribute(PatientDataSetEditor.IS_UNSAVED_ATTR);
	}
	
	private PatientDataSetDefinition getFromSession(HttpSession session) {
		try {
			return (PatientDataSetDefinition) session.getAttribute(PatientDataSetEditor.DSD_ATTR);
		}
		catch (ClassCastException ex) {
			// module has been reloaded
			removeFromSession(session);
			return null;
		}
	}
}
