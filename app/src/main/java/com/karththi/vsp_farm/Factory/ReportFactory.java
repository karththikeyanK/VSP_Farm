package com.karththi.vsp_farm.Factory;

import android.content.Context;

import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.dto.BillSummary;
import com.karththi.vsp_farm.dto.LoanPaymentDto;
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
        String[] headers = {"Item", "Quantity", "Discount", "total"};
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
        String[] subItemHeaders = {"Item","Sub Item", "Quantity", "Discount", "Total"};
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
        String[] dateHeaders = {"Date", "Item", "Quantity", "Discount", "Total"};
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
        int[] dateSubItemColWidth = {80,80, 80, 80, 80, 80};

        String date = "";

        for (BillSummary summary : subItemSummaryByDate) {

            if (!date.equals(summary.getDate())){
                date = summary.getDate();
                report.addSpace();
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
        report.finishReport(fileName,AppConstant.GET_SUMMARY_FOLDER);
    }


    public void downloadLoanPaymentPdf(String cusName, List<LoanPaymentDto> loanPaymentDtoList, String date1, String date2){
        CreateReport report = new CreateReport(context);
        report.startPage(595, 842); // A4 size
        report.addCenteredHeader("VSP FARM", "Theniyambai, Valvettithurai, Sri Lanka", "077 023 8493");
        report.addReportTitle("Loan Payment Report", date1+" ---> "+date2);
        report.addTableHeading("Customer: "+cusName);
        String[] headers = {"Date","Customer" ,"Amount"};
        int[] colWidth = {100, 100, 100};
        report.addTableHeader(headers, colWidth, 0);
        for (LoanPaymentDto dto: loanPaymentDtoList){
            String[] row = {
                    dto.getPaymentDate(),
                    dto.getCustomerName(),
                    formatAmount(dto.getPaymentAmount())
            };
            report.addTableRow(row, colWidth, 0);
        }
        String fileName = "VSP_LP_Report___" + cusName + "___"+date1+"___"+date2+".pdf";
        report.finishReport(fileName,AppConstant.LOAN_PAYMENT_FOLDER);
    }

    public void downloadDetailReportPdfByCustomer(String startDate, String endDate, double total,double cash,double loan,double deleted,String customer,List<BillItemsDetailDto> cashList, List<BillItemsDetailDto> loanList,List<BillItemsDetailDto> deletedBillItemList) {
        CreateReport report = new CreateReport(context);
        report.startPage(842, 595); // A4 size

        report.addCenteredHeader("VSP FARM", "Theniyambai, Valvettithurai, Sri Lanka", "077 023 8493");
        report.addReportTitle("Sales Report - "+customer, startDate+" ---> "+endDate);

        report.addTableHeading("Summary");
        String[] summary_headers = {"Total", "Cash", "Loan", "Deleted"};
        int[] summary_col_width = {120, 120, 100, 120};
        report.addTableHeader(summary_headers,summary_col_width,0);
        String[] summary = {
                formatAmount(total),
                formatAmount(cash),
                formatAmount(loan),
                formatAmount(deleted)
        };
        report.addTableRow(summary,summary_col_width,0);

        createTable(report,cashList,"Cash Bills");
        report.drawLine();
        createTable(report,loanList,"Loan Bills");
        report.drawLine();
        createTable(report,deletedBillItemList,"Deleted Bills");

        String fileName = "VSP_Customer_Report_"+customer+"_" + DateTimeUtils.getCurrentDate()+" "+DateTimeUtils.getCurrentTime() + ".pdf";
        report.finishReport(fileName,AppConstant.GET_CUSTOMER_FOLDER);
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
            return;
        }

        int billId = -1;
        boolean isAlternate = false;

        report.addTableHeading(heading);
        String[] detailHeaders = {"Date", "Time", "Customer", "Ref No", "Item", "Sub Item", "Discount", "Quantity", "Price", "Payment", "Status", "User"};
        int[] detailColWidth = {80, 60, 80, 80, 60, 80, 60, 60, 70, 50, 50, 50};

        double total =-1;
        String date ="";
        for (BillItemsDetailDto dto : bills) {
            if (billId != dto.getBillId()) {
                isAlternate = !isAlternate;
                billId = dto.getBillId();
            }

            if (!date.equals(dto.getCreatedAt())){
                if (total != -1){
                    report.addCustomTotal("Total",total);
                }
                date = dto.getCreatedAt();
                report.addSpace();
                report.addTableSubHeading("Date: "+date);
                report.addTableHeader(detailHeaders, detailColWidth, 10);
                total =0;
            }

            total += dto.getBillItemPrice();
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
        if (total != -1){
            report.addCustomTotal("Total",total);
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
