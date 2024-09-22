package io.sesac.admin.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.sesac.admin.config.BaseIT;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class MemberResourceTest extends BaseIT {

    @Test
    @Sql("/data/memberData.sql")
    void getAllMembers_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/members")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("_embedded.memberDTOList.get(0).id", Matchers.equalTo(1000))
                    .body("_links.self.href", Matchers.endsWith("/api/members?page=0&size=20&sort=id,asc"));
    }

    @Test
    @Sql("/data/memberData.sql")
    void getAllMembers_filtered() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/members?filter=1001")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("_embedded.memberDTOList.get(0).id", Matchers.equalTo(1001));
    }

    @Test
    @Sql("/data/memberData.sql")
    void getMember_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/members/1000")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", Matchers.equalTo("Sed diam voluptua."))
                    .body("_links.self.href", Matchers.endsWith("/api/members/1000"));
    }

    @Test
    void getMember_notFound() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/members/1666")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createMember_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/memberDTORequest.json"))
                .when()
                    .post("/api/members")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, memberRepository.count());
    }

    @Test
    @Sql("/data/memberData.sql")
    void updateMember_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/memberDTORequest.json"))
                .when()
                    .put("/api/members/1000")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("_links.self.href", Matchers.endsWith("/api/members/1000"));
        assertEquals("Duis autem vel.", memberRepository.findById(((long)1000)).orElseThrow().getName());
        assertEquals(2, memberRepository.count());
    }

    @Test
    @Sql("/data/memberData.sql")
    void deleteMember_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/members/1000")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, memberRepository.count());
    }

}
