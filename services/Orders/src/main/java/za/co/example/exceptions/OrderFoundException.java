package za.co.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class OrderFoundException extends RuntimeException {

    private final Object var;
    private final Object val;

    public OrderFoundException(Object var, Object val) {
        super("Order with " + var + " = " + val + " already exists");
        this.var = var;
        this.val = val;
    }

    public OrderFoundException(String message) {
        super(message);
        this.var = null;
        this.val = null;
    }

    public Object getVar() {
        return var;
    }

    public Object getVal() {
        return val;
    }
}
