package com.karththi.vsp_farm.Factory;

import android.content.Context;

import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.dto.BillSummary;
import com.karththi.vsp_farm.dto.Sale;
import com.karththi.vsp_farm.facade.LoanFacade;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.pdf.CreateReport;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.service.BillItemService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportFactory {
    private Context context;
    private BillItemService billItemService;

    private LoanFacade loanFacade;

    private List<BillItemsDetailDto> billItemsDetailDtoList;

    private List<BillItemsDetailDto> loanBillItemList;

    private List<BillItemsDetailDto> cashBillItemList;

    private List<BillItemsDetailDto> deletedBillItemList;



    public ReportFactory(Context context) {
        this.context = context;
        billItemService = new BillItemService(context);
        loanFacade = new LoanFacade(context);

        billItemsDetailDtoList = new ArrayList<>();
        billItemsDetailDtoList = billItemService.getAllBillDtoByDate(DateTimeUtils.getCurrentDate());
        deletedBillItemList = new ArrayList<>();
        loanBillItemList = new ArrayList<>();
        cashBillItemList = new ArrayList<>();

        for (BillItemsDetailDto dto : billItemsDetailDtoList){
            if (dto.getStatus().equals(AppConstant.DELETED)){
                deletedBillItemList.add(dto);
            }else if (dto.getPaymentMethod().equals(AppConstant.LOAN)){
                loanBillItemList.add(dto);
            }else if (dto.getPaymentMethod().equals(AppConstant.CASH)){
                cashBillItemList.add(dto);
            }
        }

    }


    public void downloadReportByDateRangePdf(
            String startDate, String endDate,
            List<Sale> saleList,
            List<BillSummary> summaryList,
            List<BillSummary> subItemSummary,
            List<BillSummary> summaryListByDate,
            List<BillSummary> subItemSummaryByDate

    ) {
        CreateReport report = new CreateReport(context);
        report.startPage(595, 842); // A4 size
        report.addCenteredHeader("VSP FARM", "Theniyambai, Valvettithurai, Sri Lanka", "077 023 8493");
        report.addReportTitle("Sales Report", startDate+" ---> "+endDate);
        addSummary(report,saleList);

        report.addTableHeading("Summary by Item");
        String[] headers = {"Item", "Quantity", "Discount", "Price"};
        int[] colWidth = {120, 120, 120, 120};
        report.addTableHeader(headers, colWidth, 0);
        for (BillSummary summary : summaryList) {
            String[] row = {
                    summary.getItemName(),
                    formatAmount(summary.getTotalQuantity()),
                    formatAmount(summary.getTotalDiscount()),
                    formatAmount(summary.getTotalPrice())
            };
            report.addTableRow(row, colWidth, 0);
        }

        report.addTableHeading("Summary by Sub Item");
        String[] subItemHeaders = {"Item","Sub Item", "Quantity", "Discount", "Price"};
        int[] subItemColWidth = {100, 100, 100, 100, 100};
        report.addTableHeader(subItemHeaders, subItemColWidth, 0);
        for (BillSummary summary : subItemSummary) {
            String[] row = {
                    summary.getItemName(),
                    summary.getSubItemName(),
                    formatAmount(summary.getTotalQuantity()),
                    formatAmount(summary.getTotalDiscount()),
                    formatAmount(summary.getTotalPrice())
            };
            report.addTableRow(row, subItemColWidth, 0);
        }

        report.addTableHeading("Summary by Date");
        String[] dateHeaders = {"Date", "Item", "Quantity", "Discount", "Price"};
        int[] dateColWidth = {80,80, 80, 80, 80};
        report.addTableHeader(dateHeaders, dateColWidth, 12);
        boolean changeBg = false;
        for (BillSummary summary : summaryListByDate) {
            if (changeBg){
                report.changeBgColour(dateColWidth);
                changeBg = false;
            }else {
                report.removeBg(dateColWidth);
                changeBg = true;
            }
            String[] row = {
                    summary.getDate(),
                    summary.getItemName(),
                    formatAmount(summary.getTotalQuantity()),
                    formatAmount(summary.getTotalDiscount()),
                    formatAmount(summary.getTotalPrice())
            };
            report.addTableRow(row, dateColWidth, 12);
        }

        report.addTableHeading("Summary by Date and Sub Item");
        String[] dateSubItemHeaders = {"Date", "Item", "Sub Item", "Quantity", "Discount", "Price"};
        int[] dateSubItemColWidth = {80,80, 80, 80, 80, 80};
        changeBg = false;
        String date = "";

        for (BillSummary summary : subItemSummaryByDate) {

            if (!date.equals(summary.getDate())){
                date = summary.getDate();
                report.addSpace();
                report.addTableHeading("Date: "+date);
                report.addTableHeader(dateSubItemHeaders, dateSubItemColWidth, 12);
            }
            if (changeBg){
                report.changeBgColour(dateSubItemColWidth);
                changeBg = false;
            }else {
                report.removeBg(dateSubItemColWidth);
                changeBg = true;
            }
            String[] row = {
                    summary.getDate(),
                    summary.getItemName(),
                    summary.getSubItemName(),
                    formatAmount(summary.getTotalQuantity()),
                    formatAmount(summary.getTotalDiscount()),
                    formatAmount(summary.getTotalPrice())
            };
            report.addTableRow(row, dateSubItemColWidth, 12);
        }

        String fileName = "VSP_Farm_Report_" + startDate+"-"+endDate + ".pdf";
        report.finishReport(fileName);
    }

    public void downloadDetailReportPdf(String startDate, String endDate,List<Sale> saleList){
        CreateReport report = new CreateReport(context);
        report.startPage(842, 595); // A4 size

        report.addCenteredHeader("VSP FARM", "Theniyambai, Valvettithurai, Sri Lanka", "077 023 8493");
        report.addReportTitle("Daily Sales Report", DateTimeUtils.getCurrentDate() + " "+DateTimeUtils.getCurrentTime());
        addSummary(report,saleList);

        report.addSpace();

        createTable(report,deletedBillItemList,"Deleted Bills");
        String fileName = "VSP_Detail_Report_" + DateTimeUtils.getCurrentDate()+" "+DateTimeUtils.getCurrentTime() + ".pdf";
        report.finishReport(fileName);
    }

    private void addSummary(CreateReport report,List<Sale> saleList){
        report.addTableHeading("Summary");
        String[] summary_headers = {"Total", "Cash", "Loan", "Deleted"};
        int[] summary_col_width = {120, 120, 100, 120};
        report.addTableHeader(summary_headers,summary_col_width,0);
        double total =0;
        double totalCash =0;
        double totalLoan = 0;
        double totalDelete =0;

        for (Sale sale: saleList){
            total += sale.getCash()+sale.getLoan();
            totalCash += sale.getCash();
            totalLoan += sale.getLoan();
            totalDelete += sale.getDelete();
        }

        String[] summary = {
                formatAmount(total),
                formatAmount(totalCash),
                formatAmount(totalLoan),
                formatAmount(totalDelete)
        };
        report.addTableRow(summary,summary_col_width,0);

        report.addTableHeading("Detail Summary by Date");
        String[] detailSummary = {"Date","Total", "Cash", "Loan", "Deleted"};
        int[] detailSummaryColWidth = {100, 100, 100, 100, 100};
        report.addTableHeader(detailSummary,detailSummaryColWidth,0);

        boolean changeBg = false;
        for (Sale sale: saleList){
            if (changeBg){
                report.changeBgColour(detailSummaryColWidth);
                changeBg = false;
            }else {
                report.removeBg(detailSummaryColWidth);
                changeBg = true;
            }
            String[] row = {
                    sale.getDate(),
                    formatAmount(sale.getCash()+sale.getLoan()),
                    formatAmount(sale.getCash()),
                    formatAmount(sale.getLoan()),
                    formatAmount(sale.getDelete())
            };
            report.addTableRow(row,detailSummaryColWidth,0);
        }
    }





    private void createTable(CreateReport report, List<BillItemsDetailDto> bills, String heading) {
        if (bills == null || bills.isEmpty()) {
            // Early return if no bills are present
            return;
        }

        int billId = -1;  // Set to -1 for the initial check since bill IDs should be positive
        boolean isAlternate = false;  // Track row background alternation

        // Add table heading and headers
        report.addTableHeading(heading);
        String[] detailHeaders = {"Date", "Time", "Customer", "Ref No", "Item", "Sub Item", "Discount", "Quantity", "Price", "Payment", "Status", "User"};
        int[] detailColWidth = {80, 60, 80, 80, 60, 80, 60, 60, 70, 50, 50, 50};

        String date ="";
        // Iterate over bills and add rows with background logic
        for (BillItemsDetailDto dto : bills) {
            // Switch the background when bill ID changes
            if (billId != dto.getBillId()) {
                isAlternate = !isAlternate;
                billId = dto.getBillId();
            }

            if (!date.equals(dto.getCreatedAt())){
                date = dto.getCreatedAt();
                report.addSpace();
                report.addTableHeading("Date: "+date);
                report.addTableHeader(detailHeaders, detailColWidth, 10);
            }

            if (isAlternate) {
                report.changeBgColour(detailColWidth);
            } else {
                report.removeBg(detailColWidth);
            }

            // Prepare the row data
            String[] row = {
                    dto.getCreatedAt(),
                    dto.getCreateTime(),
                    dto.getCustomerName(),
                    dto.getReferenceNumber(),
                    dto.getItemName(),
                    dto.getSubItemName(),
                    formatAmount(dto.getDiscount()),
                    String.valueOf(dto.getQuantity()),
                    String.valueOf(dto.getBillItemPrice()),
                    dto.getPaymentMethod(),
                    dto.getStatus(),
                    dto.getUserName()
            };

            // Add the row to the report
            report.addTableRow(row, detailColWidth, 10);
        }
    }


    private String formatAmount(double total) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        String formattedTotal = nf.format(total);
        return formattedTotal.replace(',', ' ');
    }

}
