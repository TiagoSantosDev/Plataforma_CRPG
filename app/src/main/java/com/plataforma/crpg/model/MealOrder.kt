package com.plataforma.crpg.model

data class MealOrder(
        val data: String,
        val pedido_feito: Int,
        val pedido_carne: Boolean,
        val pedido_peixe: Boolean,
        val pedido_dieta: Boolean,
        val pedido_vegetariano: Boolean
){
    override fun toString(): String {
        return "data: ${this.data},carne: ${this.pedido_carne}, peixe: ${this.pedido_peixe}, dieta: ${this.pedido_dieta}, " +
                "vegetariano: ${this.pedido_vegetariano}"
    }
}