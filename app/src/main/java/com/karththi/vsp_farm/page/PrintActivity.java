package com.karththi.vsp_farm.page;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.epson.epos2.Epos2Exception;
import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.printer.EpsonPrinterHelper;

public class PrintActivity extends AppCompatActivity {

    private EpsonPrinterHelper printerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            printerHelper = new EpsonPrinterHelper(this);
            printerHelper.connectPrinter();
            printerHelper.printHelloWorld();
        } catch (Epos2Exception e) {
            throw new RuntimeException(e);
        }


    }

}
