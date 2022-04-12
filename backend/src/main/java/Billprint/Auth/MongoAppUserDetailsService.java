package Billprint.Auth;

import Billprint.Auth.User.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class MongoAppUserDetailsService implements UserDetailsService {
    private final AppUserService appUserService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserService.findByEmail(email)
                .map(gotUser -> new User(gotUser.getEmail(),gotUser.getPassword(), List.of( new SimpleGrantedAuthority("ROLE_" +gotUser.getRole()))))
                .orElseThrow (() -> new UsernameNotFoundException("Dieser User existiert nicht" ));
    }


}
