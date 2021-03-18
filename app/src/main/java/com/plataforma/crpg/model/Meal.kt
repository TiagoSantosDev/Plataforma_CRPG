package com.plataforma.crpg.model

data class Meal(
    val carne: String,
    val peixe: String,
    val dieta: String,
    val vegetariano: String
){
    override fun toString(): String {
        return "carne: ${this.carne}, peixe: ${this.peixe}, dieta: ${this.dieta}, " +
                "vegetariano: ${this.vegetariano}"
    }
}