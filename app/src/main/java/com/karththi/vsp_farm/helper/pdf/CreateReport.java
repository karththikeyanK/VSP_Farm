package com.karththi.vsp_farm.helper.pdf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.karththi.vsp_farm.helper.AppConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class CreateReport {
    private PdfDocument pdfDocument;
    private Canvas canvas;
    private Paint textPaint;
    private Paint borderPaint;
    private int xPos;
    private int yPos;
    private PdfDocument.Page currentPage;

    private Context context;

    private AppConstant appConstant;

    private int pageNumber=1;

    private int pageHeight;
    private int pageWidth;

    // Constructor
    public CreateReport(Context context) {
        this.context = context;
        appConstant = new AppConstant(context);
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
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create();
        currentPage = pdfDocument.startPage(pageInfo);
        this.canvas = currentPage.getCanvas();
        this.pageHeight = pageHeight;
        this.pageWidth = pageWidth;
    }

    // Method to add centered header
    public void addCenteredHeader(String title, String address, String mobileNumber) {
        checkPageHeight();
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
        checkPageHeight();
        yPos += 30;
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(headingName, xPos, yPos, textPaint);
        yPos += 30;
    }

    // Method to add report title and date
    public void addReportTitle(String reportName, String date) {
        checkPageHeight();
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(reportName, xPos, yPos, textPaint);
        yPos += 20;
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Date: " + date, xPos, yPos, textPaint);
        yPos += 20;
    }

    public void addTotalAndQuantity(double total, double discount){
        checkPageHeight();
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Total = " + formatTotal(total), canvas.getWidth() / 2, yPos, textPaint);
        yPos += 20;
        checkPageHeight();
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Discount = " + formatTotal(discount), canvas.getWidth() / 2, yPos, textPaint);
        yPos += 20;
    }

    public void addCustomTotal(String name, double amount){
        checkPageHeight();
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(name+" = " + formatTotal(amount), canvas.getWidth() / 2, yPos, textPaint);
        yPos += 20;
        }
    private String formatTotal(double total) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        String formattedTotal = nf.format(total);
        return formattedTotal.replace(',', ' ');
    }

    // Method to add table header with bold text
    public void addTableHeader(String[] headers, int[] colWidths,int textSize) {
        checkPageHeight();
        if(textSize > 0){
            this.textPaint.setTextSize(textSize);
        }
        yPos+=20;
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        drawTableRow(headers, colWidths);
    }


    public void addTableRow(String[] rowData, int[] colWidths, int textSize) {
        int startX = xPos;
        if(textSize > 0){
            this.textPaint.setTextSize(textSize);
        }
        checkPageHeight();
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        drawTableRow(rowData, colWidths);
        this.textPaint.setTextSize(16);
    }

    public void changeBgColour(int[] colWidths){
        int rowBackgroundColor = Color.LTGRAY;
        canvas.drawRect(35, yPos-20, 40 + getTotalWidth(colWidths) - 5, yPos + 10, getBackgroundPaint(rowBackgroundColor));
    }

    public void removeBg(int[] colWidths){
        int rowBackgroundColor = Color.TRANSPARENT;
        canvas.drawRect(35, yPos-20, 40 + getTotalWidth(colWidths) - 5, yPos + 10, getBackgroundPaint(rowBackgroundColor));
    }

    private int getTotalWidth(int[] colWidths) {
        int totalWidth = 0;
        for (int width : colWidths) {
            totalWidth += width;
        }
        return totalWidth;
    }

    private void drawTableRow(String[] data, int[] colWidths) {

        int startX = xPos;
        checkPageHeight();
        for (int i = 0; i < data.length; i++) {
            String d = data[i];
            if (d.equals(AppConstant.LOAN) || d.equals(AppConstant.DELETED)){
                changeColour("red");
            }else if (d.equals(AppConstant.LOAN)){
                changeColour("bule");
            }
            canvas.drawText(d, startX, yPos, textPaint);
            canvas.drawRect(startX - 5, yPos - 20, startX + colWidths[i] - 5, yPos + 10, borderPaint);
            startX += colWidths[i];
            changeColour("black");
        }
        yPos += 30; // Adjust row height
    }

    private Paint getBackgroundPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }
    public void addSpace(){
        yPos+=20;
    }

    public void changeColour(String colour) {
        switch (colour.toLowerCase()) {
            case "green":
                this.textPaint.setColor(Color.GREEN);
                break;
            case "red":
                this.textPaint.setColor(Color.RED);
                break;
            case "blue":
                this.textPaint.setColor(Color.BLUE);
                break;
            default:
                this.textPaint.setColor(Color.BLACK);  // Default color if input doesn't match
                break;
        }
    }



    private void checkPageHeight(){
        if (yPos >= pageHeight - 30) {
            pdfDocument.finishPage(currentPage);
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            currentPage = pdfDocument.startPage(pageInfo);
            this.canvas = currentPage.getCanvas();
            this.xPos = 40;
            this.yPos = 40;
            pageNumber += 1;
        }
    }




    public void finishReport(String fileName) {
        // Finish the current page
        pdfDocument.finishPage(currentPage);

        // Get the directory where the file will be saved
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        // Write the PDF file to storage
        File pdfFile = new File(directory, fileName);
        int count = 0;
        while (pdfFile.exists()) {
            count++;
            String newFileName = fileName.replace(".pdf", "_" + count + ".pdf");
            pdfFile = new File(directory, newFileName);
        }
        try {
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            pdfDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get a handler for the main thread
        Handler mainHandler = new Handler(Looper.getMainLooper());

        // Post a runnable to the main thread
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                // Call ErrorAlert on the main thread
                appConstant.ErrorAlert(AppConstant.SUCCESS,"Pdf created with name "+fileName);
            }
        });
;
    }
}
