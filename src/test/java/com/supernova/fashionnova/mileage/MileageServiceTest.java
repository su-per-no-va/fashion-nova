package com.supernova.fashionnova.mileage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.supernova.fashionnova.mileage.dto.MileageResponseDto;
import com.supernova.fashionnova.order.Order;
import com.supernova.fashionnova.order.OrdersRepository;
import com.supernova.fashionnova.user.User;
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
    private OrdersRepository ordersRepository;

    @InjectMocks
    private MileageService mileageService;

    @Test
    @DisplayName("마일리지 내역 조회 테스트")
    public void getMileageHistoryListTest() {
        // given
        int page = 0;

        User mockUser = Mockito.mock(User.class);
        Order order = Mockito.mock(Order.class);

        List<Order> orderList = Collections.singletonList(order);
        Page<Order> ordersPage = new PageImpl<>(orderList);

        given(ordersRepository.findByUser(any(User.class), any(Pageable.class))).willReturn(ordersPage);

        // when
        List<MileageResponseDto> responseDtoList = mileageService.getMileageHistoryList(mockUser, page);

        // then
        assertThat(responseDtoList).isNotNull();
        assertThat(responseDtoList.size()).isEqualTo(1);
    }

}
