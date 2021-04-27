package test.java;

import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.sql2o.*;

import jdk.jfr.Description;

import org.junit.*;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;



public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();
  public static ServerRule server;
  public static DatabaseRule database;
  
  @BeforeEach
  public void setup() throws Exception {
	  database = new DatabaseRule();
  }
  
  //@Rule
  //public DatabaseRule database = new DatabaseRule();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @BeforeAll
  public void setupServer() throws Exception{ 
	 server = new ServerRule();
  }
  
  /*@ClassRule
  public static ServerRule server = new ServerRule();*/
  
  
  
  
  
  @Test
  @DisplayName("Correct Home Page - Acceptance Test")
  @Description("This test makes sure that the source page is the correct one, by verifying that Pokedex is found in the HTML source code")
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Pokedex");
  }

  @Test
  @DisplayName("Pokedex is displayed - Acceptance Test")
  @Description("This Test makes sure that the whole Pokedex is displayed. Specifically checks if two Pokemons that are supposed to be present in that page are found")
  public void allPokemonPageIsDisplayed() {
    goTo("http://localhost:4567/");
    click("#viewDex");
    assertThat(pageSource().contains("Ivysaur"));
    assertThat(pageSource().contains("Charizard"));
  }

  @Test
  @DisplayName("Pokemon Description Page displaued - Acceptance Test")
  @Description("This test checks if the page that contains the description of a unique pokemon is displayed correctly. Here it checks for a specific Pokemon entry (Charizard)")
  public void individualPokemonPageIsDisplayed() {
    goTo("http://localhost:4567/pokepage/6");
    assertThat(pageSource().contains("Charizard"));
  }

  @Test
  @DisplayName("Cycle through Pokemons - Integration Test")
  @Description("This test checks if the user is able to cycle through the pokemons and checks if the next Pokemon to show is the correct one (Squirtle)")
  public void arrowsCycleThroughPokedexCorrectly() {
    goTo("http://localhost:4567/pokepage/6");
    click(".glyphicon-triangle-right");
    assertThat(pageSource().contains("Squirtle"));
  }

  @Test
  @DisplayName("Searching for existing Pokemon - Integration Test")
  @Description("This test makes sure that the Pokedex will return the correct Pokemon when searching for it. ")
  public void searchResultsReturnMatches() {
    goTo("http://localhost:4567/pokedex");
    fill("#name").with("char");
    assertThat(pageSource().contains("Charizard"));
  }

  @Test
  @DisplayName("Searching for non-existing Pokemon - Integration Test")
  @Description("This test makes sure that when searching for non-existing Pokemons, the Pokedex does not list any Pokemons")
  public void searchResultsReturnNoMatches() {
    goTo("http://localhost:4567/pokedex");
    fill("#name").with("x");
    assertThat(pageSource().contains("No matches for your search results"));
  }

}
