package de.diedavids.jmix.jesrexample.entity;

import de.diedavids.jmix.jesr.entity.EntitySoftReferenceConverter;
import io.jmix.core.FileRef;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.PropertyDatatype;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@JmixEntity
@Table(name = "DOCUMENT")
@Entity
public class Document {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @InstanceName
    @Column(name = "NAME")
    private String name;

    @Column(name = "FILE_", length = 1024)
    private FileRef file;

    @PropertyDatatype("EntitySoftReference")
    @Column(name = "REFERS_TO")
    @Convert(converter = EntitySoftReferenceConverter.class)
    private io.jmix.core.Entity refersTo;

    public io.jmix.core.Entity getRefersTo() {
        return refersTo;
    }

    public void setRefersTo(io.jmix.core.Entity refersTo) {
        this.refersTo = refersTo;
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