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

import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientsummary.api.PatientSummaryService;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportDesignResource;
import org.openmrs.module.reporting.report.renderer.TextTemplateRenderer;
import org.openmrs.module.reporting.report.service.ReportService;
import org.openmrs.util.OpenmrsClassLoader;

/**
 * Tests {@link {PatientSummaryService}}.
 */
@Ignore
public class PatientSummaryTestUtil {

	/**
	 * Utility method that tests that the results of evaluating a particular template,
	 * for a particular report definition, produces a particular result
	 */
	public static void testGroovyTemplate(PatientSummaryReportDefinition rd, Integer patientId, String templatePrefix) throws Exception {

		PatientSummaryTemplate template = createGroovyTemplate("Test", rd, "templates/" + templatePrefix + "Template.txt");
		String expectedResults = PatientSummaryTestUtil.getResourceAsString("templates/" + templatePrefix + "Output.txt");

		PatientSummaryService pss = Context.getService(PatientSummaryService.class);
		PatientSummaryResult result = pss.evaluatePatientSummaryTemplate(template, patientId, new HashMap<String, Object>());

		Assert.assertEquals(expectedResults, new String(result.getRawContents()));
		Assert.assertNull(result.getErrorDetails());
	}

	/**
	 * Utility method to retrieve a text-based resource as a String
	 */
	public static String getResourceAsString(String resource) throws Exception {
		InputStream is = null;
		try {
			is = OpenmrsClassLoader.getInstance().getResourceAsStream(resource);
			return IOUtils.toString(is, "UTF-8");
		}
		finally {
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * Utility method that will construct a PatientSummaryTemplate for the given report definition,
	 * using a TextTemplateRenderer with support for groovy scripting, with template located at the given resource
	 */
	public static PatientSummaryTemplate createGroovyTemplate(String name, PatientSummaryReportDefinition reportDefinition, String groovyResource) throws Exception {
		ReportDesign reportDesign = new ReportDesign();
		reportDesign.setName(name);
		reportDesign.setReportDefinition(reportDefinition);
		reportDesign.setRendererType(TextTemplateRenderer.class);
		reportDesign.addPropertyValue(TextTemplateRenderer.TEMPLATE_TYPE, "Groovy");

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.txt");
		resource.setReportDesign(reportDesign);
		InputStream is = null;
		try {
			is = OpenmrsClassLoader.getInstance().getResourceAsStream(groovyResource);
			resource.setContents(IOUtils.toByteArray(is));
		}
		finally {
			IOUtils.closeQuietly(is);
		}
		reportDesign.addResource(resource);
		reportDesign = Context.getService(ReportService.class).saveReportDesign(reportDesign);
		return new PatientSummaryTemplate(reportDesign);
	}
}
