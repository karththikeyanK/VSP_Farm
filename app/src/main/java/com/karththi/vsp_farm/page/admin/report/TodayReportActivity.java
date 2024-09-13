package com.karththi.vsp_farm.page.admin.report;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.BillSummary;
import com.karththi.vsp_farm.facade.TodayReportFacade;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.service.BillService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.PrimitiveIterator;

public class TodayReportActivity extends AppCompatActivity {

    private TodayReportFacade todayReportFacade;

    private TableLayout generalReportTableCash;
    private TableLayout generalReportTableLoan;

    private TableLayout getGeneralReportTable;

    private TextView reportDate;

    private Button downloadPdfButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_report);

        todayReportFacade = new TodayReportFacade(this);

        reportDate = findViewById(R.id.reportDate);

        reportDate.setText("Date - "+ DateTimeUtils.getCurrentDate()+" "+DateTimeUtils.getCurrentTime());


        generalReportTableCash = findViewById(R.id.generalReportTableCash);
        generalReportTableLoan = findViewById(R.id.generalReportTableLoan);
        getGeneralReportTable = findViewById(R.id.generalReportTable);
        downloadPdfButton = findViewById(R.id.downloadPdfButton);

        downloadPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPdf();
            }
        });

        loadGeneralReport();
    }

    public void loadGeneralReport() {
        List<BillSummary> cashList = todayReportFacade.getTodaySummary(AppConstant.CASH);
        populateTable(generalReportTableCash, cashList,false);

        List<BillSummary> loanList = todayReportFacade.getTodaySummary(AppConstant.LOAN);
        populateTable(generalReportTableLoan, loanList,false);

        List<BillSummary> summaryList = todayReportFacade.getTodayTotalSummary();
        populateTable(getGeneralReportTable, summaryList,true);
    }

    private void populateTable(TableLayout tableLayout, List<BillSummary> billSummaryList, boolean isSummary) {

        double totalD=0;
        double totalP=0;


        for (BillSummary summary : billSummaryList) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TableRow row = (TableRow) inflater.inflate(R.layout.report_gentral_row, null);

            TextView itemName = row.findViewById(R.id.itemName);
            TextView subItemName = row.findViewById(R.id.subItemName);
            TextView totalQuantity = row.findViewById(R.id.totalQuantity);
            TextView totalDiscount = row.findViewById(R.id.totalDiscount);
            TextView itemTotal = row.findViewById(R.id.itemTotal);

            if (isSummary){
                subItemName.setVisibility(View.GONE);
            }

            itemName.setText(summary.getItemName());
            subItemName.setText(summary.getSubItemName());
            totalQuantity.setText(String.valueOf(summary.getTotalQuantity()));
            totalDiscount.setText(String.valueOf(summary.getTotalDiscount()));
            itemTotal.setText(String.format("%.2f", summary.getTotalPrice()));

            tableLayout.addView(row);

            totalP += summary.getTotalPrice();
            totalD += summary.getTotalDiscount();
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow row = (TableRow) inflater.inflate(R.layout.report_gentral_row, null);

        TextView itemName = row.findViewById(R.id.itemName);
        TextView subItemName = row.findViewById(R.id.subItemName);
        TextView totalQuantity = row.findViewById(R.id.totalQuantity);
        TextView totalDiscount = row.findViewById(R.id.totalDiscount);
        TextView itemTotal = row.findViewById(R.id.itemTotal);
        itemTotal.setTextSize(20);
        itemTotal.setTypeface(null, Typeface.BOLD);

        if (isSummary){
            subItemName.setVisibility(View.GONE);
        }

        itemName.setText("TOTAL");
        subItemName.setText("==");
        totalQuantity.setText("==");
        totalDiscount.setText(String.valueOf(totalD));
        itemTotal.setText(String.format("%.2f", totalP));
        tableLayout.addView(row);

    }


    private void downloadPdf() {
        // Create a new PdfDocument
        PdfDocument document = new PdfDocument();

        // Get the content view (root layout)
        View content = findViewById(R.id.reportRootLayout);

        // Measure the content's actual height and width
        content.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int contentHeight = content.getMeasuredHeight();
        int contentWidth = content.getMeasuredWidth();

        // Define page width and height (A4 size)
        int pageWidth = 595;
        int pageHeight = 842;
        float scale = (float) pageWidth / contentWidth;
        int scaledContentHeight = (int) (contentHeight * scale);

        int totalPages = (int) Math.ceil((float) scaledContentHeight / pageHeight);

        Paint paint = new Paint();
        paint.setTextSize(12);

        for (int i = 0; i < totalPages; i++) {
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, i + 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            Canvas canvas = page.getCanvas();

            if (i == 0) {
                canvas.drawText("Today Report", 80, 50, paint); // Only on the first page
            }

            // Scale the canvas to fit the content width
            canvas.save();
            canvas.scale(scale, scale); // Scale the canvas based on calculated scale factor

            // Calculate the portion of the content to draw on this page
            int yOffset = i * (int) (pageHeight / scale); // Adjust yOffset to account for scaling
            canvas.translate(0, -yOffset); // Translate the canvas to draw the next portion of content

            // Render the current portion of the layout into the canvas
            content.draw(canvas);

            canvas.restore();

            // Finish the current page
            document.finishPage(page);
        }

        // Save the document to a file
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(directoryPath, "Today Report-" + DateTimeUtils.getCurrentTime() + ".pdf");

        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF saved to Downloads", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Close the document
        document.close();
    }


}
