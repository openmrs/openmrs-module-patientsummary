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

/**
 * Constants used by the module.
 */
public final class PatientSummaryConstants {
	
	public static final String MODULE_ID = "patientsummary";
	
	public static final String DEFAULT_TAB_NAME_OR_MSG_CODE = MODULE_ID + ".defaultTabNameMessageCode";
	
	//================ Global Property Constants =======================
	
	public static final String GP_PATIENT_SUMMARY_TAB_NAME = MODULE_ID + ".tabNameOrMessageCode";
	
	public static final String GP_REPORT_DESIGN_UUIDS = MODULE_ID + ".reportDesignUuids";
	
	//================ Privilege constants =============================
	
	public static final String PRIV_VIEW_PATIENT_SUMMARIES = "View Patient Summaries";
}
