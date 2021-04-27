package test.java;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import org.junit.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import jdk.jfr.Description;
import main.java.Move;
import main.java.Pokemon;

@RunWith(Parameterized.class)
public class PokemonTest {
    
    private Pokemon myPokemon; 
    
    public PokemonTest(Pokemon myPokemon) {
		// TODO Auto-generated constructor stub
    	this.myPokemon = myPokemon;

    	}
  
/* Sources: 
 * 1 : https://www.tutorialspoint.com/junit/junit_parameterized_test.htm
 *
 * 2: https://www.eviltester.com/post/junit/junit-4-parameterized-tests/	
*/ 
    
  @Parameterized.Parameters
  public static List<Object> input() {
	  return Arrays.asList(
			  new Object[] {new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false)
					  , new Pokemon("Gyrados", "Water", "None", "A cute serpent", 50.0, 12, 16, false)
					  , new Pokemon("Pikachu", "Electric", "None", "A cute rabbit", 50.0, 12, 16, false)
					  , new Pokemon("Charizard", "Fire", "None", "A cute dragon", 50.0, 12, 16, false)
					  , new Pokemon("Muk", "Poison", "None", "A cute sludge", 50.0, 12, 16, false)
					  , new Pokemon("Blastoise", "Water", "None", "A powerfull turtle", 50.0, 12, 16, false)
					  
			  
			  }
			  );
  }
    

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  @DisplayName("Pokemon is created correctly - Unit Test")
  @Description("This test makes sure that Pokemon creation is created successfully")
  public void Pokemon_instantiatesCorrectly_true() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    assertEquals(true, myPokemon instanceof Pokemon);
  }

  @Test
  @DisplayName("Get Pokemon Name - Unit Test")
  @Description("This test makes sure that we are able to access the access the Pokemon's name attribute through the getter method")
  public void getName_pokemonInstantiatesWithName_String() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    assertEquals("Squirtle", myPokemon.getName());
  }

  @Test
  @DisplayName("Empty Pokedex at launch - Unit Test")
  @Description("This test checks that the size of the Pokemon list is 0 at the start")
  public void all_emptyAtFirst() {
    assertEquals(Pokemon.all().size(), 0);
  }

  @Test
  @DisplayName("Pokemons are the same Instances - Unit Test")
  @Description("This test checks if two Pokemon instances are the same, given identical attributes")
  public void equals_returnsTrueIfPokemonAreTheSame_true() {
    Pokemon firstPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    Pokemon secondPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    assertTrue(firstPokemon.equals(secondPokemon));
  }

  @Test
  @DisplayName("Pokemon instance is saved correctly - Integration Test")
  @Description("This test checks if a created Pokemon instance is saved correctly (i.e added to the global Pokemon List), by checking that the size of the list is equal to 1 after inserting the first Pokemon")
  public void save_savesPokemonCorrectly_1() {
    Pokemon newPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    newPokemon.save();
    assertEquals(1, Pokemon.all().size());
  }

  @Test
  @DisplayName("Pokemon is found in Database - Integration Test")
  @Description("This test makes sure that the correct Pokemon is found in the database when queried by the instance Id attribute")
  public void find_findsPokemonInDatabase_true() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    Pokemon savedPokemon = Pokemon.find(myPokemon.getId());
    assertTrue(myPokemon.equals(savedPokemon));
  }

  @Test
  @DisplayName("Add attack move to Pokemon - Integration Test")
  @Description("This test makes sure that a move is correctly added to a Pokemon instance, by checking whether the move Id is equal to the Pokemon's new move Id")
  public void addMove_addMoveToPokemon() {
    Move myMove = new Move("Punch", "Normal", 50.0, 100);
    myMove.save();
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    myPokemon.addMove(myMove);
    Move savedMove = myPokemon.getMoves().get(0);
    assertTrue(myMove.equals(savedMove));
  }

  @Test
  @DisplayName("Pokemon Deletion - Integration Test")
  @Description("This test makes sure that when deleting a Pokemon, the associated move to that Pokemon instance is also deleted.")
  public void delete_deleteAllPokemonAndMovesAssociations() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    Move myMove = new Move("Bubble", "Water", 50.0, 100);
    myMove.save();
    myPokemon.addMove(myMove);
    myPokemon.delete();
    assertEquals(0, Pokemon.all().size());
    assertEquals(0, myPokemon.getMoves().size());
  }

  @Test
  @DisplayName("Retrieve Pokemon from database by Name attribute - Integration Test")
  @Description("This test verifies that the correct Pokemon is extracted from the database when querying with the given Name attribute")
  public void searchByName_findAllPokemonWithSearchInputString_List() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    assertEquals(myPokemon, Pokemon.searchByName("squir").get(0));
  }

  
  @Test
  @DisplayName("Damage applied to Defending Pokemon - Integration Test")
  @Description("This test verifies that the defending Pokemon is applied the correct amount of Damage after a specific attack type (Here : Water). The hp attribute is checked after the attack")
  public void fighting_damagesDefender() {
    myPokemon.save();
    myPokemon.hp = 500;
    Move myMove = new Move("Bubble", "Water", 50.0, 100);
    System.out.println("===Pokemon Type = " + myPokemon.getType1());
    myMove.attack(myPokemon);
    System.out.println(myPokemon.hp);
    myMove.attack(myPokemon);
        System.out.println(myPokemon.hp);
    myMove.attack(myPokemon);
        System.out.println(myPokemon.hp);
    myMove.attack(myPokemon);
    
    
    switch (myPokemon.getType1()) {
    	case "Water":
    		assertEquals(400, myPokemon.hp);
    		break;
    	case "Fire":
    		assertEquals(100, myPokemon.hp);
    		break;
    	case "Electric":
    		assertEquals(300, myPokemon.hp);
    		break;
    	case "Poison":
    		assertEquals(300, myPokemon.hp);
    		break;
    	
    	default:
    		assertEquals(500, myPokemon.hp);
    	
 
    }
    
  }

}
