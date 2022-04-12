package Billprint.Auth.User;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class LoginData {

    private String email;
    private String password;
    private String passwordValidate;


}
