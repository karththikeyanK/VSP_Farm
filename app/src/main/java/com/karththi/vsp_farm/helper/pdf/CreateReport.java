package com.karththi.vsp_farm.helper.pdf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateReport {
    private PdfDocument pdfDocument;
    private Canvas canvas;
    private Paint textPaint;
    private Paint borderPaint;
    private int xPos;
    private int yPos;
    private PdfDocument.Page currentPage;

    // Constructor
    public CreateReport() {
        this.pdfDocument = new PdfDocument();
        this.textPaint = new Paint();
        this.textPaint.setColor(Color.BLACK);
        this.textPaint.setTextSize(16);
        this.borderPaint = new Paint();
        this.borderPaint.setStyle(Paint.Style.STROKE);
        this.borderPaint.setColor(Color.BLACK);
        this.borderPaint.setStrokeWidth(2);
        this.xPos = 40;
        this.yPos = 40;
    }

    // Method to create a new page
    public void startPage(int pageWidth, int pageHeight) {
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        currentPage = pdfDocument.startPage(pageInfo);
        this.canvas = currentPage.getCanvas();
    }

    // Method to add centered header
    public void addCenteredHeader(String title, String address, String mobileNumber) {
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(title, canvas.getWidth() / 2, yPos, textPaint);

        yPos += 20;
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(address, canvas.getWidth() / 2, yPos, textPaint);

        yPos += 20;
        canvas.drawText(mobileNumber, canvas.getWidth() / 2, yPos, textPaint);

        yPos += 30; // Adjust the y-position for the next content
        textPaint.setTextAlign(Paint.Align.LEFT); // Reset text alignment to left
    }

    public void addTableHeading(String headingName){
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(headingName, canvas.getWidth() / 2, yPos, textPaint);
        yPos += 30;
    }

    // Method to add report title and date
    public void addReportTitle(String reportName, String date) {
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(reportName, xPos, yPos, textPaint);
        yPos += 20;
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Date: " + date, xPos, yPos, textPaint);
        yPos += 20;
    }

    // Method to add table header with bold text
    public void addTableHeader(String[] headers, int[] colWidths) {
        yPos+=20;
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        drawTableRow(headers, colWidths);
    }

    // Method to add table rows with normal text
    public void addTableRow(String[] rowData, int[] colWidths) {
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        drawTableRow(rowData, colWidths);
    }

    // Common method to draw table rows with custom column widths
    private void drawTableRow(String[] data, int[] colWidths) {
        int startX = xPos;
        for (int i = 0; i < data.length; i++) {
            canvas.drawText(data[i], startX, yPos, textPaint);
            canvas.drawRect(startX - 5, yPos - 20, startX + colWidths[i] - 5, yPos + 10, borderPaint);
            startX += colWidths[i];
        }
        yPos += 30; // Adjust row height
    }

    public void finishReport(String fileName) {
        // Finish the current page
        pdfDocument.finishPage(currentPage);

        // Write the PDF file to storage
        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        try {
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            pdfDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
