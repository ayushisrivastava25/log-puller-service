package com.log.puller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchLogsResponse {

    private List<String> logLines;
}
