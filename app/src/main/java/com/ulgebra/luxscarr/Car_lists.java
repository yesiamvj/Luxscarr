package com.ulgebra.luxscarr;

/**
 * Created by Vijayakumar on 07/07/2016.
 */
import java.util.ArrayList;
public class Car_lists {


    private String car_name;
    private String car_cost;
    private String car_image;
    private String car_no;
    private String Car_id;


    public void set_carname(String its_nm){
        this.car_name=its_nm;
    }
    public void setCar_cost(String its_nm){
        this.car_cost=its_nm;
    }
    public void setCar_image(String its_nm){
        this.car_image=its_nm;
    }
    public void setCar_id(String its_nm){
        this.Car_id=its_nm;
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

    public String getCar_id() {
        return Car_id;
    }
}
