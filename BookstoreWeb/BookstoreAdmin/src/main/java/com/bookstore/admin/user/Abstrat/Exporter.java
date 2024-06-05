package com.bookstore.admin.user.Abstrat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.http.HttpServletResponse;

public class Exporter {
	public void setResponseHeader(HttpServletResponse response, String contentType, String extension) throws IOException {

		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		String timestamp = dateFormatter.format(new Date());
		String fileName = "users_" + timestamp + extension;

		response.setContentType(contentType);

		String headerKey = "Content-Disposition";
		String headervalude = "attachment; fileName=" + fileName;

		response.setHeader(headerKey, headervalude);
	}
}
