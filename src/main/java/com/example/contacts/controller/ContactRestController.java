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
    @ResponseStatus(HttpStatus.OK)
    public void saveContact(@RequestBody Contact contact) {

        if (contact.getId() > 0)
            service.editContact(contact.getFirstName(), contact.getLastName(), contact.getAge(), contact.getId());
        else
            service.addContact(contact.getFirstName(), contact.getLastName(), contact.getAge());
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
    public Collection<Contact> getContacts(@RequestParam(required = false) String firstNameContains) {
        return service.searchByName(firstNameContains);
    }

    @GetMapping("/contact/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContactToDisplayDto getContactById(@PathVariable(name = "id") int contactId) {
        Contact contact = service.getAllContacts()
                .stream()
                .filter(contact1 -> contact1.getId() == contactId)
                .findFirst()
                .get();

        return contactMapper.toDto(contact);
    }

    @ResponseBody
    @PutMapping("/contact/{id}")
    public Contact editContact(@PathVariable(name = "id") int contactId, @RequestBody ContactToDisplayDto contactToDisplayDto) {

        Contact contact = service.getById(contactId);
        contact.setFirstName(contactToDisplayDto.getFirstName());
        contact.setLastName(contactToDisplayDto.getLastName());
        contact.setAge(contactToDisplayDto.getAge());
        return contact;
    }

    @DeleteMapping("/contact/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable(name = "id") int contactId) {
        service.deleteById(contactId);
    }
}
