package com.app.service;

import com.app.utility.DataCustomerGenerator;
import com.app.utility.DataManager;
import com.app.utility.DataPreferencesGenerator;
import com.app.utility.DataProductGenerator;

import java.util.stream.Collectors;


public class ControlService {

    private DataProductGenerator dataProductGenerator;
    private DataCustomerGenerator dataCustomerGenerator;
    private DataPreferencesGenerator dataPreferencesGenerator;
    private final PurchaseService purchaseService;
    private final CustomerService customerService;


    public ControlService() {
        initialisationData();
        customerService = new CustomerService();
        purchaseService = new PurchaseService();
    }

    public void controlRun() {
        printDataFromJsonFiles();
        purchaseOperation();
        findResults();
    }



    private void printDataFromJsonFiles() {
        System.out.println("\n>>>>>>>>>> CUSTOMERS DATA >>>>>>>>>>");
        customerService.getAllCustomers().forEach(System.out::println);
        System.out.println("\n>>>>>>>>>> PRODUCTS DATA >>>>>>>>>>");
        purchaseService.getProductService().findAll().forEach(System.out::println);
        System.out.println("\n>>>>>>>>>> PREFERENCES DATA >>>>>>>>>>");
        customerService.getPreferences().forEach(System.out::println);
    }

    private void findResults() {
        System.out.println("View customer data that purchased the most products");
        System.out.println(purchaseService.getCustomerHowPurchasedMostProducts());
        DataManager.getLine("\n PRESS ANY NUMBER TO CONTINUE");
        System.out.println("View customer data that has purchased products about Total values");
        System.out.println(purchaseService.findCustomerHowPurchasedMostProductsWithTotalValue());
        DataManager.getLine("\n PRESS ANY NUMBER TO CONTINUE");
        System.out.println("View Map with Product and Number of Them which were selected by customer");
        purchaseService.getMapWithProductAndNumbersSelectingThem().forEach((k, v) -> System.out.println(k + "::::::::" + v));
        DataManager.getLine("\n PRESS ANY NUMBER TO CONTINUE");
        System.out.println("Print Product which was most often selected by customer");
        System.out.println(purchaseService.findProductWhichWasMostOftenSelected());
        DataManager.getLine("\n PRESS ANY NUMBER TO CONTINUE");
        System.out.println("Print Product which was least often selected by customer");
        System.out.println(purchaseService.findProductWhichWasLeastOftenSelected());
        DataManager.getLine("\n PRESS ANY NUMBER TO CONTINUE");
        System.out.println("View Map with Category product and count them");
        purchaseService.findMapWithCategoryAndNumberOfThem().forEach((k, v) -> System.out.println(k + "::::::" + v));
    }

    private void purchaseOperation() {
        customerService.getAllCustomers().stream().peek(purchaseService::purchaseGoodsByCustomer).collect(Collectors.toList());
    }

    private void initialisationData() {
        System.out.println("\n >>>>>>>>>> DATA INITIALIZATION OPERATION >>>>>>>>>>");
        dataProductGenerator = new DataProductGenerator();
        dataCustomerGenerator = new DataCustomerGenerator();
        dataPreferencesGenerator = new DataPreferencesGenerator();
    }


}

