package model;

public class WorkMonthEmployee {
    private int employeeId;
    private String employeeName;
    private int totalWorkingTime;

    public WorkMonthEmployee(int employeeId, String employeeName, int totalWorkingTime) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.totalWorkingTime = totalWorkingTime;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public int getTotalWorkingTime() {
        return totalWorkingTime;
    }
}

