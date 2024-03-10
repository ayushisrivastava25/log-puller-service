package com.log.puller.service;

import com.log.puller.dto.SearchLogsRequest;
import com.log.puller.dto.SearchLogsResponse;

public interface GrepSearchService {

    SearchLogsResponse searchLogs(SearchLogsRequest searchLogsRequest);
}
