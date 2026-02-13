package org.portfolio.config;
import org.portfolio.adapters.out.DynamoDBRepositoryImpl;
import org.portfolio.adapters.out.SesEmailNotifierImpl;
import org.portfolio.ports.spi.EmailNotifier;
import org.portfolio.ports.spi.MessageRepository;
import org.portfolio.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.regions.Region;


public class DependencyFactory {

    private DependencyFactory() {
        throw new IllegalStateException("Clase de utilidad, no se instancia");
    }

    // Configuración explícita para evitar Timeouts
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .httpClient(UrlConnectionHttpClient.create())
            .region(Region.US_EAST_1)
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .build();

    private static final SesClient sesClient = SesClient.builder()
            .httpClient(UrlConnectionHttpClient.create())
            .region(Region.US_EAST_1)
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .build();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final MessageRepository messageRepository = new DynamoDBRepositoryImpl(dynamoDbClient);
    private static final EmailNotifier emailNotifier = new SesEmailNotifierImpl(sesClient);
    private static final ContactService contactService = new ContactService(messageRepository, emailNotifier);

    public static ContactService getContactService() {
        return contactService;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}