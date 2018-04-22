package com.parkinglotmanagement;

/**
 * Created by HP_PC on 06-04-2018.
 */

public class CardDetails
{
    String cardNo;
    String expiry;

    public CardDetails() {
    }

    public CardDetails(String cardNo, String expiry) {
        this.cardNo = cardNo;
        this.expiry = expiry;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}
