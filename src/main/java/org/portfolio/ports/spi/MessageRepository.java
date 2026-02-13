package org.portfolio.ports.spi;

import org.portfolio.domain.ContactRequest;

public interface MessageRepository {
    String save(ContactRequest request);
}
