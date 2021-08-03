package com.example.contacts.repository;

import com.example.contacts.entity.Contact;

import java.util.List;
import java.util.Optional;

public interface IContractRepo {

    void add(Contact contact);

    List<Contact> getAll();

    Optional<Contact> getById(int id);

    void delete(Contact contact);

}
