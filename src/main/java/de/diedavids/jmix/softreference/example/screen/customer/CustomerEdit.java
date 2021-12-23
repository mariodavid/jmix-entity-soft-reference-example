package de.diedavids.jmix.softreference.example.screen.customer;

import de.diedavids.jmix.softreference.example.entity.Customer;
import io.jmix.ui.screen.*;

@UiController("Customer.edit")
@UiDescriptor("customer-edit.xml")
@EditedEntityContainer("customerDc")
public class CustomerEdit extends StandardEditor<Customer> {
}