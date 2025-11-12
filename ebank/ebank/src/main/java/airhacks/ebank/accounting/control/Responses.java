package airhacks.ebank.accounting.control;

import java.net.URI;

import airhacks.ebank.accounting.control.AccountCreationResult.AlreadyExists;
import airhacks.ebank.accounting.control.AccountCreationResult.Invalid;
import jakarta.ws.rs.core.Response;

public interface Responses {
    
    static Response created(AccountCreationResult.Created created) {
        var iban = created.account().iban();
        var uri = URI.create("/" + iban);
        return Response.created(uri).build();
    }

    static Response alreadyExists(AlreadyExists exists) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(exists)
                .build();
    }

    static Response ok(Object entity) {
        return Response
                .ok(entity)
                .build();
    }

    static Response invalid(Invalid invalid) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(invalid)
                .build();
    }

    static Response noContent() {
        return Response.noContent().build();
    }
}
