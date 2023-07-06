package com.berryinkstamp.berrybackendservice.quabbly;

import com.berryinkstamp.berrybackendservice.quabbly.dto.GraphqlResponseDTO;
import com.berryinkstamp.berrybackendservice.quabbly.dto.QuabblyRecordInputDTO;
import com.berryinkstamp.berrybackendservice.quabbly.dto.Record;
import com.berryinkstamp.berrybackendservice.utils.GraphqlSchemaReaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuabblyRestUtil {

    @Value("${quabbly.base.url}")
    private String quabblyBase;

    @Value("${quabbly.organisation.id}")
    private String quabblyId;

    private final RestTemplate restTemplate;


    public Record createRecord(QuabblyRecordInputDTO inputDTO, String boardId) throws IOException {
        String url = quabblyBase + "/v1/api";

        String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("quabblyCreateRecordQuery");
        query = query.replace("idOfBoard", boardId);
        query = query.replace("createRecordDTO", inputDTO.toString());

        HttpEntity<String> entity = new HttpEntity<>(query, getQuabblyHttpHeader(quabblyId));

        ResponseEntity<GraphqlResponseDTO> response = null;
        Record record = null;

        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, GraphqlResponseDTO.class);
            record = Objects.requireNonNull(response.getBody()).getData().getRecord();
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("error occurred while connecting to quabbly :: {}", exception.getLocalizedMessage());
            log.error("query :: {}", query);
            log.error("response :: {}", response);

        }
        return record;

    }

    public Record updateRecord(QuabblyRecordInputDTO recordInputDTO, String userInfoBoardId, String quabblyRecordId) throws IOException {
        String url = quabblyBase + "/v1/api";

        String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("quabblyUpdateRecordQuery");
        query = query.replace("$boardId", userInfoBoardId);
        query = query.replace("$recordId", quabblyRecordId);
        query = query.replace("$dto", recordInputDTO.toString());

        HttpEntity<String> entity = new HttpEntity<>(query, getQuabblyHttpHeader(quabblyId));

        ResponseEntity<GraphqlResponseDTO> response = null;
        Record record = null;

        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, GraphqlResponseDTO.class);
            record = Objects.requireNonNull(response.getBody()).getData().getUpdateRecord();
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("error occurred while connecting to quabbly :: {}", exception.getLocalizedMessage());
            log.error("query :: {}", query);
            log.error("response :: {}", response);

        }
        return record;
    }

    public List<Record> fetAllBoardRecords(String boardId) throws IOException {
        String url = quabblyBase + "/v1/api";

        String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("quabblyGetBoardRecordsQuery");
        query = query.replace("$boardId", boardId);

        HttpEntity<String> entity = new HttpEntity<>(query, getQuabblyHttpHeader(quabblyId));

        ResponseEntity<GraphqlResponseDTO> response = null;
        List<Record> records = null;

        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, GraphqlResponseDTO.class);
            records = Objects.requireNonNull(response.getBody()).getData().getBoardRecords();
            return records;
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("error occurred while connecting to quabbly :: {}", exception.getLocalizedMessage());
            log.error("query :: {}", query);
            log.error("response :: {}", response);

        }
        return records;
    }

    public Record getRecord(String recordId) throws IOException {
        String url = quabblyBase + "/v1/api";

        String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("quabblyGetRecordByIdQuery");
        query = query.replace("$recordId", recordId);

        HttpEntity<String> entity = new HttpEntity<>(query, getQuabblyHttpHeader(quabblyId));

        ResponseEntity<GraphqlResponseDTO> response = null;
        Record record = null;

        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, GraphqlResponseDTO.class);
            record = Objects.requireNonNull(response.getBody()).getData().getRecordById();
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("error occurred while connecting to quabbly :: {}", exception.getLocalizedMessage());
            log.error("query :: {}", query);
            log.error("response :: {}", response);


        }
        return record;
    }

    public String deletedRecord(String recordId, String transReqBoardId) throws IOException {
        String url = quabblyBase + "/v1/api";

        String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("quabblyDeleteRecordQuery");
        query = query.replace("$recordId", recordId);
        query = query.replace("$boardId", transReqBoardId);

        HttpEntity<String> entity = new HttpEntity<>(query, getQuabblyHttpHeader(quabblyId));

        ResponseEntity<GraphqlResponseDTO> response = null;
        String status = null;

        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, GraphqlResponseDTO.class);
            status = Objects.requireNonNull(response.getBody()).getData().getDeleteRecord();
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("error occurred while connecting to quabbly :: {}", exception.getLocalizedMessage());
            log.error("query :: {}", query);
            log.error("response :: {}", response);
        }
        return status;
    }

    private HttpHeaders getQuabblyHttpHeader(String quabblyId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("x-org-id", quabblyId);
        httpHeaders.set("content-type", "application/graphql");
        return httpHeaders;
    }


}
