package Billprint.Auth.User;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@RequiredArgsConstructor
@Document(collection= "user")
public class AppUser {

    @Id
    private String id;
    private String email;
    private String password;
    private String role = "USER"; // kann man z.b. mit enum ersetzen


}
