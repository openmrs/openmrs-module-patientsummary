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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlwidgets.web.WidgetUtil;
import org.openmrs.module.patientsummary.PatientSummaryConstants;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.renderer.ReportRenderer;
import org.openmrs.module.reporting.report.renderer.SimpleHtmlReportRenderer;
import org.openmrs.module.reporting.report.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Processes request for viewing patient summaries
 */
@Controller
public class ViewPatientSummaryFormController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private static final String VIEW_PATIENT_SUMMARY_FORM = "/module/" + PatientSummaryConstants.MODULE_ID
	        + "/viewPatientSummary";
	
	/**
	 * Receives requests to run a report design
	 * 
	 * @param model
	 * @param reportDesignUuid the uuid of the report design
	 * @param patientId the patient's id
	 * @param showParametersFormIfNecessary specifies if we need to prompt the user for required
	 *            parameter values
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(VIEW_PATIENT_SUMMARY_FORM)
	public String viewPatientSummary(ModelMap model,
	                                 @RequestParam("reportDesignUuid") String reportDesignUuid,
	                                 @RequestParam("patientId") Integer patientId,
	                                 @RequestParam(required = false, value = "showParametersFormIfNecessary") boolean showParametersFormIfNecessary,
	                                 HttpServletRequest request, HttpServletResponse response) {
		
		ReportDesign rd = Context.getService(ReportService.class).getReportDesignByUuid(reportDesignUuid);
		if (rd != null) {
			if (showParametersFormIfNecessary && !rd.getReportDefinition().getParameters().isEmpty()) {
				model.addAttribute("showParametersForm", true);
				model.addAttribute("reportDesignUuid", reportDesignUuid);
				model.addAttribute("patientId", patientId);
				model.addAttribute("parameters", rd.getReportDefinition().getParameters());
			} else {
				Map<String, Object> parameterValueMap = new HashMap<String, Object>();
				Map<String, String> parameterErrors = null;
				if (!rd.getReportDefinition().getParameters().isEmpty()) {
					for (Parameter parameter : rd.getReportDefinition().getParameters()) {
						Object paramVal = WidgetUtil.getFromRequest(request, parameter.getName(), parameter.getType(),
						    parameter.getCollectionType());
						if (paramVal == null) {
							if (parameterErrors == null)
								parameterErrors = new HashMap<String, String>();
							parameterErrors.put(parameter.getName(), "error.required");
							continue;
						}
						parameterValueMap.put(parameter.getName(), paramVal);
					}
				}
				
				if (MapUtils.isEmpty(parameterErrors)) {
					processRequest(model, patientId, rd, response, parameterValueMap);
					if (!SimpleHtmlReportRenderer.class.isAssignableFrom(rd.getRendererType())) {
						//Nothing else to write and the output stream is already closed above
						return null;
					}
				} else {
					model.addAttribute("showParametersForm", true);
					model.addAttribute("reportDesignUuid", reportDesignUuid);
					model.addAttribute("patientId", patientId);
					model.addAttribute("parameters", rd.getReportDefinition().getParameters());
					model.addAttribute("parameterValueMap", parameterValueMap);
					model.addAttribute("parameterErrors", parameterErrors);
				}
			}
		}
		
		return VIEW_PATIENT_SUMMARY_FORM;
	}
	
	/**
	 * Processes ajax requests to run a report design
	 * 
	 * @param reportDesignUuid
	 * @param patientId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/module/" + PatientSummaryConstants.MODULE_ID + "/processAjaxRequest")
	public Object processAjaxRequest(@RequestParam("reportDesignUuid") String reportDesignUuid,
	                                 @RequestParam("patientId") Integer patientId) {
		
		Map<String, String> ret = new HashMap<String, String>();
		ReportDesign rd = Context.getService(ReportService.class).getReportDesignByUuid(reportDesignUuid);
		if (rd != null) {
			try {
				ret.put("results", generateSummary(patientId, rd, null, rd.getRendererType().newInstance()));
			}
			catch (Exception e) {
				e.printStackTrace();
				ret.put("errorDetails", "An error was encountered while generating the summary:" + e.getMessage());
			}
		}
		
		return ret;
	}
	
	/**
	 * Convenience method that processes a request to run a report design
	 * 
	 * @param model
	 * @param patientId
	 * @param rd
	 * @param response
	 * @param parameters
	 */
	private void processRequest(ModelMap model, Integer patientId, ReportDesign rd, HttpServletResponse response,
	                            Map<String, Object> parameters) {
		try {
			ReportRenderer renderer = rd.getRendererType().newInstance();
			String generatedSummary = generateSummary(patientId, rd, parameters, renderer);
			if (SimpleHtmlReportRenderer.class.isAssignableFrom(rd.getRendererType())) {
				model.addAttribute("generatedSummary", generatedSummary);
			} else {
				response.setHeader("Content-Type", renderer.getRenderedContentType(rd.getReportDefinition(), null));
				response.setHeader("Content-Disposition",
				    "attachment; filename=\"" + renderer.getFilename(rd.getReportDefinition(), null) + "\"");
				
				OutputStream out = response.getOutputStream();
				out.write(generatedSummary.getBytes("UTF-8"));
				out.flush();
			}
		}
		catch (Exception e) {
			model.addAttribute("errorDetails", "An error was encountered while generating the summary:" + e.getMessage());
		}
	}
	
	/**
	 * Convenience method that performs the evaluation of a report definition associated to the
	 * specified report design and returns the generated summary
	 * 
	 * @param patientId
	 * @param rd
	 * @param parameters
	 * @param renderer
	 * @return
	 * @throws Exception
	 */
	private String generateSummary(Integer patientId, ReportDesign rd, Map<String, Object> parameters,
	                               ReportRenderer renderer) throws Exception {
		
		EvaluationContext context = new EvaluationContext();
		Cohort baseCohort = new Cohort();
		baseCohort.addMember(patientId);
		context.setBaseCohort(baseCohort);
		if (parameters != null) {
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				context.addParameterValue(entry.getKey(), entry.getValue());
			}
		}
		
		ReportData data = Context.getService(ReportDefinitionService.class).evaluate(rd.getReportDefinition(), context);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			renderer.render(data, rd.getUuid(), baos);
			return baos.toString("UTF-8");
		}
		finally {
			try {
				baos.close();
			}
			catch (Exception e) {
				log.error("Error:" + e.getMessage());
			}
		}
	}
}
