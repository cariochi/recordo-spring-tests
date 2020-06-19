package com.cariochi.recordo.web;

import com.cariochi.recordo.annotation.EnableRecordo;
import com.cariochi.recordo.annotation.Given;
import com.cariochi.recordo.annotation.Verify;
import com.cariochi.recordo.junit5.RecordoExtension;
import com.cariochi.recordo.mockmvc.Request;
import com.cariochi.recordo.mockmvc.Response;
import com.cariochi.recordo.mockmvc.annotations.*;
import com.cariochi.recordo.verify.Verifier;
import com.cariochi.recordo.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@Slf4j
@WebMvcTest(UserController.class)
@ExtendWith(RecordoExtension.class)
@RequiredArgsConstructor
class UserControllerTest {

    @Autowired
    @EnableRecordo
    private MockMvc mockMvc;

    @Test
    void should_get_user_by_id(
            @Get("/users/{id}?name={name}") @Headers("locale: UA") Request<UserDto> request,
            @Verify("response") Verifier verifier
    ) {
        final Response<UserDto> response = request.execute(1, "Test User");
        verifier.verify(response);
    }

    @Test
    void should_get_user_by_id(
            @Get("/users/1?name=Test User") @Headers("locale: UA") Response<UserDto> response,
            @Verify("response") Verifier verifier
    ) {
        verifier.verify(response);
    }

    @Test
    void should_get_user_by_id(
            @Get("/users/1?name=Test User") @Headers("locale: UA") UserDto user,
            @Verify("user") Verifier verifier
    ) {
        verifier.verify(user);
    }

    @Test
    void should_get_all_users(
            @Get("/users") Request<List<UserDto>> request,
            @Verify("response") Verifier verifier
    ) {
        final Response<List<UserDto>> response = request.execute();
        verifier.verify(response);
    }

    @Test
    void should_get_all_users(
            @Get("/users") Response<List<UserDto>> response,
            @Verify("response") Verifier verifier
    ) {
        verifier.verify(response);
    }

    @Test
    void should_get_all_users(
            @Get("/users") List<UserDto> users,
            @Verify("users") Verifier verifier
    ) {
        verifier.verify(users);
    }

    @Test
    void should_create_user(
            @Given("user") UserDto user,
            @Post("/users") Request<UserDto> request,
            @Verify("response") Verifier verifier
    ) {
        final Response<UserDto> response = request.withBody(user).execute();
        verifier.verify(response);
    }

    @Test
    void should_create_user(
            @Post("/users") @Body("user") Request<UserDto> request,
            @Verify("response") Verifier verifier
    ) {
        final Response<UserDto> response = request.execute();
        verifier.verify(response);
    }

    @Test
    void should_create_user(
            @Post("/users") @Body("user") Response<UserDto> response,
            @Verify("response") Verifier verifier
    ) {
        verifier.verify(response);
    }

    @Test
    void should_create_user(
            @Post("/users") @Body("user") UserDto user,
            @Verify("user") Verifier verifier
    ) {
        verifier.verify(user);
    }

    @Test
    void should_delete_user_by_id(
            @Delete("/users/{id}") Request<Void> request,
            @Verify("response") Verifier verifier
    ) {
        final Response<Void> response = request.execute(1);
        verifier.verify(response);
    }

    @Test
    void should_delete_user_by_id(
            @Delete("/users/1") Response<UserDto> response,
            @Verify("response") Verifier verifier
    ) {
        verifier.verify(response);
    }

    @Test
    void should_update_user(
            @Put("/users") @Body("user") UserDto user,
            @Verify("user") Verifier verifier
    ) {
        verifier.verify(user);
    }

    @Test
    void should_patch_user(
            @Patch("/users/100") @Body("user") UserDto user,
            @Verify("user") Verifier verifier
    ) {
        verifier.verify(user);
    }
}
