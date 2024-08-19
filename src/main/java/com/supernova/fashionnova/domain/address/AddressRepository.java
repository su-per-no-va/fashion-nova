package com.supernova.fashionnova.domain.address;

import com.supernova.fashionnova.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long>, AddressRepositoryQuery  {

    List<Address> findByUser(User user);

}
