package com.ulgebra.luxscar;

/**
 * Created by Vijayakumar on 07/07/2016.
 */

public class Car_lists {


    private String car_name;
    private String car_cost;
    private String car_image;
    private String car_no;
    private int Car_id;
    private  String booking_id;
    private String ride_dure;
    private String booked_on;


    public void setBooked_on(String booked_on) {
        this.booked_on = booked_on;
    }

    public String getBooked_on() {
        return booked_on;
    }

    public void set_carname(String its_nm){
        this.car_name=its_nm;
    }
    public void setCar_cost(String its_nm){
        this.car_cost=its_nm;
    }
    public void setCar_image(String its_nm){
        this.car_image=its_nm;
    }
    public void setCar_id(int its_nm){
        this.Car_id=its_nm;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public void setRide_dure(String ride_dure) {
        this.ride_dure = ride_dure;
    }

    public String getRide_dure() {
        return ride_dure;
    }

    public String getCar_cost() {
        return car_cost;
    }

    public String getCar_image() {
        return car_image;
    }

    public String getCar_name() {
        return car_name;
    }

    public int getCar_id() {
        return Car_id;
    }

    public String getBooking_id() {
        return booking_id;
    }

}
