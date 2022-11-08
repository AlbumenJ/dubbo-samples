package org.apache.dubbo.samples.generic.call.api;

import java.io.Serializable;

public class AcctInfo implements Serializable {

    private String cardNo;

    private String cardDate;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardDate() {
        return cardDate;
    }

    public void setCardDate(String cardDate) {
        this.cardDate = cardDate;
    }
}
