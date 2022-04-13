package Billprint.Auth.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class RegisterData {

    private String email;
    private String password;
    private String passwordValidate;

}
