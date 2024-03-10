package com.log.puller.service.impl;

import com.log.puller.Constants;
import com.log.puller.config.S3ClientConfig;
import com.log.puller.config.properties.S3Properties;
import com.log.puller.dto.SearchLogsRequest;
import com.log.puller.dto.SearchLogsResponse;
import com.log.puller.exception.BadRequestException;
import com.log.puller.repository.S3Repository;
import com.log.puller.service.GrepSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class GrepSearchServiceImpl implements GrepSearchService {

    private S3Properties s3Properties;
    private S3Repository s3Repository;
    public GrepSearchServiceImpl(S3Properties s3Properties, S3Repository s3Repository) {
        this.s3Properties = s3Properties;
        this.s3Repository = s3Repository;
    }

    @Override
    public SearchLogsResponse searchLogs(SearchLogsRequest searchLogsRequest) {
        validateRequest(searchLogsRequest);

        Map<String, List<String>> folders = s3Repository.listObjects(s3Properties.getConfig().getBucket());

        List<String> fileKeys = getFileKeys(folders, searchLogsRequest);

        return SearchLogsResponse.builder().logLines(search(fileKeys, searchLogsRequest.getSearchKeyword())).build();
    }

    private void validateRequest(SearchLogsRequest searchLogsRequest) {
        if (StringUtils.isEmpty(searchLogsRequest.getSearchKeyword())) {
            throw new BadRequestException("Search Keyword is empty");
        }
    }

    private List<String> getFileKeys(Map<String, List<String>> folders, SearchLogsRequest searchLogsRequest) {
        ZonedDateTime from = searchLogsRequest.getFrom();
        ZonedDateTime to = searchLogsRequest.getTo();
        List<String> files = new ArrayList<>();
        int fromFlag = 0, toFlag = 1;
        if (Objects.nonNull(to) && Objects.nonNull(from)) {
            String toKey = to.getYear() + Constants.HYPHEN + to.getMonth() + Constants.HYPHEN + to.getDayOfMonth();
            String fromKey = from.getYear() + Constants.HYPHEN + from.getMonth() + Constants.HYPHEN + from.getDayOfMonth();
            for (Map.Entry<String, List<String>> folder : folders.entrySet()) {
                if (folder.getKey().equals(fromKey)) {
                    files.addAll(folder.getValue());
                    fromFlag = 1;
                } else if (folder.getKey().equals(toKey)) {
                    files.addAll(folder.getValue());
                    toFlag = 0;
                } else if (fromFlag == 1 && toFlag == 1) {
                    files.addAll(folder.getValue());
                }
            }
        } else if (Objects.nonNull(searchLogsRequest.getFrom()) && Objects.isNull(searchLogsRequest.getTo())) {
            String fromKey = from.getYear() + Constants.HYPHEN + from.getMonth() + Constants.HYPHEN + from.getDayOfMonth();
            for (Map.Entry<String, List<String>> folder : folders.entrySet()) {
                if (folder.getKey().equals(fromKey)) {
                    files.addAll(folder.getValue());
                    fromFlag = 1;
                } else if (fromFlag == 1) {
                    files.addAll(folder.getValue());
                }
            }
        } else if (Objects.nonNull(searchLogsRequest.getTo()) && Objects.isNull(searchLogsRequest.getFrom())) {
            String toKey = to.getYear() + Constants.HYPHEN + to.getMonth() + Constants.HYPHEN + to.getDayOfMonth();
            for (Map.Entry<String, List<String>> folder : folders.entrySet()) {
                files.addAll(folder.getValue());
                if (folder.getKey().equals(toKey)) {
                    break;
                }
            }
        } else {
            for (Map.Entry<String, List<String>> folder : folders.entrySet()) {
                files.addAll(folder.getValue());
            }
        }
        return search(files, searchLogsRequest.getSearchKeyword());
    }

    private List<String> search(List<String> fileKeys, String searchKeyword) {
        List<String> lines = new ArrayList<>();
        for (String fileKey : fileKeys) {
            byte[] byteArray = s3Repository.getObject(s3Properties.getConfig().getBucket(), fileKey).getBytes(StandardCharsets.UTF_8);
            readData(byteArray, searchKeyword, lines);
        }
        return lines;
    }

    private void readData(byte[] byteArray, String searchKeyword, List<String> lines) {
        try {
            InputStream inputStream = new ByteArrayInputStream(byteArray);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(searchKeyword)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            log.error("Error occurred while reading file");
        }
    }
}
