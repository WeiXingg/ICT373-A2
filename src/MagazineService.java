/*
 ICT373 Assignment 1 Question 2
 Ong Wei Xing 34444625
 13/6/2023
 MagazineService.java
 MagazineService class, implementation for the emails
 */

import java.io.Serializable;
import java.util.ArrayList;

public class MagazineService implements Serializable {

    private ArrayList<Supplement> supplements;
    private ArrayList<Customer> customers;

    public MagazineService() {
        this.supplements = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

    public void addSupplement(Supplement supplement) {
        supplements.add(supplement);
    }

    public void removeSupplement(Supplement supplement) {
        supplements.remove(supplement);
    }

    public ArrayList<Supplement> getSupplements() {
        return supplements;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void sendWeeklyEmails() {
        System.out.println("\n=====================================\n");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println("Email: " + customers.get(i).getEmail());
            // Address test
            System.out.println("Address: " + customers.get(i).getAddress().getStreetNumber()
                    + " " + customers.get(i).getAddress().getStreetName()
                    + " " + customers.get(i).getAddress().getSuburb()
                    + " " + customers.get(i).getAddress().getPostcode());
            System.out.println("Subject: Weekly Email");
            System.out.println("Content:\nHello " + customers.get(i).getName()
                    + ",");
            System.out.println("Your magazine is ready to look at.");
            ArrayList<Supplement> supplements = customers.get(i).getSupplement();
            int a = supplements.size() - 1;
            System.out.print("Supplements subscribed to: ");
            for (int j = 0; j < supplements.size(); j++) {
                System.out.print(supplements.get(j).getName());
                if (j == a) // To delete last ,
                {
                    break;
                } else {
                    System.out.print(", ");
                }
            }
            System.out.println("\nEnjoy reading!");
            System.out.println("\n=====================================\n");
        }
    }

    public void sendMonthlyEmails() {
        for (int i = 0; i < customers.size(); i++) {
            // Check if current index is a paying customer
            if (customers.get(i) instanceof PayingCustomer) {
                // Type casting
                PayingCustomer payingCustomer = (PayingCustomer) customers.get(i);
                System.out.println("Email: " + customers.get(i).getEmail());
                System.out.println("Subject: Monthly Email");
                System.out.println("Content:\nHello " + customers.get(i).getName() + ",");
                System.out.println("This email is an invoice for your subscription(s).");
                double totalCost = 0.0;
                totalCost += calculateSupplementsCost(payingCustomer);
                for (int j = 0; j < payingCustomer.getAssociateCustomers().size(); j++) {
                    totalCost += calculateSupplementsCost(
                            payingCustomer.getAssociateCustomers().get(j));
                }
                System.out.println("\nTotal amount payable: " + totalCost);
                System.out.println("\n=====================================\n");
            }
        }
    }

    private double calculateSupplementsCost(Customer customer) {
        double cost = 0.0;
        for (int i = 0; i < customer.getSupplement().size(); i++) {
            // Assuming monthly = weeklycost x 4
            cost += (customer.getSupplement().get(i).getCost() * 4);
        }
        return cost;
    }
}
