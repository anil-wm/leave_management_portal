package com.wavemaker.leavemanagement.model;

import java.sql.Date;
import java.util.Objects;

public class Leave {
    int employeeId;
    String leaveType;
    Date fromDate;
    Date toDate;
    Date appliedOn;
    String leaveDescription;
    String statusOfLeave;

    public Leave(int employeeId, String leaveType, Date fromDate, Date toDate, Date appliedOn,
                 String leaveDescription, String statusOfLeave) {

        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.appliedOn = appliedOn;
        this.leaveDescription = leaveDescription;
        this.statusOfLeave = statusOfLeave;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getAppliedOn() {
        return appliedOn;
    }

    public void setAppliedOn(Date appliedOn) {
        this.appliedOn = appliedOn;
    }

    public String getLeaveDescription() {
        return leaveDescription;
    }

    public void setLeaveDescription(String leaveDescription) {
        this.leaveDescription = leaveDescription;
    }

    public String getStatusOfLeave() {
        return statusOfLeave;
    }

    public void setStatusOfLeave(String statusOfLeave) {
        this.statusOfLeave = statusOfLeave;
    }

    @Override
    public String toString() {
        return "Leave{" +
                "leaveType='" + leaveType + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", appliedOn=" + appliedOn +
                ", leaveDescription='" + leaveDescription + '\'' +
                ", statusOfLeave=" + statusOfLeave +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leave leave = (Leave) o;
        return statusOfLeave.equals(leave.statusOfLeave) && Objects.equals(leaveType, leave.leaveType)
                && Objects.equals(fromDate, leave.fromDate) && Objects.equals(toDate, leave.toDate)
                && Objects.equals(appliedOn, leave.appliedOn)
                && Objects.equals(leaveDescription, leave.leaveDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leaveType, fromDate, toDate, appliedOn, leaveDescription, statusOfLeave);
    }
}
