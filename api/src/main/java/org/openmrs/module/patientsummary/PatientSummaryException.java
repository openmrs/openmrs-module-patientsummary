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

import org.openmrs.api.APIException;

/**
 * Indicates an exception in this module
 */
public class PatientSummaryException extends APIException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor that takes in a message
	 */
	public PatientSummaryException(String message) {
		super(message);
	}
	
	/**
	 * Constructor that takes in a message and a cause
	 */
	public PatientSummaryException(String message, Throwable cause) {
		super(message, cause);
	}
}
