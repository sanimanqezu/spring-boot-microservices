package za.co.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AddressesNotFoundException extends RuntimeException {

    private final Object var;
    private final Object val;

    public AddressesNotFoundException(Object var, Object val) {
        super("No address with " + var + " = " + val + "was found");
        this.var = var;
        this.val = val;
    }

    public AddressesNotFoundException(String message) {
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
