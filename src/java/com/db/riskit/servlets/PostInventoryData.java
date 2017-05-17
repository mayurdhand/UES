package com.db.riskit.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.InventoryDTO;
import com.db.riskit.utils.file.InventoryFileOperations;
import com.db.riskit.utils.logging.Logger;

public class PostInventoryData extends HttpServlet implements RiskItConstants{

	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = null;
		Integer sheetNo = null;
		String line = null;;
		String[] data = null;
		InventoryDTO ivDTO = null;
		BufferedReader r = null;
		Map<String, InventoryDTO> ivRequestMap = null;	
		InputStream in = null;
		String env = null;
		String region = null;
		int i = 0;
		try {
			out = response.getWriter();
			in = request.getInputStream();
			env = (String) request.getParameter("env");
			region = (String) request.getParameter("region");
			
			if (region == null) {
				out.print("Invalid Region");
				return;
			} else if (region.equalsIgnoreCase(REGION_LONDON)) {
				sheetNo = 0;
			} else if (region.equalsIgnoreCase(REGION_NEW_YORK)) {
				sheetNo = 1;
			} else if (region.equalsIgnoreCase(REGION_SYDNEY) || region.equalsIgnoreCase(REGION_SINGAPORE)	|| region.equalsIgnoreCase(REGION_TOKYO)) {
				sheetNo = 2;
			} else {
				out.println("Invalid Region");
				return;
			}

			if (env == null	|| !(env.equalsIgnoreCase(ENV_QA)	|| env.equalsIgnoreCase(ENV_UAT) || env.equalsIgnoreCase(ENV_PROD_UAT))) {
				out.println("Invalid Environment");
				return;
			}
			
			out.println("REGION:" + region + " ,ENV:" + env + " ,SHEET-NO:"+ sheetNo);	
			Logger.log(this.getClass().getName(), Logger.INFO, "REGION:" + region + " ,ENV:" + env + " ,SHEET-NO:"+ sheetNo);
			
			ivRequestMap = new HashMap<String, InventoryDTO>();
			r = new BufferedReader(new InputStreamReader(in));
			
			while ((line = r.readLine()) != null) 
			{
				out.println("Received : " +line);
				Logger.log(this.getClass().getName(), Logger.INFO, "Received : " + line);
				data = line.split(",");
				ivDTO = new InventoryDTO();
				ivDTO.setConfigName(data[i++]);
				ivDTO.setReVersion(data[i++]);
				ivDTO.setJsm(data[i++]);
				ivDTO.setCacheTrades(data[i++]);
				ivDTO.setDbName(data[i++]);
				ivDTO.setDefaultDBAX(data[i++]);
				ivDTO.setDbaxParserVersion(data[i++]); 
				ivDTO.setCcInstance(data[i++]); 
				ivDTO.setCcVersion(data[i++]); 
				ivDTO.setJsmInstance(data[i++]); 
				ivDTO.setJsmVersion(data[i++]); 
				ivDTO.setTdcInstance(data[i++]); 
				ivDTO.setTdcVersion(data[i++]);
				ivDTO.setJsmSchema(data[i++]); 
				ivDTO.setJsmSchemaVersion(data[i++]); 
				i=0; // Reset Countor
				ivRequestMap.put(data[0], ivDTO);
			}
			InventoryFileOperations.getInstance().postInventory(sheetNo, region, env, ivRequestMap);
			out.println("Data Received Successfully.");
			Logger.log(this.getClass().getName(), Logger.INFO, "Data Received Successfully.");
		} catch (Exception e) {
			out.println(Logger.getStackTrace(e));
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Short description";
	}
	
}
