package com.shahaam.equipend.exceptions;

public class AmountMismatchExpection extends RuntimeException {
    public AmountMismatchExpection() {
        super("Total share amount do not match Expense amount");
    }
    public AmountMismatchExpection(String payer, String reciever) {
        super("Payer: " + payer + " and Reciever: " + reciever + " | Split doesn't exist / Amount more then Split");
    }
}
