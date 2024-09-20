package com.karththi.vsp_farm.Factory;

import android.content.Context;

import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.dto.BillSummary;
import com.karththi.vsp_farm.dto.LoanDto;
import com.karththi.vsp_farm.facade.LoanFacade;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.pdf.CreateReport;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.service.BillItemService;
import com.karththi.vsp_farm.service.BillService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TodayReportFactory {

    private Context context;
    private BillItemService billItemService;

    private BillService billService;

    private LoanFacade loanFacade;

    private  List<BillItemsDetailDto> billItemsDetailDtoList;

    private List<BillItemsDetailDto> loanBillItemList;

    private List<BillItemsDetailDto> cashBillItemList;

    private List<BillItemsDetailDto> deletedBillItemList;



    public TodayReportFactory(Context context) {
        this.context = context;
        billItemService = new BillItemService(context);
        billService = new BillService(context);

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


   public List<BillSummary> getTodaySummary(String methode){
        return billService.getSummaryByDateAndPaymentMethode(DateTimeUtils.getCurrentDate(),methode);
   }

    public List<BillSummary> getTodayTotalSummary(){
        return billService.getSummaryByDate(DateTimeUtils.getCurrentDate());
    }

    public void downloadTodayPdf() {
        CreateReport report = new CreateReport(context);
        report.startPage(595, 842); // A4 size
        report.addCenteredHeader("VSP FARM", "Theniyambai, Valvettithurai, Sri Lanka", "077 023 8493");
        report.addReportTitle("Daily Sales Report", DateTimeUtils.getCurrentDate());
        addTodaySummary(report);
        String fileName = "VSP_Farm_Report_" + DateTimeUtils.getCurrentDate()+" "+DateTimeUtils.getCurrentTime() + ".pdf";
        report.finishReport(fileName);
    }

    private void addTodaySummary(CreateReport report){
        report.addTableHeading("Summary");
        String[] summary_headers = {"Item", "Quantity", "Discount", "Total"};
        int[] summary_col_width = {150, 100, 100, 150};
        report.addTableHeader(summary_headers,summary_col_width,0);

        double totalPrice = 0;
        double totalDiscount = 0;
        for (BillSummary summary : getTodayTotalSummary()) {
            String[] row = {
                    summary.getItemName(),
                    String.valueOf(summary.getTotalQuantity()),
                    String.valueOf(summary.getTotalDiscount()),
                    String.format("%.2f", summary.getTotalPrice())
            };
            report.addTableRow(row,summary_col_width,0);
            totalPrice += summary.getTotalPrice();
            totalDiscount += summary.getTotalDiscount();
        }

        report.addTotalAndQuantity(totalPrice,totalDiscount);

        report.addTableHeading("Cash Sales");
        String[] detail_header = {"Item", "Sub Item", "Quantity", "Discount", "Total"};
        int[] detail_col_width = {100, 150, 80, 80, 120};
        report.addTableHeader(detail_header, detail_col_width,0);

        double cashTotal = 0;
        double cashDiscount = 0;
        for (BillSummary cashSummary : getTodaySummary(AppConstant.CASH)) {
            String[] row = {
                    cashSummary.getItemName(),
                    cashSummary.getSubItemName(),
                    String.valueOf(cashSummary.getTotalQuantity()),
                    String.valueOf(cashSummary.getTotalDiscount()),
                    String.valueOf(cashSummary.getTotalPrice())
            };
            cashTotal += cashSummary.getTotalPrice();
            cashDiscount += cashSummary.getTotalDiscount();
            report.addTableRow(row, detail_col_width,0);

        }
        report.addTotalAndQuantity(cashTotal,cashDiscount);

        report.addTableHeading("Loan Sales");

        report.addTableHeader( detail_header, detail_col_width,0);


        double loanTotal = 0;
        double loanDiscount = 0;
        for (BillSummary loanSummary : getTodaySummary(AppConstant.LOAN)) {
            String[] row = {
                    loanSummary.getItemName(),
                    loanSummary.getSubItemName(),
                    String.valueOf(loanSummary.getTotalQuantity()),
                    String.valueOf(loanSummary.getTotalDiscount()),
                    String.valueOf(loanSummary.getTotalPrice())
            };
            loanTotal += loanSummary.getTotalPrice();
            loanDiscount += loanSummary.getTotalDiscount();
            report.addTableRow(row, detail_col_width,0);
        }
        report.addTotalAndQuantity(loanTotal,loanDiscount);

        List<LoanDto> loanDtoList = loanFacade.getAllLoanDto();
        report.addTableHeading("Loan Details : ("+DateTimeUtils.getCurrentDate()+" "+DateTimeUtils.getCurrentTime() +")");
        String[] loanHeaders = {"Customer","Last Payment", "Last pay Date","Remaining"};
        int[] loan_col_widths = {150, 120, 130, 130};
        report.addTableHeader(loanHeaders,loan_col_widths,0);
        double remainingLoan = 0;
        for (LoanDto dto : loanDtoList){
            String lastPaymentDate="N/A";
            String lastPayment = "N/A";
            if (dto.getLastPayment()!=null){
                lastPaymentDate = dto.getLastPayment().getPaymentDate();
                lastPayment = formatAmount(dto.getLastPayment().getPaymentAmount());
            }
            String[] row = {dto.getLoan().getCustomerName(),lastPayment,lastPaymentDate,formatAmount(dto.getLoan().getRemainingAmount())};
            report.addTableRow(row,loan_col_widths,12);
            remainingLoan += dto.getLoan().getRemainingAmount();
        }
        report.addCustomTotal("Remaining Total",remainingLoan);
    }


    public void downloadTodayDetailPdf(List<BillItemsDetailDto> bills, List<BillItemsDetailDto> deletedBillItemList){
        CreateReport report = new CreateReport(context);
        report.startPage(842, 595); // A4 size

        report.addCenteredHeader("VSP FARM", "Theniyambai, Valvettithurai, Sri Lanka", "077 023 8493");
        report.addReportTitle("Daily Sales Report", DateTimeUtils.getCurrentDate() + " "+DateTimeUtils.getCurrentTime());
        addTodaySummary(report);

        report.addSpace();

        createTable(report,bills,"All Bills Without Deleted Bills");
        createTable(report,deletedBillItemList,"Deleted Bills");
        String fileName = "VSP_Detail_Report_" + DateTimeUtils.getCurrentDate()+" "+DateTimeUtils.getCurrentTime() + ".pdf";
        report.finishReport(fileName);
    }

    public void downloadDetailPdfByDateRange(String startDate, String endDate,List<BillItemsDetailDto> bills, List<BillItemsDetailDto> deletedBillItemList){
        CreateReport report = new CreateReport(context);
        report.startPage(842, 595); // A4 size

        report.addCenteredHeader("VSP FARM", "Theniyambai, Valvettithurai, Sri Lanka", "077 023 8493");
        report.addReportTitle("Sales Report", startDate + " --> "+endDate);

        report.addSpace();

        createTable(report,bills,"All Bills Without Deleted Bills");
        createTable(report,deletedBillItemList,"Deleted Bills");
        String fileName = "VSP_Detail_Report_" + startDate+"----" +endDate  + ".pdf";
        report.finishReport(fileName);
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
        report.addTableHeader(detailHeaders, detailColWidth, 10);

        // Iterate over bills and add rows with background logic
        for (BillItemsDetailDto dto : bills) {
            // Switch the background when bill ID changes
            if (billId != dto.getBillId()) {
                isAlternate = !isAlternate;  // Alternate background color on bill change
                billId = dto.getBillId();    // Update the current bill ID
            }

            // Set the background for the row based on the alternation
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
