package com.cariochi.recordo.web;

import com.cariochi.recordo.annotation.EnableRecordo;
import com.cariochi.recordo.annotation.Given;
import com.cariochi.recordo.annotation.Resources;
import com.cariochi.recordo.annotation.Verify;
import com.cariochi.recordo.junit5.RecordoExtension;
import com.cariochi.recordo.mockmvc.Request;
import com.cariochi.recordo.mockmvc.Response;
import com.cariochi.recordo.mockmvc.annotations.*;
import com.cariochi.recordo.verify.Expected;
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
@Resources("/users")
@RequiredArgsConstructor
class UserControllerTest {

    @Autowired
    @EnableRecordo
    private MockMvc mockMvc;

    @Test
    void should_get_user_by_id(
            @Get("/users/{id}?name={name}") @Headers("locale: UA") Request<UserDto> request,
            @Verify("get_user_response.json") Expected<Response<UserDto>> expected
    ) {
        final Response<UserDto> response = request.execute(1, "Test User");
        expected.assertEquals(response);
    }

    @Test
    void should_get_user_by_id(
            @Get("/users/1?name=Test User") @Headers("locale: UA") Response<UserDto> response,
            @Verify("get_user_response.json") Expected<Response<UserDto>> expected
    ) {
        expected.assertEquals(response);
    }

    @Test
    void should_get_user_by_id(
            @Get("/users/1?name=Test User") @Headers("locale: UA") UserDto user,
            @Verify("user.json") Expected<UserDto> expected
    ) {
        expected.assertEquals(user);
    }

    @Test
    void should_get_all_users(
            @Get("/users") Request<List<UserDto>> request,
            @Verify("get_all_users_response.json") Expected<Response<List<UserDto>>> expected
    ) {
        final Response<List<UserDto>> response = request.execute();
        expected.assertEquals(response);
    }

    @Test
    void should_get_all_users(
            @Get("/users") Response<List<UserDto>> response,
            @Verify("get_all_users_response.json") Expected<Response<List<UserDto>>> expected
    ) {
        expected.assertEquals(response);
    }

    @Test
    void should_get_all_users(
            @Get("/users") List<UserDto> users,
            @Verify("users.json") Expected<List<UserDto>> expected
    ) {
        expected.assertEquals(users);
    }

    @Test
    void should_create_user(
            @Given("new_user.json") UserDto user,
            @Post("/users") Request<UserDto> request,
            @Verify("create_user_response.json") Expected<Response<UserDto>> expected
    ) {
        final Response<UserDto> response = request.withBody(user).execute();
        expected.assertEquals(response);
    }

    @Test
    void should_create_user(
            @Post("/users") @Body("new_user.json") Request<UserDto> request,
            @Verify("create_user_response.json") Expected<Response<UserDto>> expected
    ) {
        final Response<UserDto> response = request.execute();
        expected.assertEquals(response);
    }

    @Test
    void should_create_user(
            @Post("/users") @Body("new_user.json") Response<UserDto> response,
            @Verify("create_user_response.json") Expected<Response<UserDto>> expected
    ) {
        expected.assertEquals(response);
    }

    @Test
    void should_create_user(
            @Post("/users") @Body("new_user.json") UserDto user,
            @Verify("user.json") Expected<UserDto> expected
    ) {
        expected.assertEquals(user);
    }

    @Test
    void should_delete_user_by_id(
            @Delete("/users/{id}") Request<Void> request,
            @Verify("delete_user_response.json") Expected<Response<Void>> expected
    ) {
        final Response<Void> response = request.execute(1);
        expected.assertEquals(response);
    }

    @Test
    void should_delete_user_by_id(
            @Delete("/users/1") Response<Void> response,
            @Verify("delete_user_response.json") Expected<Response<Void>> expected
    ) {
        expected.assertEquals(response);
    }

    @Test
    void should_update_user(
            @Put("/users") @Body("user.json") UserDto user,
            @Verify("updated_user.json") Expected<UserDto> expected
    ) {
        expected.assertEquals(user);
    }

    @Test
    void should_patch_user(
            @Patch("/users/1") @Body("user.json") UserDto user,
            @Verify("updated_user.json") Expected<UserDto> expected
    ) {
        expected.assertEquals(user);
    }
}
