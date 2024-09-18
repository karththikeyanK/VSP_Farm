package com.karththi.vsp_farm.facade;

import android.content.Context;
import android.util.Log;

import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.dto.BillSummary;
import com.karththi.vsp_farm.dto.ItemReport;
import com.karththi.vsp_farm.dto.LoanDto;
import com.karththi.vsp_farm.dto.SubItemReport;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.pdf.CreateReport;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.service.BillItemService;
import com.karththi.vsp_farm.service.BillService;
import com.karththi.vsp_farm.service.CustomerService;
import com.karththi.vsp_farm.service.ItemService;
import com.karththi.vsp_farm.service.SubItemService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TodayReportFacade {

    private Context context;
    private BillItemService billItemService;

    private LoanFacade loanFacade;

    private  List<BillItemsDetailDto> billItemsDetailDtoList;

    private List<BillItemsDetailDto> loanBillItemList;

    private List<BillItemsDetailDto> cashBillItemList;

    private List<BillItemsDetailDto> deletedBillItemList;

    private  CreateReport report;


    public TodayReportFacade(Context context) {
        this.context = context;
        billItemService = new BillItemService(context);
        report = new CreateReport(context);
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
        return billItemService.getSummaryByDateAndPaymentMethode(DateTimeUtils.getCurrentDate(),methode);
   }

    public List<BillSummary> getTodayTotalSummary(){
        return billItemService.getSummaryByDate(DateTimeUtils.getCurrentDate());
    }

    public void downloadTodayPdf() {
        report.startPage(595, 842); // A4 size
        report.addCenteredHeader("VSP FARM", "Theniyambai, Valvettithurai, Sri Lanka", "077 023 8493");
        report.addReportTitle("Daily Sales Report", DateTimeUtils.getCurrentDate());
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
        String fileName = "VSP_Farm_Report_" + DateTimeUtils.getCurrentDate()+" "+DateTimeUtils.getCurrentTime() + ".pdf";
        report.finishReport(fileName);
    }


    public void downloadTodayDetailPdf(double total, double cash, double loan, double delete, List<BillItemsDetailDto> bills, List<BillItemsDetailDto> deletedBillItemList){
        report.startPage(842, 595); // A4 size

        report.addCenteredHeader("VSP FARM", "Theniyambai, Valvettithurai, Sri Lanka", "077 023 8493");
        report.addReportTitle("Daily Sales Report", DateTimeUtils.getCurrentDate() + " "+DateTimeUtils.getCurrentTime());
        report.addTableHeading("Summary");
        String[] summary_headers = {"Total", "Cash", "Loan", "Delete"};
        int[] summary_col_width = {120, 120, 120, 120};
        report.addTableHeader(summary_headers,summary_col_width,0);
        String[] summary ={formatAmount(total),formatAmount(cash),formatAmount(loan),formatAmount(delete)};
        report.addTableRow(summary,summary_col_width,0);

        report.addSpace();

        createTable(report,bills,"All Bills Without Deleted Bills");
        createTable(report,deletedBillItemList,"Deleted Bills");
        String fileName = "VSP_Detail_Report_" + DateTimeUtils.getCurrentDate()+" "+DateTimeUtils.getCurrentTime() + ".pdf";
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
