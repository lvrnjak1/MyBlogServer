package ba.unsa.etf.zavrsni.app.graphql.config;

import ba.unsa.etf.zavrsni.app.auth.JwtProcessor;
import graphql.servlet.internal.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import java.util.Map;
import java.util.Optional;

public class ApolloSubscriptionProtocolFactory extends SubscriptionProtocolFactory {
    private final JwtProcessor jwtProcessor;

    public ApolloSubscriptionProtocolFactory(JwtProcessor jwtProcessor) {
        super("graphql-ws");
        this.jwtProcessor = jwtProcessor;
    }

    public SubscriptionProtocolHandler createHandler(SubscriptionHandlerInput subscriptionHandlerInput) {
        return new ApolloSubscriptionProtocolHandler(subscriptionHandlerInput) {
            @Override
            public void onMessage(HandshakeRequest request, Session session, WsSessionSubscriptions subscriptions, String text) {
                ApolloSubscriptionProtocolHandler.OperationMessage message;
                try {
                    message =
                            (ApolloSubscriptionProtocolHandler.OperationMessage)subscriptionHandlerInput
                                    .getGraphQLObjectMapper().getJacksonMapper()
                                    .readValue(text, ApolloSubscriptionProtocolHandler.OperationMessage.class);

                    Map<String, Object> payload = (Map<String, Object>) message.getPayload();
                    var authToken = Optional.ofNullable(payload.get("authToken"))
                            .map(String::valueOf)
                            .orElse(null);

                    var auth = jwtProcessor.process(authToken);
                    SecurityContextHolder.getContext()
                            .setAuthentication(new UsernamePasswordAuthenticationToken(authToken, null));
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                super.onMessage(request, session, subscriptions, text);
            }
        };
    }
}

