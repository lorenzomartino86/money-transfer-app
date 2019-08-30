package com.lmartino.bank.app;

import org.junit.Test;

import java.util.Map;

public class MoneyTransferAppTest {

    @Test
    public void canRunApplication(){
        MoneyTransferApp app = MoneyTransferApp.init();
        app.start();
        app.stop();
    }

    @Test
    public void canRunMain(){
        MoneyTransferApp.main();
    }

}