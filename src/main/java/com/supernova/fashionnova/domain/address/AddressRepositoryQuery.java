package com.supernova.fashionnova.domain.address;

import com.supernova.fashionnova.domain.user.User;
import java.util.List;

public interface AddressRepositoryQuery {

    List<Address> findByUserOrderByDefaultAddressDesc(User user);

    void updateDefaultAddress(Long userId, Long addressId);

}
