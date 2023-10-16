package com.github.simulatan.sew_quarkus_introduction.repository

import com.github.simulatan.sew_quarkus_introduction.entity.RecipeEntity
import io.quarkus.test.Mock
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@Mock
@ApplicationScoped
class TestingRecipeRepository : RecipeRepository() {
	@PostConstruct
	fun addMockData() {
		buildList<RecipeEntity> {
			create { id = 1; name = "Kaisaschmoan" }
			create { id = 2; name = "Nudeelnnnnnn"; suitableForPeople = 4 }
			create { id = 3; name = "Aiaspais"; suitableForPeople = 1 }
			create { id = 4; name = "lasagna" ; timeToCook = 10.hours.toJavaDuration() }
			create { id = 5; name = "mir foit nix mehr ei" ; timeToCook = 10.minutes.toJavaDuration() }
		}.forEach(::addRecipe)
	}

	companion object {
		fun create(block: RecipeEntity.() -> Unit) = RecipeEntity().apply(block)
	}
}
