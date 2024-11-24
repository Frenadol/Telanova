package org.example.Model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

public class Worker extends User{
    private LocalDate hireDate;

    public Worker(String username, int id_user, String password, String gmail, boolean isAdmin, LocalDate hireDate) {
        super(username, id_user, password, gmail, isAdmin);
        this.hireDate = hireDate;
    }

    public Worker(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    @Override
    public String toString() {
        return super.toString() + ", hiredate=" + hireDate;

    }
}
