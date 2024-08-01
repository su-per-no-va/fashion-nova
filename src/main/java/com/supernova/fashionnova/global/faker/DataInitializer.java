package com.supernova.fashionnova.global.faker;

import com.supernova.fashionnova.address.Address;
import com.supernova.fashionnova.address.AddressRepository;
import com.supernova.fashionnova.address.dto.AddressRequestDto;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductDetail;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.product.dto.ProductRequestDto;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.user.UserRepository;
import com.supernova.fashionnova.user.dto.SignupRequestDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private DataGenerator dataGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        List<SignupRequestDto> userList = dataGenerator.generateUsers(20);
        saveUsers(userList);

        List<AddressRequestDto> addressList = dataGenerator.generateAddresses(10);
        saveAddresses(addressList);

        List<ProductRequestDto> productList = dataGenerator.generateProducts(100);
        saveProducts(productList);
    }

    private void saveUsers(List<SignupRequestDto> requestDtoList) {
        requestDtoList.forEach(dto -> {
            User user = User.builder()
                .userName(dto.getUserName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
            userRepository.save(user);
        });
    }

    private void saveAddresses(List<AddressRequestDto> requestDtoList) {
        requestDtoList.forEach(dto -> {
            Address address = Address.builder()
                .name(dto.getName())
                .recipientName(dto.getRecipientName())
                .recipientNumber(dto.getRecipientNumber())
                .zipCode(dto.getZipCode())
                .address(dto.getAddress())
                .detail(dto.getDetail())
                .build();
            addressRepository.save(address);
        });
    }

    private void saveProducts(List<ProductRequestDto> requestDtoList) {
        requestDtoList.forEach(dto -> {
            Product product = Product.builder()
                .product(dto.getProduct())
                .price(dto.getPrice())
                .explanation(dto.getExplanation())
                .category(dto.getCategory())
                .productStatus(dto.getProductStatus())
                .build();

            List<ProductDetail> productDetailList = dto.getProductDetailList().stream()
                .map(productDetailRequestDto -> ProductDetail.builder()
                    .size(productDetailRequestDto.getSize())
                    .color(productDetailRequestDto.getColor())
                    .quantity(productDetailRequestDto.getQuantity())
                    .product(product)
                    .build())
                .collect(Collectors.toList());

            product.addDetailList(productDetailList);
            productRepository.save(product);
        });
    }



}
