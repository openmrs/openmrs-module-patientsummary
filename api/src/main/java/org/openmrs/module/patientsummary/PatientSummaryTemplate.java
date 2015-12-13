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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.reporting.report.ReportDesign;

/**
 * Represents a particular Patient Summary Template
 */
public class PatientSummaryTemplate {

	protected final Log log = LogFactory.getLog(this.getClass());

	//***** PROPERTIES *****

	private ReportDesign reportDesign;

	//***** CONSTRUCTORS *****

	/**
	 * Default constructor
	 */
	public PatientSummaryTemplate() {}

	/**
	 * Full Constructor
	 */
	public PatientSummaryTemplate(ReportDesign reportDesign) {
		this.reportDesign = reportDesign;
	}

	//***** METHODS *****

	/**
	 * @return the contentType for this PatientSummary
	 */
	public String getContentType() {
        return "text/html";
	}

	/**
	 * @return the filename for this PatientSummary for use when exporting
	 */
	public String getExportFilename() {
        return reportDesign.getName() + ".html";
	}

	/**
	 * @return the primary key id
	 */
	public Integer getId() {
		return getReportDesign() != null ? getReportDesign().getId() : null;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return getReportDesign() != null ? getReportDesign().getUuid() : null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return getReportDesign() != null ? getReportDesign().getName() : null;
	}

	/**
	 * @return the associated patient summary report definition
	 */
	public PatientSummaryReportDefinition getReportDefinition() {
		return getReportDesign() != null ? (PatientSummaryReportDefinition)getReportDesign().getReportDefinition() : null;
	}

	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof PatientSummaryTemplate) {
			PatientSummaryTemplate that = (PatientSummaryTemplate)o;
			if (this.getReportDesign() != null && that.getReportDesign() != null) {
				return this.getReportDesign().equals(that.getReportDesign());
			}
		}
		return super.equals(o);
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (this.getReportDesign() != null) {
			return this.getReportDesign().hashCode();
		}
		return super.hashCode();
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		if (getReportDesign() != null) {
			if (getReportDesign().getReportDefinition() != null) {
				return getReportDesign().getReportDefinition().getName() + " (" + getReportDesign().getName() + ")";
			}
			return getReportDesign().getName();
		}
		return super.toString();
	}

	//***** PROPERTY ACCESS *****

	/**
	 * @return the reportDesign
	 */
	public ReportDesign getReportDesign() {
		return reportDesign;
	}

	/**
	 * @param reportDesign the reportDesign to set
	 */
	public void setReportDesign(ReportDesign reportDesign) {
		this.reportDesign = reportDesign;
	}
}
