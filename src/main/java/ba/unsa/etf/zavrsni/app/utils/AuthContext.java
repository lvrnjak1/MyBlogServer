package ba.unsa.etf.zavrsni.app.utils;

import ba.unsa.etf.zavrsni.app.model.User;
import graphql.servlet.GraphQLContext;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

public class AuthContext extends GraphQLContext {
    private final User user;

    public AuthContext(User user, HttpServletRequest httpServletRequest, HandshakeRequest handshakeRequest, Subject subject) {
        super(httpServletRequest, handshakeRequest, subject);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
