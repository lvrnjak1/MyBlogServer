package ba.unsa.etf.zavrsni.app.config;

import ba.unsa.etf.zavrsni.app.auth.JwtProcessor;
import graphql.servlet.GraphQLInvocationInputFactory;
import graphql.servlet.GraphQLObjectMapper;
import graphql.servlet.GraphQLQueryInvoker;
import graphql.servlet.GraphQLWebsocketServlet;
import graphql.servlet.internal.FallbackSubscriptionProtocolFactory;
import graphql.servlet.internal.SubscriptionHandlerInput;
import graphql.servlet.internal.SubscriptionProtocolFactory;
import graphql.servlet.internal.SubscriptionProtocolHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class GraphQLWebSocketConfig {
    private static final String HANDSHAKE_REQUEST_KEY = HandshakeRequest.class.getName();
    private static final String PROTOCOL_HANDLER_REQUEST_KEY = SubscriptionProtocolHandler.class.getName();



    private static SubscriptionProtocolFactory getSubscriptionProtocolFactory(
            List<SubscriptionProtocolFactory> subscriptionProtocolFactories,
            SubscriptionProtocolFactory fallbackSubscriptionProtocolFactory,
            List<String> accept
    ) {
        Iterator var1 = accept.iterator();

        while(var1.hasNext()) {
            String protocol = (String)var1.next();
            Iterator var3 = subscriptionProtocolFactories.iterator();

            while(var3.hasNext()) {
                SubscriptionProtocolFactory subscriptionProtocolFactory = (SubscriptionProtocolFactory)var3.next();
                if (subscriptionProtocolFactory.getProtocol().equals(protocol)) {
                    return subscriptionProtocolFactory;
                }
            }
        }

        return fallbackSubscriptionProtocolFactory;
    }


    @Bean
    public GraphQLWebsocketServlet graphQLWebsocketServlet(
            JwtProcessor jwtProcessor,
            GraphQLInvocationInputFactory invocationInputFactory,
            GraphQLQueryInvoker queryInvoker,
            GraphQLObjectMapper graphQLObjectMapper
    ) {
        final List<SubscriptionProtocolFactory> subscriptionProtocolFactories = Collections.singletonList(new ApolloSubscriptionProtocolFactory(jwtProcessor));
        final SubscriptionProtocolFactory fallbackSubscriptionProtocolFactory = new FallbackSubscriptionProtocolFactory();
        final List<String> allSubscriptionProtocols = (List) Stream.concat(subscriptionProtocolFactories.stream(), Stream.of(fallbackSubscriptionProtocolFactory)).map(SubscriptionProtocolFactory::getProtocol).collect(Collectors.toList());

        var subscriptionHandlerInput = new SubscriptionHandlerInput(invocationInputFactory, queryInvoker, graphQLObjectMapper);
        return new GraphQLWebsocketServlet(queryInvoker, invocationInputFactory, graphQLObjectMapper) {
            @Override
            public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
                sec.getUserProperties().put(HANDSHAKE_REQUEST_KEY, request);
                List<String> protocol = (List)request.getHeaders().get("Sec-WebSocket-Protocol");
                if (protocol == null) {
                    protocol = Collections.emptyList();
                }

                SubscriptionProtocolFactory subscriptionProtocolFactory = getSubscriptionProtocolFactory(
                        subscriptionProtocolFactories,
                        fallbackSubscriptionProtocolFactory,
                        protocol
                );
                sec.getUserProperties().put(PROTOCOL_HANDLER_REQUEST_KEY, subscriptionProtocolFactory.createHandler(subscriptionHandlerInput));
                if (request.getHeaders().get("Sec-WebSocket-Accept") != null) {
                    response.getHeaders().put("Sec-WebSocket-Accept", allSubscriptionProtocols);
                }

                if (!protocol.isEmpty()) {
                    response.getHeaders().put("Sec-WebSocket-Protocol", Collections.singletonList(subscriptionProtocolFactory.getProtocol()));
                }

            }
        };
    }

}
