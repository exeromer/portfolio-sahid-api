package org.portfolio.adapters.out;
import org.portfolio.domain.ContactRequest;
import org.portfolio.ports.spi.EmailNotifier;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.ses.SesClient;

@RequiredArgsConstructor
public class SesEmailNotifierImpl implements EmailNotifier {

    private final SesClient sesClient;

    private static final String SENDER_EMAIL = "sahidromer@gmail.com";

    @Override
    public void sendNotification(ContactRequest request) {
        String subject = "Nuevo mensaje Portfolio: " + request.name();

        String htmlBody = String.format("""
            <html>
            <body>
                <h2>Has recibido un nuevo mensaje</h2>
                <p><strong>Nombre:</strong> %s</p>
                <p><strong>Email:</strong> %s</p>
                <p><strong>Mensaje:</strong></p>
                <blockquote style="background: #f9f9f9; padding: 10px; border-left: 5px solid #007bff;">
                    %s
                </blockquote>
            </body>
            </html>
            """, request.name(), request.email(), request.message());

        sesClient.sendEmail(req -> req
                .source(SENDER_EMAIL)
                .destination(d -> d.toAddresses(SENDER_EMAIL))
                .message(msg -> msg
                        .subject(s -> s.data(subject))
                        .body(b -> b.html(h -> h.data(htmlBody)))
                )
        );
    }
}