package com.db.riskit.utils;

import org.apache.axis2.transport.http.HttpTransportProperties;

import com.db.riskit.dto.TicketDTO;
import com.db.riskit.stub.servicenow.inc.ServiceNow_u_incident_platformStub;
import com.db.riskit.utils.logging.Logger;

public class TicketUtil {	


	public void raiseINC(TicketDTO ticketDTO) 
	{
		ServiceNow_u_incident_platformStub.InsertResponse resp = null;
		ServiceNow_u_incident_platformStub.Insert inc = null;
		try {

			HttpTransportProperties.Authenticator basicAuthentication = new HttpTransportProperties.Authenticator();
			basicAuthentication.setUsername("AS_RPL_UES_interface");
			basicAuthentication.setPassword("$9v%Y?(Y");
			
			ServiceNow_u_incident_platformStub proxy = new ServiceNow_u_incident_platformStub();
			proxy._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);
			proxy._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE,basicAuthentication);

			inc = new ServiceNow_u_incident_platformStub.Insert();
			resp = new ServiceNow_u_incident_platformStub.InsertResponse();

			inc.setU_affected_service_ci(ticketDTO.getAffectedCi());
			inc.setU_reported_by(ticketDTO.getRptBy());
			inc.setShort_description(ticketDTO.getShortDesc());
			inc.setU_technical_impact_description(ticketDTO.getLongDesc());
			inc.setU_business_impact_description("P4");
			inc.setU_symptom_code("Unavailable");
			resp = proxy.insert(inc);
			
			ticketDTO.setInCode(resp.getDisplay_value());
			if(resp.getStatus().equalsIgnoreCase("error")){
				ticketDTO.setError(resp.getStatus_message());
			}		
			Logger.log(this.getClass().getName(), Logger.INFO, "Incident : " + resp.getDisplay_value());
			Logger.log(this.getClass().getName(), Logger.INFO, "Status Message : " + resp.getStatus_message());
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

}
