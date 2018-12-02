/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.model.Car;
import com.github.adminfaces.starter.service.CarService;
import com.github.adminfaces.starter.util.Utils;
import com.github.adminfaces.template.exception.AccessDeniedException;
import org.omnifaces.util.Faces;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

/**
 * @author rmpestano
 */
@Named
@ViewScoped
public class CarFormMB implements Serializable {

    @Inject
    CarService carService;

    private Integer id;
    private Car car;


    public void init() {
        if (Faces.isAjaxRequest()) {
            return;
        }
        if (has(id)) {
            car = carService.findById(id);
        } else {
            car = new Car();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }


    public void remove() throws IOException {
        if (!Utils.isUserInRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("User not authorized! Only role <b>admin</b> can remove cars.");
        }
        if (has(car) && has(car.getId())) {
            carService.remove(car);
            addDetailMessage("Car " + car.getModel()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("user/car-list.jsf");
        }
    }

    public void save() {
        String msg;
        if (car.getId() == null) {
            carService.insert(car);
            msg = "Car " + car.getModel() + " created successfully";
        } else {
            carService.update(car);
            msg = "Car " + car.getModel() + " updated successfully";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        car = new Car();
        id = null;
    }

    public boolean isNew() {
        return car == null || car.getId() == null;
    }


}
