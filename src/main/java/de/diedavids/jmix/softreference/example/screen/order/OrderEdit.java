package de.diedavids.jmix.softreference.example.screen.order;

import io.jmix.ui.screen.*;
import de.diedavids.jmix.softreference.example.entity.Order;

@UiController("Order_.edit")
@UiDescriptor("order-edit.xml")
@EditedEntityContainer("orderDc")
public class OrderEdit extends StandardEditor<Order> {
}