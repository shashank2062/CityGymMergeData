package com.springcore.Merge;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.Scanner;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class FitternityGymWebscraping{
    public static void main(String[] args) throws IOException {
        int entry = 1;

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String city1 = sc.nextLine().toLowerCase();
        String city = city1;

        String url = "https://www.fitternity.com/" + city + "/gyms?page=";

        Document doc1 = Jsoup.connect(url).get();
        Elements elements = doc1.select(".last-page");
        String text = elements.text();
        int pages = Integer.parseInt(text);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Gyms");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Gym Name");
        headerRow.createCell(1).setCellValue("Address");
        headerRow.createCell(2).setCellValue("Contact");
        headerRow.createCell(3).setCellValue("Service");
        headerRow.createCell(4).setCellValue("Price");
        headerRow.createCell(5).setCellValue("Rating");
        headerRow.createCell(6).setCellValue("PeopleRated");

        int rowNum = 1;

        for (int i = 1; i <= pages; i++) {
            Document doc = Jsoup.connect(url + i).get();

            Elements gymNames = doc.select(".vendorname-span");

            Elements ratings = doc.select(".vendor-rating");

            Elements locations = doc.select(".location-name");

            Elements Peopleratedcount = doc.select(".count");

            for (int j = 0; j < gymNames.size(); j++) {
                String name = gymNames.get(j).text();
                String ratingText = ratings.get(j).text();
                String locationText = locations.get(j).text();
                String Peoplerated = Peopleratedcount.get(j).text();

                String n=null;
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(name);
                row.createCell(1).setCellValue(locationText);
                row.createCell(2).setCellValue(n);
                row.createCell(3).setCellValue(n);
                row.createCell(4).setCellValue(n);
                row.createCell(5).setCellValue(ratingText);
                row.createCell(6).setCellValue(Peoplerated);
                entry++;
            }
        }
        for(int i=0;i<6;i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream outputStream = new FileOutputStream( city+"Fitternitygyms.xlsx")) {
            workbook.write(outputStream);
            workbook.close();
            System.out.println(city.toUpperCase() + " gym details file created for all " + pages + "-Pages with "
                    + entry + "entries");
            sc.close();
        }
    }
}