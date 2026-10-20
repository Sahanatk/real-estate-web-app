package com.example.myOwnRealtorWebsite.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.example.myOwnRealtorWebsite.dto.ListingDescriptionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class ListingDescriptionServiceTest {

    private MockRestServiceServer server;
    private ListingDescriptionService service;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://api.anthropic.com");
        server = MockRestServiceServer.bindTo(builder).build();
        service = new ListingDescriptionService(builder.build(), "test-model",false);
    }

    private ListingDescriptionRequest sample() {
        return new ListingDescriptionRequest("RESIDENTIAL", "San Diego", "CA",
                3, 2.0, 1800.0, null, 1995, "2-car garage", 850000.0, null, "ocean view");
    }

    @Test
    void returnsDescriptionFromApi() {
        server.expect(requestTo("https://api.anthropic.com/v1/messages"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(
                        "{\"content\":[{\"type\":\"text\",\"text\":\"  A bright coastal home.  \"}]}",
                        MediaType.APPLICATION_JSON));

        assertEquals("A bright coastal home.", service.generate(sample()));
        server.verify();
    }

    @Test
    void apiErrorBecomesFriendlyException() {
        server.expect(requestTo("https://api.anthropic.com/v1/messages"))
                .andRespond(withServerError());

        assertThrows(DescriptionGenerationException.class, () -> service.generate(sample()));
        server.verify();
    }

    @Test
    void promptIncludesFactsAndSkipsMissingFields() {
        String prompt = service.buildPrompt(sample());

        assertTrue(prompt.contains("Bedrooms: 3"));
        assertTrue(prompt.contains("Location: San Diego, CA"));
        assertTrue(prompt.contains("Highlights: ocean view"));
        assertFalse(prompt.contains("Lot size"));     // null, so skipped
        assertFalse(prompt.contains("850000"));       // price is excluded
    }

    @Test
    void emptyApiResponseBecomesFriendlyException() {
        server.expect(requestTo("https://api.anthropic.com/v1/messages"))
                .andRespond(withSuccess("{\"content\":[]}", MediaType.APPLICATION_JSON));

        assertThrows(DescriptionGenerationException.class, () -> service.generate(sample()));
    }
}
