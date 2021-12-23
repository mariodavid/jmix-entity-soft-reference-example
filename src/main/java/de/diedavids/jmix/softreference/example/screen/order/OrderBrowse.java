package de.diedavids.jmix.softreference.example.screen.order;

import io.jmix.ui.screen.*;
import de.diedavids.jmix.softreference.example.entity.Order;

@UiController("Order_.browse")
@UiDescriptor("order-browse.xml")
@LookupComponent("ordersTable")
public class OrderBrowse extends StandardLookup<Order> {
}