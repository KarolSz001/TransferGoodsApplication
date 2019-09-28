package com.app.main;
import com.app.service.ControlService;

public class App {
    public static void main(String[] args) {

        final String appName = "\n TransferGoodApplication v1.0 Karol Szot 08.09.2019 ";
        System.out.println(appName);
        ControlService controlService = new ControlService();
        controlService.controlRun();
    }


}
