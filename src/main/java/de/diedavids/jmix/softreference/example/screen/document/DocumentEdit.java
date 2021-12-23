package de.diedavids.jmix.softreference.example.screen.document;

import de.diedavids.jmix.softreference.screen.SoftReferenceFormFieldGenerator;
import de.diedavids.jmix.softreference.example.entity.Customer;
import de.diedavids.jmix.softreference.example.entity.Order;
import de.diedavids.jmix.softreference.example.entity.SupportsDocumentReference;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Form;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import de.diedavids.jmix.softreference.example.entity.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;

@UiController("Document.edit")
@UiDescriptor("document-edit.xml")
@EditedEntityContainer("documentDc")
public class DocumentEdit extends StandardEditor<Document> {

    @Autowired
    protected InstanceContainer<Document> documentDc;

    @Autowired
    protected ScreenBuilders screenBuilders;

    @Autowired
    private Form form;

    @Autowired
    private SoftReferenceFormFieldGenerator softReferenceFormFieldGenerator;


    @Subscribe
    protected void onInit(InitEvent event) {
        softReferenceFormFieldGenerator.initSoftReferenceFormField(form, documentDc, "refersTo");
    }

    @Subscribe("assignToCustomer")
    protected void onAssignToCustomer(Action.ActionPerformedEvent event) {
        screenBuilders.lookup(Customer.class, this).withSelectHandler(customers -> {
            Customer customer = new LinkedList<>(customers).getFirst();
            setSoftReferenceValue(customer);
        }).build().show();
    }

    @Subscribe("assignToOrder")
    protected void onAssignToOrder(Action.ActionPerformedEvent event) {
        screenBuilders.lookup(Order.class, this).withSelectHandler(orders -> {
            Order order = new LinkedList<>(orders).getFirst();
            setSoftReferenceValue(order);
        }).build().show();
    }

    private void setSoftReferenceValue(SupportsDocumentReference softReferenceValue) {
        documentDc.getItem().setRefersTo(softReferenceValue);
    }
}