package com.github.simulatan.sew_quarkus_introduction.repository

import com.github.simulatan.sew_quarkus_introduction.entity.RecipeEntity
import jakarta.enterprise.context.ApplicationScoped
import java.lang.IllegalArgumentException

@ApplicationScoped
class RecipeRepository {
	val recipeMap = mutableMapOf<Long, RecipeEntity>()

	fun addRecipe(recipe: RecipeEntity) {
		recipeMap.putIfAbsent(recipe.id!!, recipe)?.let {
			throw IllegalArgumentException("Trying to insert '$recipe' but entity with key ${it.id} '$it' already exists!")
		}
	}

	fun getAllRecipes() = recipeMap.values

	fun getById(id: Long) = recipeMap[id]

	fun getTotalTimeToCook() = recipeMap.values
		.mapNotNull { it.timeToCook }
		.reduce { acc, duration -> acc + duration }

	fun getShortestRecipe() = recipeMap.values
		.filter { it.timeToCook != null }
		.minBy { it.timeToCook!! }
}
