package com.example.benbriggs.food_journal.user;

/**
 * Created by benbriggs on 31/01/2018.
 */

public class NotFoodOrDrinkException extends Exception {
    private String message = "The item scanned is not food or drink.";

    public NotFoodOrDrinkException(){}

    @Override
    public String getMessage() {
        return message;
    }
}
