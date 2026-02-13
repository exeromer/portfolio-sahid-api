package org.portfolio.ports.spi;

import org.portfolio.domain.ContactRequest;

public interface EmailNotifier {
    void sendNotification(ContactRequest request);
}
