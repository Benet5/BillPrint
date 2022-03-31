package Billprint.Import.Client;

import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

public class ClientAlreadyExistsException extends IllegalStateException{
    ClientAlreadyExistsException(){
        super("client already exists");
    }
}
