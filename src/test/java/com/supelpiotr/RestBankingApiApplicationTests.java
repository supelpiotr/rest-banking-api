package com.supelpiotr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.exchange.data.ExchangeDTO;
import com.supelpiotr.user.dto.UserDTO;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestBankingApiApplicationTests {

    public static final String EXCHANGE_URL = "/api/exchange";
    public static final String CREATE_SUB_ACCOUNT_URL = "/api/create/subaccount/USD";
    public static final String GET_USERS_URL = "/api/users";
    public static final String LOGIN_URL = "/api/session/login";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(value = "87061612345", userDetailsServiceBeanName="userService")
    public void beforeAllshouldLogin() throws Exception {

        UserDTO exchangeDTO = UserDTO
                .builder()
                .pesel("87061612345")
                .password("test123")
                .build();

        mvc.perform(post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(exchangeDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "87061612345", userDetailsServiceBeanName="userService")
    public void shouldCreateSubAccountInUSD() throws Exception {
        mvc.perform(post(CREATE_SUB_ACCOUNT_URL)).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "87061612345", userDetailsServiceBeanName = "userService")
    public void shouldExchangePlnToUsd() throws Exception {

        ExchangeDTO exchangeDTO = ExchangeDTO
                .builder()
                .initialCurrency(AccountType.PLN)
                .finalCurrency(AccountType.USD)
                .requestedValue(BigDecimal.valueOf(20))
                .build();

        mvc.perform(post(EXCHANGE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(exchangeDTO)))
                .andExpect(status().isOk());

    }

    @Test
    @WithUserDetails(value = "87061612345", userDetailsServiceBeanName = "userService")
    public void shouldExchangeUsdToPln() throws Exception {

        ExchangeDTO exchangeDTO = ExchangeDTO
                .builder()
                .initialCurrency(AccountType.USD)
                .finalCurrency(AccountType.PLN)
                .requestedValue(BigDecimal.valueOf(2))
                .build();

        mvc.perform(post(EXCHANGE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(exchangeDTO)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void getAllUsersByAdmin() throws Exception {
        mvc.perform(get(GET_USERS_URL)).andExpect(status().isOk());
    }

    public static String toJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
