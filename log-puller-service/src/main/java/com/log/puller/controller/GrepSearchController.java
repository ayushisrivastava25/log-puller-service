package com.log.puller.controller;

import com.log.puller.dto.SearchLogsRequest;
import com.log.puller.dto.SearchLogsResponse;
import com.log.puller.service.GrepSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/grep/search")
public class GrepSearchController {

    private GrepSearchService grepSearchService;

    public GrepSearchController(GrepSearchService grepSearchService) {
        this.grepSearchService = grepSearchService;
    }

    @GetMapping
    public ResponseEntity<SearchLogsResponse> searchLogs(@RequestBody SearchLogsRequest searchLogsRequest) {
        return ResponseEntity.ok(grepSearchService.searchLogs(searchLogsRequest));
    }
}
