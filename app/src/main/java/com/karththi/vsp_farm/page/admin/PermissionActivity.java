package com.karththi.vsp_farm.page.admin;

import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.Permission;
import com.karththi.vsp_farm.repo.PermissionRepository;

import java.util.List;

public class PermissionActivity extends AppCompatActivity {

    private Switch todaySummaryReportSwitch, todayDetailReportSwitch, getSummaryReportSwitch,
            getDetailReportSwitch, getCustomerReportSwitch, addCustomerSwitch,
            editCustomerSwitch, addItemSwitch, editItemSwitch, addSubItemSwitch,
            editSubItemSwitch, disableEnableItemSwitch,viewLoanPaymentsSwitch;


    private PermissionRepository permissionRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permision_setting);

        permissionRepository = new PermissionRepository(this);

        todaySummaryReportSwitch = findViewById(R.id.todaySummaryReportSwitch);
        todayDetailReportSwitch = findViewById(R.id.todayDetailReportSwitch);
        getSummaryReportSwitch = findViewById(R.id.getSummaryReportSwitch);
        getDetailReportSwitch = findViewById(R.id.getDetailReportSwitch);
        getCustomerReportSwitch = findViewById(R.id.getCustomerReportSwitch);
        addCustomerSwitch = findViewById(R.id.addCustomerSwitch);
        editCustomerSwitch = findViewById(R.id.editCustomerSwitch);
        addItemSwitch = findViewById(R.id.addItemSwitch);
        editItemSwitch = findViewById(R.id.editItemSwitch);
        addSubItemSwitch = findViewById(R.id.addSubItemSwitch);
        editSubItemSwitch = findViewById(R.id.editSubItemSwitch);
        disableEnableItemSwitch = findViewById(R.id.disableEnableItemSwitch);
        viewLoanPaymentsSwitch = findViewById(R.id.viewLoanPaymentsSwitch);

        fetchedPermission();

        todayDetailReportSwitch.setChecked(AppConstant.TODAY_DETAIL_REPORT_PERMISSION);
        todaySummaryReportSwitch.setChecked(AppConstant.TODAY_SUMMARY_REPORT_PERMISSION);
        getSummaryReportSwitch.setChecked(AppConstant.GET_SUMMARY_REPORT_PERMISSION);
        getDetailReportSwitch.setChecked(AppConstant.GET_DETAIL_REPORT_PERMISSION);
        getCustomerReportSwitch.setChecked(AppConstant.GET_CUSTOMER_REPORT_PERMISSION);
        addCustomerSwitch.setChecked(AppConstant.ADD_CUSTOMER_PERMISSION);
        editCustomerSwitch.setChecked(AppConstant.EDIT_CUSTOMER_PERMISSION);
        addItemSwitch.setChecked(AppConstant.ADD_ITEM_PERMISSION);
        editItemSwitch.setChecked(AppConstant.EDIT_ITEM_PERMISSION);
        addSubItemSwitch.setChecked(AppConstant.ADD_SUB_ITEM_PERMISSION);
        editSubItemSwitch.setChecked(AppConstant.EDIT_SUB_ITEM_PERMISSION);
        disableEnableItemSwitch.setChecked(AppConstant.DISABLE_OR_ENABLE_ITEM_PERMISSION);
        viewLoanPaymentsSwitch.setChecked(AppConstant.VIEW_LOAN_PAYMENT_PERMISSION);



        todaySummaryReportSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("TODAY_SUMMARY_REPORT_PERMISSION", isChecked ? 1 : 0));

        todayDetailReportSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("TODAY_DETAIL_REPORT_PERMISSION", isChecked ? 1 : 0));

        getSummaryReportSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("GET_SUMMARY_REPORT_PERMISSION", isChecked ? 1 : 0));

        getDetailReportSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("GET_DETAIL_REPORT_PERMISSION", isChecked ? 1 : 0));

        getCustomerReportSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("GET_CUSTOMER_REPORT_PERMISSION", isChecked ? 1 : 0));

        addCustomerSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("ADD_CUSTOMER_PERMISSION", isChecked ? 1 : 0));

        editCustomerSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("EDIT_CUSTOMER_PERMISSION", isChecked ? 1 : 0));

        addItemSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("ADD_ITEM_PERMISSION", isChecked ? 1 : 0));

        editItemSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("EDIT_ITEM_PERMISSION", isChecked ? 1 : 0));

        addSubItemSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("ADD_SUB_ITEM_PERMISSION", isChecked ? 1 : 0));

        editSubItemSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("EDIT_SUB_ITEM_PERMISSION", isChecked ? 1 : 0));

        disableEnableItemSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("DISABLE_OR_ENABLE_ITEM_PERMISSION", isChecked ? 1 : 0));
        viewLoanPaymentsSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                permissionRepository.updatePermission("VIEW_LOAN_PAYMENT_PERMISSION", isChecked ? 1 : 0));

    }

    private void fetchedPermission() {
        List<Permission> permissions = permissionRepository.getAll();
        for (Permission permission : permissions) {
            if (permission.getIsGranted() == 1) {
                switch (permission.getPermission()) {
                    case "ADD_CUSTOMER_PERMISSION":
                        AppConstant.ADD_CUSTOMER_PERMISSION = true;
                        break;
                    case "EDIT_CUSTOMER_PERMISSION":
                        AppConstant.EDIT_CUSTOMER_PERMISSION = true;
                        break;
                    case "ADD_ITEM_PERMISSION":
                        AppConstant.ADD_ITEM_PERMISSION = true;
                        break;
                    case "EDIT_ITEM_PERMISSION":
                        AppConstant.EDIT_ITEM_PERMISSION = true;
                        break;
                    case "ADD_SUB_ITEM_PERMISSION":
                        AppConstant.ADD_SUB_ITEM_PERMISSION = true;
                        break;
                    case "EDIT_SUB_ITEM_PERMISSION":
                        AppConstant.EDIT_SUB_ITEM_PERMISSION = true;
                        break;
                    case "DISABLE_OR_ENABLE_ITEM_PERMISSION":
                        AppConstant.DISABLE_OR_ENABLE_ITEM_PERMISSION = true;
                        break;
                    case "TODAY_SUMMARY_REPORT_PERMISSION":
                        AppConstant.TODAY_SUMMARY_REPORT_PERMISSION = true;
                        break;
                    case "TODAY_DETAIL_REPORT_PERMISSION":
                        AppConstant.TODAY_DETAIL_REPORT_PERMISSION = true;
                        break;
                    case "GET_SUMMARY_REPORT_PERMISSION":
                        AppConstant.GET_SUMMARY_REPORT_PERMISSION = true;
                        break;
                    case "GET_DETAIL_REPORT_PERMISSION":
                        AppConstant.GET_DETAIL_REPORT_PERMISSION = true;
                        break;
                    case "GET_CUSTOMER_REPORT_PERMISSION":
                        AppConstant.GET_CUSTOMER_REPORT_PERMISSION = true;
                        break;
                    case "VIEW_LOAN_PAYMENT_PERMISSION":
                        AppConstant.VIEW_LOAN_PAYMENT_PERMISSION = true;
                        break;
                    default:
                        // Handle unknown permission if necessary
                        break;
                }
            }
        }
    }
}
