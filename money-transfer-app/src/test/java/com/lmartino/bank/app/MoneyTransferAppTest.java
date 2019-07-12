package com.lmartino.bank.app;

import org.junit.Test;

public class MoneyTransferAppTest {

    @Test
    public void canRunApplication(){
        MoneyTransferApp app = MoneyTransferApp.init();
        app.start();
        app.stop();
    }

}