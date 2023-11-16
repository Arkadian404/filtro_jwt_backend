package com.ark.security.auth;

import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.PasswordNotMatchException;
import com.ark.security.exception.BadCredentialsException;
import com.ark.security.models.Employee;
import com.ark.security.models.token.Token;
import com.ark.security.service.user.EmployeeService;
import com.ark.security.service.user.TokenService;
import com.ark.security.models.token.TokenType;
import com.ark.security.models.user.User;
import com.ark.security.config.JwtService;
import com.ark.security.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmployeeService employeeService;

    public void register(RegisterRequest request){
        if(!userService.matchPassword(request.getPassword(), request.getConfirmPassword())){
            throw new PasswordNotMatchException("Mật khẩu không khớp");
        }
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        userService.saveUser(user);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (DisabledException ex){
            throw new UsernameNotFoundException("Tài khoản đã bị khóa");
        }   catch (AuthenticationException ex){
            throw new BadCredentialsException("Tài khoản hoặc mật khẩu không đúng");
        }
        var user = this.userService.getByUsername(request.getUsername());
        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, token);
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }



    public AuthenticationResponse authenticateEmployee(AuthenticationRequest request){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException ex){
            throw new BadCredentialsException("Tài khoản hoặc mật khẩu không đúng");
        }
        var user = this.userService.getByUsername(request.getUsername());
        if(user.getRole().toString().equals("EMPLOYEE") || user.getRole().toString().equals("ADMIN")){
                var token = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, token);
                return AuthenticationResponse.builder()
                        .accessToken(token)
                        .refreshToken(refreshToken)
                        .build();
        }
        else{
            throw new BadCredentialsException("Tài khoản không có quyền truy cập");
        }



    }


    private void saveUserToken(User user, String token) {
        var savedToken = Token.builder()
                .user(user)
                .token(token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(savedToken);
    }

    public User getCurrentUser(HttpServletRequest request, HttpServletResponse response){
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            var role = jwtService.extractRole(jwt);
            var username = jwtService.extractUsername(jwt);
            if(username!=null){
                return userService.getByUsername(username);
            }else{
                throw new NotFoundException("Không tìm thấy tài khoản");
            }
        }
        return null;
    }

    public Employee getCurrentEmployee(HttpServletRequest request, HttpServletResponse response){
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            var role = jwtService.extractRole(jwt);
            if(role!=null){
                String roleStr = role.stream().filter(r -> r.equals("ROLE_EMPLOYEE") || r.equals("ROLE_ADMIN")).findFirst().map(
                        Object::toString
                ).orElse(null);
                roleStr = roleStr.replace("ROLE_", "");
                if(roleStr.equals("EMPLOYEE")|| roleStr.equals("ADMIN")){
                    var username = jwtService.extractUsername(jwt);
                    if(username!=null){
                        User user = userService.getByUsername(username);
                        Employee employee = employeeService.getEmployeeByUserId(user.getId());
                        return employee;
                    }
                }
            }else{
                throw new NotFoundException("Không tìm thấy tài khoản");
            }
        }
        return null;
    }

    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenService.getAllValidTokenByUserId(user.getId());
        if(validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token ->{
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenService.saveAll(validUserTokens);
    }

//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        final String authHeader = request.getHeader(AUTHORIZATION);
//        final String refreshToken;
//        final String username;
//        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        username = jwtService.extractUsername(refreshToken);
//        if(username!=null){
//            var user = this.userService.getByUsername(username).orElseThrow();
//            if(jwtService.isValidToken(refreshToken, user)){
//                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }

    public AuthenticationResponse refreshToken(String refreshToken){
        final String username;
        username = jwtService.extractUsername(refreshToken);

        if(username!=null){
            var user = userService.getByUsername(username);
            if(jwtService.isValidToken(refreshToken, user)){
                String accessToken = jwtService.    generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
        return null;
    }

}
