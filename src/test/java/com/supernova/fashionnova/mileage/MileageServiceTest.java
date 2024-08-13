package com.supernova.fashionnova.mileage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.supernova.fashionnova.domain.mileage.Mileage;
import com.supernova.fashionnova.domain.mileage.MileageRepository;
import com.supernova.fashionnova.domain.mileage.MileageService;
import com.supernova.fashionnova.domain.mileage.dto.MileageResponseDto;
import com.supernova.fashionnova.domain.user.User;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class MileageServiceTest {

    @Mock
    private MileageRepository mileageRepository;

    @InjectMocks
    private MileageService mileageService;

    @Test
    @DisplayName("마일리지 내역 조회 테스트")
    public void getMileageHistoryListTest() {
        // given
        int page = 0;

        User mockUser = Mockito.mock(User.class);
        Mileage mileage = Mockito.mock(Mileage.class);

        List<Mileage> mileageList = Collections.singletonList(mileage);
        Page<Mileage> mileagePage = new PageImpl<>(mileageList);

        given(mileageRepository.findByUser(any(User.class), any(Pageable.class)))
            .willReturn(mileagePage);

        // when
        List<MileageResponseDto> responseDtoList =
            mileageService.getMileageHistoryList(mockUser, page);

        // then
        assertThat(responseDtoList).isNotNull();
        assertThat(responseDtoList.size()).isEqualTo(1);
    }

}
