package com.db.riskit.interceptors;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import com.db.riskit.utils.logging.Logger;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ValidationInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;

	public String intercept(ActionInvocation actionInvocation) throws Exception {
		String actionName = ActionContext.getContext().getName();		
		ActionMapping am = ServletActionContext.getActionMapping();
		Logger.log(this.getClass().getName(), Logger.INFO, " Action:" + actionName + ", Method:" + am.getMethod());
		//
		// Map<String, Object> map = ActionContext.getContext().getContextMap();
		// for (String key : map.keySet()) {
		// System.out.println("Context Key = " + key);
		// System.out.println("Context Value = " + map.get(key));
		// }
		//
		// Map<String, Object> sessMap =
		// ActionContext.getContext().getSession();
		// for (String sessKey : sessMap.keySet()) {
		// System.out.println("Session Key = " + sessKey);
		// System.out.println("Session Value = " + sessMap.get(sessKey));
		// }
		//
		// Map<String, Object> applicationMap =
		// ActionContext.getContext().getApplication();
		// for (String appKey : applicationMap.keySet()) {
		// System.out.println("Application Key = " + appKey);
		// System.out.println("Application Value = " +
		// applicationMap.get(appKey));
		// }

		try {
			if (am.getMethod() == null) {
				return actionInvocation.invoke();
			}

			if (am.getMethod().equalsIgnoreCase("bookConfig")) {
				// if (ServletActionContext.getRequest()
				// .getParameter("projectName").equals("")) {
				// addActionMessage(actionInvocation,
				// "Please enter Project Name.");
				// return "bookConfig";
				// }
			} else if (am.getMethod().equalsIgnoreCase("searchConfigBooking")
					|| am.getMethod().equalsIgnoreCase("resultsConfigBookings")
					|| am.getMethod().equalsIgnoreCase("viewConfigBookings")) {
				// ValueStack vs = ActionContext.getContext().getValueStack();
				// for (int i = 0; i < vs.size(); i++) {
				// Object ob = vs.pop();
				// if (ob instanceof BookConfigAction) {
				// ((BookConfigAction) ob).loadCombosWithAll();
				// vs.push(ob);
				// } else {
				// vs.push(ob);
				// }
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actionInvocation.invoke();
	}

	// private void addActionMessage(ActionInvocation invocation, String
	// message) {
	// Object action = invocation.getAction();
	// ((ActionSupport) action).addActionError(message);
	//
	// }
}
