package com.example.benbriggs.food_journal.user;

/**
 * Created by benbriggs on 31/01/2018.
 */

public class NotTescoOwnBrandException extends Exception{
    private String message = "The item scanned is not a Tesco Brand.";

    public NotTescoOwnBrandException(String message){
        this.message = "The item scanned is a " + message + " brand, not a Tesco brand.";
    }

    public NotTescoOwnBrandException(){
    }

    @Override
    public String getMessage() {
        return message;
    }
}
