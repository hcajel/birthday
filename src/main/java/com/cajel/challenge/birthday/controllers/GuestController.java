package com.cajel.challenge.birthday.controllers;

import com.cajel.challenge.birthday.entities.Guest;
import com.cajel.challenge.birthday.services.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @GetMapping
    public List<Guest> getAllGuests() {
        return guestService.getAllGuests();
    }

    @PostMapping
    public Guest saveGuest(@RequestBody Guest guest) {
        return guestService.saveGuest(guest);
    }

    @DeleteMapping("/{id}")
    public void deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
    }

    @PutMapping("/{id}/status")
    public Guest updateGuestStatus(@PathVariable Long id, @RequestBody String status) {
        return guestService.updateGuest(id, Guest.Status.valueOf(status));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex) {
        return "An error occurred: " + ex.getMessage();
    }
}
