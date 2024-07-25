package com.supernova.fashionnova.adimn;

import com.supernova.fashionnova.user.UserRepository;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;


    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(UserResponseDto::new).toList();
    }
}
