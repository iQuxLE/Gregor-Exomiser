package org.monarchinitiative.gregor.mendel;

public class GregorException extends Exception{

    public static final long serialVersionUID = 2L;

    public GregorException(){
        super();
    }

    public GregorException(String msg) {
        super(msg);
    }

    public GregorException(String msg, Throwable cause){
        super(msg, cause);
    }
}
