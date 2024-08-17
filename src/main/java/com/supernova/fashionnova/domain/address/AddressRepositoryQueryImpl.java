package com.supernova.fashionnova.domain.address;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.supernova.fashionnova.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryQueryImpl implements AddressRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Address> findByUserOrderByDefaultAddressDesc(User user) {

        QAddress address = QAddress.address1;

        return queryFactory.selectFrom(address)
            .where(address.user.eq(user))
            .orderBy(address.defaultAddress.desc())
            .fetch();
    }

    @Override
    public void updateDefaultAddress(Long userId, Long addressId) {

        QAddress address = QAddress.address1;

        // 기존 defaultAddress 를 false 로
        queryFactory.update(address)
            .set(address.defaultAddress, false)
            .where(address.user.id.eq(userId), address.defaultAddress.isTrue())
            .execute();

        // 새로운 Address 를 defaultAddress 로
        queryFactory.update(address)
            .set(address.defaultAddress, true)
            .where(address.user.id.eq(userId), address.id.eq(addressId))
            .execute();
    }

}
