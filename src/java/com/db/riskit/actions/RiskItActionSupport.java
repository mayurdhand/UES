package com.db.riskit.actions;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;

import com.db.riskit.constants.RiskItConstants;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.tree.utils.common.StringUtils;

public abstract class RiskItActionSupport extends ActionSupport implements
ServletResponseAware,ServletRequestAware, ServletContextAware, Preparable, RiskItConstants {

	private static final long serialVersionUID = 1L;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ServletContext context;
	protected String idefAction;
	protected String idefMethod;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	public HttpServletResponse getServletResponse() {		
		return response;
	}
	
	@Override
	public void setServletContext(ServletContext context) {
		this.context = context;
	}

	/**
	 * Clears up the errors and messages stack of any previous existing
	 * messages.
	 */
	public void prepare() throws Exception {
		this.clearErrorsAndMessages();
	}

	/**
	 * default method for action processing.does not do any processing. just
	 * returns input as response.
	 */
	@Override
	public String execute() {
		return INPUT;
	}

	public String getActionName() {
		String actionName = ActionContext.getContext().getName();
		return actionName;
	}

	public String getUserIdFromSession() {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		Object userID = session.getAttribute("User-id");
		if (userID == null) {
			return null;
		}
		return userID.toString();
	}

	public Object getSessionAttribute(String key) {
		return this.request.getSession().getAttribute(key);
	}

	protected void setSessionAttribute(String key, Object value) {
		this.request.getSession().setAttribute(key, value);
	}

	protected void removeSessionAttribute(String key) {
		if (this.request.getSession().getAttribute(key) != null)
			this.request.getSession().removeAttribute(key);
	}
	 @SuppressWarnings("unchecked")
	protected void clearSession() {
		if (this.request.getSession() != null) {
			Enumeration<String> sessionAttributes = this.request.getSession()
					.getAttributeNames();
			while (sessionAttributes.hasMoreElements())
				this.request.getSession().removeAttribute(
						sessionAttributes.nextElement());
		}
	}

	protected void invalidateSession() {
		if (this.request.getSession() != null) {
			clearSession();
			this.request.getSession().invalidate();
		}
	}

	public Object getApplicationAttribute(String key) {
		return this.context.getAttribute(key);
	}

	protected void setApplicationAttribute(String key, Object value) {
		this.context.setAttribute(key, value);
	}
	 @SuppressWarnings("unchecked")
	protected void clearApplication() {
		Enumeration<String> applicationAttributes = this.context
				.getAttributeNames();
		while (applicationAttributes.hasMoreElements())
			this.context.removeAttribute(applicationAttributes.nextElement());
	}

	public String getIdefAction() {
		return idefAction;
	}

	public void setIdefAction(String idefAction) {
		if (StringUtils.isStringNull(idefAction))
			this.idefAction = null;
		else
			this.idefAction = StringUtils.compressAndTrim(idefAction);
	}

	public String getIdefMethod() {
		return idefMethod;
	}

	public void setIdefMethod(String idefMethod) {
		if (StringUtils.isStringNull(idefMethod))
			this.idefMethod = null;
		else
			this.idefMethod = StringUtils.compressAndTrim(idefMethod);
	}

	public String getBackUrl() {
		String backUrl = null;
		if (idefAction != null) {
			if (idefMethod != null)
				backUrl = idefAction + "!" + idefMethod + ".action?"
						+ request.getQueryString();
			else {
				backUrl = idefAction + ".action?" + request.getQueryString();
			}
		}
		return backUrl;
	}
}
