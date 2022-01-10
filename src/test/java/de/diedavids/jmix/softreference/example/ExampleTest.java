package de.diedavids.jmix.softreference.example;

import de.diedavids.jmix.softreference.SoftReferenceService;
import de.diedavids.jmix.softreference.example.entity.Customer;
import de.diedavids.jmix.softreference.example.entity.Document;
import de.diedavids.jmix.softreference.example.entity.Tag;
import io.jmix.core.DataManager;
import io.jmix.core.EntityStates;
import io.jmix.core.Id;
import io.jmix.core.SaveContext;
import io.jmix.core.security.SystemAuthenticator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ExampleTest {

    @Autowired
    DataManager dataManager;
    @Autowired
    SystemAuthenticator systemAuthenticator;
    @Autowired
    EntityStates entityStates;
    @Autowired
    SoftReferenceService sut;


    private Customer customer1;
    private Customer customer2;


    @BeforeEach
    void setUp() {
        customer1 = storeCustomer("Customer 1");
        customer2 = storeCustomer("Customer 2");
    }

    @Nested
    class DoSoftReferencesExists {

        @Test
        void given_softReferenceToCustomer_then_resultIsTrue() {

            asSystem(() -> {

                // given
                storeDocumentWithReference(customer1);

                // expect
                assertThat(doSoftReferencesExistsFor(customer1)).isTrue();
            });
        }

        @Test
        void given_noSoftReferenceToCustomer_then_resultIsFalse() {

            asSystem(() -> {
                // given
                storeDocumentWithReference(customer1);

                // expect
                assertThat(doSoftReferencesExistsFor(customer2)).isFalse();
                
            });
        }

        private boolean doSoftReferencesExistsFor(Customer customer) {
            return sut.doSoftReferencesExist(
                    Document.class,
                    customer,
                    "refersTo"
            );
        }

    }

    @Nested
    class LoadEntitiesForSoftReference {

        @Test
        void given_softReferenceToCustomer_then_resultIsDocument() {

            asSystem(() -> {

                // given
                final Document documentForCustomer1 = storeDocumentWithReference(customer1);

                // expect
                assertThat(loadEntitiesForSoftReferenceFor(customer1))
                        .containsExactly(documentForCustomer1);
                
            });
        }

        @Test
        void given_softReferenceToCustomer_when_askingForEntityWithFetchPlan_then_resultIsDocumentWithCorrectFetchPlan() {

            asSystem(() -> {
                // given
                storeDocumentWithReferenceAndTags(customer1, "customer1", "contract", "en_US");

                // when
                final Collection<Document> documentsWithTags = loadEntitiesForSoftReferenceFor(customer1, "documentWithTags");


                // then
                final Optional<Document> potentialDocument = documentsWithTags.stream().findFirst();
                assertThat(potentialDocument)
                        .isPresent();

                final Document document = potentialDocument.get();

                // and
                final boolean documentWithTags = entityStates.isLoadedWithFetchPlan(document, "documentWithTags");
                assertThat(documentWithTags)
                        .isTrue();

                // and
                assertThat(document.getTags())
                        .extracting("name")
                        .containsExactlyInAnyOrder("customer1", "contract", "en_US");
                
            });
        }


        @Test
        void given_noSoftReferenceToCustomer_then_resultIsEmpty() {

            asSystem(() -> {
                // given
                final Document documentForCustomer1 = storeDocumentWithReference(customer1);

                // expect
                assertThat(loadEntitiesForSoftReferenceFor(customer2))
                        .doesNotContain(documentForCustomer1)
                        .isEmpty();
                
            });
        }

        private Collection<Document> loadEntitiesForSoftReferenceFor(Customer customer) {
            return sut.loadEntitiesForSoftReference(
                    Document.class,
                    customer,
                    "refersTo"
            );
        }

        private Collection<Document> loadEntitiesForSoftReferenceFor(Customer customer, String fetchPlan) {
            return sut.loadEntitiesForSoftReference(
                    Document.class,
                    customer,
                    "refersTo",
                    fetchPlan
            );
        }
    }

    @Nested
    class CountEntitiesForSoftReference {

        @Test
        void given_softReferenceToCustomer_then_resultIsDocument() {

            asSystem(() -> {
                // given
                storeDocumentWithReference(customer1);

                // expect
                assertThat(countEntitiesForSoftReferenceFor(customer1))
                        .isOne();
                
            });
        }

        @Test
        void given_noSoftReferenceToCustomer_then_resultIsEmpty() {

            asSystem(() -> {
                // given
                storeDocumentWithReference(customer1);

                // expect
                assertThat(countEntitiesForSoftReferenceFor(customer2))
                        .isZero();
                
            });
        }

        private int countEntitiesForSoftReferenceFor(Customer customer) {
            return sut.countEntitiesForSoftReference(
                    Document.class,
                    customer,
                    "refersTo"
            );
        }
    }


    private Document documentWithRandomId() {
        final Document document = dataManager.create(Document.class);
        document.setId(UUID.randomUUID());
        return document;
    }


    private Document storeDocumentWithReference(Customer customer) {
        final Document document = documentWithRandomId();
        document.setName("Document for " + customer.getName());
        document.setRefersTo(customer);

        return dataManager.save(document);
    }

    private Document storeDocumentWithReferenceAndTags(Customer customer, String... tagNames) {

        final Document document = documentWithRandomId();
        document.setName("Document for " + customer.getName());
        document.setRefersTo(customer);

        final SaveContext saveContext = new SaveContext();
        saveContext.saving(document);

        Arrays.stream(tagNames)
                .map(tagName -> createTagFor(document, tagName))
                .forEach(saveContext::saving);

        dataManager.save(saveContext);

        return dataManager.load(Id.of(document))
                .one();
    }

    private Tag createTagFor(Document document, String tagName) {
        return systemAuthenticator.withSystem(() -> {
            final Tag tag = dataManager.create(Tag.class);
            tag.setName(tagName);
            tag.setDocument(document);
            return tag;
        });
    }

    private Customer storeCustomer(String name) {
        return systemAuthenticator.withSystem(() -> {
            Customer customer = dataManager.create(Customer.class);
            customer.setName(name);
            return dataManager.save(customer);
        });
    }

    public void asSystem(Runnable authenticatedOperation) {
        systemAuthenticator.withSystem(() -> {
            authenticatedOperation.run();
            return null;
        });
    }

}