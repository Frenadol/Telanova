package com.github.Frenadol.Model;

public class Worker extends User {
    /** Indicates if the user is a worker */
    private boolean isWorker;

    /** The hire date of the worker */
    private String hireDate;

    /**
     * Constructor with all fields
     * @param username the username of the worker
     * @param id_user the ID of the worker
     * @param password the password of the worker
     * @param gmail the email of the worker
     * @param profilePicture the profile picture of the worker
     * @param isWorker indicates if the user is a worker
     * @param hireDate the hire date of the worker
     */
    public Worker(String username, int id_user, String password, String gmail, byte[] profilePicture, boolean isWorker, String hireDate) {
        super(username, id_user, password, gmail, profilePicture);
        this.isWorker = isWorker;
        this.hireDate = hireDate;
    }

    /**
     * Constructor with isWorker and hireDate fields
     * @param isWorker indicates if the user is a worker
     * @param hireDate the hire date of the worker
     */
    public Worker(boolean isWorker, String hireDate) {
        this.isWorker = isWorker;
        this.hireDate = hireDate;
    }

    /**
     * Constructor with isWorker field
     * @param isWorker indicates if the user is a worker
     */
    public Worker(boolean isWorker) {
        this.isWorker = isWorker;
    }

    /** Default constructor */
    public Worker() {
    }

    /**
     * Getter for isWorker
     * @return true if the user is a worker, false otherwise
     */
    public boolean isWorker() {
        return isWorker;
    }

    /**
     * Setter for isWorker
     * @param worker indicates if the user is a worker
     */
    public void setWorker(boolean worker) {
        isWorker = worker;
    }

    /**
     * Getter for hireDate
     * @return the hire date of the worker
     */
    public String getHireDate() {
        return hireDate;
    }

    /**
     * Setter for hireDate
     * @param hireDate the hire date of the worker
     */
    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    /**
     * Returns a string representation of the object
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return super.toString() + "Worker{" +
                "isWorker=" + isWorker +
                ", hireDate='" + hireDate + '\'' +
                '}';
    }
}