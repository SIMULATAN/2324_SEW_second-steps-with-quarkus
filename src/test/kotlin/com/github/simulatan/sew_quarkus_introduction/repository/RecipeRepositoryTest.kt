package com.github.simulatan.sew_quarkus_introduction.repository

import com.github.simulatan.sew_quarkus_introduction.repository.TestingRecipeRepository.Companion.create
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration
import kotlin.time.toKotlinDuration

@QuarkusTest
class RecipeRepositoryTest {
	@Test
	fun `test add and get basic recipes`() {
		val repository = RecipeRepository()

		val recipe = create { id = 1; name = "Kaisaschmoan" }
		repository.addRecipe(recipe)
		assertEquals(recipe, repository.getById(1))

		val recipe2 = create { id = 2; name = "Nudeelnnnnnn"; suitableForPeople = 4 }
		repository.addRecipe(recipe2)
		assertEquals(recipe, repository.getById(1))

		repository.addRecipe(create { id = 3; name = "Aiaspais"; suitableForPeople = 1 })
		repository.addRecipe(create { id = 4; name = "lasagna" ; timeToCook = 10.hours.toJavaDuration() })
		repository.addRecipe(create { id = 5; name = "mir foit nix mehr ei" ; timeToCook = 10.minutes.toJavaDuration() })
	}

	@Test
	fun `test add and get all recipes`() {
		val repository = RecipeRepository()

		val recipes = listOf(
			create { id = 1; name = "Kaisaschmoan" },
			create { id = 2; name = "Nudeelnnnnnn"; suitableForPeople = 4 },
			create { id = 3; name = "Aiaspais"; suitableForPeople = 1 },
			create { id = 4; name = "lasagna" ; timeToCook = 10.hours.toJavaDuration() },
			create { id = 5; name = "mir foit nix mehr ei" ; timeToCook = 10.minutes.toJavaDuration() }
		)

		recipes.forEach(repository::addRecipe)

		assertEquals(recipes.toHashSet(), repository.getAllRecipes().toHashSet())
	}

	@Test
	fun `test get quickest recipe`() {
		val repository = RecipeRepository()

		val aiaspais = create { id = 3; name = "Aiaspais"; suitableForPeople = 1; timeToCook = 5.minutes.toJavaDuration() }

		val recipes = listOf(
			create { id = 1; name = "Kaisaschmoan" },
			create { id = 2; name = "Nudeelnnnnnn"; suitableForPeople = 4 },
			aiaspais,
			create { id = 4; name = "lasagna" ; timeToCook = 10.hours.toJavaDuration() },
			create { id = 5; name = "mir foit nix mehr ei" ; timeToCook = 10.minutes.toJavaDuration() }
		)

		recipes.forEach(repository::addRecipe)

		assertEquals(aiaspais, repository.getShortestRecipe())
	}

	@Test
	fun `test get total recipe duration`() {
		val repository = RecipeRepository()

		val recipes = listOf(
			create { id = 1; name = "Kaisaschmoan" },
			create { id = 2; name = "Nudeelnnnnnn"; suitableForPeople = 4 },
			create { id = 3; name = "Aiaspais"; suitableForPeople = 1 },
			create { id = 4; name = "lasagna" ; timeToCook = 10.hours.toJavaDuration() },
			create { id = 5; name = "mir foit nix mehr ei" ; timeToCook = 10.minutes.toJavaDuration() }
		)

		recipes.forEach(repository::addRecipe)

		assertEquals(10.hours + 10.minutes, repository.getTotalTimeToCook().toKotlinDuration())
	}
}
