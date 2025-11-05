package com.example.keycloak;

import jakarta.ws.rs.core.Response;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.models.UserModel;

public class SecretQuestionAuthenticator implements Authenticator {

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        Response challenge = context.form()
                .createForm("secret-question.ftl");
        context.challenge(challenge);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        String answer = context.getHttpRequest().getDecodedFormParameters().getFirst("secret_answer");

        UserModel user = context.getUser();
        String expectedAnswer = user.getFirstAttribute("secret_answer");

        if (expectedAnswer != null && expectedAnswer.equalsIgnoreCase(answer)) {
            context.success();
        } else {
            Response challenge = context.form()
                    .setError("Invalid answer. Please try again.")
                    .createForm("secret-question.ftl");
            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
        }
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(org.keycloak.models.KeycloakSession session,
                                 org.keycloak.models.RealmModel realm,
                                 UserModel user) {
        return user.getFirstAttribute("secret_answer") != null;
    }

    @Override
    public void setRequiredActions(org.keycloak.models.KeycloakSession session,
                                   org.keycloak.models.RealmModel realm,
                                   UserModel user) {
        // No required actions here for simplicity
    }

    @Override
    public void close() {
    }
}
