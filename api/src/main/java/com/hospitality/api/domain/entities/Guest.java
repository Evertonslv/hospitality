package com.hospitality.api.domain.entities;

import com.hospitality.api.domain.exceptions.MissingInformationException;
import lombok.Getter;

@Getter
public class Guest {
    private final String document;
    private Long id;
    private String name;
    private String phone;

    public Guest(Long id, String name, String document, String phone) {
        this(name, document, phone);
        this.id = id;
    }

    public Guest(String name, String document, String phone) {
        this.name = name;
        this.document = document;
        this.phone = phone;

        this.validateDates();
    }

    public void updatePhone(String newPhone) {
        this.phone = newPhone;
        this.validateDates();
    }

    public void updateName(String newName) {
        this.name = newName;
        this.validateDates();
    }

    private void validateDates() {
        if (name == null || name.isEmpty()) {
            throw new MissingInformationException("Name is required");
        }

        if (document == null || document.isEmpty()) {
            throw new MissingInformationException("Document is required");
        }

        if (phone == null || phone.isEmpty()) {
            throw new MissingInformationException("Phone number is required");
        }
    }
}
