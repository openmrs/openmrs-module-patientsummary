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
package org.openmrs.module.patientsummary;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.GlobalPropertyListener;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.service.ReportService;

public final class PatientSummaryUtil implements GlobalPropertyListener {
	
	private static final Log log = LogFactory.getLog(PatientSummaryUtil.class);
	
	private static String patientSummaryTabName;
	
	private static List<String> reportDesignUuids;
	
	/**
	 * Returns the label or message code for the patient summary tab as defined by the value of the
	 * PatientSummaryConstants.GP_PATIENT_SUMMARY_TAB_NAME {@link GlobalProperty} otherwise defaults
	 * to PatientSummaryConstants.DEFAULT_TAB_NAME_OR_MSG_CODE
	 * 
	 * @return the text or message code
	 */
	public static String getPatientSummaryTabName() {
		if (patientSummaryTabName == null) {
			patientSummaryTabName = Context.getAdministrationService().getGlobalProperty(
			    PatientSummaryConstants.GP_PATIENT_SUMMARY_TAB_NAME);
			if (StringUtils.isBlank(patientSummaryTabName))
				patientSummaryTabName = PatientSummaryConstants.DEFAULT_TAB_NAME_OR_MSG_CODE;
		}
		
		return patientSummaryTabName;
	}
	
	/**
	 * Returns a list of configured report designs to be run from the patient summary tab on the
	 * patient dashboard as defined by the value of the
	 * PatientSummaryConstants.GP_REPORT_DESIGN_UUIDS {@link GlobalProperty}
	 * 
	 * @return a list of report designs
	 */
	public static List<ReportDesign> getReportDesigns() {
		if (reportDesignUuids == null) {
			reportDesignUuids = new ArrayList<String>();
			String gpValue = Context.getAdministrationService().getGlobalProperty(
			    PatientSummaryConstants.GP_REPORT_DESIGN_UUIDS);
			if (StringUtils.isNotBlank(gpValue)) {
				String[] uuids = StringUtils.split(gpValue, ",");
				for (String uuid : uuids) {
					if (StringUtils.isBlank(uuid))
						continue;
					
					reportDesignUuids.add(uuid.trim());
				}
			}
		}
		
		ReportService rs = Context.getService(ReportService.class);
		List<ReportDesign> reportDesigns = new ArrayList<ReportDesign>();
		for (String reportDesignUuid : reportDesignUuids) {
			try {
				ReportDesign rd = rs.getReportDesignByUuid(reportDesignUuid);
				if (rd == null) {
					log.error("No report design found with uuid:" + reportDesignUuid);
					continue;
				}
				
				reportDesigns.add(rd);
			}
			catch (Exception e) {
				log.error(e);
			}
		}
		
		return reportDesigns;
	}
	
	/**
	 * @see org.openmrs.api.GlobalPropertyListener#globalPropertyChanged(org.openmrs.GlobalProperty)
	 */
	@Override
	public void globalPropertyChanged(GlobalProperty gp) {
		if (PatientSummaryConstants.DEFAULT_TAB_NAME_OR_MSG_CODE.equalsIgnoreCase(gp.getProperty()))
			patientSummaryTabName = null;
		else if (PatientSummaryConstants.GP_REPORT_DESIGN_UUIDS.equalsIgnoreCase(gp.getProperty()))
			reportDesignUuids = null;
	}
	
	/**
	 * @see org.openmrs.api.GlobalPropertyListener#globalPropertyDeleted(java.lang.String)
	 */
	@Override
	public void globalPropertyDeleted(String gpName) {
		if (PatientSummaryConstants.DEFAULT_TAB_NAME_OR_MSG_CODE.equalsIgnoreCase(gpName))
			patientSummaryTabName = PatientSummaryConstants.DEFAULT_TAB_NAME_OR_MSG_CODE;
		else if (PatientSummaryConstants.GP_REPORT_DESIGN_UUIDS.equalsIgnoreCase(gpName))
			reportDesignUuids = null;
	}
	
	/**
	 * @see org.openmrs.api.GlobalPropertyListener#supportsPropertyName(java.lang.String)
	 */
	@Override
	public boolean supportsPropertyName(String gpName) {
		return PatientSummaryConstants.DEFAULT_TAB_NAME_OR_MSG_CODE.equalsIgnoreCase(gpName)
		        || PatientSummaryConstants.GP_REPORT_DESIGN_UUIDS.equalsIgnoreCase(gpName);
	}
}
