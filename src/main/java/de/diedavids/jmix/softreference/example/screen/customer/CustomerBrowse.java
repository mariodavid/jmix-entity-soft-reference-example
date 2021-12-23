package de.diedavids.jmix.softreference.example.screen.customer;

import de.diedavids.jmix.softreference.example.entity.Customer;
import io.jmix.ui.screen.*;

@UiController("Customer.browse")
@UiDescriptor("customer-browse.xml")
@LookupComponent("customersTable")
public class CustomerBrowse extends StandardLookup<Customer> {
}