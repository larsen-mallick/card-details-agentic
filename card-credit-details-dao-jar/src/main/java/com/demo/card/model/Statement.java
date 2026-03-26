package com.demo.card.model;

import java.time.LocalDate;

public class Statement {
    private String id;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDate generatedOn;
    private LocalDate dueDate;
    private double totalDue;
    private double minimumDue;

    public Statement() {}

    public Statement(String id, LocalDate periodStart, LocalDate periodEnd, LocalDate generatedOn, LocalDate dueDate,
                      double totalDue, double minimumDue) {
        this.id = id;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.generatedOn = generatedOn;
        this.dueDate = dueDate;
        this.totalDue = totalDue;
        this.minimumDue = minimumDue;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDate getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }
    public LocalDate getGeneratedOn() { return generatedOn; }
    public void setGeneratedOn(LocalDate generatedOn) { this.generatedOn = generatedOn; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public double getTotalDue() { return totalDue; }
    public void setTotalDue(double totalDue) { this.totalDue = totalDue; }
    public double getMinimumDue() { return minimumDue; }
    public void setMinimumDue(double minimumDue) { this.minimumDue = minimumDue; }
}
