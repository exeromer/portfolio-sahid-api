package org.portfolio.service;

import org.portfolio.domain.ContactRequest;
import org.portfolio.domain.ContactResponse;
import org.portfolio.ports.spi.EmailNotifier;
import org.portfolio.ports.spi.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class ContactService {

    private final MessageRepository repository;
    private final EmailNotifier emailNotifier;

    public ContactResponse processContact(ContactRequest request) {
        log.info("Processing contact request from: {}", request.email());

        String id = repository.save(request);
        log.info("Message persisted with ID: {}", id);

        // Notificación
        try {
            emailNotifier.sendNotification(request);
        } catch (Exception e) {
            log.error("Failed to send email notification", e);
        }

        return new ContactResponse(id, "RECEIVED", "Message received successfully");
    }
}