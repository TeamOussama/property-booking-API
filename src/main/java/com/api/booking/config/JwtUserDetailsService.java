package com.api.booking.config;

import com.api.booking.dto.JwtUserDetails;
import com.api.booking.repository.UserRepository;
import com.api.booking.repository.entity.User;
import com.api.booking.repository.entity.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public JwtUserDetails loadUserByUsername(final String username) {
        final User user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            log.warn("user not found: {}", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        final String role = "manager".equals(username) ? UserRole.MANAGER.name() :
                ("owner".equals(username) ? UserRole.OWNER.name() : UserRole.GUEST.name());
        final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        return new JwtUserDetails(user.getId(), username, user.getPassword(), authorities);
    }

}
