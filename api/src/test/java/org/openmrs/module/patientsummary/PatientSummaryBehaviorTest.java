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
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientsummary.api.PatientSummaryService;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.patient.definition.EncountersForPatientDataDefinition;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.AgeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ObsActiveListPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ObsForPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredAddressDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.data.person.service.PersonDataService;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Tests {@link {PatientSummaryService}}.
 */
public class PatientSummaryBehaviorTest extends BaseModuleContextSensitiveTest {
	
	@Test
	public void shouldSupportDemographicData() throws Exception {
		PatientSummaryService pss = getService();

		PatientSummaryReportDefinition rd = new PatientSummaryReportDefinition();
		rd.setName("Test Patient Summary");
		PatientDataSetDefinition dsd = rd.getPatientDataSetDefinition();

		dsd.addColumn("patientName", new PreferredNameDataDefinition(), "");
		dsd.addColumn("age", new AgeDataDefinition(), "");
		dsd.addColumn("gender", new GenderDataDefinition(), "");
		dsd.addColumn("birthdate", new BirthdateDataDefinition(), "");
		dsd.addColumn("address", new PreferredAddressDataDefinition(), "");

		rd = pss.savePatientSummaryReportDefinition(rd);

		PatientSummaryTestUtil.testGroovyTemplate(rd, 7, "Demographics");
	}

	@Test
	public void shouldSupportEncounterData() throws Exception {
		PatientSummaryService pss = getService();

		PatientSummaryReportDefinition rd = new PatientSummaryReportDefinition();
		rd.setName("Test Patient Summary");
		PatientDataSetDefinition dsd = rd.getPatientDataSetDefinition();

		EncountersForPatientDataDefinition firstScheduledVisit = new EncountersForPatientDataDefinition();
		firstScheduledVisit.setWhich(TimeQualifier.FIRST);
		firstScheduledVisit.addType(Context.getEncounterService().getEncounterType("Scheduled"));

		dsd.addColumn("firstScheduledVisit", firstScheduledVisit, "");

		rd = pss.savePatientSummaryReportDefinition(rd);

		PatientSummaryTestUtil.testGroovyTemplate(rd, 7, "Encounters");
	}

	@Test
	public void shouldSupportObsData() throws Exception {
		PatientSummaryService pss = getService();

		PatientSummaryReportDefinition rd = new PatientSummaryReportDefinition();
		rd.setName("Test Patient Summary");
		PatientDataSetDefinition dsd = rd.getPatientDataSetDefinition();

		Concept weight = Context.getConceptService().getConcept("WEIGHT (KG)");

		ObsForPersonDataDefinition firstWeight = new ObsForPersonDataDefinition();
		firstWeight.setWhich(TimeQualifier.FIRST);
		firstWeight.setQuestion(weight);
		dsd.addColumn("firstWeight", firstWeight, "");

		ObsForPersonDataDefinition lastWeight = new ObsForPersonDataDefinition();
		lastWeight.setWhich(TimeQualifier.LAST);
		lastWeight.setQuestion(weight);
		dsd.addColumn("lastWeight", lastWeight, "");

		rd = pss.savePatientSummaryReportDefinition(rd);

		PatientSummaryTestUtil.testGroovyTemplate(rd, 7, "Obs");
	}
	
	@Test
	public void shouldSupportConditionalAlertsOrReminders() throws Exception {
		PatientSummaryService pss = getService();

		PatientSummaryReportDefinition rd = new PatientSummaryReportDefinition();
		rd.setName("Test Patient Summary");
		PatientDataSetDefinition dsd = rd.getPatientDataSetDefinition();

		Concept cd4Count = Context.getConceptService().getConcept("CD4 COUNT");

		ObsForPersonDataDefinition lastCd4Count = new ObsForPersonDataDefinition();
		lastCd4Count.setWhich(TimeQualifier.LAST);
		lastCd4Count.setQuestion(cd4Count);
		dsd.addColumn("lastCD4Count", lastCd4Count, "");
		
		rd = pss.savePatientSummaryReportDefinition(rd);

		PatientSummaryTestUtil.testGroovyTemplate(rd, 7, "Alert");
	}

	private PatientSummaryService getService() {
	    return Context.getService(PatientSummaryService.class);
    }
	
	@Test
	public void shouldDisplayObsBasedProblemList() throws Exception {
		PatientSummaryReportDefinition rd = new PatientSummaryReportDefinition();
		rd.setName("Test Patient Summary");
		PatientDataSetDefinition dsd = rd.getPatientDataSetDefinition();

		ObsActiveListPersonDataDefinition d = new ObsActiveListPersonDataDefinition();

		List<Concept> problemsAdded = new ArrayList<Concept>();
		problemsAdded.add(Context.getConceptService().getConcept(7));
		d.setStartingConcepts(problemsAdded);

		List<Concept> problemsResolved = new ArrayList<Concept>();
		problemsResolved.add(Context.getConceptService().getConcept(8));
		d.setEndingConcepts(problemsResolved);

		dsd.addColumn("obs", d, "");
		
		getService().savePatientSummaryReportDefinition(rd);
		
		saveObs(7, "2012-01-01", 7, 6);
		saveObs(7, "2012-02-01", 7, 18);
		
		PatientSummaryTestUtil.testGroovyTemplate(rd, 7, "ObsBasedProblemList");
	}
	
	private void saveObs(Integer personId, String dateStr, Integer question, Integer answer) {
		Person p = Context.getPersonService().getPerson(personId);
		Date d = DateUtil.parseDate(dateStr, "yyyy-MM-dd");
		Concept q = Context.getConceptService().getConcept(question);
		Concept a = Context.getConceptService().getConcept(answer);
		Location l = Context.getLocationService().getLocation(1);
		Obs o = new Obs(p, q, d, l);
		o.setValueCoded(a);
		Context.getObsService().saveObs(o, "Test");
	}
	
	@Test
	public void shouldSupportLastThreeObsData() throws Exception {
		PatientSummaryService pss = Context.getService(PatientSummaryService.class);

		PatientSummaryReportDefinition rd = new PatientSummaryReportDefinition();
		rd.setName("Test Patient Summary");
		PatientDataSetDefinition dsd = rd.getPatientDataSetDefinition();

		Concept weight = Context.getConceptService().getConcept("WEIGHT (KG)");

		ObsForPersonDataDefinition weights = new ObsForPersonDataDefinition();
		weights.setWhich(TimeQualifier.ANY);
		weights.setQuestion(weight);
		dsd.addColumn("weights", weights, "");

		rd = pss.savePatientSummaryReportDefinition(rd);

		PatientSummaryTestUtil.testGroovyTemplate(rd, 7, "LastThreeObs");
	}
}
