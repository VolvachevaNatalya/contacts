package com.example.contacts.controller;

import com.example.contacts.dto.ContactToDisplayDto;
import com.example.contacts.dto.SearchDto;
import com.example.contacts.entity.Contact;
import com.example.contacts.mapper.ContactMapper;
import com.example.contacts.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")

public class ContactRestController {
    private final ContactService service;
    private final ContactMapper contactMapper;

    public ContactRestController(ContactService service, ContactMapper contactMapper) {
        this.service = service;
        this.contactMapper = contactMapper;
    }

    @PostMapping("/contact")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveContact(@RequestBody ContactToDisplayDto contactToDisplayDto) {
        service.addContact(contactToDisplayDto.getFirstName(), contactToDisplayDto.getLastName(), contactToDisplayDto.getAge());
    }

    @GetMapping("/contact")
    @ResponseStatus(HttpStatus.OK)
    public List<ContactToDisplayDto> contacts() {
       return service
                .getAllContacts()
                .stream()
                .map(contact -> contactMapper.toDto(contact))
                .collect(Collectors.toList());
    }

    @GetMapping("/contact/search")
    public List<ContactToDisplayDto> getContacts(@RequestParam(required = false) String firstNameContains) {
        return service.searchByName(firstNameContains)
                .stream()
                .map(contact -> contactMapper.toDto(contact))
                .collect(Collectors.toList());
    }

    @GetMapping("/contact/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContactToDisplayDto getContactById(@PathVariable(name = "id") int contactId) {
        Contact contact = service.getById(contactId);
        return contactMapper.toDto(contact);
    }

    @ResponseBody
    @PutMapping("/contact/{id}")
    public void editContact(@PathVariable(name = "id") int contactId, @RequestBody ContactToDisplayDto contactToDisplayDto) {
        service.editContact(contactToDisplayDto.getFirstName(), contactToDisplayDto.getLastName(), contactToDisplayDto.getAge(), contactId);
    }

    @DeleteMapping("/contact/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable(name = "id") int contactId) {
        service.deleteById(contactId);
    }
}
