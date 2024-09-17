package com.karththi.vsp_farm.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.Customer;
import com.karththi.vsp_farm.page.admin.CustomerListActivity;
import com.karththi.vsp_farm.repo.CustomerRepository;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private Context context;

    private CustomerRepository customerRepository;

    AppConstant appConstant;

    public CustomerService(Context context) {
        this.context = context;
        customerRepository = new CustomerRepository(context);
        appConstant = new AppConstant(context);
    }

    public void createCustomer(String name, String mobile, String description) {
        Log.i("CustomerService", "CustomerService::createCustomer()::Creating customer is called");
        if (customerRepository.checkCustomerExists(name)){
            Log.e("CustomerService", "CustomerService::createCustomer()::Customer already exists");
            appConstant.ErrorAlert("Error", "Customer already exists");
            return;

        }
        Customer customer = new Customer(0,name, mobile, description);
        customerRepository.addCustomer(customer);
        Log.i("CustomerService", "CustomerService::createCustomer()::Customer created successfully");
        Intent intent = new Intent(context, CustomerListActivity.class);
        appConstant.SuccessAlert(AppConstant.SUCCESS,"Customer created successfully", intent);
    }

    public void updateCustomer(int id, String name, String mobile, String description) {
        Log.i("CustomerService", "CustomerService::updateCustomer()::Updating customer is called");
        if (customerRepository.checkCustomerExists(name) && customerRepository.getByName(name).getId() != id){
            Log.e("CustomerService", "CustomerService::updateCustomer()::Customer already exists");
            appConstant.ErrorAlert("Error", "Customer already exists with this name: "+name);
            return;

        }
        Customer customer = new Customer(id, name, mobile, description);
        customerRepository.updateCustomer(customer);
        Log.i("CustomerService", "CustomerService::updateCustomer()::Customer updated successfully");
        Intent intent = new Intent(context, CustomerListActivity.class);
        appConstant.SuccessAlert(AppConstant.SUCCESS,"Customer updated successfully", intent);
    }

    public void deleteCustomer(int id) {
        Log.i("CustomerService", "CustomerService::deleteCustomer()::Deleting customer is called");
        customerRepository.deleteCustomer(id);
        Log.i("CustomerService", "CustomerService::deleteCustomer()::Customer deleted successfully");
        Intent intent = new Intent(context, CustomerListActivity.class);
        appConstant.SuccessAlert(AppConstant.SUCCESS,"Customer deleted successfully", intent);

    }

    public Customer getCustomerById(int id) {
        Log.i("CustomerService", "CustomerService::getCustomer()::Getting customer is called");
        Customer customer = customerRepository.getById(id);
        Log.i("CustomerService", "CustomerService::getCustomer()::Customer retrieved successfully");
        return customer;
    }

    public List<Customer> getAllCustomers() {
        Log.i("CustomerService", "CustomerService::getAllCustomers()::Getting all customers is called");
        List<Customer> customers = new ArrayList<>();
        customers = customerRepository.getAll();
        Log.i("CustomerService", "CustomerService::getAllCustomers()::All customers retrieved successfully");
        return customers;
    }


}
