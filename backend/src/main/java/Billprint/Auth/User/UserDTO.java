package Billprint.Auth.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    String email;


    public static UserDTO of(String email) {
        UserDTO newDTO = new UserDTO();
        newDTO.setEmail(email);
        return newDTO;
    }
}