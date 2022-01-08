package de.diedavids.jmix.softreference.example.entity;

import de.diedavids.jmix.softreference.entity.SoftReferenceConverter;
import io.jmix.core.FileRef;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.PropertyDatatype;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "DOCUMENT")
@Entity
public class Document {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Composition
    @OneToMany(mappedBy = "document")
    private List<Tag> tags;

    @InstanceName
    @Column(name = "NAME")
    private String name;

    @Column(name = "FILE_", length = 1024)
    private FileRef file;

    @PropertyDatatype("SoftReference")
    @Column(name = "REFERS_TO")
    @Convert(converter = SoftReferenceConverter.class)
    private Object refersTo;

    public void setRefersTo(Object refersTo) {
        this.refersTo = refersTo;
    }

    public Object getRefersTo() {
        return refersTo;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public FileRef getFile() {
        return file;
    }

    public void setFile(FileRef file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}