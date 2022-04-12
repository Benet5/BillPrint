package Billprint.Auth.User;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class RegisterData {

    private String email;
    private String password;
    private String passwordValidate;

}
