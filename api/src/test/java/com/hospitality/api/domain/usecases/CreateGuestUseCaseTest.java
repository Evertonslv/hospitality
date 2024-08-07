package com.hospitality.api.domain.usecases;

import com.hospitality.api.domain.entities.Guest;
import com.hospitality.api.domain.exceptions.MissingInformationException;
import com.hospitality.api.domain.usecases.dtos.CreateGuestRequest;
import com.hospitality.api.domain.usecases.dtos.GuestResponse;
import com.hospitality.api.domain.usecases.gateways.GuestGateway;
import com.hospitality.api.domain.usecases.mappers.GuestDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateGuestUseCaseTest {
    @Mock
    private GuestGateway guestGateway;

    @Spy
    private GuestDtoMapper guestDtoMapper;

    @InjectMocks
    private CreateGuestUseCase createGuestUseCase;


    @Test
    public void shouldSaveGuestAndReturnResponse() {
        Guest savedGuest = new Guest("Bruce Wayne", "123456789", "(47) 99999-9999");
        CreateGuestRequest createGuestRequest = new CreateGuestRequest("Bruce Wayne", "123456789", "(47) 99999-9999");

        when(guestGateway.save(any(Guest.class))).thenReturn(savedGuest);

        GuestResponse response = createGuestUseCase.execute(createGuestRequest);

        verify(guestGateway).save(any(Guest.class));
        verify(guestDtoMapper).toResponse(savedGuest);
        assertEquals("Bruce Wayne", response.name());
        assertEquals("123456789", response.document());
        assertEquals("(47) 99999-9999", response.phone());
    }

    @Test
    public void shouldThrowMissingInformationExceptionWhenNameDoesNotExist() {
        CreateGuestRequest createGuestRequest = new CreateGuestRequest("", "123456789", "(47) 99999-9999");

        assertThatThrownBy(() -> createGuestUseCase.execute(createGuestRequest))
                .isInstanceOf(MissingInformationException.class)
                .hasMessage("O nome é obrigatório");

        verifyNoMoreInteractions(guestGateway, guestDtoMapper);
    }

    @Test
    public void shouldThrowMissingInformationExceptionWhenPhoneDoesNotExist() {
        CreateGuestRequest createGuestRequest = new CreateGuestRequest("Bruce Wayne", "", "(47) 99999-9999");

        assertThatThrownBy(() -> createGuestUseCase.execute(createGuestRequest))
                .isInstanceOf(MissingInformationException.class)
                .hasMessage("O documento é obrigatório");

        verifyNoMoreInteractions(guestGateway, guestDtoMapper);
    }

    @Test
    public void shouldThrowMissingInformationExceptionWhenDocumentDoesNotExist() {
        CreateGuestRequest createGuestRequest = new CreateGuestRequest("Bruce Wayne", "123456789", "");

        assertThatThrownBy(() -> createGuestUseCase.execute(createGuestRequest))
                .isInstanceOf(MissingInformationException.class)
                .hasMessage("O número de telefone é obrigatório");

        verifyNoMoreInteractions(guestGateway, guestDtoMapper);
    }
}
