package ar.fdelbo.jpos.exception;

public class JPosException extends RuntimeException {

    public JPosException(String msg, Throwable t) {
        super(msg, t);
    }

}
