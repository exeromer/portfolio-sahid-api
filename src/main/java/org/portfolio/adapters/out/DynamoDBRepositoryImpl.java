package org.portfolio.adapters.out;
import org.portfolio.domain.ContactRequest;
import org.portfolio.ports.spi.MessageRepository;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class DynamoDBRepositoryImpl implements MessageRepository {

    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "PortfolioMessages"; // Debe coincidir con la tabla creada

    @Override
    public String save(ContactRequest request) {
        String id = UUID.randomUUID().toString();
        String timestamp = Instant.now().toString();

        // Mapeo Manual (Más eficiente que reflexión para Lambdas)
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(id).build());
        item.put("name", AttributeValue.builder().s(request.name()).build());
        item.put("email", AttributeValue.builder().s(request.email()).build());
        item.put("message", AttributeValue.builder().s(request.message()).build());
        item.put("createdAt", AttributeValue.builder().s(timestamp).build());
        item.put("status", AttributeValue.builder().s("PENDING").build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDbClient.putItem(putItemRequest);

        return id;
    }
}