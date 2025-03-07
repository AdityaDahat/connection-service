package com.connection.constants;

public class Connectors {

    public static final String SOURCE_CONNECTION = "sourceConnection";
    public static final String TARGET_CONNECTION = "targetConnection";
    private Connectors() {

    }

    public enum Types {

        JDBC("JDBC"), REST_API("REST API"), SOAP("SOAP"), WEBHOOK("Webhook"), AMQP("AMQP"), SFTP("SFTP"),
        OTHER("Other");

        Types(String type) {
            this.type = type;
        }

        private String type;

        @Override
        public String toString() {
            return this.type;
        }

    }

    public static class Authorization {

        private Authorization() {

        }

        public static final String RE_AUTH = "re-auth";
        public static final String CONNECTION_ID = "connection-id";
        public static final String UI_CALLBACK = "ui-callback";
        public static final String CODE_CHALLENGE = "code-challenge";
        public static final String CODE_VERIFIER = "code_verifier";
    }

}
