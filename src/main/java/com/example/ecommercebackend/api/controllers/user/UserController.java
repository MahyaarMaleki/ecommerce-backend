package com.example.ecommercebackend.api.controllers.user;

import com.example.ecommercebackend.api.models.DataChange;
import com.example.ecommercebackend.models.Address;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.repositories.AddressRepository;
import com.example.ecommercebackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final UserService userService;

    @GetMapping(path = "/{userId}/address")
    public ResponseEntity<List<Address>> getAddresses(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId) {
        if(!userService.userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(addressRepository.findByUser_Id(userId));
    }

    @PutMapping(path = "/{userId}/address")
    public ResponseEntity<Address> putAddress(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @RequestBody Address address) {
        if(!userService.userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        address.setId(null);
        LocalUser refUser = new LocalUser();
        refUser.setId(userId);
        address.setUser(refUser);
        Address savedAddress = addressRepository.save(address);
        simpMessagingTemplate.convertAndSend("/topic/user/" + userId + "/address", new DataChange<>(address, DataChange.ChangeType.INSERT));
        return ResponseEntity.ok(savedAddress);
    }

    @PatchMapping(path = "/{userId}/address/{addressId}")
    public ResponseEntity<Address> patchAddress(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @PathVariable Long addressId,
                                                @RequestBody Address address) {
        if(!userService.userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(address.getId() == addressId) {
            Optional<Address> optionalOriginalAddress = addressRepository.findById(addressId);
            if(optionalOriginalAddress.isPresent()) {
                LocalUser originalUser = optionalOriginalAddress.get().getUser();
                if(originalUser.getId() == userId) {
                    address.setUser(originalUser);
                    Address updatedAddress = addressRepository.save(address);
                    simpMessagingTemplate.convertAndSend("/topic/user/" + userId + "/address", new DataChange<>(address, DataChange.ChangeType.UPDATE));
                    return ResponseEntity.ok(updatedAddress);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
