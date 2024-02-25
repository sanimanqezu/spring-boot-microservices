package za.co.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    private final Object var;
    private final Object val;

    public ProductNotFoundException(Object var, Object val) {
        super("Product with " + var + " = " + val + " was not found");
        this.var = var;
        this.val = val;
    }

    public ProductNotFoundException(String message) {
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
