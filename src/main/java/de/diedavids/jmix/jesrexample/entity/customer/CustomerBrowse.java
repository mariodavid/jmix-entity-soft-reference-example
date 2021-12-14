package de.diedavids.jmix.jesrexample.entity.customer;

import io.jmix.ui.screen.*;
import de.diedavids.jmix.jesrexample.entity.Customer;

@UiController("Customer.browse")
@UiDescriptor("customer-browse.xml")
@LookupComponent("customersTable")
public class CustomerBrowse extends StandardLookup<Customer> {
}