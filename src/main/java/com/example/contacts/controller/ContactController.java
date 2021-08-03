package com.example.contacts.controller;


import com.example.contacts.dto.ContactToAddDto;
import com.example.contacts.dto.ContactToDisplayDto;
import com.example.contacts.dto.SearchDto;
import com.example.contacts.entity.Contact;
import com.example.contacts.mapper.ContactMapper;
import com.example.contacts.service.ContactService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Controller
public class ContactController {

    private final ContactService service;
    private final ContactMapper contactMapper;

    public ContactController(ContactService service, ContactMapper contactMapper) {
        this.service = service;
        this.contactMapper = contactMapper;
    }

    // @GetMapping("contacts")
    @RequestMapping(value = "/contacts", method = RequestMethod.GET)
    public String contacts(Model model) {

        List<ContactToDisplayDto> contactToDisplayDtos = service
                .getAllContacts()
                .stream()
                .map(contact -> contactMapper.toDto(contact))
                .collect(Collectors.toList());

        model.addAttribute("contacts", contactToDisplayDtos);

        return "contacts";
    }

    @PostMapping("/contacts/search")
    public String searchContacts(@ModelAttribute SearchDto searchDto, Model model) {
        List<ContactToDisplayDto> contactToDisplayDtos = service.searchByName(searchDto.searchName)
                .stream()
                .map(contact -> contactMapper.toDto(contact))
                .collect(Collectors.toList());

        model.addAttribute("contacts", contactToDisplayDtos);
        return "contacts";
    }

    @GetMapping
    @RequestMapping(value = "/contact-info/{id}")
    public String contactDetail(@PathVariable(name = "id") int contactId, Model model) {

        ContactToDisplayDto contactToDisplayDto = contactMapper.toDto(service.getById(contactId));
        model.addAttribute("contact", contactToDisplayDto);

        return "contact-details";
    }

    @GetMapping
    @RequestMapping(value = "/form")
    public String contactForm(Model model) {

        model.addAttribute("contact", new Contact());

        return "contact-form";
    }

    @GetMapping
    @RequestMapping(value = "/edit/{id}")
    public String editContact(@PathVariable(name = "id") int contactId, Model model) {

        Contact contact = service.getById(contactId);
        model.addAttribute("contact", contact);

        return "contact-form";
    }

    @PostMapping
    @RequestMapping(value = "/save")
    public String saveContact(@ModelAttribute Contact contact) {

        if (contact.getId() > 0)
            service.editContact(contact.getFirstName(), contact.getLastName(), contact.getAge(), contact.getId());
        else
            service.addContact(contact.getFirstName(), contact.getLastName(), contact.getAge());

        return "redirect:/contacts";
    }

    @DeleteMapping
    @RequestMapping(value = "/delete/{id}")
    public String deleteContact(@PathVariable(name = "id") int contactId) {
        service.deleteById(contactId);
        return "redirect:/contacts";
    }
}