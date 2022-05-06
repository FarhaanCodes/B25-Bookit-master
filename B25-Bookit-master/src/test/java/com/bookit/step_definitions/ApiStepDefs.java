package com.bookit.step_definitions;

import com.bookit.utilities.BookitUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DBUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class ApiStepDefs {

    String token;
    Response response;
    String emailGlobal;

    @Given("I logged Bookit api using {string} and {string}")
    public void i_logged_Bookit_api_using_and(String email, String password) {


        token = BookitUtils.generateToken(email,password);
        emailGlobal = email;
    }

    @When("I get the current user information from api")
    public void i_get_the_current_user_information_from_api() {

        response = given().accept(ContentType.JSON)
                .and()
                .header("Authorization", token)
                .when().get(ConfigurationReader.get("base_url") + "/api/users/me");

        response.prettyPrint();

    }

    @Then("status code should be {int}")
    public void status_code_should_be(int statusCode) {

        //get the status code from global response, which is stored from previous request
        //and verify if it matches with the status code from feature file
        System.out.println(response.statusCode());
        Assert.assertEquals(statusCode,response.statusCode());

    }

    @Then("the information about current user from api and database should match")
    public void the_information_about_current_user_from_api_and_database_should_match() {

        //get information from database
        String query ="SELECT firstname,lastname,role\n" +
                "FROM users\n" +
                "WHERE email = '"+emailGlobal+"'";

        Map<String, Object> dbMap = DBUtils.getRowMap(query);
        System.out.println(dbMap);

        //save database information into expected variables
        String expectedFirstName = (String) dbMap.get("firstname");
        String expectedLastName = (String) dbMap.get("lastname");
        String expectedRole= (String) dbMap.get("role");


        //get information from api
        JsonPath jsonPath = response.jsonPath();

        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");

        //compare database vs api

        Assert.assertEquals(expectedFirstName,actualFirstName);
        Assert.assertEquals(expectedLastName,actualLastName);
        Assert.assertEquals(expectedRole,actualRole);


    }



}