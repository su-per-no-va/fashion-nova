package com.supernova.fashionnova.domain.warn;

import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.warn.dto.WarnResponseDto;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WarnService {

    private final WarnRepository warnRepository;

    public WarnService(WarnRepository warnRepository) {
        this.warnRepository = warnRepository;
    }

    @Transactional(readOnly = true)
    public List<WarnResponseDto> getWarningsByUser(User user) {

        return warnRepository.findByUser(user)
            .stream()
            .map(WarnResponseDto::new)
            .toList();
    }
}