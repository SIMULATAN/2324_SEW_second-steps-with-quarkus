package com.github.simulatan.sew_quarkus_introduction.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.time.Duration

@Entity
class RecipeEntity {
	@get:GeneratedValue
	@get:Id
	var id: Long? = null
	@NotBlank(message = "dude how are we even supposed to display this")
	var name: String? = null
	@Min(message = "Has to be suitable for at least 1 person", value = 1)
	var suitableForPeople: Int? = null
	var timeToCook: Duration? = null
}
