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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.patient.EvaluatedPatientData;
import org.openmrs.module.reporting.data.patient.definition.EncountersForPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.service.PatientDataService;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportDesignResource;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.renderer.TextTemplateRenderer;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.util.OpenmrsClassLoader;

/**
 * Tests various ways of rendering the patient summary
 */
public class PatientSummaryRendererTest extends BaseModuleContextSensitiveTest {
	
	@Test
	public void shouldDisplayDateOfFirstEncounter() throws Exception {
		
		EvaluationContext context = new EvaluationContext();
		context.setBaseCohort(new Cohort("7"));
		
		EncountersForPatientDataDefinition dataDefinition = new EncountersForPatientDataDefinition();
		dataDefinition.setWhich(TimeQualifier.FIRST);
		dataDefinition.addType(new EncounterType(1));
		
		EvaluatedPatientData pd = Context.getService(PatientDataService.class).evaluate(dataDefinition, context);
		Assert.assertTrue(pd.getData().get(7) instanceof Encounter);
		
		ReportDefinition reportDefinition = new ReportDefinition();
		reportDefinition.setName("Patient Summary");
		
		PatientDataSetDefinition dataset = new PatientDataSetDefinition();
		dataset.addColumn("encounter", dataDefinition, (String) null, null);
		reportDefinition.addDataSetDefinition("encounters", dataset, null);
		
		final ReportDesign reportDesign = new ReportDesign();
		reportDesign.setName("TestDesign");
		reportDesign.setReportDefinition(reportDefinition);
		reportDesign.setRendererType(TextTemplateRenderer.class);
		
		reportDesign.addPropertyValue(TextTemplateRenderer.TEMPLATE_TYPE, "Groovy");
		
		ReportDefinitionService rs = Context.getService(ReportDefinitionService.class);
		ReportData reportData = rs.evaluate(reportDefinition, context);
		
		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("DateOfFirstEncounter.txt");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/DateOfFirstEncounter.txt");
		resource.setContents(IOUtils.toByteArray(is));
		IOUtils.closeQuietly(is);
		reportDesign.addResource(resource);
		
		TextTemplateRenderer renderer = new TextTemplateRenderer() {
			
			public ReportDesign getDesign(String argument) {
				return reportDesign;
			}
		};
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderer.render(reportData, "ReportData", baos);
		String renderedOutput = StringUtils.deleteWhitespace(baos.toString());
		
		String xml = "FirstEncounteron15-Aug-2008";
		Assert.assertEquals(xml, renderedOutput);
	}
}
