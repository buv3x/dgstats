package com.datascience.service;

import com.datascience.dto.pdga.PdgaCompetitionInfo;
import com.datascience.dto.pdga.PdgaResponse;
import com.datascience.dto.pdga.PdgaRoundResults;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PdgaConnector {

    private static final Logger log = LoggerFactory.getLogger(PdgaConnector.class);
    private final RestTemplate restTemplate;

    public PdgaResponse<PdgaCompetitionInfo> getCompetitionInfo(Long tournamentId) {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            log.error("Sleep failed");
        }
        String urlTemplate = UriComponentsBuilder.fromUriString(
                "https://www.pdga.com/apps/tournament/live-api/live_results_fetch_event")
                .queryParam("TournID", "{TournID}")
                .encode()
                .toUriString();

        Map<String, String> params = new HashMap<>();
        params.put("TournID", tournamentId.toString());

        PdgaResponse<PdgaCompetitionInfo> body = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                jsonHeaders(),
                new ParameterizedTypeReference<PdgaResponse<PdgaCompetitionInfo>>() {
                },
                params
        ).getBody();
        log.info(tournamentId + ". Tournament info processed.");
        return body;
    }

    public PdgaResponse<PdgaRoundResults> getRoundResults(Long tournamentId, String division, Integer round) {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            log.error("Sleep failed");
        }
        String urlTemplate = UriComponentsBuilder.fromUriString(
                "https://www.pdga.com/apps/tournament/live-api/live_results_fetch_round")
                .queryParam("TournID", "{TournID}")
                .queryParam("Division", "{Division}")
                .queryParam("Round", "{Round}")
                .encode()
                .toUriString();

        Map<String, String> params = new HashMap<>();
        params.put("TournID", tournamentId.toString());
        params.put("Division", division);
        params.put("Round", round.toString());

        PdgaResponse<PdgaRoundResults> body = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                jsonHeaders(),
                new ParameterizedTypeReference<PdgaResponse<PdgaRoundResults>>() {
                },
                params
        ).getBody();
        log.info(tournamentId + ". " + division + ". " + round + ". Tournament results processed.");
        return body;
    }

    private HttpEntity<?> jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }
}
