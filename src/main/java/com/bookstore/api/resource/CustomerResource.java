package com.bookstore.api.resource;

import com.bookstore.api.data.CustomerData;
import com.bookstore.api.exception.CustomerNotFoundException;
import com.bookstore.api.exception.InvalidInputException;
import com.bookstore.api.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerResource.class);


    /**
     * Creates a new customer.
     *
     * @param customer the customer to create
     * @return the created customer
     */
    @POST
    public Response createCustomer(Customer customer) {
        LOGGER.info("Creating customer: {}", customer);
        // Validation
        if (customer.getName() == null || customer.getName().isEmpty() ||
                customer.getEmail() == null || !isValidEmail(customer.getEmail()) ||
                customer.getPassword() == null || customer.getPassword().isEmpty()) {
            throw new InvalidInputException("Invalid customer data: name, valid email, and password are required.");
        }
        // Check for unique email
        if (CustomerData.findCustomerByEmail(customer.getEmail()) != null) {
            throw new InvalidInputException("A customer with email " + customer.getEmail() + " already exists.");
        }
        Customer createdCustomer = CustomerData.addCustomer(customer);
        LOGGER.info("Created customer with ID: {}", createdCustomer.getId());
        return Response.status(Response.Status.CREATED).entity(createdCustomer).build();
    }

    /**
     * Retrieves all customers.
     *
     * @return a list of customers
     */

    @GET
    public Response getAllCustomers() {
        LOGGER.info("Retrieving all customers");
        List<Customer> customers = CustomerData.getAllCustomers();
        return Response.ok(customers).build();
    }

    /**
     * Retrieves a customer by ID.
     *
     * @param id the ID of the customer
     * @return the customer with the specified ID
     */
    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") int id) {
        LOGGER.info("Retrieving customer with ID: {}", id);
        Customer customer = CustomerData.findCustomerById(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        return Response.ok(customer).build();
    }

    /**
     * Updates an existing customer.
     *
     * @param id       the ID of the customer to update
     * @param customer the updated customer data
     * @return the updated customer
     */
    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer customer) {
        LOGGER.info("Updating customer with ID: {}", id);
        Customer existingCustomer = CustomerData.findCustomerById(id);
        if (existingCustomer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        // Validation
        if (customer.getName() == null || customer.getName().isEmpty() ||
                customer.getEmail() == null || !isValidEmail(customer.getEmail()) ||
                customer.getPassword() == null || customer.getPassword().isEmpty()) {
            throw new InvalidInputException("Invalid customer data: name, valid email, and password are required.");
        }
        // Check for unique email (excluding current customer)
        Customer customerWithEmail = CustomerData.findCustomerByEmail(customer.getEmail());
        if (customerWithEmail != null && customerWithEmail.getId() != id) {
            throw new InvalidInputException("A customer with email " + customer.getEmail() + " already exists.");
        }
        customer.setId(id);
        Customer updatedCustomer = CustomerData.updateCustomer(customer);
        LOGGER.info("Updated customer with ID: {}", id);
        return Response.ok(updatedCustomer).build();
    }

    /**
     * Deletes a customer by ID.
     *
     * @param id the ID of the customer to delete
     * @return a response indicating the result of the operation
     */

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        LOGGER.info("Deleting customer with ID: {}", id);
        Customer customer = CustomerData.findCustomerById(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        CustomerData.deleteCustomer(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // Simple email validation
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}