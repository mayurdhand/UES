package com.db.riskit.utils;

import java.io.File;
import java.net.URL;

import com.db.riskit.constants.RiskItConstants;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateUtil implements RiskItConstants{
	
	public static Template getTemplate(String templateName) throws Exception{
		Configuration cfg = new Configuration();
		URL url = Template.class.getResource(TEMPLATE_PATH);
		File file = new File(url.toURI());
		cfg.setDirectoryForTemplateLoading(file);
		Template template = cfg.getTemplate(templateName);
		return template;
	}

}
