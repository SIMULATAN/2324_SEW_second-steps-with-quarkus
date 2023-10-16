package com.github.simulatan.sew_quarkus_introduction.resource

import com.github.simulatan.sew_quarkus_introduction.entity.RecipeEntity
import com.github.simulatan.sew_quarkus_introduction.repository.RecipeRepository
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.net.URI

@Path("/recipe")
class RecipeResource(
	private val recipeRepository: RecipeRepository
) {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("all")
	fun getAllRecipes() = recipeRepository.getAllRecipes()

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	fun getRecipeById(@PathParam("id") id: Long) = recipeRepository.getById(id)

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("new")
	fun createRecipe(@Valid recipeEntity: RecipeEntity): Response? {
		recipeRepository.addRecipe(recipeEntity)
		return Response.created(URI.create("/recipe/${recipeEntity.id}"))
			.build()
	}
}
