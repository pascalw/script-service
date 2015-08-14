package nl.pwiddershoven.script.service.script.module;

public class ScriptExecutionException extends RuntimeException {
    public ScriptExecutionException(String message) {
        super(message);
    }

    public ScriptExecutionException(Throwable cause) {
        super(cause);
    }
}
