package org.portfolio.adapters.in;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.portfolio.config.DependencyFactory;
import org.portfolio.domain.ContactRequest;
import org.portfolio.domain.ContactResponse;
import org.portfolio.service.ContactService;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger log = LogManager.getLogger(LambdaHandler.class);
    private final ContactService contactService;
    private final ObjectMapper objectMapper;

    // Constructor sin argumentos requerido por AWS Lambda
    public LambdaHandler() {
        // Inyección de dependencias manual (Singleton Pattern)
        this.contactService = DependencyFactory.getContactService();
        this.objectMapper = DependencyFactory.getObjectMapper();
    }

    @Override
    @SneakyThrows
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        log.info("Received request: {}", event.getBody());

        // CORS Headers
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "Access-Control-Allow-Origin", "*",
                "Access-Control-Allow-Methods", "POST,OPTIONS",
                "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token"
        );
        if ("OPTIONS".equalsIgnoreCase(event.getHttpMethod())) {
            return createResponse(200, "{}", headers);
        }

        log.info("Received request: {}", event.getBody());

        try {
            if (event.getBody() == null) {
                return createResponse(400, "{\"error\": \"Empty body\"}", headers);
            }

            ContactRequest request = objectMapper.readValue(event.getBody(), ContactRequest.class);
            ContactResponse response = contactService.processContact(request);
            return createResponse(200, objectMapper.writeValueAsString(response), headers);

        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return createResponse(400, String.format("{\"error\": \"%s\"}", e.getMessage()), headers);

        } catch (Exception e) {
            log.error("Internal server error", e);
            return createResponse(500, "{\"error\": \"Internal processing error\"}", headers);        }
    }

    private APIGatewayProxyResponseEvent createResponse(int statusCode, String body, Map<String, String> headers) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(headers)
                .withBody(body);
    }

}