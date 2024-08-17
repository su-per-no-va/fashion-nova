package com.supernova.fashionnova.domain.warn;

import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.warn.dto.WarnResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WarnService {

    private final WarnRepository warnRepository;

    /**
     * 본인 경고 조회
     *
     * @param user
     * @return List<WarnResponseDto>
     */
    @Transactional(readOnly = true)
    public List<WarnResponseDto> getWarningsByUser(User user) {

        return warnRepository.findByUser(user)
            .stream()
            .map(WarnResponseDto::new)
            .toList();
    }

}
