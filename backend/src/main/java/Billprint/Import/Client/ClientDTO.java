package Billprint.Import.Client;
import Billprint.Import.Item.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    private String name;
    private String street;
    private String location;
    private boolean tax;
    private int fee;
    private int skonto;
    private List<Link> links;


    public static ClientDTO of(Client client){
        List<Link> links = List.of(
                Link.of("api/client/" + client.getId(), "self")
        );

        return new ClientDTO(
                client.getAddress().getName(),
                client.getAddress().getStreet(),
                client.getAddress().getLocation(),
                client.isTax(),  // ist das der getter? testen!
                client.getFee(),
                client.getSkonto(),
                links


        );

    }


    public Client toClient(){
        Address address = new Address(this.getName(), this.getStreet(), this.getLocation());
        return new Client(null, address, tax, fee, skonto);
    }

}
