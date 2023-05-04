package com.springcore.Merge;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GympikGymWebscraping {
	public static void main(String[] args) {

		int totalPages = 1;

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter city name: ");
		String city = sc.nextLine().toLowerCase();
		String baseUrl = "https://www.gympik.com/centers/" + city + "/gyms~";

		@SuppressWarnings("resource")
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Gym Details");
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Gym Name");
		headerRow.createCell(1).setCellValue("Address");
		headerRow.createCell(2).setCellValue("Contact");
		headerRow.createCell(3).setCellValue("Service");
		headerRow.createCell(4).setCellValue("Price");
		headerRow.createCell(5).setCellValue("Rating");
		headerRow.createCell(6).setCellValue("People Rated");
		int rowNum = 1;

		try {
			// Loop through all pages
			for (int page = 1; page <= totalPages; page++) {
				String url = baseUrl + page;

				// Send a GET request to the page
				Document document = Jsoup.connect(url).get();

				// Find all gym names and addresses
				Elements gymNames = document.select(".trendingName");
				Elements addresses = document.select(".centerAdres");

				Elements gymPage = document.select(".paginationInfo");
				String paginationText = gymPage.text();
				String[] parts = paginationText.split(" ");
				String totalPagesString = parts[4];
				int totalPages1 = Integer.parseInt(totalPagesString);
				totalPages = totalPages1;

				// Extract gym names, addresses, contacts, and prices into lists
				List<String> gymNameList = new ArrayList<>();
				List<String> addressList = new ArrayList<>();
				List<String> contactList = new ArrayList<>();
				List<String> serviceList = new ArrayList<>();
				List<String> priceList = new ArrayList<>();

				// Find all contacts, services, and prices for the current gym entry
				for (int j = 1; j <= 10; j++) {
					Elements contacts = document.select(
							"html > body > div:nth-child(3) > div:nth-child(2) > div:nth-child(1) > div:nth-child(" + j
									+ ") > div > div:nth-child(2) > p:nth-child(4) > span");
					Elements services = document.select(
							"html > body > div:nth-child(3) > div:nth-child(2) > div:nth-child(1) > div:nth-child(" + j
									+ ") > div > div:nth-child(2) > p:nth-child(3) > span");
					Elements prices = document.select(
							"html > body > div:nth-child(3) > div:nth-child(2) > div:nth-child(1) > div:nth-child(" + j
									+ ") > div > div:nth-child(2) > p:nth-child(5) > span");

					for (Element contact : contacts) {
						contactList.add(contact.text());
					}

					for (Element service : services) {
						serviceList.add(service.text());
					}

					for (Element price : prices) {
						priceList.add(price.text());
					}
				}

				for (Element gymName : gymNames) {
					gymNameList.add(gymName.text());
				}

				for (Element address : addresses) {
					addressList.add(address.text());
				}

				// Store the gym details in the Excel file
				for (int i = 0; i < gymNameList.size(); i++) {
					String name = gymNameList.get(i);
					String address = (i < addressList.size()) ? addressList.get(i) : "";
					String contact = (i < contactList.size()) ? contactList.get(i) : "";
					String service = (i < serviceList.size()) ? serviceList.get(i) : "";
					String price = (i < priceList.size()) ? priceList.get(i) : "";

					String n=null;
					Row row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(name);
					row.createCell(1).setCellValue(address);
					row.createCell(2).setCellValue(service);
					row.createCell(3).setCellValue(contact);
					row.createCell(4).setCellValue(price);
					row.createCell(5).setCellValue(n);
					row.createCell(6).setCellValue(n);
				}
			}
		
		for(int i=0;i<6;i++) {
			sheet.autoSizeColumn(i);
		}
			// Write the workbook to an Excel file
			try (FileOutputStream outputStream = new FileOutputStream(city+"Gympik.xlsx")) {
				workbook.write(outputStream);
				System.out.println("Excel file created successfully!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}