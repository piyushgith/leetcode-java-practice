package org.java.design.pattern.behavioral.chainofresponsibility;

// Request object
class LeaveRequest {
    private String employeeName;
    private int days;
    private double salary;

    public LeaveRequest(String employeeName, int days, double salary) {
        this.employeeName = employeeName;
        this.days = days;
        this.salary = salary;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public int getDays() {
        return days;
    }

    public double getSalary() {
        return salary;
    }
}

// Handler interface - defines contract for handlers in the chain
abstract class Approver {
    protected Approver nextApprover;
    protected double approvalLimit;

    public void setNextApprover(Approver nextApprover) {
        this.nextApprover = nextApprover;
    }

    public abstract void processRequest(LeaveRequest request);

    protected void approve(LeaveRequest request) {
        System.out.println(this.getClass().getSimpleName() + " approved leave for " + request.getEmployeeName() + " for " + request.getDays() + " days");
    }

    protected void reject(LeaveRequest request) {
        System.out.println(this.getClass().getSimpleName() + " rejected leave for " + request.getEmployeeName());
    }
}

// Concrete handlers
class Manager extends Approver {
    public Manager() {
        this.approvalLimit = 3; // Can approve up to 3 days
    }

    @Override
    public void processRequest(LeaveRequest request) {
        if (request.getDays() <= approvalLimit) {
            approve(request);
        } else if (nextApprover != null) {
            System.out.println(this.getClass().getSimpleName() + " forwarding to next approver...");
            nextApprover.processRequest(request);
        } else {
            reject(request);
        }
    }
}

class Director extends Approver {
    public Director() {
        this.approvalLimit = 7; // Can approve up to 7 days
    }

    @Override
    public void processRequest(LeaveRequest request) {
        if (request.getDays() <= approvalLimit) {
            approve(request);
        } else if (nextApprover != null) {
            System.out.println(this.getClass().getSimpleName() + " forwarding to next approver...");
            nextApprover.processRequest(request);
        } else {
            reject(request);
        }
    }
}

class CEO extends Approver {
    public CEO() {
        this.approvalLimit = Integer.MAX_VALUE; // Can approve any amount
    }

    @Override
    public void processRequest(LeaveRequest request) {
        if (request.getDays() <= approvalLimit) {
            approve(request);
        } else {
            reject(request);
        }
    }
}

// Client code
public class ChainOfResponsibilityDemo {
    public static void main(String[] args) {
        // Create handlers
        Approver manager = new Manager();
        Approver director = new Director();
        Approver ceo = new CEO();

        // Build the chain: Manager -> Director -> CEO
        manager.setNextApprover(director);
        director.setNextApprover(ceo);

        System.out.println("=== Leave Request Processing ===\n");

        // Request 1: 2 days (handled by Manager)
        System.out.println("Request 1: 2 days leave");
        LeaveRequest request1 = new LeaveRequest("Alice", 2, 5000);
        manager.processRequest(request1);

        System.out.println();

        // Request 2: 5 days (forwarded to Director)
        System.out.println("Request 2: 5 days leave");
        LeaveRequest request2 = new LeaveRequest("Bob", 5, 6000);
        manager.processRequest(request2);

        System.out.println();

        // Request 3: 10 days (forwarded to CEO)
        System.out.println("Request 3: 10 days leave");
        LeaveRequest request3 = new LeaveRequest("Charlie", 10, 7000);
        manager.processRequest(request3);

        System.out.println();

        // Request 4: 30 days (rejected)
        System.out.println("Request 4: 30 days leave");
        LeaveRequest request4 = new LeaveRequest("David", 30, 8000);
        manager.processRequest(request4);
    }
}
