package org.monarchinitiative.gregor.mendel;

import java.io.Serial;

public class GregorException extends Exception{

    @Serial
    private static final long serialVersionUID = 2L;

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
