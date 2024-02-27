package com.cajel.challenge.birthday;

import com.cajel.challenge.birthday.entities.Guest;
import com.cajel.challenge.birthday.exceptions.ResourceNotFoundException;
import com.cajel.challenge.birthday.repositories.GuestRepository;
import com.cajel.challenge.birthday.services.GuestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class BirthdayApplicationTests {

   @Mock
    private GuestRepository guestRepository;

   @InjectMocks
    private GuestService guestService;

   @Test
    public void testUpdateGuestStatus() {
       Long id = 1L;
       Guest.Status newStatus = Guest.Status.CONFIRMED;
       Guest guest = Guest.builder()
               .id(id)
               .status(Guest.Status.POSSIBLE)
               .name("Name")
               .lastname("Lastname")
               .build();

       when(guestRepository.findById(id)).thenReturn(Optional.of(guest));
       when(guestRepository.save(guest)).thenReturn(guest);

       Guest updateGuest = guestService.updateGuest(id, newStatus);

       verify(guestRepository, times(1)).findById(id);
       verify(guestRepository, times(1)).save(guest);
       assert  updateGuest != null;
       assert updateGuest.getStatus().equals(newStatus);

       when(guestRepository.findById(id)).thenReturn(Optional.empty());

       Assertions.assertThrows(ResourceNotFoundException.class, () -> {
          guestService.updateGuest(id, newStatus);
       });
   }

}
