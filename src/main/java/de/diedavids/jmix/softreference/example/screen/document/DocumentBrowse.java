package de.diedavids.jmix.softreference.example.screen.document;

import de.diedavids.jmix.softreference.screen.SoftReferenceInstanceNameTableColumnGenerator;
import de.diedavids.jmix.softreference.example.entity.SupportsDocumentReference;
import io.jmix.core.MetadataTools;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.UiComponents;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.screen.*;
import de.diedavids.jmix.softreference.example.entity.Document;

import javax.inject.Inject;

@UiController("Document.browse")
@UiDescriptor("document-browse.xml")
@LookupComponent("documentsTable")
public class DocumentBrowse extends StandardLookup<Document> {


    @Inject
    protected GroupTable<Document> documentsTable;

    @Inject
    protected UiComponents uiComponents;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected ScreenBuilders screenBuilders;

    @Subscribe
    protected void onInit(InitEvent event) {
        documentsTable.addGeneratedColumn("refersTo",
                new SoftReferenceInstanceNameTableColumnGenerator<Document, SupportsDocumentReference>(
                        "refersTo",
                        uiComponents,
                        metadataTools,
                        screenBuilders,
                        this
                ));
    }
}