package com.ark.security.auth;

import com.ark.security.exception.*;
import com.ark.security.models.Employee;
import com.ark.security.models.token.Token;
import com.ark.security.models.user.Role;
import com.ark.security.repository.user.UserRepository;
import com.ark.security.service.user.EmployeeService;
import com.ark.security.service.user.TokenService;
import com.ark.security.models.token.TokenType;
import com.ark.security.models.user.User;
import com.ark.security.config.JwtService;
import com.ark.security.service.user.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmployeeService employeeService;


    @Value("${application.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${application.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${application.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${application.security.oauth2.client.registration.google.user-info-uri}")
    private String userInfoUri;
    @Value("${application.security.oauth2.client.registration.google.scope}")
    private String scope;
    @NonFinal
    private final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";

    public void register(RegisterRequest request){
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new PasswordNotMatchException("Mật khẩu không khớp");
        }
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .createdDate(LocalDateTime.now())
                .build();
        userRepository.save(user);
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
        var user = userRepository.findUserByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
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
        var user = userRepository.findUserByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
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
                return userRepository.findUserByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
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
                        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
                        return employeeService.getEmployeeByUserId(user.getId());
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
            var user = userRepository.findUserByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            if(jwtService.isValidToken(refreshToken, user)){
                String accessToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
        return null;
    }

    public Map<String, Object> authenticateAndFetchProfile(String code){
        try{
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new GsonFactory(),
                    clientId,
                    clientSecret,
                    code,
                    redirectUri
            ).execute();

            String idTokenString = tokenResponse.getIdToken();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            ).setAudience(Collections.singletonList(clientId)).build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            return getUserInfo(idToken);

        }catch (IOException | GeneralSecurityException ex){
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private static Map<String, Object> getUserInfo(GoogleIdToken idToken) {
        if(idToken == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

        GoogleIdToken.Payload payload =  idToken.getPayload();
        String accountId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String givenName = (String) payload.get("given_name");
        String familyName = (String) payload.get("family_name");
        String pictureUrl = (String) payload.get("picture");

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("accountId", accountId);
        map.put("email", email);
        map.put("name", name);
        map.put("firstname", givenName);
        map.put("lastname", familyName);
        map.put("pictureUrl", pictureUrl);
        return map;
    }

    public AuthenticationResponse authenticateWithGoogle(String code){
        Map<String, Object> userInfo = authenticateAndFetchProfile(code);
        String email = (String) userInfo.get("email");

        Optional<User> userOpt = userRepository.findByEmail(email);
        User user;
        if(userOpt.isPresent()){
            user = userOpt.get();
            user.setFirstname((String) userInfo.get("firstname"));
            user.setLastname((String) userInfo.get("lastname"));
            user.setAccountId((String) userInfo.get("accountId"));
            userRepository.save(user);
        }else{
            user = new User();
            user.setEmail((String) userInfo.get("email"));
            user.setUsername((String) userInfo.get("accountId"));
            user.setFirstname((String) userInfo.get("firstname"));
            user.setLastname((String) userInfo.get("lastname"));
            user.setRole(Role.USER);
            user.setCreatedDate(LocalDateTime.now());
            userRepository.save(user);
        }
        String accountId = "Google:" + user.getAccountId();
        Map<String, Object> map = new HashMap<>();
        map.put("accountId", accountId);
        String accessToken = jwtService.generateToken(map, user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public String generateAuthUrl(){
        return UriComponentsBuilder
                .fromHttpUrl(GOOGLE_AUTH_URL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", scope)
                .encode()
                .build()
                .toUriString();
    }

}
