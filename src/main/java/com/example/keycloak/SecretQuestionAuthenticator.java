package com.example.keycloak;

import jakarta.ws.rs.core.Response;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.Objects;

public class SecretQuestionAuthenticator implements Authenticator {

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        // Render the secret question form (FTL)
        Response challenge = context.form().createForm("secret-question.ftl");
        context.challenge(challenge);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        String answer = context.getHttpRequest().getDecodedFormParameters().getFirst("secret_answer");
        UserModel user = context.getUser();
        String expectedAnswer = user.getFirstAttribute("secret_answer");

        if (Objects.equals(answer, expectedAnswer)) {
            context.success();
        } else {
            Response challenge = context.form()
                    .setError("Incorrect answer to secret question")
                    .createForm("secret-question.ftl");
            context.failureChallenge(org.keycloak.authentication.AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
        }
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return user.getFirstAttribute("secret_answer") != null;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        // No required actions
    }

    @Override
    public void close() {
    }
}
