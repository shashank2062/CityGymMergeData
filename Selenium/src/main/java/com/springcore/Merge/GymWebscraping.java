package com.springcore.Merge;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GymWebscraping {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter city name: ");
		String city = sc.nextLine().toLowerCase();
		
		String gympikFile = city+"Gympik.xlsx";
		String fitternityFile = city+"Fitternitygyms.xlsx";
		String justdialFile = city+"Justdialgyms.xlsx";
		sc.close();
		try {
			Workbook mergedWorkbook = new XSSFWorkbook();
			Sheet mergedSheet = mergedWorkbook.createSheet("Merged Gyms");
			Row columnNamesRow = mergedSheet.createRow(0);
			columnNamesRow.createCell(0).setCellValue("Gym Name");
			columnNamesRow.createCell(1).setCellValue("Merged Address");
			columnNamesRow.createCell(2).setCellValue("Merged Contact");
			columnNamesRow.createCell(3).setCellValue("Merged Service");
			columnNamesRow.createCell(4).setCellValue("Merged Rating");
			columnNamesRow.createCell(6).setCellValue("Merged price");
			columnNamesRow.createCell(7).setCellValue("Merged peoplerated");

			Map<String, GymDetails> gymDetailsMap = new HashMap<>();

			FileInputStream gympikInputStream = new FileInputStream(gympikFile);
			Workbook gympikWorkbook = new XSSFWorkbook(gympikInputStream);
			Sheet gympikSheet = gympikWorkbook.getSheetAt(0);

			for (Row row : gympikSheet) {
				String gymName = row.getCell(0).getStringCellValue();

				if (!gymDetailsMap.containsKey(gymName)) {
					gymDetailsMap.put(gymName, new GymDetails());
				}

				GymDetails gymDetails = gymDetailsMap.get(gymName);
				gymDetails.setAddress1(row.getCell(1).getStringCellValue());
				gymDetails.setContact1(row.getCell(2).getStringCellValue());
				gymDetails.setService1(row.getCell(3).getStringCellValue());
				gymDetails.setRating1(row.getCell(4).getStringCellValue());
				gymDetails.setPrice1(row.getCell(5).getStringCellValue());
				gymDetails.setPeoplerated1(row.getCell(6).getStringCellValue());

			}

			gympikWorkbook.close();
			gympikInputStream.close();

			FileInputStream fitternityInputStream = new FileInputStream(fitternityFile);
			Workbook fitternityWorkbook = new XSSFWorkbook(fitternityInputStream);
			Sheet fitternitySheet = fitternityWorkbook.getSheetAt(0);

			for (Row row : fitternitySheet) {
				String gymName = row.getCell(0).getStringCellValue();

				if (!gymDetailsMap.containsKey(gymName)) {
					gymDetailsMap.put(gymName, new GymDetails());
				}

				GymDetails gymDetails = gymDetailsMap.get(gymName);
				gymDetails.setAddress2(row.getCell(1).getStringCellValue());
				gymDetails.setContact2(row.getCell(2).getStringCellValue());
				gymDetails.setService2(row.getCell(3).getStringCellValue());
				gymDetails.setRating2(row.getCell(4).getStringCellValue());
				gymDetails.setPrice2(row.getCell(5).getStringCellValue());
				gymDetails.setPeoplerated2(row.getCell(6).getStringCellValue());
			}

			fitternityWorkbook.close();
			fitternityInputStream.close();

			FileInputStream justdialInputStream = new FileInputStream(justdialFile);
			Workbook justdialWorkbook = new XSSFWorkbook(justdialInputStream);
			Sheet justdialSheet = justdialWorkbook.getSheetAt(0);

			for (Row row : justdialSheet) {
				String gymName = row.getCell(0).getStringCellValue();

				if (!gymDetailsMap.containsKey(gymName)) {
					gymDetailsMap.put(gymName, new GymDetails());
				}

				GymDetails gymDetails = gymDetailsMap.get(gymName);
				gymDetails.setAddress3(row.getCell(1).getStringCellValue());
				gymDetails.setContact3(row.getCell(2).getStringCellValue());
				gymDetails.setService3(row.getCell(3).getStringCellValue());
				gymDetails.setRating3(row.getCell(3).getStringCellValue());
				gymDetails.setPrice3(row.getCell(5).getStringCellValue());
				gymDetails.setPeoplerated3(row.getCell(6).getStringCellValue());
			}

			justdialWorkbook.close();
			justdialInputStream.close();

			int rowNum = 0;
			for (Map.Entry<String, GymDetails> entry : gymDetailsMap.entrySet()) {
				Row mergedRow = mergedSheet.createRow(rowNum++);
				GymDetails gymDetails = entry.getValue();
				mergedRow.createCell(0).setCellValue(entry.getKey()); // Gym Name
				mergedRow.createCell(1).setCellValue(mergeAddress(gymDetails)); // Merged Ratings
				mergedRow.createCell(2).setCellValue(mergeContact(gymDetails)); // Merged Address
				mergedRow.createCell(3).setCellValue(mergeService(gymDetails)); // Merged Service or Price
				mergedRow.createCell(4).setCellValue(mergeRating(gymDetails)); // merged Ratings
				mergedRow.createCell(5).setCellValue(mergePrice(gymDetails)); // merged price
				mergedRow.createCell(6).setCellValue(mergePeopleRated(gymDetails)); // mereged people rated

				for (int i = 0; i < 5; i++) {
					mergedSheet.autoSizeColumn(i);
				}
			}

			String mergedFile = "MergedGyms.xlsx";
			FileOutputStream outputStream = new FileOutputStream(city+mergedFile);
			mergedWorkbook.write(outputStream);
			outputStream.close();

			mergedWorkbook.close();

			System.out.println("city "+city+" Merged Excel file created successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String mergeAddress(GymDetails gymDetails) {
		String address1 = gymDetails.getAddress1();
		String address2 = gymDetails.getAddress2();
		String address3 = gymDetails.getAddress3();

		StringBuilder mergedAddress = new StringBuilder();
		if (address1 != null && !address1.isEmpty()) {
			mergedAddress.append(address1);
		}
		if (address2 != null && !address2.isEmpty()) {
			if (mergedAddress.length() > 0) {
				mergedAddress.append(", ");
			}
			mergedAddress.append(address2);
		}
		if (address3 != null && !address3.isEmpty()) {
			if (mergedAddress.length() > 0) {
				mergedAddress.append(", ");
			}
			mergedAddress.append(address3);
		}

		return mergedAddress.toString();
	}

	private static String mergeContact(GymDetails gymDetails) {
		String contact1 = gymDetails.getContact1();
		String contact2 = gymDetails.getContact2();
		String contact3 = gymDetails.getContact3();

		StringBuilder mergedContact = new StringBuilder();
		if (contact1 != null && !contact1.isEmpty()) {
			mergedContact.append(contact1);
		}
		if (contact2 != null && !contact2.isEmpty()) {
			if (mergedContact.length() > 0) {
				mergedContact.append(", ");
			}
			mergedContact.append(contact2);
		}
		if (contact3 != null && !contact3.isEmpty()) {
			if (mergedContact.length() > 0) {
				mergedContact.append(", ");
			}
			mergedContact.append(contact3);
		}

		return mergedContact.toString();
	}

	private static String mergeService(GymDetails gymDetails) {
		String Service1 = gymDetails.getService1();
		String Service2 = gymDetails.getService2();
		String Service3 = gymDetails.getService3();

		StringBuilder mergedService = new StringBuilder();
		if (Service1 != null && !Service1.isEmpty()) {
			mergedService.append(Service1);
		}
		if (Service2 != null && !Service2.isEmpty()) {
			if (mergedService.length() > 0) {
				mergedService.append(", ");
			}
			mergedService.append(Service2);
		}
		if (Service3 != null && !Service3.isEmpty()) {
			if (mergedService.length() > 0) {
				mergedService.append(", ");
			}
			mergedService.append(Service3);
		}
		return mergedService.toString();
	}

	private static String mergeRating(GymDetails gymDetails) {
		String rating1 = gymDetails.getRating1();
		String rating2 = gymDetails.getRating2();
		String rating3 = gymDetails.getRating3();

		StringBuilder mergedRating = new StringBuilder();
		if (rating1 != null && !rating1.isEmpty()) {
			mergedRating.append(rating1);
		}
		if (rating2 != null && !rating2.isEmpty()) {
			if (mergedRating.length() > 0) {
				mergedRating.append(", ");
			}
			mergedRating.append(rating2);
		}
		if (rating3 != null && !rating3.isEmpty()) {
			if (mergedRating.length() > 0) {
				mergedRating.append(", ");
			}
			mergedRating.append(rating3);
		}

		return mergedRating.toString();
	}

	private static String mergePrice(GymDetails gymDetails) {
		String price1 = gymDetails.getPrice1();
		String price2 = gymDetails.getPrice2();
		String price3 = gymDetails.getPrice3();

		StringBuilder mergedPrice = new StringBuilder();
		if (price1 != null && !price1.isEmpty()) {
			mergedPrice.append(price1);
		}
		if (price2 != null && !price2.isEmpty()) {
			if (mergedPrice.length() > 0) {
				mergedPrice.append(", ");
			}
			mergedPrice.append(price2);
		}
		if (price3 != null && !price3.isEmpty()) {
			if (mergedPrice.length() > 0) {
				mergedPrice.append(", ");
			}
			mergedPrice.append(price3);
		}

		return mergedPrice.toString();
	}

	private static String mergePeopleRated(GymDetails gymDetails) {
		String peoplerated1 = gymDetails.getPeoplerated1();
		String peoplerated2 = gymDetails.getPeoplerated2();
		String peoplerated3 = gymDetails.getPeoplerated3();

		StringBuilder mergedPeoplerated = new StringBuilder();
		if (peoplerated1 != null && !peoplerated1.isEmpty()) {
			mergedPeoplerated.append(peoplerated1);
		}
		if (peoplerated2 != null && !peoplerated2.isEmpty()) {
			if (mergedPeoplerated.length() > 0) {
				mergedPeoplerated.append(", ");
			}
			mergedPeoplerated.append(peoplerated2);
		}
		if (peoplerated3 != null && !peoplerated3.isEmpty()) {
			if (mergedPeoplerated.length() > 0) {
				mergedPeoplerated.append(", ");
			}
			mergedPeoplerated.append(peoplerated3);
		}

		return mergedPeoplerated.toString();
	}
}