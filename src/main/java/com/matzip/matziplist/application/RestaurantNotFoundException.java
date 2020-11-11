package com.matzip.matziplist.application;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(Long id) {
        super("Could not find Restaurant: " + id);
    }
}
