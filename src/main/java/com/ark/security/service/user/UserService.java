package com.ark.security.service.user;

import com.ark.security.dto.request.UserInfoRequest;
import com.ark.security.dto.request.UserRequest;
import com.ark.security.dto.response.UserResponse;
import com.ark.security.exception.*;
import com.ark.security.mapper.UserMapper;
import com.ark.security.models.order.Order;
import com.ark.security.models.order.OrderStatus;
import com.ark.security.models.user.Role;
import com.ark.security.models.user.User;
import com.ark.security.repository.user.UserRepository;
import com.ark.security.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CacheService cacheService;

    private static final String USER_KEY = "user:";


    public UserResponse getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserResponse user = userMapper.toUserResponse(
                userRepository.findUserByUsername(username)
                        .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND))
        );
        return cacheService.getOrSetCache(
                USER_KEY+"name:"+username,
                user,
                UserResponse.class,
                4,
                TimeUnit.HOURS
        );
    }

    public UserResponse updateCurrentUser(UserInfoRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUserInfo(user, request);
        user.setUpdatedDate(LocalDateTime.now());
        UserResponse response =  userMapper.toUserResponse(userRepository.save(user));
        cacheService.deleteKey(USER_KEY+"name:"+username);
        return response;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserById(Integer id){
        return userMapper.toUserResponse(
                userRepository.findById(id)
                        .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND))
        );
    }

    public boolean hasUserBoughtProduct(Integer userId, Integer productId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getOrders()
                .stream()
                .filter(order -> order.getStatus().equals(OrderStatus.CONFIRMED))
                .flatMap(order -> order.getOrderDetails().stream())
                .anyMatch(orderDetail -> orderDetail.getProductDetail()
                        .getProduct()
                        .getId()
                        .equals(productId));
    }

    public boolean hasUserEverBoughtAProduct(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Order> orders = user.getOrders();
        for(Order order: orders){
            if(order.getStatus().equals(OrderStatus.CONFIRMED)){
                return true;
            }
        }
        return false;
    }


    public UserResponse createUser(UserRequest userRequest){
        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedDate(LocalDateTime.now());
        user.setUpdatedDate(LocalDateTime.now());
        user.setEnabled(true);
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(int id, UserRequest userRequest){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, userRequest);
        user.setUpdatedDate(LocalDateTime.now());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    public void changePassword(int id, String oldPassword, String newPassword){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(!new BCryptPasswordEncoder().matches(oldPassword, user.getPassword())){
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        user.setEnabled(true);
        userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public boolean checkUserExistById(int userId) {
        return userRepository.existsById(userId);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public void updateUserPassword(Integer id, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        user.setEnabled(true);
        userRepository.save(user);
    }
}
