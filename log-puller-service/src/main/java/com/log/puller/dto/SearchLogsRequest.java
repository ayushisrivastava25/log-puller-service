package com.log.puller.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class SearchLogsRequest {

    private String searchKeyword;

    private ZonedDateTime from;

    private ZonedDateTime to;
}
