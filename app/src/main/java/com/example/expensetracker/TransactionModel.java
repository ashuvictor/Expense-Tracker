package com.example.expensetracker;

public class TransactionModel {

    private String note;
    private String id;
    private String amount;
    private String type;
    private String date;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }





    public TransactionModel() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TransactionModel(String note, String id, String amount, String type,String date) {
        this.note = note;
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.date=date;
    }
}
