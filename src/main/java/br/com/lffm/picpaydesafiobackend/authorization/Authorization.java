package br.com.lffm.picpaydesafiobackend.authorization;

record Authorization(String message) {
    public boolean isAuthorized() {
        return message.equals("Autorizado");
    }

    public static class UnauthorizedTransactionException extends RuntimeException {
        public UnauthorizedTransactionException(String message) {
            super(message);
        }
    }
}
