package com.bisnode.opa;

import com.bisnode.opa.client.OpaClient;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.client.query.QueryForDocumentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class XmasHappening {
    private static final Logger log = LoggerFactory.getLogger(XmasHappening.class);

    public static final String TYPE_RULE_PRESENTS_PACKAGING_PATH = "type_rule/presents_packaging";
    public static final String AGE_QUERY_PATH = "age_rule/access_to_xmas_wine";
    public static final String NAME_QUERY_PATH = "name_rule/access_to_chimney";
    public static final String OPA_URL = "http://localhost:8181/";

    public static void main(String[] args) {

        List<OpaInput> sleighCrew = List.of(
                new OpaInput("SantaClaus", 99, SantaPartyMember.SANTA.name()),
                new OpaInput("Buddy", 17, SantaPartyMember.ELF.name()),
                new OpaInput("Grinch", 22, SantaPartyMember.GRINCH.name()),
                new OpaInput("Rudolf", 33, SantaPartyMember.REINDEER.name()),
                new OpaInput("Niko", 25, SantaPartyMember.REINDEER.name()));

        sleighCrew.forEach(XmasHappening::callOpa);

    }

    static void callOpa(OpaInput testInput) {
        log.info("Creating query for input {}", testInput);
        OpaQueryApi queryApi = OpaClient.builder()
                .opaConfiguration(OPA_URL)
                .header("X-API-Key", "secret-key-12345")
                .header("X-Request-Source", "xmas-app")
                .build();

        QueryForDocumentRequest ageRequest = new QueryForDocumentRequest(testInput, AGE_QUERY_PATH);
        OpaOutput ageResponse = queryApi.queryForDocument(ageRequest, OpaOutput.class);
        log.info(ageResponse.toString());

        QueryForDocumentRequest typeRequest = new QueryForDocumentRequest(testInput, TYPE_RULE_PRESENTS_PACKAGING_PATH);
        OpaOutput typeResponse = queryApi.queryForDocument(typeRequest, OpaOutput.class);
        log.info(typeResponse.toString());

        QueryForDocumentRequest nameRequest = new QueryForDocumentRequest(testInput, NAME_QUERY_PATH);
        OpaOutput nameResponse = queryApi.queryForDocument(nameRequest, OpaOutput.class);
        log.info(nameResponse.toString());
    }
}

class OpaOutput {
    private Boolean allow;
    private String reason;

    public Boolean getAllow() {
        return allow;
    }

    public void setAllow(Boolean allow) {
        this.allow = allow;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {

        this.reason = reason;
    }

    @Override
    public String toString() {
        return "OpaOutput{" +
                "allow=" + allow +
                ", reason='" + reason + '\'' +
                '}';
    }
}

class OpaInput {
    private final String name;
    private final int age;
    private final String santaPartyMember;

    OpaInput(String name, int age, String santaPartyMember) {
        this.name = name;
        this.age = age;
        this.santaPartyMember = santaPartyMember;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getSantaPartyMember() {
        return santaPartyMember;
    }

    @Override
    public String toString() {
        return "OpaInput{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", santaPartyMember=" + santaPartyMember +
                '}';
    }
}

enum SantaPartyMember {
    SANTA,
    ELF,
    REINDEER,
    GRINCH
}
