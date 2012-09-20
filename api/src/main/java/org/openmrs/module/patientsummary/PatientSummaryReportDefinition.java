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

import java.util.List;

import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;

/**
 * A {@link ReportDefinition} subclass that represents the metadata that describes a particular
 * patient summary report that can be evaluated.
 * 
 * @see ReportDefinition
 */
@Localized("reporting.PatientSummaryReportDefinition")
public class PatientSummaryReportDefinition extends ReportDefinition {
	
	public static final String DEFAULT_DATASET_KEY = "defaultDataSet";
	
	/**
	 * Default Constructor
	 */
	public PatientSummaryReportDefinition() {
		super();
		addDataSetDefinition(DEFAULT_DATASET_KEY, new PatientDataSetDefinition(), null);
	}
	
	/**
	 * @param d
	 */
	public PatientSummaryReportDefinition(ReportDesign d) {
		this();
		setName(d.getReportDefinition().getName());
		setParameters(d.getReportDefinition().getParameters());
		getDataSetDefinitions().putAll(d.getReportDefinition().getDataSetDefinitions());
	}
	
	/**
	 * @see org.openmrs.module.reporting.evaluation.BaseDefinition#getParameters()
	 */
	@Override
	public List<Parameter> getParameters() {
		return getDataSetDefinitions().get(DEFAULT_DATASET_KEY).getParameterizable().getParameters();
	}
	
	/**
	 * @param dataSetDefinition
	 */
	public void addDataSetDefinition(DataSetDefinition dataSetDefinition) {
		addDataSetDefinition(dataSetDefinition.getName(), dataSetDefinition, null);
	}
}
