package com.cariochi.recordo.mockhttp.client;

import com.cariochi.recordo.EnableRecordo;
import com.cariochi.recordo.Read;
import com.cariochi.recordo.RecordoExtension;
import com.cariochi.recordo.mockhttp.client.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;

import static com.cariochi.recordo.assertions.RecordoAssertion.assertAsJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@WebMvcTest(UserController.class)
@ExtendWith(RecordoExtension.class)
@RequiredArgsConstructor
class MockHttpClientTest {

    private static final ParameterizedTypeReference<Page<UserDto>> USER_PAGE =
            new ParameterizedTypeReference<Page<UserDto>>() {};

    @Autowired
    @EnableRecordo
    private MockMvc mockMvc;

    @Test
    void should_get_user_by_id_with_mock_http_client(MockHttpClient http) {
        final Response<UserDto> response = http.get("/users/{id}", UserDto.class)
                .uriVars(1)
                .param("name", "Test User")
                .header("locale", "UA")
                .expectedStatus(OK)
                .execute();

        assertAsJson(response).isEqualTo("/mockmvc/get_user_response.json");
    }

    @Test
    void should_get_user_by_id_with_mock_http_request(
            @MockHttpRequest(
                    method = GET,
                    path = "/users/1?name=Test User",
                    interceptors = LocaleInterceptor.class
            ) Response<UserDto> response
    ) {
        assertAsJson(response).isEqualTo("/mockmvc/get_user_response.json");
    }

    @Test
    void should_get_user_by_id_with_mock_http_get(
            @MockHttpGet(value = "/users/1?name=Test User", headers = "locale=UA", expectedStatus = OK) Response<UserDto> response
    ) {
        assertAsJson(response).isEqualTo("/mockmvc/get_user_response.json");
    }

    @Test
    void should_get_user_by_id_with_mock_http_get(
            @MockHttpGet(value = "/users/1?name=Test User", headers = "locale=UA") UserDto user
    ) {
        assertAsJson(user).isEqualTo("/mockmvc/user.json");
    }

    @Test
    void should_get_all_users_with_mock_http_client(MockHttpClient http) {
        final Response<Page<UserDto>> response = http.get("/users", USER_PAGE).execute();
        assertAsJson(response).isEqualTo("/mockmvc/get_all_users_response.json");
    }

    @Test
    void should_get_all_users(
            @MockHttpGet("/users") Request<Page<UserDto>> request
    ) {
        final Response<Page<UserDto>> response = request.execute();
        assertAsJson(response).isEqualTo("/mockmvc/get_all_users_response.json");
    }

    @Test
    void should_get_all_users(
            @MockHttpGet("/users") Response<Page<UserDto>> response
    ) {
        assertAsJson(response).isEqualTo("/mockmvc/get_all_users_response.json");
    }

    @Test
    void should_get_all_users(
            @MockHttpGet("/users") Page<UserDto> users
    ) {
        assertAsJson(users).isEqualTo("/mockmvc/users.json");
    }

    @Test
    void should_get_empty_page(
            @MockHttpGet("/users?count=0") Page<UserDto> users
    ) {
        assertThat(users).isEmpty();
    }

    @Test
    void should_create_user(
            MockHttpClient http,
            @Read("/mockmvc/new_user.json") UserDto user
    ) {
        final Response<UserDto> response = http.post("/users", UserDto.class).body(user).execute();
        assertAsJson(response).isEqualTo("/mockmvc/create_user_response.json");
    }

    @Test
    void should_create_user(
            @MockHttpPost(value = "/users", body = "/mockmvc/new_user.json") Request<UserDto> request
    ) {
        final Response<UserDto> response = request.execute();
        assertAsJson(response).isEqualTo("/mockmvc/create_user_response.json");
    }

    @Test
    void should_create_user(
            @MockHttpPost(value = "/users", body = "/mockmvc/new_user.json") Response<UserDto> response
    ) {
        assertAsJson(response).isEqualTo("/mockmvc/create_user_response.json");
    }

    @Test
    void should_create_user(
            @MockHttpPost(value = "/users", body = "/mockmvc/new_user.json") UserDto user
    ) {
        assertAsJson(user).isEqualTo("/mockmvc/user.json");
    }

    @Test
    void should_delete_user_by_id(MockHttpClient http) {
        final Response<Void> response = http.delete("/users/{id}").uriVars(1).execute();
        assertAsJson(response).isEqualTo("/mockmvc/delete_user_response.json");
    }

    @Test
    void should_delete_user_by_id(
            @MockHttpDelete("/users/{id}") Request<Void> request
    ) {
        final Response<Void> response = request.uriVars(1).execute();
        assertAsJson(response).isEqualTo("/mockmvc/delete_user_response.json");
    }

    @Test
    void should_delete_user_by_id(
            @MockHttpDelete("/users/1") Response<Void> response
    ) {
        assertAsJson(response).isEqualTo("/mockmvc/delete_user_response.json");
    }

    @Test
    void should_update_user(
            @MockHttpPut(value = "/users", body = "/mockmvc/user.json") UserDto user
    ) {
        assertAsJson(user).isEqualTo("/mockmvc/updated_user.json");
    }

    @Test
    void should_patch_user(
            @MockHttpPatch(value = "/users/1", body = "/mockmvc/user.json") UserDto user
    ) {
        assertAsJson(user).isEqualTo("/mockmvc/updated_user.json");
    }

    public static class LocaleInterceptor implements RequestInterceptor {

        @Override
        public <T> Request<T> intercept(Request<T> request, MockHttpClient http) {
            final Request<UserDto> user = http.get("/users/1", UserDto.class);
            assertThat(user).isNotNull();
            return request.header("locale", "UA");
        }
    }
}
