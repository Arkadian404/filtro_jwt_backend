package com.ark.security.service;

import com.ark.security.exception.BadCredentialsException;
import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.NullException;
import com.ark.security.models.user.Role;
import com.ark.security.models.user.User;
import com.ark.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        if(userRepository.existsUserByUsername(user.getUsername())){
            throw new DuplicateException("Username đã tồn tại");
        }
        if(userRepository.existsUserByEmail(user.getEmail())){
            throw new DuplicateException("Email đã tồn tại");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);

    }

    public void updateUser(int id, User user){
        User oldUser = getUserById(id);
        if(user == null){
            throw new NullException("Không được để trống");
        }
        if (oldUser != null) {
            oldUser.setFirstname(user.getFirstname());
            oldUser.setLastname(user.getLastname());
            oldUser.setDob(user.getDob());
            oldUser.setAddress(user.getAddress());
            oldUser.setProvince(user.getProvince());
            oldUser.setDistrict(user.getDistrict());
            oldUser.setWard(user.getWard());
            oldUser.setPhone(user.getPhone());
            oldUser.setEnabled(user.getEnabled());
            userRepository.save(oldUser);
        }
        else{
            throw new NotFoundException("Không tìm thấy người dùng: "+ id);
        }
    }

    public void updateUserPassword(int id, String password){
        User oldUser = getUserById(id);
        if(oldUser == null){
            throw new NotFoundException("Không tìm thấy người dùng: "+ id);
        }
        oldUser.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.save(oldUser);
    }


    public void deleteUser(Integer id){
        User user = getUserById(id);
        if(user == null){
            throw new NotFoundException("Không tìm thấy người dùng: "+ id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Không tìm thấy người dùng: "+ username));
    }

    public User getByUsername(String username){
        return userRepository.findUserByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Không tìm thấy người dùng: "+ username));
    }

    public List<User> getUsersByRole(Role role){
        List<User> user = userRepository.findUserByRole(role);
        if(user.isEmpty()){
            throw new NotFoundException("Không có người dùng nào");
        }
        user.forEach(u -> u.setPassword(null));
        return user;
    }

    public User getUserById(Integer id){
        return userRepository.findById(id).orElseThrow(()-> new NotFoundException("Không tìm thấy người dùng: "+ id));
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Không tìm email: "+ email));
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

    public void changePassword(int id, String oldPassword, String newPassword){
        User user = getUserById(id);
        if(user == null){
            throw new NotFoundException("Không tìm thấy người dùng: "+ id);
        }
        if(!new BCryptPasswordEncoder().matches(oldPassword, user.getPassword())){
            throw new BadCredentialsException("Mật khẩu cũ không đúng");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
    }
}
