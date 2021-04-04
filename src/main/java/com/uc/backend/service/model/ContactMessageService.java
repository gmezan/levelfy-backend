package com.uc.backend.service.model;

import com.uc.backend.entity.ContactMessage;
import com.uc.backend.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ContactMessageService {

    ContactMessageRepository contactMessageRepository;

    @Autowired
    public ContactMessageService(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }


    public ContactMessage create(ContactMessage contactMessage) {

        if (contactMessage.getId()!=0)
            return null;

        if (contactMessage.getContact()==null || contactMessage.getContact().isEmpty())
            return null;

        if (contactMessage.getMessage()==null || contactMessage.getMessage().isEmpty())
            return null;

        if (contactMessage.getName()==null || contactMessage.getName().isEmpty())
            return null;

        return contactMessageRepository.save(contactMessage);
    }
}
