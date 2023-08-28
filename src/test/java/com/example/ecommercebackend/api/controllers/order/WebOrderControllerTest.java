package com.example.ecommercebackend.api.controllers.order;

import com.example.ecommercebackend.models.WebOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mahyar Maleki
 */

@SpringBootTest
@AutoConfigureMockMvc
public class WebOrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @WithUserDetails("UserA")
    @Test
    public void testUserAAuthenticatedOrderList() throws Exception {
        testAuthenticatedListBelongsToUser("UserA");
    }

    @WithUserDetails("UserB")
    @Test
    public void testUserBAuthenticatedOrderList() throws Exception {
        testAuthenticatedListBelongsToUser("UserB");
    }

    private void testAuthenticatedListBelongsToUser(String username) throws Exception {
        mvc.perform(get("/order")).andExpect(status().is(HttpStatus.OK.value())).andExpect(result -> {
            String json = result.getResponse().getContentAsString();
            List<WebOrder> orders = new ObjectMapper().readValue(json, new TypeReference<List<WebOrder>>() {});
            for(WebOrder order : orders) {
                Assertions.assertEquals(username, order.getUser().getUsername(), "The order should belong to the user.");
            }
        });
    }

    @Test
    public void testUnauthenticatedOrderList() throws Exception {
        mvc.perform(get("/order")).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

}
