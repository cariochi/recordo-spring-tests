package com.cariochi.recordo.mockmvc;

import com.cariochi.recordo.EnableRecordo;
import com.cariochi.recordo.Given;
import com.cariochi.recordo.RecordoExtension;
import com.cariochi.recordo.given.Assertion;
import com.cariochi.recordo.mockmvc.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@WebMvcTest(UserController.class)
@ExtendWith(RecordoExtension.class)
@RequiredArgsConstructor
class MockMvcTest {

    @Autowired
    @EnableRecordo
    private MockMvc mockMvc;

    @Test
    void should_get_user_by_id(
            @GET("/users/{id}?name={name}") @Headers("locale: UA") Request<UserDto> request,
            @Given("/mockmvc/get_user_response.json") Assertion<Response<UserDto>> assertion
    ) {
        final Response<UserDto> response = request.execute(1, "Test User");
        assertion.assertAsExpected(response);
    }

    @Test
    void should_get_user_by_id(
            @GET("/users/1?name=Test User") @Headers("locale: UA") Response<UserDto> response,
            @Given("/mockmvc/get_user_response.json") Assertion<Response<UserDto>> assertion
    ) {
        assertion.assertAsExpected(response);
    }

    @Test
    void should_get_user_by_id(
            @GET("/users/1?name=Test User") @Headers("locale: UA") UserDto user,
            @Given("/mockmvc/user.json") Assertion<UserDto> assertion
    ) {
        assertion.assertAsExpected(user);
    }

    @Test
    void should_get_all_users(
            @GET("/users") Request<Page<UserDto>> request,
            @Given("/mockmvc/get_all_users_response.json") Assertion<Response<Page<UserDto>>> assertion
    ) {
        final Response<Page<UserDto>> response = request.execute();
        assertion.assertAsExpected(response);
    }

    @Test
    void should_get_all_users(
            @GET("/users") Response<Page<UserDto>> response,
            @Given("/mockmvc/get_all_users_response.json") Assertion<Response<Page<UserDto>>> assertion
    ) {
        assertion.assertAsExpected(response);
    }

    @Test
    void should_get_all_users(
            @GET("/users") Page<UserDto> users,
            @Given("/mockmvc/users.json") Assertion<Page<UserDto>> assertion
    ) {
        assertion.assertAsExpected(users);
    }

    @Test
    void should_create_user(
            @Given("/mockmvc/new_user.json") UserDto user,
            @POST("/users") Request<UserDto> request,
            @Given("/mockmvc/create_user_response.json") Assertion<Response<UserDto>> assertion
    ) {
        final Response<UserDto> response = request.withBody(user).execute();
        assertion.assertAsExpected(response);
    }

    @Test
    void should_create_user(
            @POST("/users") @Body("/mockmvc/new_user.json") Request<UserDto> request,
            @Given("/mockmvc/create_user_response.json") Assertion<Response<UserDto>> assertion
    ) {
        final Response<UserDto> response = request.execute();
        assertion.assertAsExpected(response);
    }

    @Test
    void should_create_user(
            @POST("/users") @Body("/mockmvc/new_user.json") Response<UserDto> response,
            @Given("/mockmvc/create_user_response.json") Assertion<Response<UserDto>> assertion
    ) {
        assertion.assertAsExpected(response);
    }

    @Test
    void should_create_user(
            @POST("/users") @Body("/mockmvc/new_user.json") UserDto user,
            @Given("/mockmvc/user.json") Assertion<UserDto> assertion
    ) {
        assertion.assertAsExpected(user);
    }

    @Test
    void should_delete_user_by_id(
            @DELETE("/users/{id}") Request<Void> request,
            @Given("/mockmvc/delete_user_response.json") Assertion<Response<Void>> assertion
    ) {
        final Response<Void> response = request.execute(1);
        assertion.assertAsExpected(response);
    }

    @Test
    void should_delete_user_by_id(
            @DELETE("/users/1") Response<Void> response,
            @Given("/mockmvc/delete_user_response.json") Assertion<Response<Void>> assertion
    ) {
        assertion.assertAsExpected(response);
    }

    @Test
    void should_update_user(
            @PUT("/users") @Body("/mockmvc/user.json") UserDto user,
            @Given("/mockmvc/updated_user.json") Assertion<UserDto> assertion
    ) {
        assertion.assertAsExpected(user);
    }

    @Test
    void should_patch_user(
            @PATCH("/users/1") @Body("/mockmvc/user.json") UserDto user,
            @Given("/mockmvc/updated_user.json") Assertion<UserDto> assertion
    ) {
        assertion.assertAsExpected(user);
    }
}
