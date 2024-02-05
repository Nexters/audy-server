package com.pcb.audy.domain;

import static com.pcb.audy.test.UserTest.TEST_USER;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;
import capital.scalable.restdocs.response.ResponseModifyingPreprocessors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.global.auth.PrincipalDetails;
import com.pcb.audy.global.jwt.MockSpringSecurityFilter;
import java.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class, SpringExtension.class})
public class BaseMvcTest {

    @Autowired protected ObjectMapper objectMapper;
    protected MockMvc mockMvc;
    protected Principal mockPrincipal;
    @Autowired private WebApplicationContext context;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) throws Exception {
        var mockMvcRequestBuilders =
                MockMvcRequestBuilders.get("http://example.com")
                        .header("Authorization", "Bearer <<전달받은토큰값>>");
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .alwaysDo(JacksonResultHandlers.prepareJackson(objectMapper))
                        .defaultRequest(mockMvcRequestBuilders)
                        .alwaysDo(
                                MockMvcRestDocumentation.document(
                                        "{class-name}/{method-name}",
                                        Preprocessors.preprocessRequest(),
                                        Preprocessors.preprocessResponse(
                                                ResponseModifyingPreprocessors.replaceBinaryContent(),
                                                ResponseModifyingPreprocessors.limitJsonArrayLength(objectMapper),
                                                Preprocessors.prettyPrint())))
                        .apply(springSecurity(new MockSpringSecurityFilter()))
                        .apply(
                                MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                                        .uris()
                                        .withScheme("https")
                                        .withHost("api.audy-gakka.com")
                                        .withPort(443)
                                        .and()
                                        .snippets()
                                        .withDefaults(
                                                CliDocumentation.curlRequest(),
                                                HttpDocumentation.httpRequest(),
                                                HttpDocumentation.httpResponse(),
                                                AutoDocumentation.requestFields(),
                                                AutoDocumentation.responseFields(),
                                                AutoDocumentation.pathParameters(),
                                                AutoDocumentation.requestParameters(),
                                                AutoDocumentation.description(),
                                                AutoDocumentation.methodAndPath(),
                                                AutoDocumentation.section()))
                        .build();
        mockUserSetup();
    }

    private void mockUserSetup() {
        UserDetails testUserDetails = PrincipalDetails.builder().user(TEST_USER).build();

        mockPrincipal =
                new UsernamePasswordAuthenticationToken(
                        testUserDetails, "", testUserDetails.getAuthorities());
    }
}
