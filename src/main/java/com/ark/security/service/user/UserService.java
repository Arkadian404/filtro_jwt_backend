package com.ark.security.service.user;

import com.ark.security.exception.BadCredentialsException;
import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.NullException;
import com.ark.security.models.order.Order;
import com.ark.security.models.order.OrderDetail;
import com.ark.security.models.order.OrderStatus;
import com.ark.security.models.user.Role;
import com.ark.security.models.user.User;
import com.ark.security.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final String NOT_FOUND = "Không tìm thấy người dùng: ";
    private final String EMPTY = "Không có người dùng nào";
    private final String DUPLICATE = "Người dùng đã tồn tại";
    private final String BLANK = "Không được để trống";
    private final String USERNAME_DUPLICATE = "Username đã tồn tại";
    private final String EMAIL_DUPLICATE = "Email đã tồn tại";
    private final String EMAIL_NOT_FOUND = "Không tìm thấy email: ";
    private final String OLD_PASSWORD_NOT_MATCH = "Mật khẩu cũ không đúng";


    public boolean checkUserExistById(int id){
        return userRepository.existsById(id);
    }


    public void saveUser(User user){
        if(userRepository.existsUserByUsername(user.getUsername())){
            throw new DuplicateException(USERNAME_DUPLICATE);
        }
        if(userRepository.existsUserByEmail(user.getEmail())){
            throw new DuplicateException(EMAIL_DUPLICATE);
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);

    }

    public void updateUser(int id, User user){
        User oldUser = getUserById(id);
        if(user == null){
            throw new NullException(BLANK);
        }
        if (oldUser != null) {
            oldUser.setFirstname(user.getFirstname());
            oldUser.setLastname(user.getLastname());
            oldUser.setDob(user.getDob());
            oldUser.setEmail(user.getEmail());
            oldUser.setAddress(user.getAddress());
            oldUser.setProvince(user.getProvince());
            oldUser.setDistrict(user.getDistrict());
            oldUser.setWard(user.getWard());
            oldUser.setPhone(user.getPhone());
            oldUser.setCreatedDate(LocalDateTime.now());
            oldUser.setEnabled(true);
            userRepository.save(oldUser);
        }
        else{
            throw new NotFoundException(NOT_FOUND+ id);
        }
    }

    public void updateUserPassword(int id, String password){
        User oldUser = getUserById(id);
        if(oldUser == null){
            throw new NotFoundException(NOT_FOUND+ id);
        }
        oldUser.setPassword(new BCryptPasswordEncoder().encode(password));
        oldUser.setEnabled(true);
        userRepository.save(oldUser);
    }


    public void deleteUser(Integer id){
        User user = getUserById(id);
        if(user == null){
            throw new NotFoundException(NOT_FOUND+ id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BadCredentialsException(NOT_FOUND+ username));
    }

    public User getByUsername(String username){
        return userRepository.findUserByUsername(username).orElseThrow(()-> new UsernameNotFoundException(NOT_FOUND+ username));
    }

    public List<User> getUsersByRole(Role role){
        List<User> user = userRepository.findUserByRole(role);
        if(user.isEmpty()){
            throw new NotFoundException(EMPTY);
        }
        user.forEach(u -> u.setPassword(null));
        return user;
    }

    public User getUserById(Integer id){
        return userRepository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND+ id));
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException(EMAIL_NOT_FOUND+ email));
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
            throw new NotFoundException(NOT_FOUND+ id);
        }
        if(!new BCryptPasswordEncoder().matches(oldPassword, user.getPassword())){
            throw new BadCredentialsException(OLD_PASSWORD_NOT_MATCH);
        }
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public boolean hasUserBoughtProduct(int userId, int productId){
        User user = getUserById(userId);
        List<Order> orders = user.getOrders();
        for(Order order: orders){
            List<OrderDetail> orderDetails = order.getOrderDetails();
            for(OrderDetail orderDetail: orderDetails){
                if(orderDetail.getProductDetail().getProduct().getId() == productId && order.getStatus().equals(OrderStatus.CONFIRMED)){
                    return true;
                }
            }
        }
        return false;
    }


    public boolean hasUserEverBoughtAProduct(int userId){
        User user = getUserById(userId);
        List<Order> orders = user.getOrders();
        for(Order order: orders){
            if(order.getStatus().equals(OrderStatus.CONFIRMED)){
                return true;
            }
        }
        return false;
    }
}
