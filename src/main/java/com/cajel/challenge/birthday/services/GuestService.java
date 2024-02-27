package com.cajel.challenge.birthday.services;

import com.cajel.challenge.birthday.entities.Guest;
import com.cajel.challenge.birthday.exceptions.ResourceNotFoundException;
import com.cajel.challenge.birthday.repositories.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class GuestService {
    
    @Autowired
    private GuestRepository guestRepository;
    
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }
    
    public Guest saveGuest(Guest guest) {
        return guestRepository.save(guest);
    }
    
    public void deleteGuest(Long id) {
        guestRepository.deleteById(id);
    }
    
    public Guest updateGuest(Long id, Guest.Status status) {
        Guest guest = guestRepository.findById(id).orElse(null);
        if(!isNull(guest)){
            guest.setStatus(status);
            guestRepository.save(guest);
        } else {
            throw new ResourceNotFoundException("Person with id " + id + "not found");
        }
        return guest;
    }

}
