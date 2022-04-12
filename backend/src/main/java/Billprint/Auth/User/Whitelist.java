package Billprint.Auth.User;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@RequiredArgsConstructor
@Document(collection= "whitelist")
public class Whitelist {
    @Id
    private String id;
    private String email;
}