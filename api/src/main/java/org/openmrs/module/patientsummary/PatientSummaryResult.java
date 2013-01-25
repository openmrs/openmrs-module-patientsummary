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

import java.util.Map;

/**
 * Represents a particular Patient Summary
 */
public class PatientSummaryResult {
	
	//***** PROPERTIES *****
	
	private PatientSummaryTemplate summaryTemplate;
	private Integer patientId;
	private Map<String, Object> parameters;
	private String contentType;
	private byte[] rawContents;
	private Throwable errorDetails;
	
	//***** CONSTRUCTORS *****
	
	/**
	 * Default constructor
	 */
	public PatientSummaryResult(PatientSummaryTemplate summary, Integer patientId, Map<String, Object> parameters) {
		this.summaryTemplate = summary;
		this.patientId = patientId;
		this.parameters = parameters;
	}
	
	//***** PROPERTY ACCESS *****

	/**
	 * @return the summary template
	 */
	public PatientSummaryTemplate getSummaryTemplate() {
		return summaryTemplate;
	}

	/**
	 * @param summaryTemplate the summary template to set
	 */
	public void setSummaryTemplate(PatientSummaryTemplate summaryTemplate) {
		this.summaryTemplate = summaryTemplate;
	}

	/**
	 * @return the patientId
	 */
	public Integer getPatientId() {
		return patientId;
	}

	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the rawContents
	 */
	public byte[] getRawContents() {
		return rawContents;
	}

	/**
	 * @param rawContents the rawContents to set
	 */
	public void setRawContents(byte[] rawContents) {
		this.rawContents = rawContents;
	}

	/**
	 * @return the errorDetails
	 */
	public Throwable getErrorDetails() {
		return errorDetails;
	}

	/**
	 * @param errorDetails the errorDetails to set
	 */
	public void setErrorDetails(Throwable errorDetails) {
		this.errorDetails = errorDetails;
	}
	
}
