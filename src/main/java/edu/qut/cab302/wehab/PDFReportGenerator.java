package edu.qut.cab302.wehab;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;

public class PDFReportGenerator
{
    public static void generateReport(List<moodRating> moodRatings, String filePath)
    {
        try
        {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = today.format(formatter);

            document.add(new Paragraph("Comprehensive Report for " + "generated "));

            Table table = new Table(2);
            table.addCell("Date");
            table.addCell("Mood Rating");

            for (moodRating rate : moodRatings)
            {
                table.addCell(rate.getRatingDate().toString());
                table.addCell(String.valueOf(rate.getMoodRating()));
            }
            document.add(table);

            document.close();

            System.out.println("PDF Created mate");
        } catch (Exception error) { System.err.println("Error pdf mate" + error.getMessage()); }
    }

}
