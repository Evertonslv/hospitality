package com.hospitality.api.domain.entities;

import com.hospitality.api.domain.exceptions.MissingInformationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GuestTest {

    @Test
    void shouldCreateGuestWithValidData() {
        Guest guest = new Guest("Diana Prince", "123456789", "(47) 99999-9999");

        assertThat(guest.getName()).isEqualTo("Diana Prince");
        assertThat(guest.getDocument()).isEqualTo("123456789");
        assertThat(guest.getPhone()).isEqualTo("(47) 99999-9999");
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> new Guest(null, "123456789", "(47) 99999-9999"))
                .isInstanceOf(MissingInformationException.class)
                .hasMessage("O nome é obrigatório");
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThatThrownBy(() -> new Guest("", "123456789", "(47) 99999-9999"))
                .isInstanceOf(MissingInformationException.class)
                .hasMessage("O nome é obrigatório");
    }

    @Test
    void shouldThrowExceptionWhenDocumentIsNull() {
        assertThatThrownBy(() -> new Guest("Diana Prince", null, "(47) 99999-9999"))
                .isInstanceOf(MissingInformationException.class)
                .hasMessage("O documento é obrigatório");
    }

    @Test
    void shouldThrowExceptionWhenDocumentIsEmpty() {
        assertThatThrownBy(() -> new Guest("Diana Prince", "", "(47) 99999-9999"))
                .isInstanceOf(MissingInformationException.class)
                .hasMessage("O documento é obrigatório");
    }

    @Test
    void shouldThrowExceptionWhenPhoneNumberIsNull() {
        assertThatThrownBy(() -> new Guest("Diana Prince", "123456789", null))
                .isInstanceOf(MissingInformationException.class)
                .hasMessage("O número de telefone é obrigatório");
    }

    @Test
    void shouldThrowExceptionWhenPhoneNumberIsEmpty() {
        assertThatThrownBy(() -> new Guest("Diana Prince", "123456789", ""))
                .isInstanceOf(MissingInformationException.class)
                .hasMessage("O número de telefone é obrigatório");
    }
}
