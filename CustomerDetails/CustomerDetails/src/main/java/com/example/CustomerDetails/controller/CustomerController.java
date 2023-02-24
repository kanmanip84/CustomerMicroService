package com.example.CustomerDetails.controller;

import com.example.CustomerDetails.entity.Customer;
import com.example.CustomerDetails.repository.CustomerRepository;
import com.example.CustomerDetails.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RestController

public class CustomerController {
    @Autowired
    private CustomerService service;
    @Autowired
    private CustomerRepository repository;
    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/addCustomer")
    public Customer addCustomer(@RequestBody Customer customer) {
        String msg = "This method is used to add the Customer";
        createFile(msg);
        return service.saveCustomer(customer);
    }

    @PostMapping("/addCustomers")
    public List<Customer> addCustomers(@RequestBody List<Customer> customers) {
        String msg = "This method is used to add multiple Customers";
        createFile(msg);
        return service.saveCustomers(customers);
    }

    @GetMapping("/customers")
    public List<Customer> findAllCustomers() {
        String msg = "This method is used to getting all the Customers";
        createFile(msg);
        return service.getCustomers();

    }
    @GetMapping("/customerById/{id}")
    public Customer findById(@PathVariable("id") String idString) {
        String msg = "This method is used to get the customer by id";
        createFile(msg);
        Customer customer = new Customer();
        try {
            int ids = Integer.parseInt(idString);
            customer = service.getCustomerById(ids);
            customer.setMsg("valid id format");
        } catch (NumberFormatException e) {
            customer.setMsg("Invalid id format");
        }
        return customer;
    }

    @GetMapping("/customerByName/{name}")
    public Customer getCustomerByName(@PathVariable String name) {
        String msg = "This method is used to get the customer by name";
        createFile(msg);
        return service.getCustomerByName(name);
    }

    @GetMapping("/starting-with/{startingLetter}")
    public List<Customer> getCustomersByNameStartingWith(@PathVariable String startingLetter) {
        String msg = "This method is used to get the customer by name starting with" + startingLetter;
        createFile(msg);
        return service.getCustomersByNameStartingWith(startingLetter);
    }

    @PutMapping("/update")
    public Customer updateCustomer(@RequestBody Customer customer) {
        String msg = "This method is used to Update the existing customer";
        createFile(msg);
        return service.updateCustomer(customer);
    }

    @RequestMapping("/template/accounts")
    public String getAccounts() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String msg = "This method is used to get the accounts from AccountMs";
        createFile(msg);
        return restTemplate.exchange("http://localhost:9191/accounts", HttpMethod.GET, entity, String.class).getBody();
    }

    @RequestMapping("/template/deleteByCustomerId/{id}")
    public String deleteCustomer(@PathVariable int id) {
        String msg = "This method will check whether the Entered customer is available or not." +
                     "if it is available,Before deleting the customer,this method will delete the accounts of particular customer from AccountMS";
        createFile(msg);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        Optional<Customer> customer = repository.findById(id);
        if (!customer.isPresent()) {
            return ("Entered customer is not available");
        } else {
            restTemplate.exchange("http://localhost:9191/deleteByCustomerId/" + id, HttpMethod.DELETE, entity, String.class).getBody();
            repository.deleteById(id);
            return ("Customer deleted successfully  " + id);
        }
    }
    private void createFile(String msg) {
        {
            try {
                File myObj = new File("C:\\Users\\admin\\IdeaProjects\\kanmani\\CreateFile.txt");
                myObj.createNewFile();
                System.out.println("File created: " + myObj.getName());

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            try {
                FileWriter myWriter = new FileWriter("C:\\Users\\admin\\IdeaProjects\\kanmani\\CreateFile.txt", true);
                BufferedWriter bufferedWriter = new BufferedWriter(myWriter);
                PrintWriter printWriter = new PrintWriter(bufferedWriter);
                printWriter.println(msg + "\n");
                printWriter.flush();
                bufferedWriter.flush();
                myWriter.flush();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/log")
    private StringBuilder ReadFile() {
        StringBuilder fileRead = new StringBuilder();
        String line = " ";
        try {
            File myObj = new File("C:\\Users\\admin\\IdeaProjects\\kanmani\\CreateFile.txt");
            FileReader filereader = new FileReader(myObj);
            BufferedReader reader = new BufferedReader(filereader);
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                fileRead.append(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return (fileRead);
    }
}
