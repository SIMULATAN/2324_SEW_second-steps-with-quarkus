package com.github.simulatan.sew_quarkus_introduction.repository

import com.github.simulatan.sew_quarkus_introduction.entity.RecipeEntity
import com.github.simulatan.sew_quarkus_introduction.repository.TestingRecipeRepository.Companion.create
import com.github.simulatan.sew_quarkus_introduction.resource.RecipeResource
import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import jakarta.inject.Inject
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.endsWith
import org.hamcrest.Matchers.hasItems
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@QuarkusTest
@TestHTTPEndpoint(RecipeResource::class)
class TestRecipeResource {

	@Inject
	lateinit var testingRecipeRepository: TestingRecipeRepository

	@Test
	fun `test get all recipes`() {
		val response = given()
			.contentType(ContentType.JSON)
		.`when`()
			.get("/all")
		.then()
			.statusCode(200)
			.extract()
			.`as`(Array<RecipeEntity>::class.java)

		assertThat(response.toList(), hasItems(*testingRecipeRepository.getAllRecipes().toTypedArray()))
	}

	@Test
	fun `test get individual recipe`() {
		val elementToTest = testingRecipeRepository.getAllRecipes().first()

		given()
			.contentType(ContentType.JSON)
		.`when`()
			.get("/${elementToTest.id}")
		.then()
			.statusCode(200)
			.assertThat()
			.body("id", Matchers.equalTo(elementToTest.id!!.toInt()))
			.body("name", Matchers.equalTo(elementToTest.name))
			.body("timeToCook", Matchers.equalTo(elementToTest.timeToCook?.toMillis()))
	}

	@Test
	fun `test create recipe valid`() {
		val recipe = create {
			id = Long.MAX_VALUE - 1
			name = "Test"
			timeToCook = 10.minutes.toJavaDuration()
			suitableForPeople = 1
		}

		Given {
			contentType(ContentType.JSON)
			body(recipe)
		} When {
			post("/new")
		} Then {
			statusCode(HttpStatus.SC_CREATED)
			header(HttpHeaders.LOCATION, endsWith("/recipe/${recipe.id}"))
		}

		assertThat(testingRecipeRepository.getById(recipe.id!!), Matchers.equalTo(recipe))
	}

	@Test
	fun `test create recipe invalid`() {
		val invalidRecipes = listOf(
			create {},
			create {
				id = Long.MAX_VALUE - 2
			}
		)

		for (recipe in invalidRecipes) {
			Given {
				contentType(ContentType.JSON)
				body(recipe)
			} When {
				post("/new")
			} Then {
				statusCode(HttpStatus.SC_BAD_REQUEST)
			}
		}
	}
}
