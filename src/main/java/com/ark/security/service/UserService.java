package com.ark.security.service;

import com.ark.security.exception.BadCredentialsException;
import com.ark.security.models.user.Role;
import com.ark.security.models.user.User;
import com.ark.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    public void saveUser(User user){
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public void updateUser(int id, User user){
        User oldUser = getUserById(id);
        if (oldUser != null) {
            oldUser.setFirstname(user.getFirstname());
            oldUser.setLastname(user.getLastname());
            oldUser.setEmail(user.getEmail());
            oldUser.setDob(user.getDob());
            oldUser.setAddress(user.getAddress());
            oldUser.setPhone(user.getPhone());
            userRepository.save(oldUser);
        }
    }

    public void deleteUser(Integer id){
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User not found ??: " + username));
    }

    public Optional<User> getByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public List<User> getUsersByRole(Role role){
        List<User> user = userRepository.findUserByRole(role);
        user.forEach(u -> u.setPassword(null));
        return user;
    }

    public User getUserById(Integer id){
        User user =userRepository.findById(id).orElse(null);
        if(user!=null){
            user.setPassword(null);
        }
        return user;
    }

    public Optional<User> getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean existsUserByUsername(String username){
        return userRepository.existsUserByUsername(username);
    }

    public boolean existsUserByEmail(String email){
        return userRepository.existsUserByEmail(email);
    }

    public boolean matchPassword(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }
}
