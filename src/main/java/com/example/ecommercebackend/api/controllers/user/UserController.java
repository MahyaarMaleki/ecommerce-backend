package com.example.ecommercebackend.api.controllers.user;

import com.example.ecommercebackend.models.Address;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.repositories.AddressRepository;
import com.example.ecommercebackend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "user")
public class UserController {

    private final AddressRepository addressRepository;

    private final UserService userService;

    @Operation(summary = "Get user's addresses", description = "Get the list of user's addresses by their id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "You are either logged out or trying to get someone else's addresses.", content = @Content),
            @ApiResponse(responseCode = "200", description = "Retrieved successfully.")
    })
    @GetMapping(path = "/{userId}/address")
    public ResponseEntity<List<Address>> getAddresses(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId) {
        if(!userService.userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(addressRepository.findByUser_Id(userId));
    }

    @Operation(summary = "Add to user's addresses", description = "Add a new address to the list of user's addresses by their id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "You are either logged out or trying to add to someone else's addresses.", content = @Content),
            @ApiResponse(responseCode = "200", description = "Added successfully.")
    })
    @PutMapping(path = "/{userId}/address")
    public ResponseEntity<Address> putAddress(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @RequestBody Address address) {
        if(!userService.userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        address.setId(null);
        LocalUser refUser = new LocalUser();
        refUser.setId(userId);
        address.setUser(refUser);
        return ResponseEntity.ok(addressRepository.save(address));
    }

    @Operation(summary = "Update an address", description = "Update an existing address of user's addresses by their id and the desired address id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "You are either logged out or trying to update one of someone else's addresses.", content = @Content),
            @ApiResponse(responseCode = "400", description = "The provided address id does not match with the address body id or address with provided id does not exist or the address belongs to someone else.", content = @Content),
            @ApiResponse(responseCode = "200", description = "Updated successfully.")
    })
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
                    return ResponseEntity.ok(addressRepository.save(address));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
