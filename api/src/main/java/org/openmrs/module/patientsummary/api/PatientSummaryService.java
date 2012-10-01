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
package org.openmrs.module.patientsummary.api;

import java.util.List;
import java.util.Map;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.patientsummary.PatientSummary;
import org.openmrs.module.patientsummary.PatientSummaryReportDefinition;
import org.openmrs.module.patientsummary.PatientSummaryResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * API interface for Patient Summary services
 */
public interface PatientSummaryService extends OpenmrsService {
	
	/**
	 * @return the {@link PatientSummaryReportDefinition} referenced by the passed id
	 */
	@Transactional(readOnly = true)
	public PatientSummaryReportDefinition getPatientSummaryReportDefinition(Integer id);
	
	/**
	 * @return the {@link PatientSummaryReportDefinition} referenced by the passed uuid
	 */
	@Transactional(readOnly = true)
	public PatientSummaryReportDefinition getPatientSummaryReportDefinitionByUuid(String uuid);
	
	/**
	 * @return all {@link PatientSummaryReportDefinition}s
	 */
	@Transactional(readOnly = true)
	public List<PatientSummaryReportDefinition> getAllPatientSummaryReportDefinitions(boolean includeRetired);
	
	/**
	 * @return the PatientSummary referenced by the passed id
	 */
	@Transactional(readOnly = true)
	public PatientSummary getPatientSummary(Integer id);
	
	/**
	 * @return the PatientSummary with the given uuid
	 */
	@Transactional(readOnly = true)
	public PatientSummary getPatientSummaryByUuid(String uuid);
	
	/**
	 * @return all {@link PatientSummary}s
	 */
	@Transactional(readOnly = true)
	public List<PatientSummary> getAllPatientSummaries(boolean includeRetired);
	
	/**
	 * @return the resulting patient summary result from evaluating the passed patient summary for the given patient and parameters
	 */
	@Transactional(readOnly = true)
	public PatientSummaryResult evaluatePatientSummary(PatientSummary summary, Integer patientId, Map<String, Object> parameters);

	/**
     * Purges the given report definition.
     * 
     * @param reportDefinition
     */
    public void purgeReportDefinition(PatientSummaryReportDefinition reportDefinition);
}
