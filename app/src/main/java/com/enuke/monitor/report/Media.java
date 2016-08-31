package com.enuke.monitor.report;

import java.io.File;

import com.enuke.monitor.core.event.Report;

public class Media implements Report {
	public File file;
	public String type;
	
	public Media(File file, String type) {
		this.file = file;
		this.type = type;
	}
}
