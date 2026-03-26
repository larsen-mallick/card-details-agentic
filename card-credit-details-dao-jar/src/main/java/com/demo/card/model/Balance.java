package com.demo.card.model;

import java.time.LocalDate;

public class Balance {
    private double outstanding;
    private double minimumDue;
    private LocalDate dueDate;

    public Balance() {}

    public Balance(double outstanding, double minimumDue, LocalDate dueDate) {
        this.outstanding = outstanding;
        this.minimumDue = minimumDue;
        this.dueDate = dueDate;
    }

    public double getOutstanding() { return outstanding; }
    public void setOutstanding(double outstanding) { this.outstanding = outstanding; }
    public double getMinimumDue() { return minimumDue; }
    public void setMinimumDue(double minimumDue) { this.minimumDue = minimumDue; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
