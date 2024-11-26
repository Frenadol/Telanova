package com.github.Frenadol.Model;

import java.util.Date;

public class Worker extends User {
    private boolean isWorker;
    private String hireDate;

    public Worker(String username, int id_user, String password, String gmail, boolean isWorker, String hireDate) {
        super(username, id_user, password, gmail);
        this.isWorker = isWorker;
        this.hireDate = hireDate;
    }

    public Worker(boolean isWorker, String hireDate) {
        this.isWorker = isWorker;
        this.hireDate = hireDate;
    }

    public Worker(boolean isWorker) {
        this.isWorker = isWorker;
    }
    public Worker(){

    }

    public boolean isWorker() {
        return isWorker;
    }

    public void setWorker(boolean worker) {
        isWorker = worker;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    @Override
    public String toString() {
        return super.toString()+isWorker+hireDate;
    }
}
