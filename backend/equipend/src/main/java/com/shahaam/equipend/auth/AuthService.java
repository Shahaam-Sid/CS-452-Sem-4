package com.shahaam.equipend.auth;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shahaam.equipend.config.JwtService;
import com.shahaam.equipend.exceptions.RecordNotFoundException;
import com.shahaam.equipend.exceptions.UniqueIdentifierAlreaydExistsException;
import com.shahaam.equipend.exceptions.UserNotFoundException;
import com.shahaam.equipend.utils.ValidationsMethods;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthService {
    private static final int MIN_SEARCH_LENGTH = 2;

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @Transactional
    public AuthenticationResponseDTO register(RegisterRequestDTO req) {

        User user = mapFromRegisterRequestDTO(req);

        if (userRepo.existsByEmail(user.getEmail()))
            throw new UniqueIdentifierAlreaydExistsException("Email");

        if (userRepo.existsByUsername(user.getUsername()))
            throw new UniqueIdentifierAlreaydExistsException("Username");

        try {
            userRepo.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueIdentifierAlreaydExistsException("Username or email");
        }

        String jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponseDTO(
            user.getUserID(), user.getUsername(),
            user.getFullName(), user.getEmail(),
            jwtToken
        );
    }

    @Transactional(readOnly = true)
    public AuthenticationResponseDTO authenticate(AuthenticateRequestDTO req) {

        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
            req.identifier(), req.password()
        ));

        User user = (User) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponseDTO(
            user.getUserID(), user.getUsername(), user.getFullName(),
            user.getEmail(), jwtToken
        );
    }

    @Transactional(readOnly = true)
    public List<SearchResultsDTO> lookUpSearch(String query) {

        if (query == null || query.trim().length() < MIN_SEARCH_LENGTH)
            return List.of();

        String cleaned = query.trim();

        List<User> searchResults = userRepo.findTop20ByUsernameStartingWithOrFullNameContainingIgnoreCase(
            cleaned.toLowerCase(), cleaned
        );

        return searchResults.stream().map(this::mapToSearchResultsDTO).toList();
    }

    @Transactional(readOnly = false)
    public SearchResultsDTO searchById(UUID id) {
        ValidationsMethods.checkNullObject(id, "User ID");
        
        return mapToSearchResultsDTO(userRepo.findById(id).orElseThrow(
            () -> new RecordNotFoundException("User", id.toString())
        ));
    }

    protected User mapFromRegisterRequestDTO(RegisterRequestDTO req) {
        ValidationsMethods.validatePassword(req.password());
        return new User(
            req.fullName(), clean(req.username()), clean(req.email()),
            passwordEncoder.encode(req.password()), req.dob(), req.country()
        );
    }
    protected SearchResultsDTO mapToSearchResultsDTO(User user) {
        return new SearchResultsDTO(user.getUserID(), user.getUsername(), user.getFullName());
    }
    private String clean(String value) {
        return (value == null) ? null : value.trim();
    }
}