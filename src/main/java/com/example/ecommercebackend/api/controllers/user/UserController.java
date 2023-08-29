package com.example.ecommercebackend.api.controllers.user;

import com.example.ecommercebackend.models.Address;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Mahyar Maleki
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final AddressRepository addressRepository;

    @GetMapping(path = "/{userId}/address")
    public ResponseEntity<List<Address>> getAddresses(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId) {
        if(!userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(addressRepository.findByUser_Id(userId));
    }

    @PutMapping(path = "/{userId}/address")
    public ResponseEntity<Address> putAddress(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @RequestBody Address address) {
        if(!userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        address.setId(null);
        LocalUser refUser = new LocalUser();
        refUser.setId(userId);
        address.setUser(refUser);
        return ResponseEntity.ok(addressRepository.save(address));
    }

    @PatchMapping(path = "/{userId}/address/{addressId}")
    public ResponseEntity<Address> patchAddress(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @PathVariable Long addressId,
                                                @RequestBody Address address) {
        if(!userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(address.getId() == addressId) {
            Optional<Address> optionalOriginalAddress = addressRepository.findById(addressId);
            if(optionalOriginalAddress.isPresent()) {
                LocalUser originalUser = optionalOriginalAddress.get().getUser();
                if(originalUser.getId() == userId) {
                    address.setUser(originalUser);
                    return ResponseEntity.ok(addressRepository.save(address));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    private boolean userHasPermission(LocalUser user, Long id) {
        return user.getId() == id;
    }
}
